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

import io.vertx.core.streams.WriteStream;
import rx.Subscriber;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Thomas Segismont
 */
public class WriteStreamSubscriber<R, T> extends Subscriber<R> {

  private static final int BATCH_SIZE = 16;

  private final WriteStream<T> writeStream;
  private final Function<R, T> adapter;
  private final Consumer<Throwable> onError;
  private final Runnable onComplete;

  private int outstanding;
  private boolean drainHandlerSet;

  public WriteStreamSubscriber(WriteStream<T> writeStream, Function<R, T> adapter, Consumer<Throwable> onError, Runnable onComplete) {
    Objects.requireNonNull(writeStream, "writeStream");
    Objects.requireNonNull(adapter, "adapter");
    Objects.requireNonNull(onError, "onError");
    Objects.requireNonNull(onComplete, "onComplete");
    this.writeStream = writeStream;
    this.adapter = adapter;
    this.onError = onError;
    this.onComplete = onComplete;
  }

  @Override
  public void onStart() {
    writeStream.exceptionHandler(t -> {
      unsubscribe();
      onError(t);
    });
    requestMore();
  }

  @Override
  public void onNext(R r) {
    writeStream.write(adapter.apply(r));
    synchronized (this) {
      outstanding--;
    }
    if (writeStream.writeQueueFull()) {
      setDrainHandler();
    } else {
      requestMore();
    }
  }

  @Override
  public void onError(Throwable t) {
    onError.accept(t);
  }

  @Override
  public void onCompleted() {
    onComplete.run();
  }

  private void requestMore() {
    synchronized (this) {
      if (outstanding > 0) {
        return;
      }
      outstanding = BATCH_SIZE;
    }
    request(BATCH_SIZE);
  }

  private void setDrainHandler() {
    boolean set;
    synchronized (this) {
      set = drainHandlerSet ? false : (drainHandlerSet = true);
    }
    if (set) {
      writeStream.drainHandler(this::drain);
    }
  }

  private void drain(Void v) {
    synchronized (this) {
      drainHandlerSet = false;
    }
    requestMore();
  }
}
