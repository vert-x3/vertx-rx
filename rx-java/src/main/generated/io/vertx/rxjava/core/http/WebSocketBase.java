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

package io.vertx.rxjava.core.http;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.streams.WriteStream;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.net.SocketAddress;

/**
 * Base WebSocket implementation.
 * <p>
 * It implements both {@link ReadStream} and {@link WriteStream} so it can be used with
 * {@link io.vertx.core.streams.Pump} to pump data with flow control.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public interface WebSocketBase extends ReadStream<Buffer>,  WriteStream<Buffer> {

  Object getDelegate();

  boolean writeQueueFull();

  WebSocketBase exceptionHandler(Handler<Throwable> handler);

  WebSocketBase handler(Handler<Buffer> handler);

  WebSocketBase pause();

  WebSocketBase resume();

  WebSocketBase endHandler(Handler<Void> endHandler);

  WebSocketBase write(Buffer data);

  WebSocketBase setWriteQueueMaxSize(int maxSize);

  WebSocketBase drainHandler(Handler<Void> handler);

  String binaryHandlerID();

  String textHandlerID();

  WebSocketBase writeFrame(WebSocketFrame frame);

  WebSocketBase writeMessage(Buffer data);

  WebSocketBase closeHandler(Handler<Void> handler);

  WebSocketBase frameHandler(Handler<WebSocketFrame> handler);

  void close();

  SocketAddress remoteAddress();

  SocketAddress localAddress();


  public static WebSocketBase newInstance(io.vertx.core.http.WebSocketBase arg) {
    return new WebSocketBaseImpl(arg);
  }
}

class WebSocketBaseImpl implements WebSocketBase {
  final io.vertx.core.http.WebSocketBase delegate;

  public WebSocketBaseImpl(io.vertx.core.http.WebSocketBase delegate) {
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

  public WebSocketBase exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  public WebSocketBase handler(Handler<Buffer> handler) {
    this.delegate.handler(new Handler<io.vertx.core.buffer.Buffer>() {
      public void handle(io.vertx.core.buffer.Buffer event) {
        handler.handle(new Buffer(event));
      }
    });
    return this;
  }

  public WebSocketBase pause() {
    this.delegate.pause();
    return this;
  }

  public WebSocketBase resume() {
    this.delegate.resume();
    return this;
  }

  public WebSocketBase endHandler(Handler<Void> endHandler) {
    this.delegate.endHandler(endHandler);
    return this;
  }

  public WebSocketBase write(Buffer data) {
    this.delegate.write((io.vertx.core.buffer.Buffer) data.getDelegate());
    return this;
  }

  public WebSocketBase setWriteQueueMaxSize(int maxSize) {
    this.delegate.setWriteQueueMaxSize(maxSize);
    return this;
  }

  public WebSocketBase drainHandler(Handler<Void> handler) {
    this.delegate.drainHandler(handler);
    return this;
  }

  /**
   * When a {@code Websocket} is created it automatically registers an event handler with the event bus - the ID of that
   * handler is given by this method.
   * <p>
   * Given this ID, a different event loop can send a binary frame to that event handler using the event bus and
   * that buffer will be received by this instance in its own event loop and written to the underlying connection. This
   * allows you to write data to other WebSockets which are owned by different event loops.
   *
   * @return the binary handler id
   */
  public String binaryHandlerID() {
    String ret = this.delegate.binaryHandlerID();
    return ret;
  }

  /**
   * When a {@code Websocket} is created it automatically registers an event handler with the eventbus, the ID of that
   * handler is given by {@code textHandlerID}.
   * <p>
   * Given this ID, a different event loop can send a text frame to that event handler using the event bus and
   * that buffer will be received by this instance in its own event loop and written to the underlying connection. This
   * allows you to write data to other WebSockets which are owned by different event loops.
   */
  public String textHandlerID() {
    String ret = this.delegate.textHandlerID();
    return ret;
  }

  /**
   * Write a WebSocket frame to the connection
   *
   * @param frame  the frame to write
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketBase writeFrame(WebSocketFrame frame) {
    this.delegate.writeFrame((io.vertx.core.http.WebSocketFrame) frame.getDelegate());
    return this;
  }

  /**
   * Writes a (potentially large) piece of data to the connection. This data might be written as multiple frames
   * if it exceeds the maximum WebSocket frame size.
   *
   * @param data  the data to write
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketBase writeMessage(Buffer data) {
    this.delegate.writeMessage((io.vertx.core.buffer.Buffer) data.getDelegate());
    return this;
  }

  /**
   * Set a close handler. This will be called when the WebSocket is closed.
   *
   * @param handler  the handler
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketBase closeHandler(Handler<Void> handler) {
    this.delegate.closeHandler(handler);
    return this;
  }

  /**
   * Set a frame handler on the connection. This handler will be called when frames are read on the connection.
   *
   * @param handler  the handler
   * @return a reference to this, so the API can be used fluently
   */
  public WebSocketBase frameHandler(Handler<WebSocketFrame> handler) {
    this.delegate.frameHandler(new Handler<io.vertx.core.http.WebSocketFrame>() {
      public void handle(io.vertx.core.http.WebSocketFrame event) {
        handler.handle(new WebSocketFrame(event));
      }
    });
    return this;
  }

  /**
   * Close the WebSocket.
   */
  public void close() {
    this.delegate.close();
  }

  /**
   * @return the remote address for this socket
   */
  public SocketAddress remoteAddress() {
    if (cached_0 != null) {
      return cached_0;
    }
    SocketAddress ret= SocketAddress.newInstance(this.delegate.remoteAddress());
    cached_0 = ret;
    return ret;
  }

  /**
   * @return the local address for this socket
   */
  public SocketAddress localAddress() {
    if (cached_1 != null) {
      return cached_1;
    }
    SocketAddress ret= SocketAddress.newInstance(this.delegate.localAddress());
    cached_1 = ret;
    return ret;
  }

  private SocketAddress cached_0;
  private SocketAddress cached_1;
}
