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

package io.vertx.reactivex;

import io.reactivex.Observer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * A {@link io.vertx.core.streams.WriteStream} to {@link Observer} adapter.
 *
 * @author Thomas Segismont
 */
public interface WriteStreamObserver<R> extends Observer<R> {

  /**
   * Sets the handler to invoke if the {@link io.reactivex.Observable} that was subscribed to terminates with an error.
   * <p>
   * The underlying {@link io.vertx.core.streams.WriteStream#end()} method is <strong>not</strong> invoked in this case.
   *
   * @return a reference to this, so the API can be used fluently
   */
  WriteStreamObserver<R> onError(Consumer<? super Throwable> handler);

  /**
   * Sets the handler to invoke if the {@link io.reactivex.Observable} that was subscribed to terminates successfully.
   * <p>
   * The underlying {@link io.vertx.core.streams.WriteStream#end()} method is invoked <strong>before</strong> the given {@code handler}.
   *
   * @return a reference to this, so the API can be used fluently
   * @deprecated because the {@code handler} may be called while there are outstanding writes in the underlying {@link io.vertx.core.streams.WriteStream}.
   * Use {@link #onWriteStreamEnd(Action)} instead.
   */
  @Deprecated
  WriteStreamObserver<R> onComplete(Action handler);

  /**
   * Sets the handler to invoke if the adapted {@link io.vertx.core.streams.WriteStream} fails.
   * <p>
   * The underlying {@link io.vertx.core.streams.WriteStream#end()} method is <strong>not</strong> invoked in this case.
   *
   * @return a reference to this, so the API can be used fluently
   */
  WriteStreamObserver<R> onWriteStreamError(Consumer<? super Throwable> handler);

  /**
   * Sets the handler to invoke when the adapted {@link io.vertx.core.streams.WriteStream} ends successfully.
   *
   * @return a reference to this, so the API can be used fluently
   */
  WriteStreamObserver<R> onWriteStreamEnd(Action handler);

  /**
   * Sets the handler to invoke when the adapted {@link io.vertx.core.streams.WriteStream} ends with an error.
   *
   * @return a reference to this, so the API can be used fluently
   */
  WriteStreamObserver<R> onWriteStreamEndError(Consumer<? super Throwable> handler);
}
