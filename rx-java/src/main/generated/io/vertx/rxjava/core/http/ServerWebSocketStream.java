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
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.core.Handler;

/**
 * A {@link io.vertx.core.streams.ReadStream} of {@link io.vertx.core.http.ServerWebSocket}, used for
 * notifying web socket connections to a {@link io.vertx.core.http.HttpServer}.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class ServerWebSocketStream implements ReadStream<ServerWebSocket> {

  final io.vertx.core.http.ServerWebSocketStream delegate;

  public ServerWebSocketStream(io.vertx.core.http.ServerWebSocketStream delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<ServerWebSocket> observable;

  public synchronized rx.Observable<ServerWebSocket> toObservable() {
    if (observable == null) {
      java.util.function.Function<io.vertx.core.http.ServerWebSocket, ServerWebSocket> conv = ServerWebSocket::newInstance;
      io.vertx.lang.rxjava.ReadStreamAdapter<io.vertx.core.http.ServerWebSocket, ServerWebSocket> adapter = new io.vertx.lang.rxjava.ReadStreamAdapter<>(this, conv);
      observable = rx.Observable.create(adapter);
    }
    return observable;
  }

  public ServerWebSocketStream exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  public ServerWebSocketStream handler(Handler<ServerWebSocket> handler) {
    this.delegate.handler(new Handler<io.vertx.core.http.ServerWebSocket>() {
      public void handle(io.vertx.core.http.ServerWebSocket event) {
        handler.handle(new ServerWebSocket(event));
      }
    });
    return this;
  }

  public ServerWebSocketStream pause() {
    this.delegate.pause();
    return this;
  }

  public ServerWebSocketStream resume() {
    this.delegate.resume();
    return this;
  }

  public ServerWebSocketStream endHandler(Handler<Void> endHandler) {
    this.delegate.endHandler(endHandler);
    return this;
  }


  public static ServerWebSocketStream newInstance(io.vertx.core.http.ServerWebSocketStream arg) {
    return new ServerWebSocketStream(arg);
  }
}
