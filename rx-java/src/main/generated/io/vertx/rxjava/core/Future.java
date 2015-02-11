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

package io.vertx.rxjava.core;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Represents the result of an action that may, or may not, have occurred yet.
 * <p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class Future<T> {

  final io.vertx.core.Future delegate;

  public Future(io.vertx.core.Future delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a future that hasn't completed yet
   *
   * @param <T>  the result type
   * @return  the future
   */
  public static <T> Future<T> future() {
    Future<T> ret= Future.newInstance(io.vertx.core.Future.future());
    return ret;
  }

  /**
   * Create a succeeded future with a null result
   *
   * @param <T>  the result type
   * @return  the future
   */
  public static <T> Future<T> succeededFuture() {
    Future<T> ret= Future.newInstance(io.vertx.core.Future.succeededFuture());
    return ret;
  }

  /**
   * Created a succeeded future with the specified result.
   *
   * @param result  the result
   * @param <T>  the result type
   * @return  the future
   */
  public static <T> Future<T> succeededFuture(T result) {
    Future<T> ret= Future.newInstance(io.vertx.core.Future.succeededFuture(result));
    return ret;
  }

  /**
   * Create a failed future with the specified failure message.
   *
   * @param failureMessage  the failure message
   * @param <T>  the result type
   * @return  the future
   */
  public static <T> Future<T> failedFuture(String failureMessage) {
    Future<T> ret= Future.newInstance(io.vertx.core.Future.failedFuture(failureMessage));
    return ret;
  }

  /**
   * Has the future completed?
   * <p>
   * It's completed if it's either succeeded or failed.
   *
   * @return true if completed, false if not
   */
  public boolean isComplete() {
    boolean ret = this.delegate.isComplete();
    return ret;
  }

  /**
   * Set a handler for the result.
   * <p>
   * If the future has already been completed it will be called immediately. Otherwise it will be called when the
   * future is completed.
   *
   * @param handler  the Handler that will be called with the result

   */
  public void setHandler(Handler<AsyncResult<T>> handler) {
    this.delegate.setHandler(handler);
  }

  public Observable<T> setHandlerObservable() {
    io.vertx.rx.java.ObservableFuture<T> handler = io.vertx.rx.java.RxHelper.observableFuture();
    setHandler(handler.asHandler());
    return handler;
  }

  /**
   * Set the result. Any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param result  the result
   * @throws IllegalStateException when the future is already completed
   */
  public void complete(T result) {
    this.delegate.complete(result);
  }

  /**
   * Set a null result. Any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @throws IllegalStateException when the future is already completed
   */
  public void complete() {
    this.delegate.complete();
  }

  /**
   * Set the failure. Any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param failureMessage  the failure message
   */
  public void fail(String failureMessage) {
    this.delegate.fail(failureMessage);
  }


  public static <T> Future<T> newInstance(io.vertx.core.Future arg) {
    return new Future<T> (arg);
  }
}
