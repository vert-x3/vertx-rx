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

package io.vertx.rxjava.core.net;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.core.Handler;

/**
 * A {@link io.vertx.core.streams.ReadStream} of {@link io.vertx.core.net.NetSocket}, used for notifying
 * socket connections to a {@link io.vertx.core.net.NetServer}.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class NetSocketStream implements ReadStream<NetSocket> {

  final io.vertx.core.net.NetSocketStream delegate;

  public NetSocketStream(io.vertx.core.net.NetSocketStream delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<NetSocket> observable;

  public synchronized rx.Observable<NetSocket> toObservable() {
    if (observable == null) {
      java.util.function.Function<io.vertx.core.net.NetSocket, NetSocket> conv = NetSocket::newInstance;
      io.vertx.lang.rxjava.ReadStreamAdapter<io.vertx.core.net.NetSocket, NetSocket> adapter = new io.vertx.lang.rxjava.ReadStreamAdapter<>(this, conv);
      observable = rx.Observable.create(adapter);
    }
    return observable;
  }

  public NetSocketStream exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  public NetSocketStream handler(Handler<NetSocket> handler) {
    this.delegate.handler(new Handler<io.vertx.core.net.NetSocket>() {
      public void handle(io.vertx.core.net.NetSocket event) {
        handler.handle(new NetSocket(event));
      }
    });
    return this;
  }

  public NetSocketStream pause() {
    this.delegate.pause();
    return this;
  }

  public NetSocketStream resume() {
    this.delegate.resume();
    return this;
  }

  public NetSocketStream endHandler(Handler<Void> endHandler) {
    this.delegate.endHandler(endHandler);
    return this;
  }


  public static NetSocketStream newInstance(io.vertx.core.net.NetSocketStream arg) {
    return new NetSocketStream(arg);
  }
}
