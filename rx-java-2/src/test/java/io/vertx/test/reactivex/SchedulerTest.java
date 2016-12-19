/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.vertx.test.reactivex;


import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.reactivex.ContextScheduler;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

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
    disableThreadChecks();
  }

  @Override
  protected void tearDown() throws Exception {
    workerExecutor.close();
    super.tearDown();
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
  }

  @Test
  public void testScheduleWithDelayObserveOnReturnsOnTheCorrectThread() {
    Context testContext = vertx.getOrCreateContext();
    testContext.runOnContext(v -> {
      Scheduler scheduler = new ContextScheduler(testContext, false);
      Observable<String> observable = Observable.<String>create(subscriber -> {
        assertFalse(Context.isOnVertxThread());
        subscriber.onNext("expected");
        subscriber.onComplete();
      }).delay(10, TimeUnit.MILLISECONDS, scheduler)
        .doOnNext(o -> assertEquals(Vertx.currentContext(), testContext));
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
    AtomicReference<Disposable> sub = new AtomicReference<>();
    sub.set(worker.schedulePeriodically(() -> {
      threadAssert.run();
      if (count.incrementAndGet() > 2) {
        sub.get().dispose();
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
    Disposable sub = worker.schedule(latch::countDown, 20, TimeUnit.MILLISECONDS);
    sub.dispose();
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
    AtomicReference<Disposable> sub = new AtomicReference<>();
    sub.set(worker.schedulePeriodically(() -> {
      if (count.getAndIncrement() == 0) {
        sub.get().dispose();
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
    AtomicReference<Disposable> sub = new AtomicReference<>();
    sub.set(worker.schedulePeriodically(() -> {
      if (count.incrementAndGet() == 4) {
        latch.countDown();
      }
    }, 0, 20, TimeUnit.MILLISECONDS));
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
  public void testWorkerUnsubscribeBlocking() throws Exception {
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
    Disposable sub1 = worker.schedule(latch::countDown, 40, TimeUnit.MILLISECONDS);
    Disposable sub2 = worker.schedule(latch::countDown, 40, TimeUnit.MILLISECONDS);
    worker.dispose();
    assertTrue(sub1.isDisposed());
    assertTrue(sub2.isDisposed());
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
        worker.dispose();
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
    AtomicInteger scheduled = new AtomicInteger(0);
    AtomicInteger called = new AtomicInteger(0);
    CountDownLatch latchCalled = new CountDownLatch(1);

    RxJavaPlugins.setScheduleHandler(runnable -> {
      scheduled.incrementAndGet();
      return () -> {
        runnable.run();
        called.getAndIncrement();
        latchCalled.countDown();
      };
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
