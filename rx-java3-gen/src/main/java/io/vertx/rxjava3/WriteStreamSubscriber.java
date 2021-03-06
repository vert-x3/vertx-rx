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

package io.vertx.rxjava3;

import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * A {@link io.vertx.core.streams.WriteStream} to {@link org.reactivestreams.Subscriber} adapter.
 *
 * @author Thomas Segismont
 */
public interface WriteStreamSubscriber<R> extends FlowableSubscriber<R> {

  /**
   * Sets the handler to invoke if the {@link io.reactivex.rxjava3.core.Flowable} that was subscribed to terminates with an error.
   * <p>
   * The underlying {@link io.vertx.core.streams.WriteStream#end()} method is <strong>not</strong> invoked in this case.
   *
   * @return a reference to this, so the API can be used fluently
   */
  WriteStreamSubscriber<R> onError(Consumer<? super Throwable> handler);

  /**
   * Sets the handler to invoke if the adapted {@link io.vertx.core.streams.WriteStream} fails.
   * <p>
   * The underlying {@link io.vertx.core.streams.WriteStream#end()} method is <strong>not</strong> invoked in this case.
   *
   * @return a reference to this, so the API can be used fluently
   */
  WriteStreamSubscriber<R> onWriteStreamError(Consumer<? super Throwable> handler);

  /**
   * Sets the handler to invoke when the adapted {@link io.vertx.core.streams.WriteStream} ends successfully.
   *
   * @return a reference to this, so the API can be used fluently
   */
  WriteStreamSubscriber<R> onWriteStreamEnd(Action handler);

  /**
   * Sets the handler to invoke when the adapted {@link io.vertx.core.streams.WriteStream} ends with an error.
   *
   * @return a reference to this, so the API can be used fluently
   */
  WriteStreamSubscriber<R> onWriteStreamEndError(Consumer<? super Throwable> handler);
}
