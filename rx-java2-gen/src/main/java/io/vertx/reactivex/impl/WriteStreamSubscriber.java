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

package io.vertx.reactivex.impl;

import io.reactivex.FlowableSubscriber;
import io.vertx.core.Handler;
import io.vertx.core.streams.WriteStream;
import org.reactivestreams.Subscription;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Thomas Segismont
 */
public class WriteStreamSubscriber<T> implements FlowableSubscriber<T> {

  private final WriteStream<T> writeStream;
  private final Consumer<Throwable> onError;
  private final Runnable onComplete;

  private Subscription subscription;
  private boolean drainHandlerSet;

  public WriteStreamSubscriber(WriteStream<T> writeStream, Consumer<Throwable> onError, Runnable onComplete) {
    Objects.requireNonNull(writeStream, "writeStream");
    Objects.requireNonNull(onError, "onError");
    Objects.requireNonNull(onComplete, "onComplete");
    this.writeStream = writeStream;
    this.onError = onError;
    this.onComplete = onComplete;
  }

  @Override
  public void onSubscribe(Subscription subscription) {
    this.subscription = subscription;
    subscription.request(256);
  }

  @Override
  public void onNext(T t) {
    writeStream.write(t);
    if (writeStream.writeQueueFull()) {
      Handler<Void> h;
      synchronized (this) {
        if (drainHandlerSet) {
          h = null;
        } else {
          drainHandlerSet = true;
          h = v -> subscription.request(1);
        }
      }
      if (h != null) {
        writeStream.drainHandler(v -> {
          synchronized (this) {
            drainHandlerSet = false;
          }
          h.handle(null);
        });
      }
    } else {
      subscription.request(1);
    }
  }

  @Override
  public void onError(Throwable t) {
    onError.accept(t);
  }

  @Override
  public void onComplete() {
    onComplete.run();
  }
}
