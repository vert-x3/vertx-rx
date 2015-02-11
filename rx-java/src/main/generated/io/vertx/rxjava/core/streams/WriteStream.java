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

package io.vertx.rxjava.core.streams;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.core.Handler;

/**
 *
 * Represents a stream of data that can be written to.
 * <p>
 * Any class that implements this interface can be used by a {@link Pump} to pump data from a {@code ReadStream}
 * to it.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public interface WriteStream<T> extends StreamBase {

  Object getDelegate();

  WriteStream<T> exceptionHandler(Handler<Throwable> handler);

  WriteStream<T> write(T data);

  WriteStream<T> setWriteQueueMaxSize(int maxSize);

  boolean writeQueueFull();

  WriteStream<T> drainHandler(Handler<Void> handler);


  public static <T> WriteStream<T> newInstance(io.vertx.core.streams.WriteStream arg) {
    return new WriteStreamImpl<T> (arg);
  }
}

class WriteStreamImpl<T> implements WriteStream<T> {
  final io.vertx.core.streams.WriteStream delegate;

  public WriteStreamImpl(io.vertx.core.streams.WriteStream delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Set an exception handler on the write stream.
   *
   * @param handler  the exception handler
   * @return a reference to this, so the API can be used fluently
   */
  public WriteStream<T> exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  /**
   * Write some data to the stream. The data is put on an internal write queue, and the write actually happens
   * asynchronously. To avoid running out of memory by putting too much on the write queue,
   * check the {@link #writeQueueFull} method before writing. This is done automatically if using a {@link Pump}.
   *
   * @param data  the data to write
   * @return a reference to this, so the API can be used fluently
   */
  public WriteStream<T> write(T data) {
    this.delegate.write(data);
    return this;
  }

  /**
   * Set the maximum size of the write queue to {@code maxSize}. You will still be able to write to the stream even
   * if there is more than {@code maxSize} bytes in the write queue. This is used as an indicator by classes such as
   * {@code Pump} to provide flow control.
   *
   * @param maxSize  the max size of the write stream
   * @return a reference to this, so the API can be used fluently
   */
  public WriteStream<T> setWriteQueueMaxSize(int maxSize) {
    this.delegate.setWriteQueueMaxSize(maxSize);
    return this;
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

  /**
   * Set a drain handler on the stream. If the write queue is full, then the handler will be called when the write
   * queue has been reduced to maxSize / 2. See {@link Pump} for an example of this being used.
   *
   * @param handler  the handler
   * @return a reference to this, so the API can be used fluently
   */
  public WriteStream<T> drainHandler(Handler<Void> handler) {
    this.delegate.drainHandler(handler);
    return this;
  }

}
