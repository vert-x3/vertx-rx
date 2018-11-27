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

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.vertx.core.Handler;
import io.vertx.core.streams.WriteStream;
import io.vertx.reactivex.WriteStreamSubscriber;
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
  private boolean drainHandlerSet;
  private boolean done;

  private Handler<Throwable> flowableErrorHandler;
  private Runnable flowableCompleteHandler;
  private Handler<Throwable> writeStreamExceptionHandler;

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

    if (writeStream.writeQueueFull()) {
      setDrainHandler();
    } else {
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

    Handler<Throwable> h;
    synchronized (this) {
      h = flowableErrorHandler;
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

    Runnable r;
    synchronized (this) {
      r = flowableCompleteHandler;
    }
    try {
      writeStream.end();
      if (r != null) {
        r.run();
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
  public synchronized WriteStreamSubscriber<R> onError(Handler<Throwable> handler) {
    this.flowableErrorHandler = handler;
    return this;
  }

  @Override
  public synchronized WriteStreamSubscriber<R> onComplete(Runnable handler) {
    this.flowableCompleteHandler = handler;
    return this;
  }

  @Override
  public synchronized WriteStreamSubscriber<R> onWriteStreamError(Handler<Throwable> handler) {
    this.writeStreamExceptionHandler = handler;
    return this;
  }
}
