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
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.vertx.core.Handler;
import io.vertx.core.streams.WriteStream;
import org.reactivestreams.Subscription;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Thomas Segismont
 */
public class WriteStreamSubscriber<R, T> implements FlowableSubscriber<R> {

  private final WriteStream<T> writeStream;
  private final Consumer<Throwable> onError;
  private final Runnable onComplete;
  private final Function<R, T> adapter;

  private Subscription subscription;
  private boolean done;
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
  public void onSubscribe(Subscription subscription) {
    Objects.requireNonNull(subscription, "subscription");
    if (!setSubscription(subscription)) {
      subscription.cancel();
      SubscriptionHelper.reportSubscriptionSet();
      return;
    }
    getSubscription().request(256);
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
      writeStream.write(adapter.apply(r));
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
      Handler<Void> h;
      synchronized (this) {
        if (drainHandlerSet) {
          h = null;
        } else {
          drainHandlerSet = true;
          h = v -> getSubscription().request(1);
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
      getSubscription().request(1);
    }
  }

  @Override
  public void onError(Throwable t) {
    if (!setDone()) {
      RxJavaPlugins.onError(t);
      return;
    }

    Objects.requireNonNull(t, "onError called with null");

    try {
      onError.accept(t);
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
      onComplete.run();
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
}
