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
import java.util.List;
import io.vertx.core.json.JsonObject;
import io.vertx.core.Handler;

/**
 * The execution context of a {@link io.vertx.core.Handler} execution.
 * <p>
 * When Vert.x provides an event to a handler or calls the start or stop methods of a {@link io.vertx.core.Verticle},
 * the execution is associated with a {@code Context}.
 * <p>
 * Usually a context is an *event-loop context* and is tied to a specific event loop thread. So executions for that
 * context always occur on that exact same event loop thread.
 * <p>
 * In the case of worker verticles and running inline blocking code a worker context will be associated with the execution
 * which will use a thread from the worker thread pool.
 * <p>
 * When a handler is set by a thread associated with a specific context, the Vert.x will guarantee that when that handler
 * is executed, that execution will be associated with the same context.
 * <p>
 * If a handler is set by a thread not associated with a context (i.e. a non Vert.x thread). Then a new context will
 * be created for that handler.
 * <p>
 * In other words, a context is propagated.
 * <p>
 * This means that when a verticle is deployed, any handlers it sets will be associated with the same context - the context
 * of the verticle.
 * <p>
 * This means (in the case of a standard verticle) that the verticle code will always be executed with the exact same
 * thread, so you don't have to worry about multi-threaded acccess to the verticle state and you can code your application
 * as single threaded.
 * <p>
 * This class also allows arbitrary data to be {@link #put} and {@link #get} on the context so it can be shared easily
 * amongst different handlers of, for example, a verticle instance.
 * <p>
 * This class also provides {@link #runOnContext} which allows an action to be executed asynchronously using the same context.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class Context {

  final io.vertx.core.Context delegate;

  public Context(io.vertx.core.Context delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Run the specified action asynchronously on the same context, some time after the current execution has completed.
   *
   * @param action  the action to run
   */
  public void runOnContext(Handler<Void> action) {
    this.delegate.runOnContext(action);
  }

  /**
   * If the context is associated with a Verticle deployment, this returns the deployment ID of that deployment.
   *
   * @return the deployment ID of the deployment or null if not a Verticle deployment
   */
  public String deploymentID() {
    String ret = this.delegate.deploymentID();
    return ret;
  }

  /**
   * If the context is associated with a Verticle deployment, this returns the configuration that was specified when
   * the verticle was deployed.
   *
   * @return the configuration of the deployment or null if not a Verticle deployment
   */
  public JsonObject config() {
    JsonObject ret = this.delegate.config();
    return ret;
  }

  /**
   * The process args
   */
  public List<String> processArgs() {
    List<String> ret = this.delegate.processArgs();
;
    return ret;
  }

  /**
   * @return true if this is an event loop context, false otherwise
   */
  public boolean isEventLoopContext() {
    boolean ret = this.delegate.isEventLoopContext();
    return ret;
  }

  /**
   * @return true if this is an worker context, false otherwise
   */
  public boolean isWorker() {
    boolean ret = this.delegate.isWorker();
    return ret;
  }

  /**
   * @return true if this is a multi-threaded worker context, false otherwise
   */
  public boolean isMultiThreaded() {
    boolean ret = this.delegate.isMultiThreaded();
    return ret;
  }

  /**
   * Get some data from the context.
   *
   * @param key  the key of the data
   * @param <T>  the type of the data
   * @return the data
   */
  public <T> T get(String key) {
    T ret = (T) this.delegate.get(key);
    return ret;
  }

  /**
   * Put some data in the context.
   * <p>
   * This can be used to share data between different handlers that share a context
   *
   * @param key  the key of the data
   * @param value  the data
   */
  public void put(String key, Object value) {
    this.delegate.put(key, value);
  }

  /**
   * Remove some data from the context.
   *
   * @param key  the key to remove
   * @return true if removed successfully, false otherwise
   */
  public boolean remove(String key) {
    boolean ret = this.delegate.remove(key);
    return ret;
  }


  public static Context newInstance(io.vertx.core.Context arg) {
    return new Context(arg);
  }
}
