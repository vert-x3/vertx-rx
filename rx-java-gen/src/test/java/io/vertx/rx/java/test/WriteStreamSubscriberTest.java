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

package io.vertx.rx.java.test;

import io.vertx.lang.rx.test.FakeWriteStream;
import io.vertx.rx.java.RxHelper;
import io.vertx.test.core.Repeat;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;

/**
 * @author Thomas Segismont
 */
public class WriteStreamSubscriberTest extends VertxTestBase {

  @Test
  public void testObservableErrorReported() throws Exception {
    Exception expected = new Exception();
    FakeWriteStream writeStream = new FakeWriteStream(vertx);
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(writeStream).onError(throwable -> {
      assertThat(throwable, is(sameInstance(expected)));
      complete();
    });
    Observable.<Integer>error(expected)
      .observeOn(RxHelper.scheduler(vertx))
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(subscriber);
    await();
    assertFalse("Did not expect writeStream end method to be invoked", writeStream.endInvoked());
  }

  @Test
  @Repeat(times = 10)
  public void testObservableToWriteStreamVertxThread() throws Exception {
    testObservableToWriteStream(RxHelper.scheduler(vertx.getOrCreateContext()));
  }

  @Test
  @Repeat(times = 10)
  public void testObservableToWriteStreamNonVertxThread() throws Exception {
    testObservableToWriteStream(Schedulers.from(Executors.newFixedThreadPool(5)));
  }

  private void testObservableToWriteStream(Scheduler scheduler) throws Exception {
    disableThreadChecks();
    FakeWriteStream writeStream = new FakeWriteStream(vertx);
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(writeStream).onWriteStreamEnd(this::complete);
    int count = 10000;
    Observable.range(0, count)
      .observeOn(scheduler)
      .subscribeOn(scheduler)
      .subscribe(subscriber);
    await();
    assertTrue("Expected drainHandler to be invoked", writeStream.drainHandlerInvoked());
    assertEquals(count, writeStream.getCount());
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
    Observable.<Integer>create(s -> s.onNext(0)).observeOn(RxHelper.scheduler(vertx))
      .doOnUnsubscribe(this::complete)
      .subscribeOn(RxHelper.scheduler(vertx))
      .subscribe(subscriber);
    await();
    assertFalse("Did not expect writeStream end method to be invoked", writeStream.endInvoked());
  }
}
