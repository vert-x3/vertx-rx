package io.vertx.rxjava3.test;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vertx.core.streams.ReadStream;
import io.vertx.rxjava3.impl.ReadStreamSubscriber;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A signal received while the subscriber is emitting must not be lost: the elements it makes available must be
 * delivered without waiting for an unrelated signal.
 */
public class ReadStreamSubscriberMissedSignalsTest {

  private static final int TOTAL = 100;

  @Test(timeout = 10_000)
  public void testNoPauseDeliversEverything() {
    ReadStream<Integer> stream = ReadStreamSubscriber.asReadStream(Flowable.range(0, TOTAL), Function.identity());
    AtomicInteger delivered = new AtomicInteger();
    AtomicBoolean ended = new AtomicBoolean();
    stream.endHandler(v -> ended.set(true));

    stream.handler(item -> delivered.incrementAndGet());

    assertEquals(TOTAL, delivered.get());
    assertTrue(ended.get());
  }

  /**
   * Resuming drains the pending elements, then requests more from the upstream while the subscriber is still
   * emitting. An idle upstream that answers synchronously calls onNext re-entrantly, and those elements must be
   * delivered by the same resume().
   */
  @Test(timeout = 10_000)
  public void testResumeAfterPauseDeliversEverything() {
    ReadStream<Integer> stream = ReadStreamSubscriber.asReadStream(Flowable.range(0, TOTAL), Function.identity());
    AtomicInteger delivered = new AtomicInteger();
    AtomicBoolean ended = new AtomicBoolean();
    stream.endHandler(v -> ended.set(true));
    stream.handler(item -> {
      delivered.incrementAndGet();
      if (item == 2) {
        stream.pause();
      }
    });
    assertEquals(3, delivered.get());

    stream.resume();

    assertEquals(TOTAL, delivered.get());
    assertTrue(ended.get());
  }

  /**
   * A consumer pausing itself with exactly one resume() per pause issued from another thread (what a pipe does with
   * a drain event), over an asynchronous upstream: no signal may be lost (the stream would hang), the handler must
   * never be called concurrently and the elements must stay in order.
   */
  @Test(timeout = 120_000)
  public void testSelfPausingConsumerWithAsyncUpstream() throws Exception {
    int iterations = 300;
    int total = 2_000;
    ExecutorService resumer = Executors.newFixedThreadPool(2);
    try {
      for (int iteration = 0; iteration < iterations; iteration++) {
        ReadStream<Integer> stream = ReadStreamSubscriber.asReadStream(
          Flowable.range(0, total).observeOn(Schedulers.computation(), false, 8), Function.identity());
        AtomicInteger inHandler = new AtomicInteger();
        AtomicInteger next = new AtomicInteger();
        AtomicBoolean concurrent = new AtomicBoolean();
        AtomicBoolean outOfOrder = new AtomicBoolean();
        CountDownLatch ended = new CountDownLatch(1);
        stream.endHandler(v -> ended.countDown());
        stream.handler(item -> {
          if (inHandler.incrementAndGet() > 1) {
            concurrent.set(true);
          }
          if (item != next.getAndIncrement()) {
            outOfOrder.set(true);
          }
          if (item % 5 == 0) {
            stream.pause();
            resumer.execute(stream::resume);
          }
          inHandler.decrementAndGet();
        });
        assertTrue("iteration " + iteration + " stalled after " + next.get() + "/" + total + " items",
          ended.await(10, TimeUnit.SECONDS));
        assertFalse("iteration " + iteration + ": handler called concurrently", concurrent.get());
        assertFalse("iteration " + iteration + ": elements out of order", outOfOrder.get());
      }
    } finally {
      resumer.shutdownNow();
    }
  }
}
