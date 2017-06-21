package io.vertx.rx.java.test.support;

import org.junit.Assert;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SimpleSubscriber<T> {

  private static final Object completed = new Object() {
    @Override
    public String toString() {
      return "Completed";
    }
  };

  private long prefetch = Long.MAX_VALUE;
  private final ArrayBlockingQueue<Object> events = new ArrayBlockingQueue<>(100);
  private Subscription subscription;

  public static <B> void subscribe(Observable<B> observable, SimpleSubscriber<B> subscriber) {
    observable.subscribe(subscriber.toObserver());
  }

  public void onSubscribe(Subscription sub) {
    subscription = sub;
    sub.fetch(prefetch);
  }

  public interface Subscription {

    void fetch(long val);

    void unsubscribe();

    boolean isUnsubscribed();

  }

  public Observer<T> toObserver() {
    class SubscriberImpl extends Subscriber<T> implements Subscription {
      @Override
      public void onCompleted() {
        SimpleSubscriber.this.onCompleted();
      }
      @Override
      public void onError(Throwable throwable) {
        SimpleSubscriber.this.onError(throwable);
      }
      @Override
      public void onNext(T t) {
        SimpleSubscriber.this.onNext(t);
      }
      @Override
      public void onStart() {
        SimpleSubscriber.this.onSubscribe(this);
      }
      @Override
      public void fetch(long val) {
        request(val);
      }
    }
    return new SubscriberImpl();
  }

  public SimpleSubscriber<T> prefetch(long value) {
    prefetch = value;
    return this;
  }

  public SimpleSubscriber<T> unsubscribe() {
    subscription.unsubscribe();
    return this;
  }

  public boolean isSubscribed() {
    return !isUnsubscribed();
  }

  public boolean isUnsubscribed() {
    return subscription.isUnsubscribed();
  }

  public SimpleSubscriber<T> request(long val) {
    subscription.fetch(val);
    return this;
  }

  public void onCompleted() { events.add(completed); }

  public void onError(Throwable e) {
    events.add(e);
  }

  public void onNext(T t) { events.add(t); }

  public SimpleSubscriber<T> assertItem(T expected) {
    return assertEvent(expected);
  }

  public SimpleSubscriber<T> assertItems(T... expected) {
    for (T item : expected) {
      assertItem(item);
    }
    return this;
  }

  public SimpleSubscriber<T> assertError(Throwable expected) {
    return assertEvent(expected);
  }

  public SimpleSubscriber<T> assertCompleted() {
    return assertEvent(completed);
  }

  public SimpleSubscriber<T> assertEmpty() {
    if (!events.isEmpty()) {
      throw new AssertionError("Was expecting no events instead of " + events);
    }
    return this;
  }

  private SimpleSubscriber<T> assertEvent(Object expected) {
    Object event;
    try {
      event = events.poll(1, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new AssertionError(e);
    }
    if (event == null) {
      throw new AssertionError("Was expecting at least event " + expected);
    }
    if (expected == completed) {
      Assert.assertEquals(completed, event);
    } else if (expected instanceof Throwable) {
      Assert.assertEquals(expected, event);
    } else {
      assertEquals(expected, event);
    }
    return this;
  }

  protected void assertEquals(java.lang.Object expected, java.lang.Object actual) {
    Assert.assertEquals(expected, actual);
  }
}
