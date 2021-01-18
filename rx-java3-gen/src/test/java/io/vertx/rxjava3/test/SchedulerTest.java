package io.vertx.rxjava3.test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.rxjava3.ContextScheduler;
import io.vertx.rxjava3.RxHelper;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.*;

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

  @Override
  protected void tearDown() throws Exception {
    workerExecutor.close();
    super.tearDown();
    RxJavaPlugins.setScheduleHandler(null);
  }

  private void assertEventLoopThread(Thread thread) {
    String threadName = thread.getName();
    assertTrue("Was expecting event loop thread instead of " + threadName, threadName.startsWith("vert.x-eventloop-thread"));
  }

  private void assertWorkerThread(Thread thread) {
    String threadName = thread.getName();
    assertTrue("Was expecting worker thread instead of " + threadName, threadName.startsWith("vert.x-worker-thread"));
  }

  private void assertWorkerExecutorThread(Thread thread) {
    String threadName = thread.getName();
    assertTrue("Was expecting worker executor thread instead of " + threadName, threadName.startsWith(name.getMethodName()));
  }

  @Test
  public void testScheduleImmediatly() throws Exception {
    testScheduleImmediatly(() -> new ContextScheduler(vertx, false), this::assertEventLoopThread);
  }

  @Test
  public void testScheduleImmediatlyBlocking() throws Exception {
    testScheduleImmediatly(() -> new ContextScheduler(vertx, true), this::assertWorkerThread);
  }

  @Test
  public void testScheduleImmediatlyWorkerExecutor() throws Exception {
    testScheduleImmediatly(() -> new ContextScheduler(workerExecutor), this::assertWorkerExecutorThread);
  }

  private void testScheduleImmediatly(Supplier<ContextScheduler> scheduler, Consumer<Thread> threadAssert) throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    ContextScheduler scheduler2 = scheduler.get();
    Scheduler.Worker worker = scheduler2.createWorker();
    AtomicReference<Thread> thread = new AtomicReference<>();
    worker.schedule(() -> {
      thread.set(Thread.currentThread());
      latch.countDown();
    }, 0, MILLISECONDS);
    awaitLatch(latch);
    threadAssert.accept(thread.get());
  }

  @Test
  public void testScheduleObserveOnReturnsOnTheCorrectThread() {
    Context testContext = vertx.getOrCreateContext();
    AtomicBoolean isOnVertxThread = new AtomicBoolean();
    testContext.runOnContext(v -> {
      Scheduler scheduler = new ContextScheduler(testContext, false);
      Observable<String> observable = Observable.<String>create(subscriber -> {
        isOnVertxThread.set(Context.isOnVertxThread());
        subscriber.onNext("expected");
        subscriber.onComplete();
      }).observeOn(scheduler).doOnNext(o -> assertEquals(Vertx.currentContext(), testContext));
      new Thread(() -> {
        observable.subscribe(
            item -> assertEquals("expected", item),
            this::fail,
            this::testComplete);
      }).start();
    });
    await();
    assertFalse(isOnVertxThread.get());
  }

  @Test
  public void testScheduleWithDelayObserveOnReturnsOnTheCorrectThread() {
    Context testContext = vertx.getOrCreateContext();
    AtomicBoolean isOnVertxThread = new AtomicBoolean();
    testContext.runOnContext(v -> {
      Scheduler scheduler = new ContextScheduler(testContext, false);
      Observable<String> observable = Observable.<String>create(subscriber -> {
        isOnVertxThread.set(Context.isOnVertxThread());
        subscriber.onNext("expected");
        subscriber.onComplete();
      }).delay(10, MILLISECONDS, scheduler).doOnNext(o -> assertEquals(Vertx.currentContext(), testContext));
      new Thread(() -> {
        observable.subscribe(
            item -> assertEquals("expected", item),
            this::fail,
            this::testComplete);
      }).start();
    });
    assertFalse(isOnVertxThread.get());
    await();
  }

  @Test
  public void testScheduleDelayed() throws Exception {
    testScheduleDelayed(() -> new ContextScheduler(vertx, false), this::assertEventLoopThread);
  }

  @Test
  public void testScheduleDelayedBlocking() throws Exception {
    testScheduleDelayed(() -> new ContextScheduler(vertx, true), this::assertWorkerThread);
  }

  @Test
  public void testScheduleDelayedWorkerExecutor() throws Exception {
    testScheduleDelayed(() -> new ContextScheduler(workerExecutor), this::assertWorkerExecutorThread);
  }

  private void testScheduleDelayed(Supplier<ContextScheduler> scheduler, Consumer<Thread> threadAssert) throws Exception {
    ContextScheduler scheduler2 = scheduler.get();
    Scheduler.Worker worker = scheduler2.createWorker();
    long time = System.currentTimeMillis();
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<Thread> thread = new AtomicReference<>();
    AtomicLong execTime = new AtomicLong();
    worker.schedule(() -> {
      thread.set(Thread.currentThread());
      execTime.set(System.currentTimeMillis() - time);
      latch.countDown();
    }, 40, MILLISECONDS);
    awaitLatch(latch);
    threadAssert.accept(thread.get());
    assertTrue(execTime.get() >= 40);
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

  private void testSchedulePeriodic(Supplier<ContextScheduler> scheduler, Consumer<Thread> threadAssert) {
    disableThreadChecks();
    ContextScheduler scheduler2 = scheduler.get();
    Scheduler.Worker worker = scheduler2.createWorker();
    AtomicLong time = new AtomicLong(System.currentTimeMillis() - 40);
    AtomicInteger count = new AtomicInteger();
    AtomicReference<Disposable> sub = new AtomicReference<>();
    sub.set(worker.schedulePeriodically(() -> {
      threadAssert.accept(Thread.currentThread());
      if (count.incrementAndGet() > 2) {
        sub.get().dispose();
        testComplete();
      } else {
        long now = System.currentTimeMillis();
        long delta = now - time.get();
        assertTrue("" + delta, delta >= 40);
        time.set(now);
      }
    }, 0, 40, MILLISECONDS));
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
    Disposable sub = worker.schedule(latch::countDown, 20, MILLISECONDS);
    sub.dispose();
    assertFalse(latch.await(40, MILLISECONDS));
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
    AtomicReference<Disposable> sub = new AtomicReference<>();
    sub.set(worker.schedulePeriodically(() -> {
      if (count.getAndIncrement() == 0) {
        sub.get().dispose();
      }
    }, 0, 5, MILLISECONDS));
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
    AtomicReference<Disposable> sub = new AtomicReference<>();
    sub.set(worker.schedulePeriodically(() -> {
      if (count.incrementAndGet() == 4) {
        latch.countDown();
      }
    }, 0, 20, MILLISECONDS));
    awaitLatch(latch);
    sub.get().dispose();
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
    Disposable sub1 = worker.schedule(latch::countDown, 40, MILLISECONDS);
    Disposable sub2 = worker.schedule(latch::countDown, 40, MILLISECONDS);
    worker.dispose();
    assertTrue(sub1.isDisposed());
    assertTrue(sub2.isDisposed());
    assertFalse(latch.await(40, MILLISECONDS));
    assertEquals(2, latch.getCount());
  }

  @Test
  public void testPeriodicRescheduleAfterActionBlocking() {
    ContextScheduler scheduler2 = new ContextScheduler(vertx, true);
    Scheduler.Worker worker = scheduler2.createWorker();
    AtomicBoolean b = new AtomicBoolean();
    long time = System.nanoTime();
    worker.schedulePeriodically(() -> {
      if (b.compareAndSet(false, true)) {
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          fail();
        }
      } else {
        assertTrue(System.nanoTime() - time > NANOSECONDS.convert(20 + 10 + 20, MILLISECONDS));
        worker.dispose();
        testComplete();
      }
    }, 20, 20, MILLISECONDS);
    await();
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
    AtomicInteger scheduled = new AtomicInteger();
    AtomicInteger called = new AtomicInteger();
    CountDownLatch latchCalled = new CountDownLatch(1);
    RxJavaPlugins.setScheduleHandler(action -> {
      scheduled.incrementAndGet();
      return () -> {
        action.run();
        called.getAndIncrement();
        latchCalled.countDown();
      };
    });

    ContextScheduler scheduler2 = scheduler.get();
    Scheduler.Worker worker = scheduler2.createWorker();
    assertEquals(0, scheduled.get());
    assertEquals(0, called.get());
    CountDownLatch latch = new CountDownLatch(1);
    AtomicInteger workerScheduledVal = new AtomicInteger();
    AtomicInteger workerCalledVal = new AtomicInteger();
    worker.schedule(() -> {
      workerScheduledVal.set(scheduled.get());
      workerCalledVal.set(called.get());
      latch.countDown();
    }, 0, SECONDS);
    awaitLatch(latch);
    awaitLatch(latchCalled);
    assertEquals(1, scheduled.get());
    assertEquals(1, called.get());
    assertEquals(1, workerScheduledVal.get());
    assertEquals(0, workerCalledVal.get());
  }

  @Test
  public void testRemovedFromContextAfterRun() throws Exception {
    ContextScheduler scheduler = (ContextScheduler) RxHelper.blockingScheduler(vertx);
    ContextScheduler.ContextWorker worker = scheduler.createWorker();
    CountDownLatch latch = new CountDownLatch(1);
    worker.schedule(latch::countDown);
    awaitLatch(latch);
    waitUntil(() -> worker.countActions() == 0);
  }

  @Test
  public void testRemovedFromContextAfterDelay() throws Exception {
    ContextScheduler scheduler = (ContextScheduler) RxHelper.blockingScheduler(vertx);
    ContextScheduler.ContextWorker worker = scheduler.createWorker();
    CountDownLatch latch = new CountDownLatch(1);
    worker.schedule(latch::countDown, 10, MILLISECONDS);
    awaitLatch(latch);
    waitUntil(() -> worker.countActions() == 0);
  }

  @Test
  public void testUnsubscribePeriodicInTask() throws Exception {
    ContextScheduler scheduler = (ContextScheduler) RxHelper.blockingScheduler(vertx);
    ContextScheduler.ContextWorker worker = scheduler.createWorker();
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<Disposable> ref = new AtomicReference<>();
    ref.set(worker.schedulePeriodically(() -> {
      Disposable disposable;
      while ((disposable = ref.get()) == null) {
        Thread.yield();
      }
      disposable.dispose();
      latch.countDown();
    }, 10, 10, MILLISECONDS));
    awaitLatch(latch);
    waitUntil(() -> worker.countActions() == 0);
  }
}
