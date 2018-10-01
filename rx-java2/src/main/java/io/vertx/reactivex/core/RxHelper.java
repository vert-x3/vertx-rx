package io.vertx.reactivex.core;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.plugins.RxJavaPlugins;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.reactivex.impl.AsyncResultSingle;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxHelper {

  /**
   * Create a scheduler for a {@link WorkerExecutor} object, actions are executed on the threads of this executor.
   *
   * @param executor the worker executor object
   * @return the scheduler
   */
  public static Scheduler blockingScheduler(WorkerExecutor executor) {
    return io.vertx.reactivex.RxHelper.blockingScheduler(executor.getDelegate());
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
    return io.vertx.reactivex.RxHelper.blockingScheduler(vertx.getDelegate(), ordered);
  }

  /**
   * Deploy a verticle you have created yourself, using an
   * RxJava vertx instance.
   *
   * @param vertx the vertx instance
   * @param verticle the verticle instance to deploy
   * @return the response observable
   */
  public static Single<String> deployVerticle(Vertx vertx, Verticle verticle) {
    return deployVerticle(vertx, verticle, new DeploymentOptions());
  }

  /**
   * Like {@link #deployVerticle(Vertx, Verticle)}, but {@link DeploymentOptions} are provided to configure the
   * deployment.
   *
   * @param vertx the vertx instance
   * @param verticle the verticle instance to deploy
   * @param options the deployment options.
   * @return the response observable
   */
  public static Single<String> deployVerticle(Vertx vertx, Verticle verticle, DeploymentOptions options) {
    return RxJavaPlugins.onAssembly(new AsyncResultSingle<>(handler -> vertx.getDelegate().deployVerticle(verticle, options, handler)));
  }

  /**
   * Create a scheduler for a {@link Vertx} object, actions can be blocking, they are not executed
   * on Vertx event loop.
   *
   * @param vertx the vertx object
   * @return the scheduler
   */
  public static Scheduler blockingScheduler(Vertx vertx) {
    return io.vertx.reactivex.RxHelper.blockingScheduler(vertx.getDelegate());
  }

  /**
   * Create a scheduler for a {@link Context}, actions are executed on the event loop of this context.
   *
   * @param context the context object
   * @return the scheduler
   */
  public static Scheduler scheduler(Context context) {
    return io.vertx.reactivex.RxHelper.scheduler(context.getDelegate());
  }

  /**
   * Create a scheduler for a {@link Vertx} object, actions are executed on the event loop.
   *
   * @param vertx the vertx object
   * @return the scheduler
   */
  public static Scheduler scheduler(Vertx vertx) {
    return io.vertx.reactivex.RxHelper.scheduler(vertx.getDelegate());
  }

}
