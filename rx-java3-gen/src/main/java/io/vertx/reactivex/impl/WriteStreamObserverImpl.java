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

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.internal.disposables.DisposableHelper;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.AsyncResult;
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

  private Consumer<? super Throwable> observableErrorHandler;
  private Consumer<? super Throwable> writeStreamExceptionHandler;
  private Action writeStreamEndHandler;
  private Consumer<? super Throwable> writeStreamEndErrorHandler;

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
      Consumer<? super Throwable> c;
      synchronized (this) {
        c = this.writeStreamExceptionHandler;
      }
      if (c != null) {
        try {
          c.accept(t);
        } catch (Throwable e) {
          RxJavaPlugins.onError(t);
        }
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

    Consumer<? super Throwable> c;
    synchronized (this) {
      c = observableErrorHandler;
    }
    try {
      if (c != null) {
        c.accept(t);
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

    try {
      writeStream.end(this::writeStreamEnd);
    } catch (Throwable t) {
      Exceptions.throwIfFatal(t);
      RxJavaPlugins.onError(t);
    }
  }

  private void writeStreamEnd(AsyncResult<Void> result) {
    try {
      Action a;
      if (result.succeeded()) {
        synchronized (this) {
          a = writeStreamEndHandler;
        }
        if (a != null) {
          a.run();
        }
      } else {
        Consumer<? super Throwable> c;
        synchronized (this) {
          c = this.writeStreamEndErrorHandler;
        }
        if (c != null) {
          c.accept(result.cause());
        }
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
  public synchronized WriteStreamObserver<R> onError(Consumer<? super Throwable> handler) {
    this.observableErrorHandler = handler;
    return this;
  }

  @Override
  public synchronized WriteStreamObserver<R> onWriteStreamError(Consumer<? super Throwable> handler) {
    this.writeStreamExceptionHandler = handler;
    return this;
  }

  @Override
  public synchronized WriteStreamObserver<R> onWriteStreamEnd(Action handler) {
    this.writeStreamEndHandler = handler;
    return this;
  }

  @Override
  public synchronized WriteStreamObserver<R> onWriteStreamEndError(Consumer<? super Throwable> handler) {
    this.writeStreamEndErrorHandler = handler;
    return this;
  }
}
