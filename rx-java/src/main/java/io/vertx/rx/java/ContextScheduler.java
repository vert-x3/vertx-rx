package io.vertx.rx.java;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.impl.WorkerExecutorInternal;
import io.vertx.core.json.JsonObject;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ContextScheduler extends Scheduler {

  private final RxJavaSchedulersHook schedulersHook = RxJavaPlugins.getInstance().getSchedulersHook();
  private final Vertx vertx;
  private final Context context;
  private final WorkerExecutor workerExecutor;
  private final boolean blocking;
  private final boolean ordered;

  public ContextScheduler(Context context, boolean blocking) {
    this(context, blocking, true);
  }

  public ContextScheduler(Context context, boolean blocking, boolean ordered) {
    Objects.requireNonNull(context, "context is null");
    this.vertx = context.owner();
    this.context = context;
    this.workerExecutor = null;
    this.blocking = blocking;
    this.ordered = ordered;
  }

  public ContextScheduler(Vertx vertx, boolean blocking) {
    this(vertx, blocking, true);
  }

  public ContextScheduler(Vertx vertx, boolean blocking, boolean ordered) {
    Objects.requireNonNull(vertx, "vertx is null");
    this.vertx = vertx;
    this.context = null;
    this.workerExecutor = null;
    this.blocking = blocking;
    this.ordered = ordered;
  }

  public ContextScheduler(WorkerExecutor workerExecutor) {
    this(workerExecutor, true);
  }

  public ContextScheduler(WorkerExecutor workerExecutor, boolean ordered) {
    Objects.requireNonNull(workerExecutor, "workerExecutor is null");
    this.vertx = ((WorkerExecutorInternal) workerExecutor).vertx();
    this.context = null;
    this.workerExecutor = workerExecutor;
    this.blocking = true;
    this.ordered = ordered;
  }

  @Override
  public Worker createWorker() {
    return new WorkerImpl();
  }

  private static final Object DUMB = new JsonObject();

  private class WorkerImpl extends Worker {

    private final ConcurrentHashMap<TimedAction, Object> actions = new ConcurrentHashMap<>();
    private final AtomicBoolean cancelled = new AtomicBoolean();

    @Override
    public Subscription schedule(Action0 action) {
      return schedule(action, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
      action = schedulersHook.onSchedule(action);
      TimedAction timed = new TimedAction(action, unit.toMillis(delayTime), 0);
      actions.put(timed, DUMB);
      return timed;
    }

    @Override
    public Subscription schedulePeriodically(Action0 action, long initialDelay, long period, TimeUnit unit) {
      action = schedulersHook.onSchedule(action);
      TimedAction timed = new TimedAction(action, unit.toMillis(initialDelay), unit.toMillis(period));
      actions.put(timed, DUMB);
      return timed;
    }

    @Override
    public void unsubscribe() {
      if (cancelled.compareAndSet(false, true)) {
        actions.keySet().forEach(TimedAction::unsubscribe);
      }
    }

    @Override
    public boolean isUnsubscribed() {
      return cancelled.get();
    }

    class TimedAction implements Subscription, Runnable {

      private long id;
      private final Action0 action;
      private final long periodMillis;
      private boolean cancelled;

      public TimedAction(Action0 action, long delayMillis, long periodMillis) {
        this.cancelled = false;
        this.action = action;
        this.periodMillis = periodMillis;
        if (delayMillis > 0) {
          schedule(delayMillis);
        } else {
          id = -1;
          execute();
        }
      }

      private void schedule(long delay) {
        this.id = vertx.setTimer(delay, l -> execute());
      }

      private void execute() {
        if (workerExecutor != null) {
          workerExecutor.executeBlocking(fut -> {
            run();
            fut.complete();
          }, ordered, null);
          return;
        }
        Context ctx = context != null ? context : vertx.getOrCreateContext();
        if (blocking) {
          ctx.executeBlocking(fut -> {
            run();
            fut.complete();
          }, ordered, null);
        } else {
          ctx.runOnContext(v -> {
            run();
          });
        }
      }

      @Override
      public void run() {
        synchronized (TimedAction.this) {
          if (cancelled) {
            return;
          }
        }
        action.call();
        synchronized (TimedAction.this) {
          if (periodMillis > 0) {
            schedule(periodMillis);
          }
        }
      }

      @Override
      public synchronized void unsubscribe() {
        if (!cancelled) {
          actions.remove(this);
          if (id > 0) {
            vertx.cancelTimer(id);
          }
          cancelled = true;
        }
      }

      @Override
      public synchronized boolean isUnsubscribed() {
        return cancelled;
      }
    }
  }
}
