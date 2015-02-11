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
 *
 * An asynchronous map.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>

 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class AsyncMap<K,V> {

  final io.vertx.core.shareddata.AsyncMap delegate;

  public AsyncMap(io.vertx.core.shareddata.AsyncMap delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Get a value from the map, asynchronously.
   *
   * @param k  the key
   * @param resultHandler - this will be called some time later with the async result.
   */
  public void get(K k, Handler<AsyncResult<V>> resultHandler) {
    this.delegate.get(k, resultHandler);
  }

  public Observable<V> getObservable(K k) {
    io.vertx.rx.java.ObservableFuture<V> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    get(k, resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Put a value in the map, asynchronously.
   *
   * @param k  the key
   * @param v  the value
   * @param completionHandler - this will be called some time later to signify the value has been put
   */
  public void put(K k, V v, Handler<AsyncResult<Void>> completionHandler) {
    this.delegate.put(k, v, completionHandler);
  }

  public Observable<Void> putObservable(K k, V v) {
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    put(k, v, completionHandler.asHandler());
    return completionHandler;
  }

  /**
   * Like {@link #put} but specifying a timeout. If the value cannot be put within the timeout a
   * failure will be passed to the handler
   *
   * @param k  the key
   * @param v  the value
   * @param timeout  the timoeout, in ms
   * @param completionHandler  the handler
   */
  public void put(K k, V v, long timeout, Handler<AsyncResult<Void>> completionHandler) {
    this.delegate.put(k, v, timeout, completionHandler);
  }

  public Observable<Void> putObservable(K k, V v, long timeout) {
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    put(k, v, timeout, completionHandler.asHandler());
    return completionHandler;
  }

  /**
   * Put the entry only if there is no entry with the key already present. If key already present then the existing
   * value will be returned to the handler, otherwise null.
   *
   * @param k  the key
   * @param v  the value
   * @param completionHandler  the handler
   */
  public void putIfAbsent(K k, V v, Handler<AsyncResult<V>> completionHandler) {
    this.delegate.putIfAbsent(k, v, completionHandler);
  }

  public Observable<V> putIfAbsentObservable(K k, V v) {
    io.vertx.rx.java.ObservableFuture<V> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    putIfAbsent(k, v, completionHandler.asHandler());
    return completionHandler;
  }

  /**
   * Link {@link #putIfAbsent} but specifying a timeout. If the value cannot be put within the timeout a
   * failure will be passed to the handler
   *
   * @param k  the key
   * @param v  the value
   * @param timeout  the timeout, in ms
   * @param completionHandler  the handler
   */
  public void putIfAbsent(K k, V v, long timeout, Handler<AsyncResult<V>> completionHandler) {
    this.delegate.putIfAbsent(k, v, timeout, completionHandler);
  }

  public Observable<V> putIfAbsentObservable(K k, V v, long timeout) {
    io.vertx.rx.java.ObservableFuture<V> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    putIfAbsent(k, v, timeout, completionHandler.asHandler());
    return completionHandler;
  }

  /**
   * Remove a value from the map, asynchronously.
   *
   * @param k  the key
   * @param resultHandler - this will be called some time later to signify the value has been removed
   */
  public void remove(K k, Handler<AsyncResult<V>> resultHandler) {
    this.delegate.remove(k, resultHandler);
  }

  public Observable<V> removeObservable(K k) {
    io.vertx.rx.java.ObservableFuture<V> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    remove(k, resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Remove a value from the map, only if entry already exists with same value.
   *
   * @param k  the key
   * @param v  the value
   * @param resultHandler - this will be called some time later to signify the value has been removed
   */
  public void removeIfPresent(K k, V v, Handler<AsyncResult<Boolean>> resultHandler) {
    this.delegate.removeIfPresent(k, v, resultHandler);
  }

  public Observable<Boolean> removeIfPresentObservable(K k, V v) {
    io.vertx.rx.java.ObservableFuture<Boolean> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    removeIfPresent(k, v, resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Replace the entry only if it is currently mapped to some value
   *
   * @param k  the key
   * @param v  the new value
   * @param resultHandler  the result handler will be passed the previous value
   */
  public void replace(K k, V v, Handler<AsyncResult<V>> resultHandler) {
    this.delegate.replace(k, v, resultHandler);
  }

  public Observable<V> replaceObservable(K k, V v) {
    io.vertx.rx.java.ObservableFuture<V> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    replace(k, v, resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Replace the entry only if it is currently mapped to a specific value
   *
   * @param k  the key
   * @param oldValue  the existing value
   * @param newValue  the new value
   * @param resultHandler the result handler
   */
  public void replaceIfPresent(K k, V oldValue, V newValue, Handler<AsyncResult<Boolean>> resultHandler) {
    this.delegate.replaceIfPresent(k, oldValue, newValue, resultHandler);
  }

  public Observable<Boolean> replaceIfPresentObservable(K k, V oldValue, V newValue) {
    io.vertx.rx.java.ObservableFuture<Boolean> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    replaceIfPresent(k, oldValue, newValue, resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Clear all entries in the map
   *
   * @param resultHandler  called on completion
   */
  public void clear(Handler<AsyncResult<Void>> resultHandler) {
    this.delegate.clear(resultHandler);
  }

  public Observable<Void> clearObservable() {
    io.vertx.rx.java.ObservableFuture<Void> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    clear(resultHandler.asHandler());
    return resultHandler;
  }

  /**
   * Provide the number of entries in the map
   *
   * @param resultHandler  handler which will receive the number of entries
   */
  public void size(Handler<AsyncResult<Integer>> resultHandler) {
    this.delegate.size(resultHandler);
  }

  public Observable<Integer> sizeObservable() {
    io.vertx.rx.java.ObservableFuture<Integer> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    size(resultHandler.asHandler());
    return resultHandler;
  }


  public static <K, V> AsyncMap<K,V> newInstance(io.vertx.core.shareddata.AsyncMap arg) {
    return new AsyncMap<K, V> (arg);
  }
}
