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
import io.vertx.rxjava.core.metrics.Measured;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * A TCP client.
 * <p>
 * Multiple connections to different servers can be made using the same instance.
 * <p>
 * This client supports a configurable number of connection attempts and a configurable
 * delay between attempts.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class NetClient implements Measured {

  final io.vertx.core.net.NetClient delegate;

  public NetClient(io.vertx.core.net.NetClient delegate) {
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
   * Open a connection to a server at the specific {@code port} and {@code host}.
   * <p>
   * {@code host} can be a valid host name or IP address. The connect is done asynchronously and on success, a
   * {@link NetSocket} instance is supplied via the {@code connectHandler} instance
   *
   * @param port  the port
   * @param host  the host
   * @return a reference to this, so the API can be used fluently
   */
  public NetClient connect(int port, String host, Handler<AsyncResult<NetSocket>> connectHandler) {
    this.delegate.connect(port, host, new Handler<AsyncResult<io.vertx.core.net.NetSocket>>() {
      public void handle(AsyncResult<io.vertx.core.net.NetSocket> event) {
        AsyncResult<NetSocket> f;
        if (event.succeeded()) {
          f = InternalHelper.<NetSocket>result(new NetSocket(event.result()));
        } else {
          f = InternalHelper.<NetSocket>failure(event.cause());
        }
        connectHandler.handle(f);
      }
    });
    return this;
  }

  public Observable<NetSocket> connectObservable(int port, String host) {
    io.vertx.rx.java.ObservableFuture<NetSocket> connectHandler = io.vertx.rx.java.RxHelper.observableFuture();
    connect(port, host, connectHandler.toHandler());
    return connectHandler;
  }

  /**
   * Close the client.
   * <p>
   * Any sockets which have not been closed manually will be closed here. The close is asynchronous and may not
   * complete until some time after the method has returned.
   */
  public void close() {
    this.delegate.close();
  }


  public static NetClient newInstance(io.vertx.core.net.NetClient arg) {
    return new NetClient(arg);
  }
}
