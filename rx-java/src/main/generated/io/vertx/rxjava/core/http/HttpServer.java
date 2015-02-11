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
import io.vertx.rxjava.core.metrics.Measured;
import java.util.Map;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * An HTTP and WebSockets server.
 * <p>
 * You receive HTTP requests by providing a {@link #requestHandler}. As requests arrive on the server the handler
 * will be called with the requests.
 * <p>
 * You receive WebSockets by providing a {@link #websocketHandler}. As WebSocket connections arrive on the server, the
 * WebSocket is passed to the handler.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class HttpServer implements Measured {

  final io.vertx.core.http.HttpServer delegate;

  public HttpServer(io.vertx.core.http.HttpServer delegate) {
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
   * Return the request stream for the server. As HTTP requests are received by the server,
   * instances of {@link HttpServerRequest} will be created and passed to the stream {@link io.vertx.core.streams.ReadStream#handler(io.vertx.core.Handler)}.
   *
   * @return the request stream
   */
  public HttpServerRequestStream requestStream() {
    HttpServerRequestStream ret= HttpServerRequestStream.newInstance(this.delegate.requestStream());
    return ret;
  }

  /**
   * Set the request handler for the server to {@code requestHandler}. As HTTP requests are received by the server,
   * instances of {@link HttpServerRequest} will be created and passed to this handler.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public HttpServer requestHandler(Handler<HttpServerRequest> handler) {
    HttpServer ret= HttpServer.newInstance(this.delegate.requestHandler(new Handler<io.vertx.core.http.HttpServerRequest>() {
      public void handle(io.vertx.core.http.HttpServerRequest event) {
        handler.handle(new HttpServerRequest(event));
      }
    }));
    return ret;
  }

  /**
   * Return the websocket stream for the server. If a websocket connect handshake is successful a
   * new {@link ServerWebSocket} instance will be created and passed to the stream {@link io.vertx.core.streams.ReadStream#handler(io.vertx.core.Handler)}.
   *
   * @return the websocket stream
   */
  public ServerWebSocketStream websocketStream() {
    ServerWebSocketStream ret= ServerWebSocketStream.newInstance(this.delegate.websocketStream());
    return ret;
  }

  /**
   * Set the websocket handler for the server to {@code wsHandler}. If a websocket connect handshake is successful a
   * new {@link ServerWebSocket} instance will be created and passed to the handler.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public HttpServer websocketHandler(Handler<ServerWebSocket> handler) {
    HttpServer ret= HttpServer.newInstance(this.delegate.websocketHandler(new Handler<io.vertx.core.http.ServerWebSocket>() {
      public void handle(io.vertx.core.http.ServerWebSocket event) {
        handler.handle(new ServerWebSocket(event));
      }
    }));
    return ret;
  }

  /**
   * Tell the server to start listening. The server will listen on the port and host specified in the
   * {@link io.vertx.core.http.HttpServerOptions} that was used when creating the server.
   * <p>
   * The listen happens asynchronously and the server may not be listening until some time after the call has returned.
   *
   * @return a reference to this, so the API can be used fluently
   */
  public HttpServer listen() {
    this.delegate.listen();
    return this;
  }

  /**
   * Tell the server to start listening. The server will listen on the port and host specified here,
   * ignoring any value set in the {@link io.vertx.core.http.HttpServerOptions} that was used when creating the server.
   * <p>
   * The listen happens asynchronously and the server may not be listening until some time after the call has returned.
   *
   * @param port  the port to listen on
   * @param host  the host to listen on
   *
   * @return a reference to this, so the API can be used fluently
   */
  public HttpServer listen(int port, String host) {
    this.delegate.listen(port, host);
    return this;
  }

  /**
   * Like {@link #listen(int, String)} but supplying a handler that will be called when the server is actually
   * listening (or has failed).
   *
   * @param port  the port to listen on
   * @param host  the host to listen on
   * @param listenHandler  the listen handler
   */
  public HttpServer listen(int port, String host, Handler<AsyncResult<HttpServer>> listenHandler) {
    this.delegate.listen(port, host, new Handler<AsyncResult<io.vertx.core.http.HttpServer>>() {
      public void handle(AsyncResult<io.vertx.core.http.HttpServer> event) {
        AsyncResult<HttpServer> f;
        if (event.succeeded()) {
          f = InternalHelper.<HttpServer>result(new HttpServer(event.result()));
        } else {
          f = InternalHelper.<HttpServer>failure(event.cause());
        }
        listenHandler.handle(f);
      }
    });
    return this;
  }

  public Observable<HttpServer> listenObservable(int port, String host) {
    io.vertx.rx.java.ObservableFuture<HttpServer> listenHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listen(port, host, listenHandler.asHandler());
    return listenHandler;
  }

  /**
   * Like {@link #listen(int, String)} but the server will listen on host "0.0.0.0" and port specified here ignoring
   * any value in the {@link io.vertx.core.http.HttpServerOptions} that was used when creating the server.
   *
   * @param port  the port to listen on
   *
   * @return a reference to this, so the API can be used fluently
   */
  public HttpServer listen(int port) {
    this.delegate.listen(port);
    return this;
  }

  /**
   * Like {@link #listen(int)} but supplying a handler that will be called when the server is actually listening (or has failed).
   *
   * @param port  the port to listen on
   * @param listenHandler  the listen handler
   */
  public HttpServer listen(int port, Handler<AsyncResult<HttpServer>> listenHandler) {
    this.delegate.listen(port, new Handler<AsyncResult<io.vertx.core.http.HttpServer>>() {
      public void handle(AsyncResult<io.vertx.core.http.HttpServer> event) {
        AsyncResult<HttpServer> f;
        if (event.succeeded()) {
          f = InternalHelper.<HttpServer>result(new HttpServer(event.result()));
        } else {
          f = InternalHelper.<HttpServer>failure(event.cause());
        }
        listenHandler.handle(f);
      }
    });
    return this;
  }

  public Observable<HttpServer> listenObservable(int port) {
    io.vertx.rx.java.ObservableFuture<HttpServer> listenHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listen(port, listenHandler.asHandler());
    return listenHandler;
  }

  /**
   * Like {@link #listen} but supplying a handler that will be called when the server is actually listening (or has failed).
   *
   * @param listenHandler  the listen handler
   */
  public HttpServer listen(Handler<AsyncResult<HttpServer>> listenHandler) {
    this.delegate.listen(new Handler<AsyncResult<io.vertx.core.http.HttpServer>>() {
      public void handle(AsyncResult<io.vertx.core.http.HttpServer> event) {
        AsyncResult<HttpServer> f;
        if (event.succeeded()) {
          f = InternalHelper.<HttpServer>result(new HttpServer(event.result()));
        } else {
          f = InternalHelper.<HttpServer>failure(event.cause());
        }
        listenHandler.handle(f);
      }
    });
    return this;
  }

  public Observable<HttpServer> listenObservable() {
    io.vertx.rx.java.ObservableFuture<HttpServer> listenHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listen(listenHandler.asHandler());
    return listenHandler;
  }

  /**
   * Close the server. Any open HTTP connections will be closed.
   * <p>
   * The close happens asynchronously and the server may not be closed until some time after the call has returned.
   */
  public void close() {
    this.delegate.close();
  }

  /**
   * Like {@link #close} but supplying a handler that will be called when the server is actually closed (or has failed).
   *
   * @param completionHandler  the handler
   */
  public void close(Handler<AsyncResult<Void>> completionHandler) {
    this.delegate.close(completionHandler);
  }

  public Observable<Void> closeObservable() {
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    close(completionHandler.asHandler());
    return completionHandler;
  }


  public static HttpServer newInstance(io.vertx.core.http.HttpServer arg) {
    return new HttpServer(arg);
  }
}
