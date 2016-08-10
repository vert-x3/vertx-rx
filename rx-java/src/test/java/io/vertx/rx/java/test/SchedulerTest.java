package io.vertx.rx.java.test;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.rx.java.ContextScheduler;
import io.vertx.test.core.VertxTestBase;
import org.junit.After;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SchedulerTest extends VertxTestBase {

  private WorkerExecutor workerExecutor;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    workerExecutor = vertx.createSharedWorkerExecutor(name.getMethodName());
  }

  @After
  public void after() throws Exception {
    workerExecutor.close();

    // Cleanup  any RxJavaPlugins installed
    // Needs to hack a bit since we are not in the same package
    Method meth = RxJavaPlugins.class.getDeclaredMethod("reset");
    meth.setAccessible(true);
    meth.invoke(RxJavaPlugins.getInstance());
  }

  private void assertEventLoopThread() {
    String name = Thread.currentThread().getName();
    assertTrue("Was expecting event loop thread instead of " + name, name.startsWith("vert.x-eventloop-thread"));
  }

  private void assertWorkerThread() {
    String name = Thread.currentThread().getName();
    assertTrue("Was expecting worker thread instead of " + name, name.startsWith("vert.x-worker-thread"));
  }

  private void assertWorkerExecutorThread() {
    String thread = Thread.currentThread().getName();
    assertTrue("Was expecting worker executor thread instead of " + thread, thread.startsWith(name.getMethodName()));
  }

  @Test
  public void testScheduleImmediatly() {
    testScheduleImmediatly(() -> new ContextScheduler(vertx, false), this::assertEventLoopThread);
  }

  @Test
  public void testScheduleImmediatlyBlocking() {
    testScheduleImmediatly(() -> new ContextScheduler(vertx, true), this::assertWorkerThread);
  }

  @Test
  public void testScheduleImmediatlyWorkerExecutor() {
    testScheduleImmediatly(() -> new ContextScheduler(workerExecutor), this::assertWorkerExecutorThread);
  }

  private void testScheduleImmediatly(Supplier<ContextScheduler> scheduler, Runnable threadAssert) {
    ContextScheduler scheduler2 = scheduler.get();
    Scheduler.Worker worker = scheduler2.createWorker();
    worker.schedule(() -> {
      threadAssert.run();
      testComplete();
    }, 0, TimeUnit.MILLISECONDS);
    await();
  }

  @Test
  public void testScheduleObserveOnReturnsOnTheCorrectThread() {
    Context testContext = vertx.getOrCreateContext();
    testContext.runOnContext(v -> {
      Scheduler scheduler = new ContextScheduler(testContext, false);
      Observable<String> observable = Observable.<String>create(subscriber -> {
        assertFalse(Context.isOnVertxThread());
        subscriber.onNext("expected");
        subscriber.onCompleted();
      }).observeOn(scheduler).doOnNext(o -> assertEquals(Vertx.currentContext(), testContext));
      new Thread(() -> {
        observable.subscribe(
            item -> assertEquals("expected", item),
            this::fail,
            this::testComplete);
      }).start();
    });
    await();
  }

  @Test
  public void testScheduleWithDelayObserveOnReturnsOnTheCorrectThread() {
    Context testContext = vertx.getOrCreateContext();
    testContext.runOnContext(v -> {
      Scheduler scheduler = new ContextScheduler(testContext, false);
      Observable<String> observable = Observable.<String>create(subscriber -> {
        assertFalse(Context.isOnVertxThread());
        subscriber.onNext("expected");
        subscriber.onCompleted();
      }).delay(10, TimeUnit.MILLISECONDS, scheduler).doOnNext(o -> assertEquals(Vertx.currentContext(), testContext));
      new Thread(() -> {
        observable.subscribe(
            item -> assertEquals("expected", item),
            this::fail,
            this::testComplete);
      }).start();
    });
    await();
  }

  @Test
  public void testScheduleDelayed() {
    testScheduleDelayed(() -> new ContextScheduler(vertx, false), this::assertEventLoopThread);
  }

  @Test
  public void testScheduleDelayedBlocking() {
    testScheduleDelayed(() -> new ContextScheduler(vertx, true), this::assertWorkerThread);
  }

  @Test
  public void testScheduleDelayedWorkerExecutor() {
    testScheduleDelayed(() -> new ContextScheduler(workerExecutor), this::assertWorkerExecutorThread);
  }

  private void testScheduleDelayed(Supplier<ContextScheduler> scheduler, Runnable threadAssert) {
    ContextScheduler scheduler2 = scheduler.get();
    Scheduler.Worker worker = scheduler2.createWorker();
    long time = System.currentTimeMillis();
    worker.schedule(() -> {
      threadAssert.run();
      assertTrue(System.currentTimeMillis() - time >= 40);
      testComplete();
    }, 40, TimeUnit.MILLISECONDS);
    await();
  }

  @Test
  public void testSchedulePeriodic() {
    testSchedulePeriodic(() -> new ContextScheduler(vertx, false), this::assertEventLoopThread);
  }

  @Test
  public void testSchedulePeriodicBlocking() {
    testSchedulePeriodic(() -> new ContextScheduler(vertx, true), this::assertWorkerThread);
  }

  @Test
  public void testSchedulePeriodicWorkerExecutor() {
    testSchedulePeriodic(() -> new ContextScheduler(workerExecutor), this::assertWorkerExecutorThread);
  }

  private void testSchedulePeriodic(Supplier<ContextScheduler> scheduler, Runnable threadAssert) {
    ContextScheduler scheduler2 = scheduler.get();
    Scheduler.Worker worker = scheduler2.createWorker();
    AtomicLong time = new AtomicLong(System.currentTimeMillis() - 40);
    AtomicInteger count = new AtomicInteger();
    AtomicReference<Subscription> sub = new AtomicReference<>();
    sub.set(worker.schedulePeriodically(() -> {
      threadAssert.run();
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
    testUnsubscribeBeforeExecute(() -> new ContextScheduler(vertx, false));
  }

  @Test
  public void testUnsubscribeBeforeExecuteBlocking() throws Exception {
    testUnsubscribeBeforeExecute(() -> new ContextScheduler(vertx, true));
  }

  @Test
  public void testUnsubscribeBeforeExecuteWorkerExecutor() throws Exception {
    testUnsubscribeBeforeExecute(() -> new ContextScheduler(workerExecutor));
  }

  private void testUnsubscribeBeforeExecute(Supplier<ContextScheduler> scheduler) throws Exception {
    ContextScheduler scheduler2 = scheduler.get();
    Scheduler.Worker worker = scheduler2.createWorker();
    CountDownLatch latch = new CountDownLatch(1);
    Subscription sub = worker.schedule(latch::countDown, 20, TimeUnit.MILLISECONDS);
    sub.unsubscribe();
    assertFalse(latch.await(40, TimeUnit.MILLISECONDS));
  }

  @Test
  public void testUnsubscribeDuringExecute() throws Exception {
    testUnsubscribeDuringExecute(() -> new ContextScheduler(vertx, false));
  }

  @Test
  public void testUnsubscribeDuringExecuteBlocking() throws Exception {
    testUnsubscribeDuringExecute(() -> new ContextScheduler(vertx, true));
  }

  @Test
  public void testUnsubscribeDuringExecuteWorkerExecutor() throws Exception {
    testUnsubscribeDuringExecute(() -> new ContextScheduler(workerExecutor));
  }

  private void testUnsubscribeDuringExecute(Supplier<ContextScheduler> scheduler) throws Exception {
    ContextScheduler scheduler2 = scheduler.get();
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
    testUnsubscribeBetweenActions(() -> new ContextScheduler(vertx, false));
  }

  @Test
  public void testUnsubscribeBetweenActionsBlocking() throws Exception {
    testUnsubscribeBetweenActions(() -> new ContextScheduler(vertx, true));
  }

  @Test
  public void testUnsubscribeBetweenActionsWorkerExecutor() throws Exception {
    testUnsubscribeBetweenActions(() -> new ContextScheduler(workerExecutor));
  }

  private void testUnsubscribeBetweenActions(Supplier<ContextScheduler> scheduler) throws Exception {
    ContextScheduler scheduler2 = scheduler.get();
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
    testWorkerUnsubscribe(() -> new ContextScheduler(vertx, false));
  }

  @Test
  public void testWorkerUnsubscribeBlocking() throws Exception  {
    testWorkerUnsubscribe(() -> new ContextScheduler(vertx, true));
  }

  @Test
  public void testWorkerUnsubscribeWorkerExecutor() throws Exception {
    testWorkerUnsubscribe(() -> new ContextScheduler(workerExecutor));
  }

  private void testWorkerUnsubscribe(Supplier<ContextScheduler> scheduler) throws Exception {
    ContextScheduler scheduler2 = scheduler.get();
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

  @Test
  public void testSchedulerHook() throws Exception {
    testSchedulerHook(() -> new ContextScheduler(vertx, false));
  }

  @Test
  public void testSchedulerHookBlocking() throws Exception {
    testSchedulerHook(() -> new ContextScheduler(vertx, true));
  }

  @Test
  public void testSchedulerHookWorkerExecutor() throws Exception {
    testSchedulerHook(() -> new ContextScheduler(workerExecutor));
  }

  private void testSchedulerHook(Supplier<ContextScheduler> scheduler) throws Exception {
    RxJavaPlugins plugins = RxJavaPlugins.getInstance();
    AtomicInteger scheduled = new AtomicInteger();
    AtomicInteger called = new AtomicInteger();
    CountDownLatch latchCalled = new CountDownLatch(1);
    plugins.registerSchedulersHook(new RxJavaSchedulersHook() {
      @Override
      public Action0 onSchedule(Action0 action) {
        scheduled.incrementAndGet();
        return () -> {
          action.call();
          called.getAndIncrement();
          latchCalled.countDown();
        };
      }
    });

    ContextScheduler scheduler2 = scheduler.get();
    Scheduler.Worker worker = scheduler2.createWorker();
    assertEquals(0, scheduled.get());
    assertEquals(0, called.get());
    CountDownLatch latch = new CountDownLatch(1);
    worker.schedule(() -> {
      latch.countDown();
      assertEquals(1, scheduled.get());
      assertEquals(0, called.get());
    }, 0, TimeUnit.SECONDS);
    awaitLatch(latch);
    awaitLatch(latchCalled);
    assertEquals(1, scheduled.get());
    assertEquals(1, called.get());
  }
}
