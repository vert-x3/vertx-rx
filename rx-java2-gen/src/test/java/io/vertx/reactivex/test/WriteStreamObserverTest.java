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

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.vertx.lang.rx.test.FakeWriteStream;
import io.vertx.reactivex.impl.WriteStreamObserver;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Thomas Segismont
 */
public class WriteStreamObserverTest extends VertxTestBase {

  @Test
  public void testObservableErrorReported() throws Exception {
    Exception expected = new Exception();
    AtomicReference<Throwable> throwable = new AtomicReference<>();
    Observer<Integer> observer = new WriteStreamObserver<>(new FakeWriteStream(vertx), t -> {
      if (throwable.compareAndSet(null, t)) {
        complete();
      } else {
        fail("onError invoked twice");
      }
    }, this::fail);
    Observable.<Integer>error(expected)
      .subscribe(observer);
    assertSame(expected, throwable.get());
  }

  @Test
  public void testObservableToWriteStream() throws Exception {
    FakeWriteStream writeStream = new FakeWriteStream(vertx);
    Observer<Integer> observer = new WriteStreamObserver<>(writeStream, this::fail, this::complete);
    Observable.range(0, 10000)
      .subscribe(observer);
    await();
    assertFalse("Did not expect drainHandler to be invoked", writeStream.drainHandlerInvoked());
  }

}
