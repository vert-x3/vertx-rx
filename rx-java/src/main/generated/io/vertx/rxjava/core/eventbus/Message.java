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

package io.vertx.rxjava.core.eventbus;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.rxjava.core.MultiMap;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Represents a message that is received from the event bus in a handler.
 * <p>
 * Messages have a {@link #body}, which can be null, and also {@link #headers}, which can be empty.
 * <p>
 * If the message was sent specifying a reply handler it will also have a {@link #replyAddress}. In that case the message
 * can be replied to using that reply address, or, more simply by just using {@link #reply}.
 * <p>
 * If you want to notify the sender that processing failed, then {@link #fail} can be called.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class Message<T> {

  final io.vertx.core.eventbus.Message delegate;

  public Message(io.vertx.core.eventbus.Message delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * The address the message was sent to
   */
  public String address() {
    String ret = this.delegate.address();
    return ret;
  }

  /**
   * Multi-map of message headers. Can be empty
   *
   * @return  the headers
   */
  public MultiMap headers() {
    MultiMap ret= MultiMap.newInstance(this.delegate.headers());
    return ret;
  }

  /**
   * The body of the message. Can be null.
   *
   * @return  the body, or null.
   */
  public T body() {
    if (cached_0 != null) {
      return cached_0;
    }
    T ret = (T) this.delegate.body();
    cached_0 = ret;
    return ret;
  }

  /**
   * The reply address. Can be null.
   *
   * @return the reply address, or null, if message was sent without a reply handler.
   */
  public String replyAddress() {
    String ret = this.delegate.replyAddress();
    return ret;
  }

  /**
   * Reply to this message.
   * <p>
   * If the message was sent specifying a reply handler, that handler will be
   * called when it has received a reply. If the message wasn't sent specifying a receipt handler
   * this method does nothing.
   *
   * @param message  the message to reply with.
   */
  public void reply(Object message) {
    this.delegate.reply(message);
  }

  /**
   * The same as {@code reply(R message)} but you can specify handler for the reply - i.e.
   * to receive the reply to the reply.
   *
   * @param message  the message to reply with.
   * @param replyHandler  the reply handler for the reply.
   */
  public <R> void reply(Object message, Handler<AsyncResult<Message<R>>> replyHandler) {
    this.delegate.reply(message, new Handler<AsyncResult<io.vertx.core.eventbus.Message<R>>>() {
      public void handle(AsyncResult<io.vertx.core.eventbus.Message<R>> event) {
        AsyncResult<Message<R>> f;
        if (event.succeeded()) {
          f = InternalHelper.<Message<R>>result(new Message<R>(event.result()));
        } else {
          f = InternalHelper.<Message<R>>failure(event.cause());
        }
        replyHandler.handle(f);
      }
    });
  }

  public <R> Observable<Message<R>> replyObservable(Object message) {
    io.vertx.rx.java.ObservableFuture<Message<R>> replyHandler = io.vertx.rx.java.RxHelper.observableFuture();
    reply(message, replyHandler.asHandler());
    return replyHandler;
  }

  /**
   * Link {@link #reply(Object)} but allows you to specify delivery options for the reply.
   *
   * @param message  the reply message
   * @param options  the delivery options
   */
  public void reply(Object message, DeliveryOptions options) {
    this.delegate.reply(message, options);
  }

  /**
   * The same as {@code reply(R message, DeliveryOptions)} but you can specify handler for the reply - i.e.
   * to receive the reply to the reply.
   *
   * @param message  the reply message
   * @param options  the delivery options
   * @param replyHandler  the reply handler for the reply.
   */
  public <R> void reply(Object message, DeliveryOptions options, Handler<AsyncResult<Message<R>>> replyHandler) {
    this.delegate.reply(message, options, new Handler<AsyncResult<io.vertx.core.eventbus.Message<R>>>() {
      public void handle(AsyncResult<io.vertx.core.eventbus.Message<R>> event) {
        AsyncResult<Message<R>> f;
        if (event.succeeded()) {
          f = InternalHelper.<Message<R>>result(new Message<R>(event.result()));
        } else {
          f = InternalHelper.<Message<R>>failure(event.cause());
        }
        replyHandler.handle(f);
      }
    });
  }

  public <R> Observable<Message<R>> replyObservable(Object message, DeliveryOptions options) {
    io.vertx.rx.java.ObservableFuture<Message<R>> replyHandler = io.vertx.rx.java.RxHelper.observableFuture();
    reply(message, options, replyHandler.asHandler());
    return replyHandler;
  }

  /**
   * Signal to the sender that processing of this message failed.
   * <p>
   * If the message was sent specifying a result handler
   * the handler will be called with a failure corresponding to the failure code and message specified here.
   *
   * @param failureCode A failure code to pass back to the sender
   * @param message A message to pass back to the sender
   */
  public void fail(int failureCode, String message) {
    this.delegate.fail(failureCode, message);
  }

  private T cached_0;

  public static <T> Message<T> newInstance(io.vertx.core.eventbus.Message arg) {
    return new Message<T> (arg);
  }
}
