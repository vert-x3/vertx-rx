package io.vertx.rx.java.test;

import io.vertx.rx.java.ContextScheduler;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import rx.Scheduler;
import rx.Subscription;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SchedulerTest extends VertxTestBase {

  private void assertEventLoopThread() {
    String name = Thread.currentThread().getName();
    assertTrue("Was expecting event loop thread instead of " + name, name.startsWith("vert.x-eventloop-thread"));
  }

  private void assertWorkerThread() {
    String name = Thread.currentThread().getName();
    assertTrue("Was expecting worker thread instead of " + name, name.startsWith("vert.x-worker-thread"));
  }

  @Test
  public void testScheduleImmediatly() {
    testScheduleImmediatly(true);
  }

  @Test
  public void testScheduleImmediatlyBlocking() {
    testScheduleImmediatly(false);
  }

  private void testScheduleImmediatly(boolean blocking) {
    ContextScheduler scheduler = new ContextScheduler(vertx, blocking);
    Scheduler.Worker worker = scheduler.createWorker();
    worker.schedule(() -> {
      if (blocking) {
        assertWorkerThread();
      } else {
        assertEventLoopThread();
      }
      testComplete();
    }, 0, TimeUnit.MILLISECONDS);
    await();
  }

  @Test
  public void testScheduleDelayed() {
    testScheduleDelayed(false);
  }

  @Test
  public void testScheduleDelayedBlocking() {
    testScheduleDelayed(true);
  }

  private void testScheduleDelayed(boolean blocking) {
    ContextScheduler scheduler2 = new ContextScheduler(vertx, blocking);
    Scheduler.Worker worker = scheduler2.createWorker();
    long time = System.currentTimeMillis();
    worker.schedule(() -> {
      if (blocking) {
        assertWorkerThread();
      } else {
        assertEventLoopThread();
      }
      assertTrue(System.currentTimeMillis() - time >= 40);
      testComplete();
    }, 40, TimeUnit.MILLISECONDS);
    await();
  }

  @Test
  public void testSchedulePeriodic() {
    testSchedulePeriodic(false);
  }

  @Test
  public void testSchedulePeriodicBlocking() {
    testSchedulePeriodic(true);
  }

  private void testSchedulePeriodic(boolean blocking) {
    ContextScheduler scheduler2 = new ContextScheduler(vertx, blocking);
    Scheduler.Worker worker = scheduler2.createWorker();
    AtomicLong time = new AtomicLong(System.currentTimeMillis() - 40);
    AtomicInteger count = new AtomicInteger();
    AtomicReference<Subscription> sub = new AtomicReference<>();
    sub.set(worker.schedulePeriodically(() -> {
      if (blocking) {
        assertWorkerThread();
      } else {
        assertEventLoopThread();
      }
      if (count.incrementAndGet() > 2) {
        sub.get().unsubscribe();
        testComplete();
      } else {
        long now = System.currentTimeMillis();
        long delta = now - time.get();
        assertTrue("" + delta, delta >= 40);
        time.set(now);
      }
    }, 0, 40, TimeUnit.MILLISECONDS));
    await();
  }

  @Test
  public void testUnsubscribeBeforeExecute() throws Exception {
    testUnsubscribeBeforeExecute(false);
  }

  @Test
  public void testUnsubscribeBeforeExecuteBlocking() throws Exception {
    testUnsubscribeBeforeExecute(true);
  }

  private void testUnsubscribeBeforeExecute(boolean blocking) throws Exception {
    ContextScheduler scheduler2 = new ContextScheduler(vertx, blocking);
    Scheduler.Worker worker = scheduler2.createWorker();
    CountDownLatch latch = new CountDownLatch(1);
    Subscription sub = worker.schedule(latch::countDown, 20, TimeUnit.MILLISECONDS);
    sub.unsubscribe();
    assertFalse(latch.await(40, TimeUnit.MILLISECONDS));
  }

  @Test
  public void testUnsubscribeDuringExecute() throws Exception {
    testUnsubscribeDuringExecute(false);
  }

  @Test
  public void testUnsubscribeDuringExecuteBlocking() throws Exception {
    testUnsubscribeDuringExecute(true);
  }

  private void testUnsubscribeDuringExecute(boolean blocking) throws Exception {
    ContextScheduler scheduler2 = new ContextScheduler(vertx, blocking);
    Scheduler.Worker worker = scheduler2.createWorker();
    AtomicInteger count = new AtomicInteger();
    AtomicReference<Subscription> sub = new AtomicReference<>();
    sub.set(worker.schedulePeriodically(() -> {
      if (count.getAndIncrement() == 0) {
        sub.get().unsubscribe();
      }
    }, 0, 5, TimeUnit.MILLISECONDS));
    Thread.sleep(60);
    assertEquals(1, count.get());
  }

  @Test
  public void testUnsubscribeBetweenActions() throws Exception {
    testUnsubscribeBetweenActions(false);
  }

  @Test
  public void testUnsubscribeBetweenActionsBlocking() throws Exception {
    testUnsubscribeBetweenActions(true);
  }

  private void testUnsubscribeBetweenActions(boolean blocking) throws Exception {
    ContextScheduler scheduler2 = new ContextScheduler(vertx, blocking);
    Scheduler.Worker worker = scheduler2.createWorker();
    AtomicInteger count = new AtomicInteger();
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<Subscription> sub = new AtomicReference<>();
    sub.set(worker.schedulePeriodically(() -> {
      if (count.incrementAndGet() == 4) {
        latch.countDown();
      }
    }, 0, 20, TimeUnit.MILLISECONDS));
    awaitLatch(latch);
    sub.get().unsubscribe();
    Thread.sleep(60);
    assertEquals(4, count.get());
  }

  @Test
  public void testWorkerUnsubscribe() throws Exception {
    testWorkerUnsubscribe(false);
  }

  @Test
  public void testWorkerUnsubscribeBlocking() throws Exception  {
    testWorkerUnsubscribe(true);
  }

  private void testWorkerUnsubscribe(boolean blocking) throws Exception {
    ContextScheduler scheduler2 = new ContextScheduler(vertx, blocking);
    Scheduler.Worker worker = scheduler2.createWorker();
    CountDownLatch latch = new CountDownLatch(2);
    Subscription sub1 = worker.schedule(latch::countDown, 40, TimeUnit.MILLISECONDS);
    Subscription sub2 = worker.schedule(latch::countDown, 40, TimeUnit.MILLISECONDS);
    worker.unsubscribe();
    assertTrue(sub1.isUnsubscribed());
    assertTrue(sub2.isUnsubscribed());
    assertFalse(latch.await(40, TimeUnit.MILLISECONDS));
    assertEquals(2, latch.getCount());
  }

  @Test
  public void testPeriodicRescheduleAfterActionBlocking() {
    ContextScheduler scheduler2 = new ContextScheduler(vertx, true);
    Scheduler.Worker worker = scheduler2.createWorker();
    AtomicBoolean b = new AtomicBoolean();
    long time = System.currentTimeMillis();
    worker.schedulePeriodically(() -> {
      if (b.compareAndSet(false, true)) {
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          fail();
        }
      } else {
        assertTrue(System.currentTimeMillis() - time > 20 + 10 + 20);
        worker.unsubscribe();
        testComplete();
      }
    }, 20, 20, TimeUnit.MILLISECONDS);
  }
}
