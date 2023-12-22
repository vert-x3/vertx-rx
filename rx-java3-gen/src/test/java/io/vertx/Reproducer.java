package io.vertx;

import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vertx.core.streams.ReadStream;
import io.vertx.rxjava3.impl.ReadStreamSubscriber;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class Reproducer {
  private int called = 0;
  CountDownLatch waitForSecondElement = new CountDownLatch(1);
  CountDownLatch consumerLatch = new CountDownLatch(2);
  CountDownLatch testLatch = new CountDownLatch(1);
  CountDownLatch waitForFirstElementInPending = new CountDownLatch(1);
  String total = "";

  @Test
  public void test() throws InterruptedException {
    Flowable<String> flowableContent = Flowable.generate(this::emit).subscribeOn(Schedulers.io());
    ReadStream<String> readStream = ReadStreamSubscriber.asReadStream(flowableContent, obj -> obj);
    new Thread(() -> {
      // Set the readStream as if it was paused, with data in its pending queue and more incoming data expected
      readStream.pause();
      readStream.handler(this::handler); // Fetch elements, ending in pending queue
      readStream.endHandler(unused -> testLatch.countDown());
      try {
        waitForFirstElementInPending.await(); // Wait for the first element to be published in the pending queue
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      readStream.fetch(2); // immediately consume the first element, published on this thread
      waitForSecondElement.countDown(); // release the 2nd element to be published on Rx thread.
    }).start();
    testLatch.await();
    assertEquals("msg1msg2", total);
  }

  // - Emit "msg1",
  // - wait for a latch to emit the second element,
  // - complete
  private void emit(Emitter<String> emitter) throws InterruptedException {
    if (called == 0) {
      called = 1;
      emitter.onNext("msg1");
      waitForFirstElementInPending.countDown();
    } else if (called == 1) {
      called = 2;
      waitForSecondElement.await();
      emitter.onNext("msg2");
    } else {
      emitter.onComplete();
    }
  }

  private void handler(final String s) {
    waitForSecondElement.countDown();
    try {
      // Simulate an unsynchronized delay in the consumer by blocking the first message, letting go the 2nd message
      if (consumerLatch.getCount() > 1) {
        consumerLatch.countDown();
        consumerLatch.await();
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    total += s;
    consumerLatch.countDown(); // release the 1st message
  }
}
