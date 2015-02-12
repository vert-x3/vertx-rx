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

package io.vertx.rxjava.core;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.datagram.DatagramSocket;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.core.shareddata.SharedData;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AsyncResult;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.rxjava.core.net.NetClient;
import io.vertx.core.VertxOptions;
import java.util.Set;
import io.vertx.core.net.NetClientOptions;
import io.vertx.rxjava.core.dns.DnsClient;
import io.vertx.core.net.NetServerOptions;
import io.vertx.rxjava.core.metrics.Measured;
import io.vertx.rxjava.core.net.NetServer;
import io.vertx.core.DeploymentOptions;
import io.vertx.rxjava.core.file.FileSystem;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.http.HttpClient;

/**
 * The entry point into the Vert.x Core API.
 * <p>
 * You use an instance of this class for functionality including:
 * <ul>
 *   <li>Creating TCP clients and servers</li>
 *   <li>Creating HTTP clients and servers</li>
 *   <li>Creating DNS clients</li>
 *   <li>Creating Datagram sockets</li>
 *   <li>Setting and cancelling periodic and one-shot timers</li>
 *   <li>Getting a reference to the event bus API</li>
 *   <li>Getting a reference to the file system API</li>
 *   <li>Getting a reference to the shared data API</li>
 *   <li>Deploying and undeploying verticles</li>
 * </ul>
 * <p>
 * Most functionality in Vert.x core is fairly low level.
 * <p>
 * To create an instance of this class you can use the static factory methods: {@link #vertx},
 * {@link #vertx(io.vertx.core.VertxOptions)} and {@link #clusteredVertx(io.vertx.core.VertxOptions, Handler)}.
 * <p>
 * Please see the user manual for more detailed usage information.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class Vertx implements Measured {

  final io.vertx.core.Vertx delegate;

  public Vertx(io.vertx.core.Vertx delegate) {
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
   * Creates a non clustered instance using default options.
   *
   * @return the instance
   */
  public static Vertx vertx() {
    Vertx ret= Vertx.newInstance(io.vertx.core.Vertx.vertx());
    return ret;
  }

  /**
   * Creates a non clustered instance using the specified options
   *
   * @param options  the options to use
   * @return the instance
   */
  public static Vertx vertx(VertxOptions options) {
    Vertx ret= Vertx.newInstance(io.vertx.core.Vertx.vertx(options));
    return ret;
  }

  /**
   * Creates a clustered instance using the specified options.
   * <p>
   * The instance is created asynchronously and the resultHandler is called with the result when it is ready.
   *
   * @param options  the options to use
   * @param resultHandler  the result handler that will receive the result
   */
  public static void clusteredVertx(VertxOptions options, Handler<AsyncResult<Vertx>> resultHandler) {
    io.vertx.core.Vertx.clusteredVertx(options, new Handler<AsyncResult<io.vertx.core.Vertx>>() {
      public void handle(AsyncResult<io.vertx.core.Vertx> event) {
        AsyncResult<Vertx> f;
        if (event.succeeded()) {
          f = InternalHelper.<Vertx>result(new Vertx(event.result()));
        } else {
          f = InternalHelper.<Vertx>failure(event.cause());
        }
        resultHandler.handle(f);
      }
    });
  }

  public static Observable<Vertx> clusteredVertxObservable(VertxOptions options) {
    io.vertx.rx.java.ObservableFuture<Vertx> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    clusteredVertx(options, resultHandler.toHandler());
    return resultHandler;
  }

  /**
   * Gets the current context
   *
   * @return The current context or null if no current context
   */
  public static Context currentContext() {
    Context ret= Context.newInstance(io.vertx.core.Vertx.currentContext());
    return ret;
  }

  /**
   * Gets the current context, or creates one if there isn't one
   *
   * @return The current context (created if didn't exist)
   */
  public Context getOrCreateContext() {
    Context ret= Context.newInstance(this.delegate.getOrCreateContext());
    return ret;
  }

  /**
   * Create a TCP/SSL server using the specified options
   *
   * @param options  the options to use
   * @return the server
   */
  public NetServer createNetServer(NetServerOptions options) {
    NetServer ret= NetServer.newInstance(this.delegate.createNetServer(options));
    return ret;
  }

  /**
   * Create a TCP/SSL server using default options
   *
   * @return the server
   */
  public NetServer createNetServer() {
    NetServer ret= NetServer.newInstance(this.delegate.createNetServer());
    return ret;
  }

  /**
   * Create a TCP/SSL client using the specified options
   *
   * @param options  the options to use
   * @return the client
   */
  public NetClient createNetClient(NetClientOptions options) {
    NetClient ret= NetClient.newInstance(this.delegate.createNetClient(options));
    return ret;
  }

  /**
   * Create a TCP/SSL client using default options
   *
   * @return the client
   */
  public NetClient createNetClient() {
    NetClient ret= NetClient.newInstance(this.delegate.createNetClient());
    return ret;
  }

  /**
   * Create an HTTP/HTTPS server using the specified options
   *
   * @param options  the options to use
   * @return the server
   */
  public HttpServer createHttpServer(HttpServerOptions options) {
    HttpServer ret= HttpServer.newInstance(this.delegate.createHttpServer(options));
    return ret;
  }

  /**
   * Create an HTTP/HTTPS server using default options
   *
   * @return the server
   */
  public HttpServer createHttpServer() {
    HttpServer ret= HttpServer.newInstance(this.delegate.createHttpServer());
    return ret;
  }

  /**
   * Create a HTTP/HTTPS client using the specified options
   *
   * @param options  the options to use
   * @return the client
   */
  public HttpClient createHttpClient(HttpClientOptions options) {
    HttpClient ret= HttpClient.newInstance(this.delegate.createHttpClient(options));
    return ret;
  }

  /**
   * Create a HTTP/HTTPS client using default options
   *
   * @return the client
   */
  public HttpClient createHttpClient() {
    HttpClient ret= HttpClient.newInstance(this.delegate.createHttpClient());
    return ret;
  }

  /**
   * Create a datagram socket using the specified options
   *
   * @param options  the options to use
   * @return the socket
   */
  public DatagramSocket createDatagramSocket(DatagramSocketOptions options) {
    DatagramSocket ret= DatagramSocket.newInstance(this.delegate.createDatagramSocket(options));
    return ret;
  }

  /**
   * Create a datagram socket using default options
   *
   * @return the socket
   */
  public DatagramSocket createDatagramSocket() {
    DatagramSocket ret= DatagramSocket.newInstance(this.delegate.createDatagramSocket());
    return ret;
  }

  /**
   * Get the filesystem object. There is a single instance of FileSystem per Vertx instance.
   *
   * @return the filesystem object
   */
  public FileSystem fileSystem() {
    if (cached_0 != null) {
      return cached_0;
    }
    FileSystem ret= FileSystem.newInstance(this.delegate.fileSystem());
    cached_0 = ret;
    return ret;
  }

  /**
   * Get the event bus object. There is a single instance of EventBus per Vertx instance.
   *
   * @return the event bus object
   */
  public EventBus eventBus() {
    if (cached_1 != null) {
      return cached_1;
    }
    EventBus ret= EventBus.newInstance(this.delegate.eventBus());
    cached_1 = ret;
    return ret;
  }

  /**
   * Create a DNS client to connect to a DNS server at the specified host and port
   *
   * @param port  the port
   * @param host  the host
   * @return the DNS client
   */
  public DnsClient createDnsClient(int port, String host) {
    DnsClient ret= DnsClient.newInstance(this.delegate.createDnsClient(port, host));
    return ret;
  }

  /**
   * Get the shared data object. There is a single instance of SharedData per Vertx instance.
   *
   * @return the shared data object
   */
  public SharedData sharedData() {
    if (cached_2 != null) {
      return cached_2;
    }
    SharedData ret= SharedData.newInstance(this.delegate.sharedData());
    cached_2 = ret;
    return ret;
  }

  /**
   * Set a one-shot timer to fire after {@code delay} milliseconds, at which point {@code handler} will be called with
   * the id of the timer.
   *
   * @param delay  the delay in milliseconds, after which the timer will fire
   * @param handler  the handler that will be called with the timer ID when the timer fires
   * @return the unique ID of the timer
   */
  public long setTimer(long delay, Handler<Long> handler) {
    long ret = this.delegate.setTimer(delay, handler);
    return ret;
  }

  /**
   * Returns a one-shot timer as a read stream. The timer will be fired after {@code delay} milliseconds after
   * the {@link ReadStream#handler} has been called.
   *
   * @param delay  the delay in milliseconds, after which the timer will fire
   * @return the timer stream
   */
  public TimeoutStream timerStream(long delay) {
    TimeoutStream ret= TimeoutStream.newInstance(this.delegate.timerStream(delay));
    return ret;
  }

  /**
   * Set a periodic timer to fire every {@code delay} milliseconds, at which point {@code handler} will be called with
   * the id of the timer.
   *
   *
   * @param delay  the delay in milliseconds, after which the timer will fire
   * @param handler  the handler that will be called with the timer ID when the timer fires
   * @return the unique ID of the timer
   */
  public long setPeriodic(long delay, Handler<Long> handler) {
    long ret = this.delegate.setPeriodic(delay, handler);
    return ret;
  }

  /**
   * Returns a periodic timer as a read stream. The timer will be fired every {@code delay} milliseconds after
   * the {@link ReadStream#handler} has been called.
   *
   * @param delay  the delay in milliseconds, after which the timer will fire
   * @return the periodic stream
   */
  public TimeoutStream periodicStream(long delay) {
    TimeoutStream ret= TimeoutStream.newInstance(this.delegate.periodicStream(delay));
    return ret;
  }

  /**
   * Cancels the timer with the specified {@code id}.
   *
   * @param id  The id of the timer to cancel
   * @return true if the timer was successfully cancelled, or false if the timer does not exist.
   */
  public boolean cancelTimer(long id) {
    boolean ret = this.delegate.cancelTimer(id);
    return ret;
  }

  /**
   * Puts the handler on the event queue for the current context so it will be run asynchronously ASAP after all
   * preceeding events have been handled.
   *
   * @param action - a handler representing the action to execute
   */
  public void runOnContext(Handler<Void> action) {
    this.delegate.runOnContext(action);
  }

  /**
   * Stop the the Vertx instance and release any resources held by it.
   * <p>
   * The instance cannot be used after it has been closed.
   * <p>
   * The actual close is asynchronous and may not complete until after the call has returned.
   */
  public void close() {
    this.delegate.close();
  }

  /**
   * Like {@link #close} but the completionHandler will be called when the close is complete
   *
   * @param completionHandler  The handler will be notified when the close is complete.
   */
  public void close(Handler<AsyncResult<Void>> completionHandler) {
    this.delegate.close(completionHandler);
  }

  public Observable<Void> closeObservable() {
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    close(completionHandler.toHandler());
    return completionHandler;
  }

  /**
   * Deploy a verticle instance given a name.
   * <p>
   * Given the name, Vert.x selects a {@link VerticleFactory} instance to use to instantiate the verticle.
   * <p>
   * For the rules on how factories are selected please consult the user manual.
   *
   * @param name  the name.
   */
  public void deployVerticle(String name) {
    this.delegate.deployVerticle(name);
  }

  /**
   * Like {@link #deployVerticle(String)} but the completionHandler will be notified when the deployment is complete.
   * <p>
   * If the deployment is successful the result will contain a String representing the unique deployment ID of the
   * deployment.
   * <p>
   * This deployment ID can subsequently be used to undeploy the verticle.
   *
   * @param name  The identifier
   * @param completionHandler  a handler which will be notified when the deployment is complete
   */
  public void deployVerticle(String name, Handler<AsyncResult<String>> completionHandler) {
    this.delegate.deployVerticle(name, completionHandler);
  }

  public Observable<String> deployVerticleObservable(String name) {
    io.vertx.rx.java.ObservableFuture<String> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    deployVerticle(name, completionHandler.toHandler());
    return completionHandler;
  }

  /**
   * Like {@link #deployVerticle(Verticle)} but {@link io.vertx.core.DeploymentOptions} are provided to configure the
   * deployment.
   *
   * @param name  the name
   * @param options  the deployment options.
   */
  public void deployVerticle(String name, DeploymentOptions options) {
    this.delegate.deployVerticle(name, options);
  }

  /**
   * Undeploy a verticle deployment.
   * <p>
   * The actual undeployment happens asynchronously and may not complete until after the method has returned.
   *
   * @param deploymentID  the deployment ID
   */
  public void undeploy(String deploymentID) {
    this.delegate.undeploy(deploymentID);
  }

  /**
   * Like {@link #undeploy(String) } but the completionHandler will be notified when the undeployment is complete.
   *
   * @param deploymentID  the deployment ID
   * @param completionHandler  a handler which will be notified when the undeployment is complete
   */
  public void undeploy(String deploymentID, Handler<AsyncResult<Void>> completionHandler) {
    this.delegate.undeploy(deploymentID, completionHandler);
  }

  public Observable<Void> undeployObservable(String deploymentID) {
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    undeploy(deploymentID, completionHandler.toHandler());
    return completionHandler;
  }

  /**
   * Return a Set of deployment IDs for the currently deployed deploymentIDs.
   *
   * @return Set of deployment IDs
   */
  public Set<String> deploymentIDs() {
    Set<String> ret = this.delegate.deploymentIDs();
;
    return ret;
  }

  /**
   * Safely execute some blocking code.
   * <p>
   * Executes the blocking code in the handler {@code blockingCodeHandler} using a thread from the worker pool.
   * <p>
   * When the code is complete the handler {@code resultHandler} will be called with the result on the original context
   * (e.g. on the original event loop of the caller).
   * <p>
   * A {@code Future} instance is passed into {@code blockingCodeHandler}. When the blocking code successfully completes,
   * the handler should call the {@link Future#complete} or {@link Future#complete(Object)} method, or the {@link Future#fail}
   * method if it failed.
   *
   * @param blockingCodeHandler  handler representing the blocking code to run
   * @param resultHandler  handler that will be called when the blocking code is complete
   * @param <T> the type of the result
   */
  public <T> void executeBlocking(Handler<Future<T>> blockingCodeHandler, Handler<AsyncResult<T>> resultHandler) {
    this.delegate.executeBlocking(new Handler<io.vertx.core.Future<T>>() {
      public void handle(io.vertx.core.Future<T> event) {
        blockingCodeHandler.handle(new Future<T>(event));
      }
    }, resultHandler);
  }

  public <T> Observable<T> executeBlockingObservable(Handler<Future<T>> blockingCodeHandler) {
    io.vertx.rx.java.ObservableFuture<T> resultHandler = io.vertx.rx.java.RxHelper.observableFuture();
    executeBlocking(blockingCodeHandler, resultHandler.toHandler());
    return resultHandler;
  }

  private FileSystem cached_0;
  private EventBus cached_1;
  private SharedData cached_2;

  public static Vertx newInstance(io.vertx.core.Vertx arg) {
    return new Vertx(arg);
  }
}
