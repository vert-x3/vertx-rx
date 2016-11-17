package io.vertx.rx.java.test;

import io.vertx.core.streams.ReadStream;
import io.vertx.rx.java.ReadStreamSubscriber;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamSubscriberTest extends VertxTestBase {

  private class Sender {

    private ReadStreamSubscriber<String, String> subscriber = new ReadStreamSubscriber<>(Function.identity());
    private long requested;
    private int seq;

    public Sender() {
      subscriber.setProducer(n -> requested += n);
    }

    void emit() {
      subscriber.onNext("" + seq++);
    }

    void emit(int times) {
      for (int i = 0;i < times;i++) {
        emit();
      }
    }

    void complete() {
      subscriber.onCompleted();
    }

    void assertRequested(long expected) {
      assertEquals(expected, requested);
    }

    long available() {
      return requested - seq;
    }
  }

  private class Receiver extends ArrayDeque<Object> {

    final Object DONE = new Object();

    void handle(String item) {
      add(item);
    }

    void handleException(Throwable t) {
      add(t);
    }

    void handleEnd(Void v) {
      add(DONE);
    }

    void subscribe(ReadStream<String> sender) {
      sender.exceptionHandler(this::handleException);
      sender.endHandler(this::handleEnd);
      sender.handler(this::handle);
    }

    Receiver assertEmpty() {
      assertEquals(Collections.emptyList(), new ArrayList<>(this));
      return this;
    }

    Receiver assertItems(String... items) {
      ArrayList<Object> actual = new ArrayList<>();
      while (size() > 0 && actual.size() < items.length) {
        actual.add(remove());
      }
      assertEquals(Arrays.asList(items), actual);
      return this;
    }

    void assertEnded() {
      assertEquals(DONE, remove());
      assertEmpty();
    }
  }


  @Test
  public void testInitial() throws Exception {
    Sender sender = new Sender();
    sender.assertRequested(0);
    Receiver receiver = new Receiver();
    receiver.subscribe(sender.subscriber);
    sender.assertRequested(ReadStreamSubscriber.FETCH_SIZE);
    while (sender.seq < ReadStreamSubscriber.FETCH_SIZE / 2) {
      sender.emit();
      sender.assertRequested(ReadStreamSubscriber.FETCH_SIZE);
    }
    int i = ReadStreamSubscriber.FETCH_SIZE - (sender.seq - 1);
    sender.emit();
    sender.assertRequested(ReadStreamSubscriber.FETCH_SIZE + i);
  }

  @Test
  public void testPause() {
    Sender sender = new Sender();
    sender.subscriber.resume();
    sender.subscriber.pause();
    Receiver receiver = new Receiver();
    receiver.subscribe(sender.subscriber);
    for (int i = 0;i < ReadStreamSubscriber.FETCH_SIZE;i++) {
      sender.emit();
      assertEquals(ReadStreamSubscriber.FETCH_SIZE, sender.requested);
    }
    assertEquals(0, sender.available());
    receiver.assertEmpty();
    sender.subscriber.resume();
    assertEquals(ReadStreamSubscriber.FETCH_SIZE, sender.available());
    receiver.assertItems("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15");
    receiver.assertEmpty();
  }

  @Test
  public void testEnd() {
    Sender sender = new Sender();
    Receiver receiver = new Receiver();
    receiver.subscribe(sender.subscriber);
    sender.subscriber.onCompleted();
    receiver.assertEnded();
  }

  @Test
  public void testEndWhenPaused() {
    Sender sender = new Sender();
    sender.subscriber.pause();
    Receiver receiver = new Receiver();
    receiver.subscribe(sender.subscriber);
    sender.emit(3);
    sender.complete();
    sender.subscriber.resume();
    receiver.assertItems("0", "1", "2").assertEnded();
  }

  @Test
  public void testSetNullHandlerInEndHandler() {
    Sender sender = new Sender();
    AtomicInteger count = new AtomicInteger();
    sender.subscriber.endHandler(v -> {
      count.incrementAndGet();
      sender.subscriber.handler(null);
    });
    sender.subscriber.handler(item -> {});
    sender.complete();
    assertEquals(1, count.get());
  }

  @Test
  public void testSetEndHandlerAfterComplete() {
    Sender sender = new Sender();
    sender.subscriber.handler(item -> {});
    sender.complete();
    try {
      sender.subscriber.endHandler(v -> {});
      fail();
    } catch (IllegalStateException expected) {
    }
    sender.subscriber.endHandler(null);
  }

  @Test
  public void testDontDeliverCompleteEventWhenPausedWithPendingBuffers() {
    Sender sender = new Sender();
    AtomicInteger ended = new AtomicInteger();
    sender.subscriber.handler(item -> {});
    sender.subscriber.endHandler(v -> ended.incrementAndGet());
    sender.subscriber.pause();
    sender.emit();
    sender.complete();
    assertEquals(0, ended.get());
    sender.subscriber.resume();
    assertEquals(1, ended.get());
  }

  @Test
  public void testSetEndHandlerAfterCompleteButPending() {
    Sender sender = new Sender();
    sender.subscriber.handler(item -> {});
    sender.subscriber.pause();
    sender.emit();
    sender.complete();
    sender.subscriber.endHandler(v -> {});
    sender.subscriber.endHandler(null);
  }
}
