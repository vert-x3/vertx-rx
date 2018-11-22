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

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.vertx.core.Handler;
import io.vertx.core.streams.WriteStream;
import io.vertx.reactivex.WriteStreamObserver;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Thomas Segismont
 */
public class WriteStreamObserverImpl<R, T> implements WriteStreamObserver<R> {

  private final WriteStream<T> writeStream;
  private final Function<R, T> mapping;

  private Disposable disposable;
  private boolean done;

  private Handler<Throwable> observableErrorHandler;
  private Handler<Void> observableCompleteHandler;
  private Handler<Throwable> writeStreamExceptionHandler;

  public WriteStreamObserverImpl(WriteStream<T> writeStream, Function<R, T> mapping) {
    Objects.requireNonNull(writeStream, "writeStream");
    Objects.requireNonNull(mapping, "mapping");
    this.writeStream = writeStream;
    this.mapping = mapping;
  }

  @Override
  public void onSubscribe(Disposable disposable) {
    Objects.requireNonNull(disposable, "disposable");
    if (!setDisposable(disposable)) {
      disposable.dispose();
      DisposableHelper.reportDisposableSet();
      return;
    }
    writeStream.exceptionHandler(t -> {
      if (!setDone()) {
        RxJavaPlugins.onError(t);
        return;
      }
      getDisposable().dispose();
      Handler<Throwable> h;
      synchronized (this) {
        h = this.writeStreamExceptionHandler;
      }
      if (h != null) {
        h.handle(t);
      }
    });
  }

  @Override
  public void onNext(R r) {
    if (isDone()) {
      return;
    }

    if (r == null) {
      Throwable throwable = new NullPointerException("onNext called with null");
      try {
        getDisposable().dispose();
      } catch (Throwable t) {
        Exceptions.throwIfFatal(t);
        throwable = new CompositeException(throwable, t);
      }
      onError(throwable);
      return;
    }

    try {
      writeStream.write(mapping.apply(r));
    } catch (Throwable t) {
      Exceptions.throwIfFatal(t);
      Throwable throwable;
      try {
        getDisposable().dispose();
        throwable = t;
      } catch (Throwable t1) {
        Exceptions.throwIfFatal(t1);
        throwable = new CompositeException(t, t1);
      }
      onError(throwable);
    }
  }

  @Override
  public void onError(Throwable t) {
    if (!setDone()) {
      RxJavaPlugins.onError(t);
      return;
    }

    Objects.requireNonNull(t, "onError called with null");

    Handler<Throwable> h;
    synchronized (this) {
      h = observableErrorHandler;
    }
    try {
      if (h != null) {
        h.handle(t);
      }
    } catch (Throwable t1) {
      Exceptions.throwIfFatal(t1);
      RxJavaPlugins.onError(t1);
    }
  }

  @Override
  public void onComplete() {
    if (!setDone()) {
      return;
    }

    Handler<Void> h;
    synchronized (this) {
      h = observableCompleteHandler;
    }
    try {
      writeStream.end();
      if (h != null) {
        h.handle(null);
      }
    } catch (Throwable t) {
      Exceptions.throwIfFatal(t);
      RxJavaPlugins.onError(t);
    }
  }

  private synchronized Disposable getDisposable() {
    return disposable;
  }

  private synchronized boolean setDisposable(Disposable disposable) {
    if (this.disposable == null) {
      this.disposable = disposable;
      return true;
    }
    return false;
  }

  private synchronized boolean isDone() {
    return done;
  }

  private synchronized boolean setDone() {
    return done ? false : (done = true);
  }

  @Override
  public synchronized WriteStreamObserver<R> observableErrorHandler(Handler<Throwable> observableErrorHandler) {
    this.observableErrorHandler = observableErrorHandler;
    return this;
  }

  @Override
  public synchronized WriteStreamObserver<R> observableCompleteHandler(Handler<Void> observableCompleteHandler) {
    this.observableCompleteHandler = observableCompleteHandler;
    return this;
  }

  @Override
  public synchronized WriteStreamObserver<R> writeStreamExceptionHandler(Handler<Throwable> writeStreamExceptionHandler) {
    this.writeStreamExceptionHandler = writeStreamExceptionHandler;
    return this;
  }
}
