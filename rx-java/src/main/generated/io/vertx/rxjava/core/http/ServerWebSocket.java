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
import io.vertx.rxjava.core.MultiMap;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.net.SocketAddress;

/**
 * Represents a server side WebSocket.
 * <p>
 * Instances of this class are passed into a {@link io.vertx.core.http.HttpServer#websocketHandler} or provided
 * when a WebSocket handshake is manually {@link HttpServerRequest#upgrade}ed.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class ServerWebSocket implements WebSocketBase {

  final io.vertx.core.http.ServerWebSocket delegate;

  public ServerWebSocket(io.vertx.core.http.ServerWebSocket delegate) {
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

  public ServerWebSocket exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  public ServerWebSocket handler(Handler<Buffer> handler) {
    this.delegate.handler(new Handler<io.vertx.core.buffer.Buffer>() {
      public void handle(io.vertx.core.buffer.Buffer event) {
        handler.handle(new Buffer(event));
      }
    });
    return this;
  }

  public ServerWebSocket pause() {
    this.delegate.pause();
    return this;
  }

  public ServerWebSocket resume() {
    this.delegate.resume();
    return this;
  }

  public ServerWebSocket endHandler(Handler<Void> endHandler) {
    this.delegate.endHandler(endHandler);
    return this;
  }

  public ServerWebSocket write(Buffer data) {
    this.delegate.write((io.vertx.core.buffer.Buffer) data.getDelegate());
    return this;
  }

  public ServerWebSocket setWriteQueueMaxSize(int maxSize) {
    this.delegate.setWriteQueueMaxSize(maxSize);
    return this;
  }

  public ServerWebSocket drainHandler(Handler<Void> handler) {
    this.delegate.drainHandler(handler);
    return this;
  }

  public ServerWebSocket writeFrame(WebSocketFrame frame) {
    this.delegate.writeFrame((io.vertx.core.http.WebSocketFrame) frame.getDelegate());
    return this;
  }

  public ServerWebSocket writeMessage(Buffer data) {
    this.delegate.writeMessage((io.vertx.core.buffer.Buffer) data.getDelegate());
    return this;
  }

  public ServerWebSocket closeHandler(Handler<Void> handler) {
    this.delegate.closeHandler(handler);
    return this;
  }

  public ServerWebSocket frameHandler(Handler<WebSocketFrame> handler) {
    this.delegate.frameHandler(new Handler<io.vertx.core.http.WebSocketFrame>() {
      public void handle(io.vertx.core.http.WebSocketFrame event) {
        handler.handle(new WebSocketFrame(event));
      }
    });
    return this;
  }

  public String uri() {
    String ret = this.delegate.uri();
    return ret;
  }

  /**
   * @return the WebSocket handshake path.
   */
  public String path() {
    String ret = this.delegate.path();
    return ret;
  }

  /**
   * @return the WebSocket handshake query string.
   */
  public String query() {
    String ret = this.delegate.query();
    return ret;
  }

  /**
   * @return the headers in the WebSocket handshake
   */
  public MultiMap headers() {
    if (cached_2 != null) {
      return cached_2;
    }
    MultiMap ret= MultiMap.newInstance(this.delegate.headers());
    cached_2 = ret;
    return ret;
  }

  /**
   * Reject the WebSocket.
   * <p>
   * Calling this method from the websocket handler when it is first passed to you gives you the opportunity to reject
   * the websocket, which will cause the websocket handshake to fail by returning
   * a 404 response code.
   * <p>
   * You might use this method, if for example you only want to accept WebSockets with a particular path.
   */
  public void reject() {
    this.delegate.reject();
  }

  private SocketAddress cached_0;
  private SocketAddress cached_1;
  private MultiMap cached_2;

  public static ServerWebSocket newInstance(io.vertx.core.http.ServerWebSocket arg) {
    return new ServerWebSocket(arg);
  }
}
