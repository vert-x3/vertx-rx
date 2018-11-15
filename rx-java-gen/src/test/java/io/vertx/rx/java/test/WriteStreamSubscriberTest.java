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
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Thomas Segismont
 */
public class WriteStreamSubscriberTest extends VertxTestBase {

  @Test
  public void testObservableErrorReported() throws Exception {
    Exception expected = new Exception();
    AtomicReference<Throwable> throwable = new AtomicReference<>();
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(new FakeWriteStream(vertx), t -> {
      if (throwable.compareAndSet(null, t)) {
        complete();
      } else {
        fail("onError invoked twice");
      }
    }, this::fail);
    Observable.<Integer>error(expected)
      .subscribe(subscriber);
    assertSame(expected, throwable.get());
  }

  @Test
  public void testObservableToWriteStream() throws Exception {
    FakeWriteStream writeStream = new FakeWriteStream(vertx);
    Subscriber<Integer> subscriber = RxHelper.toSubscriber(writeStream, this::fail, this::complete);
    Observable.range(0, 10000)
      .subscribe(subscriber);
    await();
    assertTrue("Expected drainHandler to be invoked", writeStream.drainHandlerInvoked());
  }
}
