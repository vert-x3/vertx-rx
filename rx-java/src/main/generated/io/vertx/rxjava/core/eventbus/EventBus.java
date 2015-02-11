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
import io.vertx.rxjava.core.metrics.Measured;
import java.util.Map;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * A Vert.x event-bus is a light-weight distributed messaging system which allows different parts of your application,
 * or different applications and services to communicate with each in a loosely coupled way.
 * <p>
 * An event-bus supports publish-subscribe messaging, point-to-point messaging and request-response messaging.
 * <p>
 * Message delivery is best-effort and messages can be lost if failure of all or part of the event bus occurs.
 * <p>
 * Please refer to the documentation for more information on the event bus.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class EventBus implements Measured {

  final io.vertx.core.eventbus.EventBus delegate;

  public EventBus(io.vertx.core.eventbus.EventBus delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * The metric base name
   *
   * @return the metric base name
   */
  public String metricBaseName() {
    String ret = this.delegate.metricBaseName();
    return ret;
  }

  /**
   * Will return the metrics that correspond with this measured object.
   *
   * @return the map of metrics where the key is the name of the metric (excluding the base name) and the value is
   * the json data representing that metric
   */
  public Map<String,JsonObject> metrics() {
    Map<String,JsonObject> ret = this.delegate.metrics();
;
    return ret;
  }

  /**
   * Sends a message.
   * <p>
   * The message will be delivered to at most one of the handlers registered to the address.
   *
   * @param address  the address to send it to
   * @param message  the message, may be {@code null}
   * @return a reference to this, so the API can be used fluently
   */
  public EventBus send(String address, Object message) {
    this.delegate.send(address, message);
    return this;
  }

  /**
   * Like {@link #send(String, Object)} but specifying a {@code replyHandler} that will be called if the recipient
   * subsequently replies to the message.
   *
   * @param address  the address to send it to
   * @param message  the message, may be {@code null}
   * @param replyHandler  reply handler will be called when any reply from the recipient is received, may be {@code null}
   * @return a reference to this, so the API can be used fluently
   */
  public <T> EventBus send(String address, Object message, Handler<AsyncResult<Message<T>>> replyHandler) {
    this.delegate.send(address, message, new Handler<AsyncResult<io.vertx.core.eventbus.Message<T>>>() {
      public void handle(AsyncResult<io.vertx.core.eventbus.Message<T>> event) {
        AsyncResult<Message<T>> f;
        if (event.succeeded()) {
          f = InternalHelper.<Message<T>>result(new Message<T>(event.result()));
        } else {
          f = InternalHelper.<Message<T>>failure(event.cause());
        }
        replyHandler.handle(f);
      }
    });
    return this;
  }

  public <T> Observable<Message<T>> sendObservable(String address, Object message) {
    io.vertx.rx.java.ObservableFuture<Message<T>> replyHandler = io.vertx.rx.java.RxHelper.observableFuture();
    send(address, message, replyHandler.asHandler());
    return replyHandler;
  }

  /**
   * Like {@link #send(String, Object)} but specifying {@code options} that can be used to configure the delivery.
   *
   * @param address  the address to send it to
   * @param message  the message, may be {@code null}
   * @param options  delivery options
   * @return a reference to this, so the API can be used fluently
   */
  public <T> EventBus send(String address, Object message, DeliveryOptions options) {
    this.delegate.send(address, message, options);
    return this;
  }

  /**
   * Like {@link #send(String, Object, DeliveryOptions)} but specifying a {@code replyHandler} that will be called if the recipient
   * subsequently replies to the message.
   *
   * @param address  the address to send it to
   * @param message  the message, may be {@code null}
   * @param options  delivery options
   * @param replyHandler  reply handler will be called when any reply from the recipient is received, may be {@code null}
   * @return a reference to this, so the API can be used fluently
   */
  public <T> EventBus send(String address, Object message, DeliveryOptions options, Handler<AsyncResult<Message<T>>> replyHandler) {
    this.delegate.send(address, message, options, new Handler<AsyncResult<io.vertx.core.eventbus.Message<T>>>() {
      public void handle(AsyncResult<io.vertx.core.eventbus.Message<T>> event) {
        AsyncResult<Message<T>> f;
        if (event.succeeded()) {
          f = InternalHelper.<Message<T>>result(new Message<T>(event.result()));
        } else {
          f = InternalHelper.<Message<T>>failure(event.cause());
        }
        replyHandler.handle(f);
      }
    });
    return this;
  }

  public <T> Observable<Message<T>> sendObservable(String address, Object message, DeliveryOptions options) {
    io.vertx.rx.java.ObservableFuture<Message<T>> replyHandler = io.vertx.rx.java.RxHelper.observableFuture();
    send(address, message, options, replyHandler.asHandler());
    return replyHandler;
  }

  /**
   * Publish a message.<p>
   * The message will be delivered to all handlers registered to the address.
   *
   * @param address  the address to publish it to
   * @param message  the message, may be {@code null}
   * @return a reference to this, so the API can be used fluently

   */
  public EventBus publish(String address, Object message) {
    this.delegate.publish(address, message);
    return this;
  }

  /**
   * Like {@link #publish(String, Object)} but specifying {@code options} that can be used to configure the delivery.
   *
   * @param address  the address to publish it to
   * @param message  the message, may be {@code null}
   * @param options  the delivery options
   * @return a reference to this, so the API can be used fluently
   */
  public EventBus publish(String address, Object message, DeliveryOptions options) {
    this.delegate.publish(address, message, options);
    return this;
  }

  /**
   * Create a message consumer against the specified address.
   * <p>
   * The returned consumer is not yet registered
   * at the address, registration will be effective when {@link MessageConsumer#handler(io.vertx.core.Handler)}
   * is called.
   *
   * @param address  the address that it will register it at
   * @return the event bus message consumer
   */
  public <T> MessageConsumer<T> consumer(String address) {
    MessageConsumer<T> ret= MessageConsumer.newInstance(this.delegate.consumer(address));
    return ret;
  }

  /**
   * Create a consumer and register it against the specified address.
   *
   * @param address  the address that will register it at
   * @param handler  the handler that will process the received messages
   *
   * @return the event bus message consumer
   */
  public <T> MessageConsumer<T> consumer(String address, Handler<Message<T>> handler) {
    MessageConsumer<T> ret= MessageConsumer.newInstance(this.delegate.consumer(address, new Handler<io.vertx.core.eventbus.Message<T>>() {
      public void handle(io.vertx.core.eventbus.Message<T> event) {
        handler.handle(new Message<T>(event));
      }
    }));
    return ret;
  }

  /**
   * Like {@link #consumer(String)} but the address won't be propagated across the cluster.
   *
   * @param address  the address to register it at
   * @return the event bus message consumer
   */
  public <T> MessageConsumer<T> localConsumer(String address) {
    MessageConsumer<T> ret= MessageConsumer.newInstance(this.delegate.localConsumer(address));
    return ret;
  }

  /**
   * Like {@link #consumer(String, Handler)} but the address won't be propagated across the cluster.
   *
   * @param address  the address that will register it at
   * @param handler  the handler that will process the received messages
   * @return the event bus message consumer
   */
  public <T> MessageConsumer<T> localConsumer(String address, Handler<Message<T>> handler) {
    MessageConsumer<T> ret= MessageConsumer.newInstance(this.delegate.localConsumer(address, new Handler<io.vertx.core.eventbus.Message<T>>() {
      public void handle(io.vertx.core.eventbus.Message<T> event) {
        handler.handle(new Message<T>(event));
      }
    }));
    return ret;
  }

  /**
   * Create a message sender against the specified address.
   * <p>
   * The returned sender will invoke the {@link #send(String, Object)}
   * method when the stream {@link io.vertx.core.streams.WriteStream#write(Object)} method is called with the sender
   * address and the provided data.
   *
   * @param address  the address to send it to
   * @return The sender
   */
  public <T> MessageProducer<T> sender(String address) {
    MessageProducer<T> ret= MessageProducer.newInstance(this.delegate.sender(address));
    return ret;
  }

  /**
   * Like {@link #sender(String)} but specifying delivery options that will be used for configuring the delivery of
   * the message.
   *
   * @param address  the address to send it to
   * @param options  the delivery options
   * @return The sender
   */
  public <T> MessageProducer<T> sender(String address, DeliveryOptions options) {
    MessageProducer<T> ret= MessageProducer.newInstance(this.delegate.sender(address, options));
    return ret;
  }

  /**
   * Create a message publisher against the specified address.
   * <p>
   * The returned publisher will invoke the {@link #publish(String, Object)}
   * method when the stream {@link io.vertx.core.streams.WriteStream#write(Object)} method is called with the publisher
   * address and the provided data.
   *
   * @param address The address to publish it to
   * @return The publisher
   */
  public <T> MessageProducer<T> publisher(String address) {
    MessageProducer<T> ret= MessageProducer.newInstance(this.delegate.publisher(address));
    return ret;
  }

  /**
   * Like {@link #publisher(String)} but specifying delivery options that will be used for configuring the delivery of
   * the message.
   *
   * @param address  the address to publish it to
   * @param options  the delivery options
   * @return The publisher
   */
  public <T> MessageProducer<T> publisher(String address, DeliveryOptions options) {
    MessageProducer<T> ret= MessageProducer.newInstance(this.delegate.publisher(address, options));
    return ret;
  }

  /**
   * Close the event bus and release any resources held
   *
   * @param completionHandler may be {@code null}
   */
  public void close(Handler<AsyncResult<Void>> completionHandler) {
    this.delegate.close(completionHandler);
  }

  public Observable<Void> closeObservable() {
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    close(completionHandler.asHandler());
    return completionHandler;
  }


  public static EventBus newInstance(io.vertx.core.eventbus.EventBus arg) {
    return new EventBus(arg);
  }
}
