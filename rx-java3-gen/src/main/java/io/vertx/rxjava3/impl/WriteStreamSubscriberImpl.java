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

package io.vertx.rxjava3.impl;

import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.internal.subscriptions.SubscriptionHelper;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.AsyncResult;
import io.vertx.core.streams.WriteStream;
import io.vertx.rxjava3.WriteStreamSubscriber;
import org.reactivestreams.Subscription;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Thomas Segismont
 */
public class WriteStreamSubscriberImpl<R, T> implements WriteStreamSubscriber<R> {

  private static final int BATCH_SIZE = 16;

  private final WriteStream<T> writeStream;
  private final Function<R, T> mapping;

  private Subscription subscription;
  private int outstanding;
  private boolean done;

  private Consumer<? super Throwable> flowableErrorHandler;
  private Consumer<? super Throwable> writeStreamExceptionHandler;
  private Action writeStreamEndHandler;
  private Consumer<? super Throwable> writeStreamEndErrorHandler;

  public WriteStreamSubscriberImpl(WriteStream<T> writeStream, Function<R, T> mapping) {
    Objects.requireNonNull(writeStream, "writeStream");
    Objects.requireNonNull(mapping, "mapping");
    this.writeStream = writeStream;
    this.mapping = mapping;
  }

  @Override
  public void onSubscribe(Subscription subscription) {
    Objects.requireNonNull(subscription, "subscription");
    if (!setSubscription(subscription)) {
      subscription.cancel();
      SubscriptionHelper.reportSubscriptionSet();
      return;
    }
    writeStream.exceptionHandler(t -> {
      if (!setDone()) {
        RxJavaPlugins.onError(t);
        return;
      }
      getSubscription().cancel();
      Consumer<? super Throwable> c;
      synchronized (this) {
        c = this.writeStreamExceptionHandler;
      }
      if (c != null) {
        try {
          c.accept(t);
        } catch (Throwable e) {
          RxJavaPlugins.onError(e);
        }
      }
    });
    writeStream.drainHandler(v -> requestMore());
    requestMore();
  }

  @Override
  public void onNext(R r) {
    if (isDone()) {
      return;
    }

    if (r == null) {
      Throwable throwable = new NullPointerException("onNext called with null");
      try {
        getSubscription().cancel();
      } catch (Throwable t) {
        Exceptions.throwIfFatal(t);
        throwable = new CompositeException(throwable, t);
      }
      onError(throwable);
      return;
    }

    try {
      writeStream.write(mapping.apply(r));
      synchronized (this) {
        outstanding--;
      }
    } catch (Throwable t) {
      Exceptions.throwIfFatal(t);
      Throwable throwable;
      try {
        getSubscription().cancel();
        throwable = t;
      } catch (Throwable t1) {
        Exceptions.throwIfFatal(t1);
        throwable = new CompositeException(t, t1);
      }
      onError(throwable);
      return;
    }

    if (!writeStream.writeQueueFull()) {
      requestMore();
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
      c = flowableErrorHandler;
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

  private synchronized Subscription getSubscription() {
    return subscription;
  }

  private synchronized boolean setSubscription(Subscription subscription) {
    if (this.subscription == null) {
      this.subscription = subscription;
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

  private void requestMore() {
    Subscription s = getSubscription();
    if (s == null) {
      return;
    }
    synchronized (this) {
      if (done || outstanding > 0) {
        return;
      }
      outstanding = BATCH_SIZE;
    }
    s.request(BATCH_SIZE);
  }

  @Override
  public synchronized WriteStreamSubscriber<R> onError(Consumer<? super Throwable> handler) {
    this.flowableErrorHandler = handler;
    return this;
  }

  @Override
  public synchronized WriteStreamSubscriber<R> onWriteStreamError(Consumer<? super Throwable> handler) {
    this.writeStreamExceptionHandler = handler;
    return this;
  }

  @Override
  public synchronized WriteStreamSubscriber<R> onWriteStreamEnd(Action handler) {
    this.writeStreamEndHandler = handler;
    return this;
  }

  @Override
  public synchronized WriteStreamSubscriber<R> onWriteStreamEndError(Consumer<? super Throwable> handler) {
    this.writeStreamEndErrorHandler = handler;
    return this;
  }
}
