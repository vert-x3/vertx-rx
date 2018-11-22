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
import io.vertx.core.Handler;

/**
 * A {@link io.vertx.core.streams.WriteStream} to {@link Observer} adpater.
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
  WriteStreamObserver<R> observableErrorHandler(Handler<Throwable> observableErrorHandler);

  /**
   * Sets the handler to invoke if the {@link io.reactivex.Observable} that was subscribed to terminates successfully.
   * <p>
   * The underlying {@link io.vertx.core.streams.WriteStream#end()} method is invoked <strong>before</strong> the {@code observableCompleteHandler}.
   *
   * @return a reference to this, so the API can be used fluently
   */
  WriteStreamObserver<R> observableCompleteHandler(Handler<Void> observableCompleteHandler);

  /**
   * Sets the handler to invoke if the adapted {@link io.vertx.core.streams.WriteStream} fails.
   * <p>
   * The underlying {@link io.vertx.core.streams.WriteStream#end()} method is <strong>not</strong> invoked in this case.
   *
   * @return a reference to this, so the API can be used fluently
   */
  WriteStreamObserver<R> writeStreamExceptionHandler(Handler<Throwable> writeStreamExceptionHandler);
}
