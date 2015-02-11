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
import io.vertx.rxjava.core.MultiMap;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.http.HttpMethod;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.net.SocketAddress;
import io.vertx.rxjava.core.net.NetSocket;

/**
 * Represents a server-side HTTP request.
 * <p>
 * Instances are created for each request and passed to the user via a handler.
 * <p>
 * Each instance of this class is associated with a corresponding {@link HttpServerResponse} instance via
 * {@link #response}.<p>
 * It implements {@link io.vertx.core.streams.ReadStream} so it can be used with
 * {@link io.vertx.core.streams.Pump} to pump data with flow control.
 * <p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class HttpServerRequest implements ReadStream<Buffer> {

  final io.vertx.core.http.HttpServerRequest delegate;

  public HttpServerRequest(io.vertx.core.http.HttpServerRequest delegate) {
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

  public HttpServerRequest exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  public HttpServerRequest handler(Handler<Buffer> handler) {
    this.delegate.handler(new Handler<io.vertx.core.buffer.Buffer>() {
      public void handle(io.vertx.core.buffer.Buffer event) {
        handler.handle(new Buffer(event));
      }
    });
    return this;
  }

  public HttpServerRequest pause() {
    this.delegate.pause();
    return this;
  }

  public HttpServerRequest resume() {
    this.delegate.resume();
    return this;
  }

  public HttpServerRequest endHandler(Handler<Void> endHandler) {
    this.delegate.endHandler(endHandler);
    return this;
  }

  /**
   * @return the HTTP version of the request
   */
  public HttpVersion version() {
    HttpVersion ret = this.delegate.version();
    return ret;
  }

  /**
   * @return the HTTP method for the request.
   */
  public HttpMethod method() {
    HttpMethod ret = this.delegate.method();
    return ret;
  }

  /**
   * @return the URI of the request. This is usually a relative URI
   */
  public String uri() {
    String ret = this.delegate.uri();
    return ret;
  }

  /**
   * @return The path part of the uri. For example /somepath/somemorepath/someresource.foo
   */
  public String path() {
    String ret = this.delegate.path();
    return ret;
  }

  /**
   * @return the query part of the uri. For example someparam=32&amp;someotherparam=x
   */
  public String query() {
    String ret = this.delegate.query();
    return ret;
  }

  /**
   * @return the response. Each instance of this class has an {@link HttpServerResponse} instance attached to it. This is used
   * to send the response back to the client.
   */
  public HttpServerResponse response() {
    if (cached_0 != null) {
      return cached_0;
    }
    HttpServerResponse ret= HttpServerResponse.newInstance(this.delegate.response());
    cached_0 = ret;
    return ret;
  }

  /**
   * @return the headers in the request.
   */
  public MultiMap headers() {
    if (cached_1 != null) {
      return cached_1;
    }
    MultiMap ret= MultiMap.newInstance(this.delegate.headers());
    cached_1 = ret;
    return ret;
  }

  /**
   * Return the first header value with the specified name
   *
   * @param headerName  the header name
   * @return the header value
   */
  public String getHeader(String headerName) {
    String ret = this.delegate.getHeader(headerName);
    return ret;
  }

  /**
   * @return the query parameters in the request
   */
  public MultiMap params() {
    if (cached_2 != null) {
      return cached_2;
    }
    MultiMap ret= MultiMap.newInstance(this.delegate.params());
    cached_2 = ret;
    return ret;
  }

  /**
   * Return the first param value with the specified name
   *
   * @param paramName  the param name
   * @return the param value
   */
  public String getParam(String paramName) {
    String ret = this.delegate.getParam(paramName);
    return ret;
  }

  /**
   * @return the remote (client side) address of the request
   */
  public SocketAddress remoteAddress() {
    if (cached_3 != null) {
      return cached_3;
    }
    SocketAddress ret= SocketAddress.newInstance(this.delegate.remoteAddress());
    cached_3 = ret;
    return ret;
  }

  /**
   * @return the local (server side) address of the server that handles the request
   */
  public SocketAddress localAddress() {
    if (cached_4 != null) {
      return cached_4;
    }
    SocketAddress ret= SocketAddress.newInstance(this.delegate.localAddress());
    cached_4 = ret;
    return ret;
  }

  /**
   * @return the absolute URI corresponding to the the HTTP request
   */
  public String absoluteURI() {
    String ret = this.delegate.absoluteURI();
    return ret;
  }

  /**
   * Convenience method for receiving the entire request body in one piece.
   * <p>
   * This saves the user having to manually setting a data and end handler and append the chunks of the body until
   * the whole body received. Don't use this if your request body is large - you could potentially run out of RAM.
   *
   * @param bodyHandler This handler will be called after all the body has been received
   */
  public HttpServerRequest bodyHandler(Handler<Buffer> bodyHandler) {
    this.delegate.bodyHandler(new Handler<io.vertx.core.buffer.Buffer>() {
      public void handle(io.vertx.core.buffer.Buffer event) {
        bodyHandler.handle(new Buffer(event));
      }
    });
    return this;
  }

  /**
   * Get a net socket for the underlying connection of this request.
   * <p>
   * USE THIS WITH CAUTION!
   * <p>
   * Writing to the socket directly if you don't know what you're doing can easily break the HTTP protocol
   *
   * @return the net socket
   */
  public NetSocket netSocket() {
    if (cached_5 != null) {
      return cached_5;
    }
    NetSocket ret= NetSocket.newInstance(this.delegate.netSocket());
    cached_5 = ret;
    return ret;
  }

  /**
   * Call this with true if you are expecting a multi-part body to be submitted in the request.
   * This must be called before the body of the request has been received
   *
   * @param expect  true - if you are expecting a multi-part body
   * @return a reference to this, so the API can be used fluently
   */
  public HttpServerRequest setExpectMultipart(boolean expect) {
    this.delegate.setExpectMultipart(expect);
    return this;
  }

  /**
   * @return  true if we are expecting a multi-part body for this request. See {@link #setExpectMultipart}.
   */
  public boolean isExpectMultipart() {
    boolean ret = this.delegate.isExpectMultipart();
    return ret;
  }

  /**
   * Set an upload handler. The handler will get notified once a new file upload was received to allow you to deal
   * with the file upload.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public HttpServerRequest uploadHandler(Handler<HttpServerFileUpload> uploadHandler) {
    this.delegate.uploadHandler(new Handler<io.vertx.core.http.HttpServerFileUpload>() {
      public void handle(io.vertx.core.http.HttpServerFileUpload event) {
        uploadHandler.handle(new HttpServerFileUpload(event));
      }
    });
    return this;
  }

  /**
   * Returns a map of all form attributes in the request.
   * <p>
   * Be aware that the attributes will only be available after the whole body has been received, i.e. after
   * the request end handler has been called.
   * <p>
   * {@link #setExpectMultipart(boolean)} must be called first before trying to get the form attributes.
   *
   * @return the form attributes
   */
  public MultiMap formAttributes() {
    if (cached_6 != null) {
      return cached_6;
    }
    MultiMap ret= MultiMap.newInstance(this.delegate.formAttributes());
    cached_6 = ret;
    return ret;
  }

  /**
   * Return the first form attribute value with the specified name
   *
   * @param attributeName  the attribute name
   * @return the attribute value
   */
  public String getFormAttribute(String attributeName) {
    String ret = this.delegate.getFormAttribute(attributeName);
    return ret;
  }

  /**
   * Upgrade the connection to a WebSocket connection.
   * <p>
   * This is an alternative way of handling WebSockets and can only be used if no websocket handlers are set on the
   * Http server, and can only be used during the upgrade request during the WebSocket handshake.
   *
   * @return  the WebSocket
   */
  public ServerWebSocket upgrade() {
    ServerWebSocket ret= ServerWebSocket.newInstance(this.delegate.upgrade());
    return ret;
  }

  private HttpServerResponse cached_0;
  private MultiMap cached_1;
  private MultiMap cached_2;
  private SocketAddress cached_3;
  private SocketAddress cached_4;
  private NetSocket cached_5;
  private MultiMap cached_6;

  public static HttpServerRequest newInstance(io.vertx.core.http.HttpServerRequest arg) {
    return new HttpServerRequest(arg);
  }
}
