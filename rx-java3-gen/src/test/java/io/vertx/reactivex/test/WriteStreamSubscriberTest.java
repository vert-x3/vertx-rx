/*
 * Copyright 2018 Red Hat, Inc.
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

package io.vertx.reactivex.test;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.exceptions.ProtocolViolationException;
import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vertx.lang.rx.test.FakeWriteStream;
import io.vertx.reactivex.RxHelper;
import io.vertx.test.core.Repeat;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import org.reactivestreams.Subscriber;

import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.*;

/**
 * @author Thomas Segismont
 */
public class WriteStreamSubscriberTest extends VertxTestBase {

  @Override
  protected void tearDown() throws Exception {
    RxJavaPlugins.reset();
    super.tearDown();
  }

  @Test
  public void testFlowableErrorReported() throws Exception {
    Exception expected = new Exception();
    FakeWriteStream writeStream = new FakeWriteStream(vertx);
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(writeStream).onError(throwable -> {
      assertThat(throwable, is(sameInstance(expected)));
      complete();
    });
    Flowable.<Integer>error(expected)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(subscriber);
    await();
    assertFalse("Did not expect writeStream end method to be invoked", writeStream.endInvoked());
  }

  @Test
  @Repeat(times = 10)
  public void testFlowableToWriteStreamVertxThread() throws Exception {
    testFlowableToWriteStream(RxHelper.scheduler(vertx.getOrCreateContext()));
  }

  @Test
  @Repeat(times = 10)
  public void testFlowableToWriteStreamNonVertxThread() throws Exception {
    testFlowableToWriteStream(Schedulers.from(Executors.newFixedThreadPool(5)));
  }

  private void testFlowableToWriteStream(Scheduler scheduler) throws Exception {
    FakeWriteStream writeStream = new FakeWriteStream(vertx);
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(writeStream).onWriteStreamEnd(this::complete);
    int count = 10000;
    Flowable.range(0, count)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(subscriber);
    await();
    assertTrue("Expected drainHandler to be invoked", writeStream.drainHandlerInvoked());
    assertEquals(count, writeStream.getCount());
    assertTrue("Expected writeStream end method to be invoked", writeStream.endInvoked());
  }

  @Test
  public void testCannotSubscribeTwice() throws Exception {
    waitFor(3);
    RxJavaPlugins.setErrorHandler(throwable -> {
      assertThat(throwable, is(instanceOf(ProtocolViolationException.class)));
      complete();
    });
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(new FakeWriteStream(vertx)).onWriteStreamEnd(this::complete);
    Flowable.range(0, 100)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(subscriber);
    Flowable.<Integer>create(emitter -> emitter.setCancellable(this::complete), BackpressureStrategy.MISSING)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(subscriber);
    await();
  }

  @Test
  public void testOnNextThrowsFatal() throws Exception {
    UnknownError expected = new UnknownError();
    vertx.exceptionHandler(throwable -> {
      assertThat(throwable, is(sameInstance(expected)));
      complete();
    });
    FakeWriteStream writeStream = new FakeWriteStream(vertx).setOnWrite(() -> {
      throw expected;
    });
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(writeStream);
    Flowable.just(0)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(subscriber);
    await();
    assertFalse("Did not expect writeStream end method to be invoked", writeStream.endInvoked());
  }

  @Test
  public void testWriteThrowsException() throws Exception {
    waitFor(2);
    RuntimeException expected = new RuntimeException();
    FakeWriteStream writeStream = new FakeWriteStream(vertx).setOnWrite(() -> {
      throw expected;
    });
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(writeStream).onError(throwable -> {
      assertThat(throwable, is(sameInstance(expected)));
      complete();
    });
    Flowable.<Integer>create(emitter -> {
      emitter.setCancellable(this::complete);
      emitter.onNext(0);
    }, BackpressureStrategy.MISSING)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(subscriber);
    await();
    assertFalse("Did not expect writeStream end method to be invoked", writeStream.endInvoked());
  }

  @Test
  public void testOnErrorThrowsException() throws Exception {
    RuntimeException expected = new RuntimeException();
    RxJavaPlugins.setErrorHandler(throwable -> {
      assertThat(throwable, is(instanceOf(UndeliverableException.class)));
      assertThat(throwable.getCause(), is(sameInstance(expected)));
      complete();
    });
    FakeWriteStream writeStream = new FakeWriteStream(vertx);
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(writeStream).onError(throwable -> {
      throw expected;
    });
    Flowable.<Integer>error(new RuntimeException())
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(subscriber);
    await();
    assertFalse("Did not expect writeStream end method to be invoked", writeStream.endInvoked());
  }

  @Test
  public void testOnWriteStreamEndThrowsException() throws Exception {
    RuntimeException expected = new RuntimeException();
    RxJavaPlugins.setErrorHandler(throwable -> {
      assertThat(throwable, is(instanceOf(UndeliverableException.class)));
      assertThat(throwable.getCause(), is(sameInstance(expected)));
      complete();
    });
    FakeWriteStream writeStream = new FakeWriteStream(vertx);
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(writeStream).onWriteStreamEnd(() -> {
      throw expected;
    });
    Flowable.just(0)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(subscriber);
    await();
    assertTrue("Expected writeStream end method to be invoked", writeStream.endInvoked());
  }

  @Test
  public void testWriteStreamError() throws Exception {
    waitFor(2);
    RuntimeException expected = new RuntimeException();
    FakeWriteStream writeStream = new FakeWriteStream(vertx).failAfterWrite(expected);
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(writeStream).onWriteStreamError(throwable -> {
      assertThat(throwable, is(sameInstance(expected)));
      complete();
    });
    Flowable.<Integer>create(emitter -> {
      emitter.setCancellable(this::complete);
      emitter.onNext(0);
    }, BackpressureStrategy.MISSING)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(subscriber);
    await();
    assertFalse("Did not expect writeStream end method to be invoked", writeStream.endInvoked());
  }
}
