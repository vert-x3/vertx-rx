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

package io.vertx.rx.java;

import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * A {@link io.vertx.core.streams.WriteStream} to {@link Subscriber} adapter.
 *
 * @author Thomas Segismont
 */
public abstract class WriteStreamSubscriber<R> extends Subscriber<R> {

  /**
   * Sets the handler to invoke if the {@link rx.Observable} that was subscribed to terminates with an error.
   * <p>
   * The underlying {@link io.vertx.core.streams.WriteStream#end()} method is <strong>not</strong> invoked in this case.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public abstract WriteStreamSubscriber<R> onError(Action1<Throwable> handler);

  /**
   * Sets the handler to invoke if the {@link rx.Observable} that was subscribed to terminates successfully.
   * <p>
   * The underlying {@link io.vertx.core.streams.WriteStream#end()} method is invoked <strong>before</strong> the given {@code handler}.
   *
   * @return a reference to this, so the API can be used fluently
   * @deprecated because the {@code handler} may be called while there are outstanding writes in the underlying {@link io.vertx.core.streams.WriteStream}.
   * Use {@link #onWriteStreamEnd(Action0)} instead.
   */
  @Deprecated
  public abstract WriteStreamSubscriber<R> onComplete(Action0 handler);

  /**
   * Sets the handler to invoke if the adapted {@link io.vertx.core.streams.WriteStream} fails.
   * <p>
   * The underlying {@link io.vertx.core.streams.WriteStream#end()} method is <strong>not</strong> invoked in this case.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public abstract WriteStreamSubscriber<R> onWriteStreamError(Action1<Throwable> handler);

  /**
   * Sets the handler to invoke when the adapted {@link io.vertx.core.streams.WriteStream} ends successfully.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public abstract WriteStreamSubscriber<R> onWriteStreamEnd(Action0 handler);

  /**
   * Sets the handler to invoke when the adapted {@link io.vertx.core.streams.WriteStream} ends with an error.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public abstract WriteStreamSubscriber<R> onWriteStreamEndError(Action1<Throwable> handler);
}
