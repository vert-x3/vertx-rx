package io.vertx.test.reactivex;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.junit.Assert;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MySubscriber<T> {

  private final Object completed = new Object() {
    @Override
    public String toString() {
      return "Completed";
    }
  };
  private final ArrayBlockingQueue<Object> events = new ArrayBlockingQueue<>(100);
  private Subscription subscription;

  public void subscribeTo(Flowable<T> flowable) {
    flowable.subscribe(new Subscriber<T>() {
      @Override
      public void onSubscribe(Subscription s) {
        subscription = s;
        MySubscriber.this.onSubscribe();
      }
      @Override
      public void onNext(T event) {
        events.add(event);
        MySubscriber.this.onNext(event);
      }
      @Override
      public void onError(Throwable t) {
        events.add(t);
        MySubscriber.this.onError(t);
      }
      @Override
      public void onComplete() {
        subscription = null;
        events.add(completed);
        MySubscriber.this.onComplete();
      }
    });
  }

  protected final void request(long n) {
    if (subscription == null) {
      throw new IllegalStateException();
    }
    subscription.request(n);
  }

  protected void onSubscribe() {
    request(Long.MAX_VALUE);
  }

  protected void onNext(T event) {
  }

  protected void onError(Throwable err) {
    err.printStackTrace();
  }

  protected void onComplete() {
  }

  public boolean isSubscribed() {
    return subscription != null;
  }

  MySubscriber<T> assertItem(T expected) {
    return assertEvent(expected);
  }

  MySubscriber<T> assertError(Throwable expected) {
    return assertEvent(expected);
  }

  MySubscriber<T> assertCompleted() {
    return assertEvent(completed);
  }

  MySubscriber<T> assertEmpty() {
    if (!events.isEmpty()) {
      throw new AssertionError("Was expecting no events instead of " + events);
    }
    return this;
  }

  private MySubscriber<T> assertEvent(Object expected) {
    Object event;
    try {
      event = events.poll(1000, TimeUnit.SECONDS);
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
