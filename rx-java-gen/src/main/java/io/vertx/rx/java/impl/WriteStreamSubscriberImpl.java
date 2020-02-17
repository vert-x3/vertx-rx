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

import io.vertx.core.AsyncResult;
import io.vertx.core.streams.WriteStream;
import io.vertx.rx.java.WriteStreamSubscriber;
import rx.functions.Action0;
import rx.functions.Action1;

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

  private Action1<Throwable> observableErrorHandler;
  private Action1<Throwable> writeStreamExceptionHandler;
  private Action0 writeStreamEndHandler;
  private Action1<Throwable> writeStreamEndErrorHandler;

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
      Action1<Throwable> a;
      synchronized (this) {
        a = this.writeStreamExceptionHandler;
      }
      if (a != null) {
        a.call(t);
      }
    });
    writeStream.drainHandler(v -> requestMore());
    requestMore();
  }

  @Override
  public void onNext(R r) {
    writeStream.write(mapping.apply(r));
    synchronized (this) {
      outstanding--;
    }
    if (!writeStream.writeQueueFull()) {
      requestMore();
    }
  }

  @Override
  public void onError(Throwable t) {
    Action1<Throwable> a;
    synchronized (this) {
      a = this.observableErrorHandler;
    }
    if (a != null) {
      a.call(t);
    }
  }

  @Override
  public void onCompleted() {
    writeStream.end(this::writeStreamEnd);
  }

  private void writeStreamEnd(AsyncResult<Void> result) {
    Action0 a;
    if (result.succeeded()) {
      synchronized (this) {
        a = writeStreamEndHandler;
      }
      if (a != null) {
        a.call();
      }
    } else {
      Action1<Throwable> c;
      synchronized (this) {
        c = this.writeStreamEndErrorHandler;
      }
      if (c != null) {
        c.call(result.cause());
      }
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

  @Override
  public synchronized WriteStreamSubscriber<R> onError(Action1<Throwable> handler) {
    this.observableErrorHandler = handler;
    return this;
  }

  @Override
  public synchronized WriteStreamSubscriber<R> onWriteStreamError(Action1<Throwable> handler) {
    this.writeStreamExceptionHandler = handler;
    return this;
  }

  @Override
  public synchronized WriteStreamSubscriber<R> onWriteStreamEnd(Action0 handler) {
    this.writeStreamEndHandler = handler;
    return this;
  }

  @Override
  public synchronized WriteStreamSubscriber<R> onWriteStreamEndError(Action1<Throwable> handler) {
    this.writeStreamEndErrorHandler = handler;
    return this;
  }
}
