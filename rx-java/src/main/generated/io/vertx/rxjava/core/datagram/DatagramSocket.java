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

package io.vertx.rxjava.core.datagram;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.metrics.Measured;
import java.util.Map;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.net.SocketAddress;

/**
 * A datagram socket can be used to send {@link DatagramPacket}'s to remote datagram servers
 * and receive {@link DatagramPacket}s .
 * <p>
 * Usually you use a datagram socket to send UDP over the wire. UDP is connection-less which means you are not connected
 * to the remote peer in a persistent way. Because of this you have to supply the address and port of the remote peer
 * when sending data.
 * <p>
 * You can send data to ipv4 or ipv6 addresses, which also include multicast addresses.
 * <p>
 * Please consult the documentation for more information on datagram sockets.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class DatagramSocket implements ReadStream<DatagramPacket>,  Measured {

  final io.vertx.core.datagram.DatagramSocket delegate;

  public DatagramSocket(io.vertx.core.datagram.DatagramSocket delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<DatagramPacket> observable;

  public synchronized rx.Observable<DatagramPacket> toObservable() {
    if (observable == null) {
      java.util.function.Function<io.vertx.core.datagram.DatagramPacket, DatagramPacket> conv = DatagramPacket::newInstance;
      io.vertx.lang.rxjava.ReadStreamAdapter<io.vertx.core.datagram.DatagramPacket, DatagramPacket> adapter = new io.vertx.lang.rxjava.ReadStreamAdapter<>(this, conv);
      observable = rx.Observable.create(adapter);
    }
    return observable;
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
   * Write the given {@link io.vertx.core.buffer.Buffer} to the {@link io.vertx.core.net.SocketAddress}.
   * The {@link io.vertx.core.Handler} will be notified once the write completes.
   *
   * @param packet  the {@link io.vertx.core.buffer.Buffer} to write
   * @param port  the host port of the remote peer
   * @param host  the host address of the remote peer
   * @param handler  the {@link io.vertx.core.Handler} to notify once the write completes.
   * @return a reference to this, so the API can be used fluently
   */
  public DatagramSocket send(Buffer packet, int port, String host, Handler<AsyncResult<DatagramSocket>> handler) {
    this.delegate.send((io.vertx.core.buffer.Buffer) packet.getDelegate(), port, host, new Handler<AsyncResult<io.vertx.core.datagram.DatagramSocket>>() {
      public void handle(AsyncResult<io.vertx.core.datagram.DatagramSocket> event) {
        AsyncResult<DatagramSocket> f;
        if (event.succeeded()) {
          f = InternalHelper.<DatagramSocket>result(new DatagramSocket(event.result()));
        } else {
          f = InternalHelper.<DatagramSocket>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<DatagramSocket> sendObservable(Buffer packet, int port, String host) {
    io.vertx.rx.java.ObservableFuture<DatagramSocket> handler = io.vertx.rx.java.RxHelper.observableFuture();
    send(packet, port, host, handler.asHandler());
    return handler;
  }

  /**
   * Returns a {@link io.vertx.core.datagram.PacketWritestream} able to send {@link Buffer} to the
   * {@link io.vertx.core.net.SocketAddress}.
   *
   * @param port the port of the remote peer
   * @param host the host address of the remote peer
   * @return the write stream for sending packets
   */
  public PacketWritestream sender(int port, String host) {
    PacketWritestream ret= PacketWritestream.newInstance(this.delegate.sender(port, host));
    return ret;
  }

  /**
   * Write the given {@link String} to the {@link io.vertx.core.net.SocketAddress} using UTF8 encoding.
   * The {@link Handler} will be notified once the write completes.
   *
   * @param str   the {@link String} to write
   * @param port  the host port of the remote peer
   * @param host  the host address of the remote peer
   * @param handler  the {@link io.vertx.core.Handler} to notify once the write completes.
   * @return a reference to this, so the API can be used fluently
   */
  public DatagramSocket send(String str, int port, String host, Handler<AsyncResult<DatagramSocket>> handler) {
    this.delegate.send(str, port, host, new Handler<AsyncResult<io.vertx.core.datagram.DatagramSocket>>() {
      public void handle(AsyncResult<io.vertx.core.datagram.DatagramSocket> event) {
        AsyncResult<DatagramSocket> f;
        if (event.succeeded()) {
          f = InternalHelper.<DatagramSocket>result(new DatagramSocket(event.result()));
        } else {
          f = InternalHelper.<DatagramSocket>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<DatagramSocket> sendObservable(String str, int port, String host) {
    io.vertx.rx.java.ObservableFuture<DatagramSocket> handler = io.vertx.rx.java.RxHelper.observableFuture();
    send(str, port, host, handler.asHandler());
    return handler;
  }

  /**
   * Write the given {@link String} to the {@link io.vertx.core.net.SocketAddress} using the given encoding.
   * The {@link Handler} will be notified once the write completes.
   *
   * @param str  the {@link String} to write
   * @param enc  the charset used for encoding
   * @param port  the host port of the remote peer
   * @param host  the host address of the remote peer
   * @param handler  the {@link io.vertx.core.Handler} to notify once the write completes.
   * @return a reference to this, so the API can be used fluently
   */
  public DatagramSocket send(String str, String enc, int port, String host, Handler<AsyncResult<DatagramSocket>> handler) {
    this.delegate.send(str, enc, port, host, new Handler<AsyncResult<io.vertx.core.datagram.DatagramSocket>>() {
      public void handle(AsyncResult<io.vertx.core.datagram.DatagramSocket> event) {
        AsyncResult<DatagramSocket> f;
        if (event.succeeded()) {
          f = InternalHelper.<DatagramSocket>result(new DatagramSocket(event.result()));
        } else {
          f = InternalHelper.<DatagramSocket>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<DatagramSocket> sendObservable(String str, String enc, int port, String host) {
    io.vertx.rx.java.ObservableFuture<DatagramSocket> handler = io.vertx.rx.java.RxHelper.observableFuture();
    send(str, enc, port, host, handler.asHandler());
    return handler;
  }

  /**
   * Closes the {@link io.vertx.core.datagram.DatagramSocket} implementation asynchronous
   * and notifies the handler once done.
   *
   * @param handler  the handler to notify once complete
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
   * Closes the {@link io.vertx.core.datagram.DatagramSocket}. The close itself is asynchronous.
   */
  public void close() {
    this.delegate.close();
  }

  /**
   * Return the {@link io.vertx.core.net.SocketAddress} to which
   * this {@link io.vertx.core.datagram.DatagramSocket} is bound.
   *
   * @return the socket address
   */
  public SocketAddress localAddress() {
    if (cached_0 != null) {
      return cached_0;
    }
    SocketAddress ret= SocketAddress.newInstance(this.delegate.localAddress());
    cached_0 = ret;
    return ret;
  }

  /**
   * Joins a multicast group and listens for packets send to it.
   * The {@link Handler} is notified once the operation completes.
   *
   * @param multicastAddress  the address of the multicast group to join
   * @param  handler  then handler to notify once the operation completes
   * @return a reference to this, so the API can be used fluently
   */
  public DatagramSocket listenMulticastGroup(String multicastAddress, Handler<AsyncResult<DatagramSocket>> handler) {
    this.delegate.listenMulticastGroup(multicastAddress, new Handler<AsyncResult<io.vertx.core.datagram.DatagramSocket>>() {
      public void handle(AsyncResult<io.vertx.core.datagram.DatagramSocket> event) {
        AsyncResult<DatagramSocket> f;
        if (event.succeeded()) {
          f = InternalHelper.<DatagramSocket>result(new DatagramSocket(event.result()));
        } else {
          f = InternalHelper.<DatagramSocket>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<DatagramSocket> listenMulticastGroupObservable(String multicastAddress) {
    io.vertx.rx.java.ObservableFuture<DatagramSocket> handler = io.vertx.rx.java.RxHelper.observableFuture();
    listenMulticastGroup(multicastAddress, handler.asHandler());
    return handler;
  }

  /**
   * Joins a multicast group and listens for packets send to it on the given network interface.
   * The {@link Handler} is notified once the operation completes.
   *
   * @param  multicastAddress  the address of the multicast group to join
   * @param  networkInterface  the network interface on which to listen for packets.
   * @param  source  the address of the source for which we will listen for multicast packets
   * @param  handler  then handler to notify once the operation completes
   * @return a reference to this, so the API can be used fluently
   */
  public DatagramSocket listenMulticastGroup(String multicastAddress, String networkInterface, String source, Handler<AsyncResult<DatagramSocket>> handler) {
    this.delegate.listenMulticastGroup(multicastAddress, networkInterface, source, new Handler<AsyncResult<io.vertx.core.datagram.DatagramSocket>>() {
      public void handle(AsyncResult<io.vertx.core.datagram.DatagramSocket> event) {
        AsyncResult<DatagramSocket> f;
        if (event.succeeded()) {
          f = InternalHelper.<DatagramSocket>result(new DatagramSocket(event.result()));
        } else {
          f = InternalHelper.<DatagramSocket>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<DatagramSocket> listenMulticastGroupObservable(String multicastAddress, String networkInterface, String source) {
    io.vertx.rx.java.ObservableFuture<DatagramSocket> handler = io.vertx.rx.java.RxHelper.observableFuture();
    listenMulticastGroup(multicastAddress, networkInterface, source, handler.asHandler());
    return handler;
  }

  /**
   * Leaves a multicast group and stops listening for packets send to it.
   * The {@link Handler} is notified once the operation completes.
   *
   * @param multicastAddress  the address of the multicast group to leave
   * @param handler  then handler to notify once the operation completes
   * @return a reference to this, so the API can be used fluently
   */
  public DatagramSocket unlistenMulticastGroup(String multicastAddress, Handler<AsyncResult<DatagramSocket>> handler) {
    this.delegate.unlistenMulticastGroup(multicastAddress, new Handler<AsyncResult<io.vertx.core.datagram.DatagramSocket>>() {
      public void handle(AsyncResult<io.vertx.core.datagram.DatagramSocket> event) {
        AsyncResult<DatagramSocket> f;
        if (event.succeeded()) {
          f = InternalHelper.<DatagramSocket>result(new DatagramSocket(event.result()));
        } else {
          f = InternalHelper.<DatagramSocket>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<DatagramSocket> unlistenMulticastGroupObservable(String multicastAddress) {
    io.vertx.rx.java.ObservableFuture<DatagramSocket> handler = io.vertx.rx.java.RxHelper.observableFuture();
    unlistenMulticastGroup(multicastAddress, handler.asHandler());
    return handler;
  }

  /**
   * Leaves a multicast group and stops listening for packets send to it on the given network interface.
   * The {@link Handler} is notified once the operation completes.
   *
   * @param  multicastAddress  the address of the multicast group to join
   * @param  networkInterface  the network interface on which to listen for packets.
   * @param  source  the address of the source for which we will listen for multicast packets
   * @param  handler the handler to notify once the operation completes
   * @return  a reference to this, so the API can be used fluently
   */
  public DatagramSocket unlistenMulticastGroup(String multicastAddress, String networkInterface, String source, Handler<AsyncResult<DatagramSocket>> handler) {
    this.delegate.unlistenMulticastGroup(multicastAddress, networkInterface, source, new Handler<AsyncResult<io.vertx.core.datagram.DatagramSocket>>() {
      public void handle(AsyncResult<io.vertx.core.datagram.DatagramSocket> event) {
        AsyncResult<DatagramSocket> f;
        if (event.succeeded()) {
          f = InternalHelper.<DatagramSocket>result(new DatagramSocket(event.result()));
        } else {
          f = InternalHelper.<DatagramSocket>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<DatagramSocket> unlistenMulticastGroupObservable(String multicastAddress, String networkInterface, String source) {
    io.vertx.rx.java.ObservableFuture<DatagramSocket> handler = io.vertx.rx.java.RxHelper.observableFuture();
    unlistenMulticastGroup(multicastAddress, networkInterface, source, handler.asHandler());
    return handler;
  }

  /**
   * Block the given address for the given multicast address and notifies the {@link Handler} once
   * the operation completes.
   *
   * @param multicastAddress  the address for which you want to block the source address
   * @param sourceToBlock  the source address which should be blocked. You will not receive an multicast packets
   *                       for it anymore.
   * @param handler  the handler to notify once the operation completes
   * @return  a reference to this, so the API can be used fluently
   */
  public DatagramSocket blockMulticastGroup(String multicastAddress, String sourceToBlock, Handler<AsyncResult<DatagramSocket>> handler) {
    this.delegate.blockMulticastGroup(multicastAddress, sourceToBlock, new Handler<AsyncResult<io.vertx.core.datagram.DatagramSocket>>() {
      public void handle(AsyncResult<io.vertx.core.datagram.DatagramSocket> event) {
        AsyncResult<DatagramSocket> f;
        if (event.succeeded()) {
          f = InternalHelper.<DatagramSocket>result(new DatagramSocket(event.result()));
        } else {
          f = InternalHelper.<DatagramSocket>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<DatagramSocket> blockMulticastGroupObservable(String multicastAddress, String sourceToBlock) {
    io.vertx.rx.java.ObservableFuture<DatagramSocket> handler = io.vertx.rx.java.RxHelper.observableFuture();
    blockMulticastGroup(multicastAddress, sourceToBlock, handler.asHandler());
    return handler;
  }

  /**
   * Block the given address for the given multicast address on the given network interface and notifies
   * the {@link Handler} once the operation completes.
   *
   * @param  multicastAddress  the address for which you want to block the source address
   * @param  networkInterface  the network interface on which the blocking should occur.
   * @param  sourceToBlock  the source address which should be blocked. You will not receive an multicast packets
   *                        for it anymore.
   * @param  handler  the handler to notify once the operation completes
   * @return  a reference to this, so the API can be used fluently
   */
  public DatagramSocket blockMulticastGroup(String multicastAddress, String networkInterface, String sourceToBlock, Handler<AsyncResult<DatagramSocket>> handler) {
    this.delegate.blockMulticastGroup(multicastAddress, networkInterface, sourceToBlock, new Handler<AsyncResult<io.vertx.core.datagram.DatagramSocket>>() {
      public void handle(AsyncResult<io.vertx.core.datagram.DatagramSocket> event) {
        AsyncResult<DatagramSocket> f;
        if (event.succeeded()) {
          f = InternalHelper.<DatagramSocket>result(new DatagramSocket(event.result()));
        } else {
          f = InternalHelper.<DatagramSocket>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<DatagramSocket> blockMulticastGroupObservable(String multicastAddress, String networkInterface, String sourceToBlock) {
    io.vertx.rx.java.ObservableFuture<DatagramSocket> handler = io.vertx.rx.java.RxHelper.observableFuture();
    blockMulticastGroup(multicastAddress, networkInterface, sourceToBlock, handler.asHandler());
    return handler;
  }

  /**
   * Start listening on the given port and host. The handler will be called when the socket is listening.
   *
   * @param port  the port to listen on
   * @param host  the host to listen on
   * @param handler  the handler will be called when listening
   * @return  a reference to this, so the API can be used fluently
   */
  public DatagramSocket listen(int port, String host, Handler<AsyncResult<DatagramSocket>> handler) {
    this.delegate.listen(port, host, new Handler<AsyncResult<io.vertx.core.datagram.DatagramSocket>>() {
      public void handle(AsyncResult<io.vertx.core.datagram.DatagramSocket> event) {
        AsyncResult<DatagramSocket> f;
        if (event.succeeded()) {
          f = InternalHelper.<DatagramSocket>result(new DatagramSocket(event.result()));
        } else {
          f = InternalHelper.<DatagramSocket>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<DatagramSocket> listenObservable(int port, String host) {
    io.vertx.rx.java.ObservableFuture<DatagramSocket> handler = io.vertx.rx.java.RxHelper.observableFuture();
    listen(port, host, handler.asHandler());
    return handler;
  }

  public DatagramSocket pause() {
    this.delegate.pause();
    return this;
  }

  public DatagramSocket resume() {
    this.delegate.resume();
    return this;
  }

  public DatagramSocket endHandler(Handler<Void> endHandler) {
    this.delegate.endHandler(endHandler);
    return this;
  }

  public DatagramSocket handler(Handler<DatagramPacket> handler) {
    this.delegate.handler(new Handler<io.vertx.core.datagram.DatagramPacket>() {
      public void handle(io.vertx.core.datagram.DatagramPacket event) {
        handler.handle(new DatagramPacket(event));
      }
    });
    return this;
  }

  public DatagramSocket exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  private SocketAddress cached_0;

  public static DatagramSocket newInstance(io.vertx.core.datagram.DatagramSocket arg) {
    return new DatagramSocket(arg);
  }
}
