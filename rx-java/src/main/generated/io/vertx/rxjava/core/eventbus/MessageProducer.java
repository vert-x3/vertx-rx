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
import io.vertx.rxjava.core.streams.WriteStream;
import io.vertx.core.Handler;

/**
 * Represents a stream of message that can be written to.
 * <p>
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class MessageProducer<T> implements WriteStream<T> {

  final io.vertx.core.eventbus.MessageProducer delegate;

  public MessageProducer(io.vertx.core.eventbus.MessageProducer delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * This will return {@code true} if there are more bytes in the write queue than the value set using {@link
   * #setWriteQueueMaxSize}
   *
   * @return true if write queue is full
   */
  public boolean writeQueueFull() {
    boolean ret = this.delegate.writeQueueFull();
    return ret;
  }

  public MessageProducer<T> exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  public MessageProducer<T> write(T data) {
    this.delegate.write(data);
    return this;
  }

  public MessageProducer<T> setWriteQueueMaxSize(int maxSize) {
    this.delegate.setWriteQueueMaxSize(maxSize);
    return this;
  }

  public MessageProducer<T> drainHandler(Handler<Void> handler) {
    this.delegate.drainHandler(handler);
    return this;
  }

  /**
   * Update the delivery options of this producer.
   *
   * @param options the new options
   * @return this producer object
   */
  public MessageProducer<T> deliveryOptions(DeliveryOptions options) {
    this.delegate.deliveryOptions(options);
    return this;
  }

  /**
   * @return The address to which the producer produces messages.
   */
  public String address() {
    String ret = this.delegate.address();
    return ret;
  }


  public static <T> MessageProducer<T> newInstance(io.vertx.core.eventbus.MessageProducer arg) {
    return new MessageProducer<T> (arg);
  }
}
