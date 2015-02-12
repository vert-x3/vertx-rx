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

package io.vertx.rxjava.core.dns;

import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.List;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Provides a way to asynchronously lookup information from DNS servers.
 * <p>
 * Please consult the documentation for more information on DNS clients.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class DnsClient {

  final io.vertx.core.dns.DnsClient delegate;

  public DnsClient(io.vertx.core.dns.DnsClient delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Try to lookup the A (ipv4) or AAAA (ipv6) record for the given name. The first found will be used.
   *
   * @param name  the name to resolve
   * @param handler  the {@link io.vertx.core.Handler} to notify with the {@link io.vertx.core.AsyncResult}.
   *                 The handler will get notified with the resolved address if a record was found. If non was found it
   *                 will get notifed with {@code null}. If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently
   */
  public DnsClient lookup(String name, Handler<AsyncResult<String>> handler) {
    this.delegate.lookup(name, handler);
    return this;
  }

  public Observable<String> lookupObservable(String name) {
    io.vertx.rx.java.ObservableFuture<String> handler = io.vertx.rx.java.RxHelper.observableFuture();
    lookup(name, handler.toHandler());
    return handler;
  }

  /**
   * Try to lookup the A (ipv4) record for the given name. The first found will be used.
   *
   * @param name  the name to resolve
   * @param handler  the {@link Handler} to notify with the {@link io.vertx.core.AsyncResult}.
   *                 The handler will get notified with the resolved {@link java.net.Inet4Address} if a record was found.
   *                 If non was found it will get notifed with {@code null}. If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently
   */
  public DnsClient lookup4(String name, Handler<AsyncResult<String>> handler) {
    this.delegate.lookup4(name, handler);
    return this;
  }

  public Observable<String> lookup4Observable(String name) {
    io.vertx.rx.java.ObservableFuture<String> handler = io.vertx.rx.java.RxHelper.observableFuture();
    lookup4(name, handler.toHandler());
    return handler;
  }

  /**
   * Try to lookup the AAAA (ipv6) record for the given name. The first found will be used.
   *
   * @param name  the name to resolve
   * @param handler  the {@link Handler} to notify with the {@link AsyncResult}. The handler will get
   *                 notified with the resolved {@link java.net.Inet6Address} if a record was found. If non was found
   *                 it will get notifed with {@code null}. If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently
   */
  public DnsClient lookup6(String name, Handler<AsyncResult<String>> handler) {
    this.delegate.lookup6(name, handler);
    return this;
  }

  public Observable<String> lookup6Observable(String name) {
    io.vertx.rx.java.ObservableFuture<String> handler = io.vertx.rx.java.RxHelper.observableFuture();
    lookup6(name, handler.toHandler());
    return handler;
  }

  /**
   * Try to resolve all A (ipv4) records for the given name.
   *
   * @param name  the name to resolve
   * @param handler  the {@link io.vertx.core.Handler} to notify with the {@link io.vertx.core.AsyncResult}.
   *                 The handler will get notified with a {@link java.util.List} that contains all the resolved
   *                 {@link java.net.Inet4Address}es. If none was found an empty {@link java.util.List} will be used.
   *                 If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently
   */
  public DnsClient resolveA(String name, Handler<AsyncResult<List<String>>> handler) {
    this.delegate.resolveA(name, handler);
    return this;
  }

  public Observable<List<String>> resolveAObservable(String name) {
    io.vertx.rx.java.ObservableFuture<List<String>> handler = io.vertx.rx.java.RxHelper.observableFuture();
    resolveA(name, handler.toHandler());
    return handler;
  }

  /**
   * Try to resolve all AAAA (ipv6) records for the given name.
   *
   * @param name  the name to resolve
   * @param handler the {@link io.vertx.core.Handler} to notify with the {@link io.vertx.core.AsyncResult}.
   *                The handler will get notified with a {@link java.util.List} that contains all the resolved
   *                {@link java.net.Inet6Address}es. If none was found an empty {@link java.util.List} will be used.
   *                If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently
   */
  public DnsClient resolveAAAA(String name, Handler<AsyncResult<List<String>>> handler) {
    this.delegate.resolveAAAA(name, handler);
    return this;
  }

  public Observable<List<String>> resolveAAAAObservable(String name) {
    io.vertx.rx.java.ObservableFuture<List<String>> handler = io.vertx.rx.java.RxHelper.observableFuture();
    resolveAAAA(name, handler.toHandler());
    return handler;
  }

  /**
   * Try to resolve the CNAME record for the given name.
   *
   * @param name  the name to resolve the CNAME for
   * @param handler  the {@link Handler} to notify with the {@link AsyncResult}. The handler will get
   *                 notified with the resolved {@link String} if a record was found. If none was found it will
   *                 get notified with {@code null}. If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently.
   */
  public DnsClient resolveCNAME(String name, Handler<AsyncResult<List<String>>> handler) {
    this.delegate.resolveCNAME(name, handler);
    return this;
  }

  public Observable<List<String>> resolveCNAMEObservable(String name) {
    io.vertx.rx.java.ObservableFuture<List<String>> handler = io.vertx.rx.java.RxHelper.observableFuture();
    resolveCNAME(name, handler.toHandler());
    return handler;
  }

  /**
   * Try to resolve the MX records for the given name.
   *
   * @param name  the name for which the MX records should be resolved
   * @param handler  the {@link io.vertx.core.Handler} to notify with the {@link io.vertx.core.AsyncResult}.
   *                 The handler will get notified with a List that contains all resolved {@link MxRecord}s, sorted by
   *                 their {@link MxRecord#priority()}. If non was found it will get notified with an empty
   *                 {@link java.util.List}.  If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently.
   */
  public DnsClient resolveMX(String name, Handler<AsyncResult<List<MxRecord>>> handler) {
    this.delegate.resolveMX(name, new Handler<AsyncResult<List<io.vertx.core.dns.MxRecord>>>() {
      public void handle(AsyncResult<List<io.vertx.core.dns.MxRecord>> event) {
        AsyncResult<List<MxRecord>> f;
        if (event.succeeded()) {
          f = InternalHelper.<List<MxRecord>>result(event.result().stream().map(MxRecord::newInstance).collect(java.util.stream.Collectors.toList()));
        } else {
          f = InternalHelper.<List<MxRecord>>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<List<MxRecord>> resolveMXObservable(String name) {
    io.vertx.rx.java.ObservableFuture<List<MxRecord>> handler = io.vertx.rx.java.RxHelper.observableFuture();
    resolveMX(name, handler.toHandler());
    return handler;
  }

  /**
   * Try to resolve the TXT records for the given name.
   *
   * @param name  the name for which the TXT records should be resolved
   * @param handler  the {@link Handler} to notify with the {@link AsyncResult}. The handler will get
   *                 notified with a List that contains all resolved {@link String}s. If none was found it will
   *                 get notified with an empty {@link java.util.List}. If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently.
   */
  public DnsClient resolveTXT(String name, Handler<AsyncResult<List<String>>> handler) {
    this.delegate.resolveTXT(name, handler);
    return this;
  }

  public Observable<List<String>> resolveTXTObservable(String name) {
    io.vertx.rx.java.ObservableFuture<List<String>> handler = io.vertx.rx.java.RxHelper.observableFuture();
    resolveTXT(name, handler.toHandler());
    return handler;
  }

  /**
   * Try to resolve the PTR record for the given name.
   *
   * @param name  the name to resolve the PTR for
   * @param handler  the {@link Handler} to notify with the {@link AsyncResult}. The handler will get
   *                 notified with the resolved {@link String} if a record was found. If none was found it will
   *                 get notified with {@code null}. If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently.
   */
  public DnsClient resolvePTR(String name, Handler<AsyncResult<String>> handler) {
    this.delegate.resolvePTR(name, handler);
    return this;
  }

  public Observable<String> resolvePTRObservable(String name) {
    io.vertx.rx.java.ObservableFuture<String> handler = io.vertx.rx.java.RxHelper.observableFuture();
    resolvePTR(name, handler.toHandler());
    return handler;
  }

  /**
   * Try to resolve the NS records for the given name.
   *
   * @param name  the name for which the NS records should be resolved
   * @param handler  the {@link Handler} to notify with the {@link AsyncResult}. The handler will get
   *                 notified with a List that contains all resolved {@link String}s. If none was found it will
   *                 get notified with an empty {@link java.util.List}.  If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently.
   */
  public DnsClient resolveNS(String name, Handler<AsyncResult<List<String>>> handler) {
    this.delegate.resolveNS(name, handler);
    return this;
  }

  public Observable<List<String>> resolveNSObservable(String name) {
    io.vertx.rx.java.ObservableFuture<List<String>> handler = io.vertx.rx.java.RxHelper.observableFuture();
    resolveNS(name, handler.toHandler());
    return handler;
  }

  /**
   * Try to resolve the SRV records for the given name.
   *
   * @param name  the name for which the SRV records should be resolved
   * @param handler  the {@link Handler} to notify with the {@link AsyncResult}. The handler will get
   *                 notified with a List that contains all resolved {@link SrvRecord}s. If none was found it will
   *                 get notified with an empty {@link java.util.List}. If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently.
   */
  public DnsClient resolveSRV(String name, Handler<AsyncResult<List<SrvRecord>>> handler) {
    this.delegate.resolveSRV(name, new Handler<AsyncResult<List<io.vertx.core.dns.SrvRecord>>>() {
      public void handle(AsyncResult<List<io.vertx.core.dns.SrvRecord>> event) {
        AsyncResult<List<SrvRecord>> f;
        if (event.succeeded()) {
          f = InternalHelper.<List<SrvRecord>>result(event.result().stream().map(SrvRecord::newInstance).collect(java.util.stream.Collectors.toList()));
        } else {
          f = InternalHelper.<List<SrvRecord>>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<List<SrvRecord>> resolveSRVObservable(String name) {
    io.vertx.rx.java.ObservableFuture<List<SrvRecord>> handler = io.vertx.rx.java.RxHelper.observableFuture();
    resolveSRV(name, handler.toHandler());
    return handler;
  }

  /**
   * Try to do a reverse lookup of an IP address. This is basically the same as doing trying to resolve a PTR record
   * but allows you to just pass in the IP address and not a valid ptr query string.
   *
   * @param ipaddress  the IP address to resolve the PTR for
   * @param handler  the {@link Handler} to notify with the {@link AsyncResult}. The handler will get
   *                 notified with the resolved {@link String} if a record was found. If none was found it will
   *                 get notified with {@code null}. If an error accours it will get failed.
   * @return a reference to this, so the API can be used fluently.
   */
  public DnsClient reverseLookup(String ipaddress, Handler<AsyncResult<String>> handler) {
    this.delegate.reverseLookup(ipaddress, handler);
    return this;
  }

  public Observable<String> reverseLookupObservable(String ipaddress) {
    io.vertx.rx.java.ObservableFuture<String> handler = io.vertx.rx.java.RxHelper.observableFuture();
    reverseLookup(ipaddress, handler.toHandler());
    return handler;
  }


  public static DnsClient newInstance(io.vertx.core.dns.DnsClient arg) {
    return new DnsClient(arg);
  }
}
