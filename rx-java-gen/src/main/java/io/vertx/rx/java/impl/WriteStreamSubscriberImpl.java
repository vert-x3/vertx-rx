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

package io.vertx.rx.java.impl;

import io.vertx.core.Handler;
import io.vertx.core.streams.WriteStream;
import io.vertx.rx.java.WriteStreamSubscriber;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Thomas Segismont
 */
public class WriteStreamSubscriberImpl<R, T> extends WriteStreamSubscriber<R> {

  private static final int BATCH_SIZE = 16;

  private final WriteStream<T> writeStream;
  private final Function<R, T> mapping;

  private int outstanding;
  private boolean drainHandlerSet;

  private Handler<Throwable> observableErrorHandler;
  private Handler<Void> observableCompleteHandler;
  private Handler<Throwable> writeStreamExceptionHandler;

  public WriteStreamSubscriberImpl(WriteStream<T> writeStream, Function<R, T> mapping) {
    Objects.requireNonNull(writeStream, "writeStream");
    Objects.requireNonNull(mapping, "mapping");
    this.writeStream = writeStream;
    this.mapping = mapping;
  }

  @Override
  public void onStart() {
    writeStream.exceptionHandler(t -> {
      unsubscribe();
      Handler<Throwable> h;
      synchronized (this) {
        h = this.writeStreamExceptionHandler;
      }
      if (h != null) {
        h.handle(t);
      }
    });
    requestMore();
  }

  @Override
  public void onNext(R r) {
    writeStream.write(mapping.apply(r));
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
    Handler<Throwable> h;
    synchronized (this) {
      h = this.observableErrorHandler;
    }
    if (h != null) {
      h.handle(t);
    }
  }

  @Override
  public void onCompleted() {
    Handler<Void> h;
    synchronized (this) {
      h = this.observableCompleteHandler;
    }
    writeStream.end();
    if (h != null) {
      h.handle(null);
    }
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

  @Override
  public synchronized WriteStreamSubscriber<R> observableErrorHandler(Handler<Throwable> observableErrorHandler) {
    this.observableErrorHandler = observableErrorHandler;
    return this;
  }

  @Override
  public synchronized WriteStreamSubscriber<R> observableCompleteHandler(Handler<Void> observableCompleteHandler) {
    this.observableCompleteHandler = observableCompleteHandler;
    return this;
  }

  @Override
  public synchronized WriteStreamSubscriber<R> writeStreamExceptionHandler(Handler<Throwable> writeStreamExceptionHandler) {
    this.writeStreamExceptionHandler = writeStreamExceptionHandler;
    return this;
  }
}
