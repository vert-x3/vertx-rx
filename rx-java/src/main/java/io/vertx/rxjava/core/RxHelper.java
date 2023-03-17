package io.vertx.rxjava.core;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.rx.java.ContextScheduler;
import io.vertx.rx.java.ObservableFuture;
import io.vertx.rx.java.UnmarshallerOperator;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientRequest;
import io.vertx.rxjava.core.http.HttpClientResponse;
import rx.Observable;
import rx.Scheduler;
import rx.plugins.RxJavaSchedulersHook;

/**
 * A set of helpers for RxJava and Vert.x.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxHelper {

  /**
   * Create a scheduler hook for a {@link io.vertx.rxjava.core.Vertx} object.
   *
   * @param vertx the vertx object
   * @return the scheduler hook
   */

  public static RxJavaSchedulersHook schedulerHook(Vertx vertx) {
    return io.vertx.rx.java.RxHelper.schedulerHook(vertx.getDelegate());
  }

  /**
   * Create a scheduler hook for a {@link io.vertx.rxjava.core.Context} object.
   *
   * @param context the context object
   * @return the scheduler hook
   */
  public static RxJavaSchedulersHook schedulerHook(Context context) {
    return io.vertx.rx.java.RxHelper.schedulerHook(context.getDelegate());
  }

  /**
   * Create a scheduler for a {@link Vertx} object, actions are executed on the event loop of the current context.
   *
   * @param vertx the vertx object
   * @return the scheduler
   */
  public static Scheduler scheduler(Vertx vertx) {
    return new ContextScheduler(vertx.getDelegate(), false);
  }

  /**
   * Create a scheduler for a {@link WorkerExecutor} object, actions are executed on the threads of this executor.
   *
   * @param executor the worker executor object
   * @return the scheduler
   */
  public static Scheduler scheduler(WorkerExecutor executor) {
    return new ContextScheduler(executor.getDelegate(), false);
  }

  /**
   * Create a scheduler for a {@link Context}, actions are executed on the event loop of this context.
   *
   * @param context the context object
   * @return the scheduler
   */
  public static Scheduler scheduler(Context context) {
    return new ContextScheduler(context.getDelegate(), false);
  }

  /**
   * Create a scheduler for a {@link Vertx} object, actions can be blocking, they are not executed
   * on Vertx event loop.
   *
   * @param vertx the vertx object
   * @return the scheduler
   */
  public static Scheduler blockingScheduler(Vertx vertx) {
    return new ContextScheduler(vertx.getDelegate().getOrCreateContext(), true);
  }

  /**
   * Create a scheduler for a {@link Vertx} object, actions can be blocking, they are not executed
   * on Vertx event loop.
   *
   * @param vertx the vertx object
   * @param ordered  if true then if when tasks are scheduled several times on the same context, the executions
   *                 for that context will be executed serially, not in parallel. if false then they will be no ordering
   *                 guarantees
   * @return the scheduler
   */
  public static Scheduler blockingScheduler(Vertx vertx, boolean ordered) {
    return new ContextScheduler(vertx.getDelegate().getOrCreateContext(), true, ordered);
  }

  /**
   * Returns a json unmarshaller for the specified java type as a {@link rx.Observable.Operator} instance.<p/>
   *
   * The marshaller can be used with the {@link rx.Observable#lift(rx.Observable.Operator)} method to transform
   * a {@literal Observable<Buffer>} into a {@literal Observable<T>}.<p/>
   *
   * The unmarshaller buffers the content until <i>onComplete</i> is called, then unmarshalling happens.<p/>
   *
   * Note that the returned observable will emit at most a single object.
   *
   * @param mappedType the type to unmarshall
   * @return the unmarshaller operator
   */
  public static <T> Observable.Operator<T, Buffer> unmarshaller(Class<T> mappedType) {
    return new UnmarshallerOperator<T, Buffer>(mappedType) {
      @Override
      public io.vertx.core.buffer.Buffer unwrap(Buffer buffer) {
        return buffer.getDelegate();
      }
    };
  }

  /**
   * Deploy a verticle you have created yourself, using an
   * RxJava vertx instance.
   *
   * @param vertx the vertx instance
   * @param verticle the verticle instance to deploy
   * @return the response observable
   */
  public static Observable<String> deployVerticle(Vertx vertx, Verticle verticle) {
    return deployVerticle(vertx, verticle, new DeploymentOptions());
  }

  /**
   * Like {@link #deployVerticle(Vertx, Verticle)}, but {@link io.vertx.core.DeploymentOptions} are provided to configure the
   * deployment.
   *
   * @param vertx the vertx instance
   * @param verticle the verticle instance to deploy
   * @param options the deployment options.
   * @return the response observable
   */
  public static Observable<String> deployVerticle(Vertx vertx, Verticle verticle, DeploymentOptions options) {
    ObservableFuture<String> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    vertx.getDelegate().deployVerticle(verticle, options).onComplete(completionHandler.toHandler());
    return completionHandler;
  }
}
