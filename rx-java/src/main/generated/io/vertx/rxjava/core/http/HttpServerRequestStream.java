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
 * A {@link io.vertx.core.streams.ReadStream} of {@link io.vertx.core.http.HttpServerRequest}, used for
 * notifying http request to a {@link io.vertx.core.http.HttpServer}.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class HttpServerRequestStream implements ReadStream<HttpServerRequest> {

  final io.vertx.core.http.HttpServerRequestStream delegate;

  public HttpServerRequestStream(io.vertx.core.http.HttpServerRequestStream delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<HttpServerRequest> observable;

  public synchronized rx.Observable<HttpServerRequest> toObservable() {
    if (observable == null) {
      java.util.function.Function<io.vertx.core.http.HttpServerRequest, HttpServerRequest> conv = HttpServerRequest::newInstance;
      io.vertx.lang.rxjava.ReadStreamAdapter<io.vertx.core.http.HttpServerRequest, HttpServerRequest> adapter = new io.vertx.lang.rxjava.ReadStreamAdapter<>(this, conv);
      observable = rx.Observable.create(adapter);
    }
    return observable;
  }

  public HttpServerRequestStream exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  public HttpServerRequestStream handler(Handler<HttpServerRequest> handler) {
    this.delegate.handler(new Handler<io.vertx.core.http.HttpServerRequest>() {
      public void handle(io.vertx.core.http.HttpServerRequest event) {
        handler.handle(new HttpServerRequest(event));
      }
    });
    return this;
  }

  public HttpServerRequestStream pause() {
    this.delegate.pause();
    return this;
  }

  public HttpServerRequestStream resume() {
    this.delegate.resume();
    return this;
  }

  public HttpServerRequestStream endHandler(Handler<Void> endHandler) {
    this.delegate.endHandler(endHandler);
    return this;
  }


  public static HttpServerRequestStream newInstance(io.vertx.core.http.HttpServerRequestStream arg) {
    return new HttpServerRequestStream(arg);
  }
}
