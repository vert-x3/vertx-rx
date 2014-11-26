package io.vertx.rx.java.test;

import rx.Subscriber;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
public class MySubscriber<T> extends Subscriber<T> {

  private final Object completed = new Object() {
    @Override
    public String toString() {
      return "Completed";
    }
  };

  final ArrayBlockingQueue<Object> events = new ArrayBlockingQueue<>(100);

  @Override
  public void onCompleted() { events.add(completed); }

  @Override
  public void onError(Throwable e) { events.add(e); }

  @Override
  public void onNext(T t) { events.add(t); }

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
      event = events.poll(1, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new AssertionError(e);
    }
    if (event == null) {
      throw new AssertionError("Was expecting at least event " + expected);
    }
    assertEquals(expected, event);
    return this;
  }
}
