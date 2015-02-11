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

package io.vertx.rxjava.core;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.core.Handler;

/**
 * A timeout stream is triggered by a timer, the {@link io.vertx.core.Handler} will be call when the timer is fired,
 * it can be once or several times depending on the nature of the timer related to this stream. The
 * {@link ReadStream#endHandler(Handler)} will be called after the timer handler has been called.
 * <p>
 * Pausing the timer inhibits the timer shots until the stream is resumed. Setting a null handler callback cancels
 * the timer.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class TimeoutStream implements ReadStream<Long> {

  final io.vertx.core.TimeoutStream delegate;

  public TimeoutStream(io.vertx.core.TimeoutStream delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<Long> observable;

  public synchronized rx.Observable<Long> toObservable() {
    if (observable == null) {
      observable = io.vertx.rx.java.RxHelper.toObservable((io.vertx.core.streams.ReadStream<java.lang.Long>) this.getDelegate());
    }
    return observable;
  }

  public TimeoutStream exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  public TimeoutStream handler(Handler<Long> handler) {
    this.delegate.handler(handler);
    return this;
  }

  public TimeoutStream pause() {
    this.delegate.pause();
    return this;
  }

  public TimeoutStream resume() {
    this.delegate.resume();
    return this;
  }

  public TimeoutStream endHandler(Handler<Void> endHandler) {
    this.delegate.endHandler(endHandler);
    return this;
  }

  /**
   * Cancels the timeout. Note this has the same effect as calling {@link #handler(Handler)} with a null
   * argument.
   */
  public void cancel() {
    this.delegate.cancel();
  }


  public static TimeoutStream newInstance(io.vertx.core.TimeoutStream arg) {
    return new TimeoutStream(arg);
  }
}
