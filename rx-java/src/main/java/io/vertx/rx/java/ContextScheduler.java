package io.vertx.rx.java;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.BooleanSubscription;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ContextScheduler extends Scheduler {

  private final Vertx vertx;

  /** Create new ContextScheduler */
  public ContextScheduler(Vertx vertx) {
    this.vertx=vertx;
  }

  @Override
  public Worker createWorker() {
    return new ContextWorker();
  }

  private class ContextWorker extends Worker {

    protected ArrayDeque<Long> timers = new ArrayDeque<>();

    protected Action0 cancelAll= () -> {
      while (!timers.isEmpty())
        vertx.cancelTimer(timers.poll());
    };

    protected BooleanSubscription innerSubscription = BooleanSubscription.create(cancelAll);

    @Override
    public Subscription schedule(final Action0 action) {
      Vertx.currentContext().runOnContext(event -> {
        if (innerSubscription.isUnsubscribed())
          return;
        action.call();
      });
      return this.innerSubscription;
    }

    @Override
    public Subscription schedule(final Action0 action, long delayTime, TimeUnit unit) {
      timers.add(vertx.setTimer(unit.toMillis(delayTime),new Handler<Long>() {
        public void handle(Long id) {
          if (innerSubscription.isUnsubscribed())
            return;
          action.call();
          timers.remove(id);
        }
      }));
      return this.innerSubscription;
    }

    @Override
    public Subscription schedulePeriodically(final Action0 action, long initialDelay, final long delayTime, final TimeUnit unit) {

      // Use a bootstrap handler to start the periodic timer after initialDelay
      Handler bootstrap= id -> {

        action.call();

        // Ensure still active
        if (innerSubscription.isUnsubscribed())
          return;

        // Start the repeating timer
        timers.add(vertx.setPeriodic(unit.toMillis(delayTime),new Handler<Long>() {
          public void handle(Long nestedId) {
            if (innerSubscription.isUnsubscribed())
              return;
            action.call();
          }
        }));
      };

      long bootDelay=unit.toMillis(initialDelay);

      // If initialDelay is 0 then fire bootstrap immediately
      if (bootDelay<1) {
        vertx.runOnContext(bootstrap);
      }
      else {
        timers.add(vertx.setTimer(bootDelay,bootstrap));
      }

      return this.innerSubscription;
    }

    @Override
    public void unsubscribe() {
      innerSubscription.unsubscribe();
    }

    @Override
    public boolean isUnsubscribed() {
      return innerSubscription.isUnsubscribed();
    }
  }
}