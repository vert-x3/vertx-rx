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

package io.vertx.rxjava.core.streams;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.core.Handler;

/**
 * Represents a stream of items that can be read from.
 * <p>
 * Any class that implements this interface can be used by a {@link Pump} to pump data from it
 * to a {@link WriteStream}.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public interface ReadStream<T> extends StreamBase {

  Object getDelegate();

  ReadStream<T> exceptionHandler(Handler<Throwable> handler);

  ReadStream<T> handler(Handler<T> handler);

  ReadStream<T> pause();

  ReadStream<T> resume();

  ReadStream<T> endHandler(Handler<Void> endHandler);

  rx.Observable<T> toObservable();


  public static <T> ReadStream<T> newInstance(io.vertx.core.streams.ReadStream arg) {
    return new ReadStreamImpl<T> (arg);
  }
}

class ReadStreamImpl<T> implements ReadStream<T> {
  final io.vertx.core.streams.ReadStream delegate;

  public ReadStreamImpl(io.vertx.core.streams.ReadStream delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<T> observable;

  public synchronized rx.Observable<T> toObservable() {
    if (observable == null) {
      observable = io.vertx.rx.java.RxHelper.toObservable((io.vertx.core.streams.ReadStream<T>) this.getDelegate());
    }
    return observable;
  }

  /**
   * Set an exception handler on the read stream.
   *
   * @param handler  the exception handler
   * @return a reference to this, so the API can be used fluently
   */
  public ReadStream<T> exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  /**
   * Set a data handler. As data is read, the handler will be called with the data.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public ReadStream<T> handler(Handler<T> handler) {
    this.delegate.handler(handler);
    return this;
  }

  /**
   * Pause the {@code ReadSupport}. While it's paused, no data will be sent to the {@code dataHandler}
   *
   * @return a reference to this, so the API can be used fluently
   */
  public ReadStream<T> pause() {
    this.delegate.pause();
    return this;
  }

  /**
   * Resume reading. If the {@code ReadSupport} has been paused, reading will recommence on it.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public ReadStream<T> resume() {
    this.delegate.resume();
    return this;
  }

  /**
   * Set an end handler. Once the stream has ended, and there is no more data to be read, this handler will be called.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public ReadStream<T> endHandler(Handler<Void> endHandler) {
    this.delegate.endHandler(endHandler);
    return this;
  }

}
