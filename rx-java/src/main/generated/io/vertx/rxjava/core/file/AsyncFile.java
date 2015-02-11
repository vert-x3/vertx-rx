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

package io.vertx.rxjava.core.file;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.streams.WriteStream;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Represents a file on the file-system which can be read from, or written to asynchronously.
 * <p>
 * This class also implements {@link io.vertx.core.streams.ReadStream} and
 * {@link io.vertx.core.streams.WriteStream}. This allows the data to be pumped to and from
 * other streams, e.g. an {@link io.vertx.core.http.HttpClientRequest} instance,
 * using the {@link io.vertx.core.streams.Pump} class
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class AsyncFile implements ReadStream<Buffer>,  WriteStream<Buffer> {

  final io.vertx.core.file.AsyncFile delegate;

  public AsyncFile(io.vertx.core.file.AsyncFile delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<Buffer> observable;

  public synchronized rx.Observable<Buffer> toObservable() {
    if (observable == null) {
      java.util.function.Function<io.vertx.core.buffer.Buffer, Buffer> conv = Buffer::newInstance;
      io.vertx.lang.rxjava.ReadStreamAdapter<io.vertx.core.buffer.Buffer, Buffer> adapter = new io.vertx.lang.rxjava.ReadStreamAdapter<>(this, conv);
      observable = rx.Observable.create(adapter);
    }
    return observable;
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

  public AsyncFile handler(Handler<Buffer> handler) {
    this.delegate.handler(new Handler<io.vertx.core.buffer.Buffer>() {
      public void handle(io.vertx.core.buffer.Buffer event) {
        handler.handle(new Buffer(event));
      }
    });
    return this;
  }

  public AsyncFile pause() {
    this.delegate.pause();
    return this;
  }

  public AsyncFile resume() {
    this.delegate.resume();
    return this;
  }

  public AsyncFile endHandler(Handler<Void> endHandler) {
    this.delegate.endHandler(endHandler);
    return this;
  }

  public AsyncFile write(Buffer data) {
    this.delegate.write((io.vertx.core.buffer.Buffer) data.getDelegate());
    return this;
  }

  public AsyncFile setWriteQueueMaxSize(int maxSize) {
    this.delegate.setWriteQueueMaxSize(maxSize);
    return this;
  }

  public AsyncFile drainHandler(Handler<Void> handler) {
    this.delegate.drainHandler(handler);
    return this;
  }

  public AsyncFile exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  /**
   * Close the file. The actual close happens asynchronously.
   */
  public void close() {
    this.delegate.close();
  }

  /**
   * Close the file. The actual close happens asynchronously.
   * The handler will be called when the close is complete, or an error occurs.
   *
   * @param handler  the handler
   */
  public void close(Handler<AsyncResult<Void>> handler) {
    this.delegate.close(handler);
  }

  public Observable<Void> closeObservable() {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    close(handler.asHandler());
    return handler;
  }

  /**
   * Write a {@link io.vertx.core.buffer.Buffer} to the file at position {@code position} in the file, asynchronously.
   * <p>
   * If {@code position} lies outside of the current size
   * of the file, the file will be enlarged to encompass it.
   * <p>
   * When multiple writes are invoked on the same file
   * there are no guarantees as to order in which those writes actually occur
   * <p>
   * The handler will be called when the write is complete, or if an error occurs.
   *
   * @param buffer  the buffer to write
   * @param position  the position in the file to write it at
   * @param handler  the handler to call when the write is complete
   * @return a reference to this, so the API can be used fluently
   */
  public AsyncFile write(Buffer buffer, long position, Handler<AsyncResult<Void>> handler) {
    this.delegate.write((io.vertx.core.buffer.Buffer) buffer.getDelegate(), position, handler);
    return this;
  }

  public Observable<Void> writeObservable(Buffer buffer, long position) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    write(buffer, position, handler.asHandler());
    return handler;
  }

  /**
   * Reads {@code length} bytes of data from the file at position {@code position} in the file, asynchronously.
   * <p>
   * The read data will be written into the specified {@code Buffer buffer} at position {@code offset}.
   * <p>
   * If data is read past the end of the file then zero bytes will be read.<p>
   * When multiple reads are invoked on the same file there are no guarantees as to order in which those reads actually occur.
   * <p>
   * The handler will be called when the close is complete, or if an error occurs.
   *
   * @param buffer  the buffer to read into
   * @param offset  the offset into the buffer where the data will be read
   * @param position  the position in the file where to start reading
   * @param length  the number of bytes to read
   * @param handler  the handler to call when the write is complete
   * @return a reference to this, so the API can be used fluently
   */
  public AsyncFile read(Buffer buffer, int offset, long position, int length, Handler<AsyncResult<Buffer>> handler) {
    this.delegate.read((io.vertx.core.buffer.Buffer) buffer.getDelegate(), offset, position, length, new Handler<AsyncResult<io.vertx.core.buffer.Buffer>>() {
      public void handle(AsyncResult<io.vertx.core.buffer.Buffer> event) {
        AsyncResult<Buffer> f;
        if (event.succeeded()) {
          f = InternalHelper.<Buffer>result(new Buffer(event.result()));
        } else {
          f = InternalHelper.<Buffer>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<Buffer> readObservable(Buffer buffer, int offset, long position, int length) {
    io.vertx.rx.java.ObservableFuture<Buffer> handler = io.vertx.rx.java.RxHelper.observableFuture();
    read(buffer, offset, position, length, handler.asHandler());
    return handler;
  }

  /**
   * Flush any writes made to this file to underlying persistent storage.
   * <p>
   * If the file was opened with {@code flush} set to {@code true} then calling this method will have no effect.
   * <p>
   * The actual flush will happen asynchronously.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public AsyncFile flush() {
    this.delegate.flush();
    return this;
  }

  /**
   * Same as {@link #flush} but the handler will be called when the flush is complete or if an error occurs
   */
  public AsyncFile flush(Handler<AsyncResult<Void>> handler) {
    this.delegate.flush(handler);
    return this;
  }

  public Observable<Void> flushObservable() {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    flush(handler.asHandler());
    return handler;
  }

  /**
   * Sets the position from which data will be read from when using the file as a {@link io.vertx.core.streams.ReadStream}.
   *
   * @param readPos  the position in the file
   * @return a reference to this, so the API can be used fluently
   */
  public AsyncFile setReadPos(long readPos) {
    this.delegate.setReadPos(readPos);
    return this;
  }

  /**
   * Sets the position from which data will be written when using the file as a {@link io.vertx.core.streams.WriteStream}.
   *
   * @param writePos  the position in the file
   * @return a reference to this, so the API can be used fluently
   */
  public AsyncFile setWritePos(long writePos) {
    this.delegate.setWritePos(writePos);
    return this;
  }


  public static AsyncFile newInstance(io.vertx.core.file.AsyncFile arg) {
    return new AsyncFile(arg);
  }
}
