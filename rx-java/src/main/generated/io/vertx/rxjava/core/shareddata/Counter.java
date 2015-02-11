/*
 * Copyright 2014 Red Hat, Inc.
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

package io.vertx.rxjava.core.shareddata;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * An asynchronous counter that can be used to across the cluster to maintain a consistent count.
 * <p>
 *
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class Counter {

  final io.vertx.core.shareddata.Counter delegate;

  public Counter(io.vertx.core.shareddata.Counter delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Get the current value of the counter
   *
   * @param resultHandler handler which will be passed the value
   */
  public void get(Handler<AsyncResult<Long>> resultHandler) {
    this.delegate.get(resultHandler);
  }

  public Observable<Long> getObservable() {
    io.vertx.rx.java.ObservableFuture<Long> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    get(resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Increment the counter atomically and return the new count
   *
   * @param resultHandler handler which will be passed the value
   */
  public void incrementAndGet(Handler<AsyncResult<Long>> resultHandler) {
    this.delegate.incrementAndGet(resultHandler);
  }

  public Observable<Long> incrementAndGetObservable() {
    io.vertx.rx.java.ObservableFuture<Long> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    incrementAndGet(resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Increment the counter atomically and return the value before the increment.
   *
   * @param resultHandler handler which will be passed the value
   */
  public void getAndIncrement(Handler<AsyncResult<Long>> resultHandler) {
    this.delegate.getAndIncrement(resultHandler);
  }

  public Observable<Long> getAndIncrementObservable() {
    io.vertx.rx.java.ObservableFuture<Long> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getAndIncrement(resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Decrement the counter atomically and return the new count
   *
   * @param resultHandler handler which will be passed the value
   */
  public void decrementAndGet(Handler<AsyncResult<Long>> resultHandler) {
    this.delegate.decrementAndGet(resultHandler);
  }

  public Observable<Long> decrementAndGetObservable() {
    io.vertx.rx.java.ObservableFuture<Long> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    decrementAndGet(resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Add the value to the counter atomically and return the new count
   *
   * @param value  the value to add
   * @param resultHandler handler which will be passed the value
   */
  public void addAndGet(long value, Handler<AsyncResult<Long>> resultHandler) {
    this.delegate.addAndGet(value, resultHandler);
  }

  public Observable<Long> addAndGetObservable(long value) {
    io.vertx.rx.java.ObservableFuture<Long> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    addAndGet(value, resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Add the value to the counter atomically and return the value before the add
   *
   * @param value  the value to add
   * @param resultHandler handler which will be passed the value
   */
  public void getAndAdd(long value, Handler<AsyncResult<Long>> resultHandler) {
    this.delegate.getAndAdd(value, resultHandler);
  }

  public Observable<Long> getAndAddObservable(long value) {
    io.vertx.rx.java.ObservableFuture<Long> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    getAndAdd(value, resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Set the counter to the specified value only if the current value is the expectec value. This happens
   * atomically.
   *
   * @param expected  the expected value
   * @param value  the new value
   * @param resultHandler  the handler will be passed true on success
   */
  public void compareAndSet(long expected, long value, Handler<AsyncResult<Boolean>> resultHandler) {
    this.delegate.compareAndSet(expected, value, resultHandler);
  }

  public Observable<Boolean> compareAndSetObservable(long expected, long value) {
    io.vertx.rx.java.ObservableFuture<Boolean> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    compareAndSet(expected, value, resultHandler.asHandler());
    return resultHandler;
  }


  public static Counter newInstance(io.vertx.core.shareddata.Counter arg) {
    return new Counter(arg);
  }
}
