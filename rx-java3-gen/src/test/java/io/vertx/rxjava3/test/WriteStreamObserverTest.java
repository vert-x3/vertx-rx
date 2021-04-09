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

package io.vertx.rxjava3.test;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.exceptions.ProtocolViolationException;
import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.lang.rx.test.FakeWriteStream;
import io.vertx.rxjava3.RxHelper;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

/**
 * @author Thomas Segismont
 */
public class WriteStreamObserverTest extends VertxTestBase {

  @Override
  protected void tearDown() throws Exception {
    RxJavaPlugins.reset();
    super.tearDown();
  }

  @Test
  public void testObservableErrorReported() throws Exception {
    Exception expected = new Exception();
    FakeWriteStream writeStream = new FakeWriteStream(vertx);
    Observer<Integer> observer = RxHelper.toObserver(writeStream).onError(throwable -> {
      assertThat(throwable, is(sameInstance(expected)));
      complete();
    });
    Observable.<Integer>error(expected)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(observer);
    await();
    assertFalse("Did not expect writeStream end method to be invoked", writeStream.endInvoked());
  }

  @Test
  public void testObservableToWriteStream() throws Exception {
    FakeWriteStream writeStream = new FakeWriteStream(vertx);
    Observer<Integer> observer = RxHelper.toObserver(writeStream).onWriteStreamEnd(this::complete);
    int count = 10000;
    Observable.range(0, count)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(observer);
    await();
    assertFalse("Did not expect drainHandler to be invoked", writeStream.drainHandlerInvoked());
    assertEquals(count, writeStream.getCount());
    assertTrue("Expected writeStream end method to be invoked", writeStream.endInvoked());
  }

  @Test
  public void testCannotSubscribeTwice() throws Exception {
    waitFor(2);
    RxJavaPlugins.setErrorHandler(throwable -> {
      assertThat(throwable, is(instanceOf(ProtocolViolationException.class)));
      complete();
    });
    Observer<Integer> observer = RxHelper.toObserver(new FakeWriteStream(vertx)).onWriteStreamEnd(this::complete);
    Observable.range(0, 100)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(observer);
    Observable.range(0, 100)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(observer);
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
    Observer<Integer> observer = RxHelper.toObserver(writeStream);
    Observable.just(0)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(observer);
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
    Observer<Integer> observer = RxHelper.toObserver(writeStream).onError(throwable -> {
      assertThat(throwable, is(sameInstance(expected)));
      complete();
    });
    Observable.<Integer>create(emitter -> {
      emitter.setCancellable(this::complete);
      emitter.onNext(0);
    }).observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(observer);
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
    Observer<Integer> observer = RxHelper.toObserver(writeStream).onError(throwable -> {
      throw expected;
    });
    Observable.<Integer>error(new RuntimeException())
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(observer);
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
    Observer<Integer> observer = RxHelper.toObserver(writeStream).onWriteStreamEnd(() -> {
      throw expected;
    });
    Observable.just(0)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(observer);
    await();
    assertTrue("Expected writeStream end method to be invoked", writeStream.endInvoked());
  }

  @Test
  public void testWriteStreamError() throws Exception {
    waitFor(2);
    RuntimeException expected = new RuntimeException();
    FakeWriteStream writeStream = new FakeWriteStream(vertx).failAfterWrite(expected);
    Observer<Integer> observer = RxHelper.toObserver(writeStream).onWriteStreamError(throwable -> {
      assertThat(throwable, is(sameInstance(expected)));
      complete();
    });
    Observable.<Integer>create(emitter -> {
      emitter.setCancellable(this::complete);
      emitter.onNext(0);
    }).observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(observer);
    await();
    assertFalse("Did not expect writeStream end method to be invoked", writeStream.endInvoked());
  }
}
