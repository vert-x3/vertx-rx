package io.vertx.rx.java.test.support;

import org.junit.Assert;
import rx.Producer;
import rx.Subscriber;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SimpleSubscriber<T> extends Subscriber<T> {

  private static final Object completed = new Object() {
    @Override
    public String toString() {
      return "Completed";
    }
  };

  private long prefetch = Long.MAX_VALUE;
  private Producer producer;
  private final ArrayBlockingQueue<Object> events = new ArrayBlockingQueue<>(100);

  public SimpleSubscriber<T> prefetch(long value) {
    prefetch = value;
    return this;
  }

  public SimpleSubscriber<T> fetch(long val) {
    request(val);
    return this;
  }

  @Override
  public void onStart() {
    request(prefetch);
  }

  @Override
  public void onCompleted() { events.add(completed); }

  @Override
  public void setProducer(Producer producer) {
    super.setProducer(producer);
    this.producer = producer;
  }

  public Producer getProducer() {
    return producer;
  }

  @Override
  public void onError(Throwable e) {
    events.add(e);
  }

  @Override
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
