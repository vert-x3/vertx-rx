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
import io.vertx.reactivex.ContextScheduler;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

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
  public void testScheduleObserveOnReturnsOnTheCorrectThread() {
    Context testContext = vertx.getOrCreateContext();
    testContext.runOnContext(v -> {
      Scheduler scheduler = new ContextScheduler(testContext, false);
      Observable<String> observable = Observable.<String>create(subscriber -> {
        org.junit.Assert.assertFalse(Context.isOnVertxThread());
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
        org.junit.Assert.assertFalse(Context.isOnVertxThread());
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
    AtomicReference<Disposable> sub = new AtomicReference<>();
    sub.set(worker.schedulePeriodically(() -> {
      if (blocking) {
        assertWorkerThread();
      } else {
        assertEventLoopThread();
      }
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
    Disposable sub = worker.schedule(latch::countDown, 20, TimeUnit.MILLISECONDS);
    sub.dispose();
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
    testWorkerUnsubscribe(false);
  }

  @Test
  public void testWorkerUnsubscribeBlocking() throws Exception {
    testWorkerUnsubscribe(true);
  }

  private void testWorkerUnsubscribe(boolean blocking) throws Exception {
    ContextScheduler scheduler2 = new ContextScheduler(vertx, blocking);
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
    testSchedulerHook(false);
  }

  @Test
  public void testSchedulerHookBlocking() throws Exception {
    testSchedulerHook(true);
  }

  private void testSchedulerHook(boolean blocking) throws Exception {
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

    ContextScheduler scheduler = new ContextScheduler(vertx, blocking);
    Scheduler.Worker worker = scheduler.createWorker();
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
