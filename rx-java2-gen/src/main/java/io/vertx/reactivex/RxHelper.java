package io.vertx.reactivex;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.streams.WriteStream;
import io.vertx.reactivex.impl.WriteStreamObserver;
import io.vertx.reactivex.impl.WriteStreamSubscriber;
import org.reactivestreams.Subscriber;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxHelper {

  /**
   * Create a scheduler for a {@link io.vertx.core.Vertx} object, actions are executed on the event loop.
   *
   * @param vertx the vertx object
   * @return the scheduler
   */
  public static Scheduler scheduler(io.vertx.core.Vertx vertx) {
    return new ContextScheduler(vertx, false);
  }

  /**
   * Create a scheduler for a {@link io.vertx.core.Context}, actions are executed on the event loop of this context.
   *
   * @param context the context object
   * @return the scheduler
   */
  public static Scheduler scheduler(io.vertx.core.Context context) {
    return new ContextScheduler(context, false);
  }

  /**
   * Create a scheduler for a {@link io.vertx.core.Vertx} object, actions can be blocking, they are not executed
   * on Vertx event loop.
   *
   * @param vertx the vertx object
   * @return the scheduler
   */
  public static Scheduler blockingScheduler(io.vertx.core.Vertx vertx) {
    return new ContextScheduler(vertx, true);
  }

  /**
   * Create a scheduler for a {@link io.vertx.core.Vertx} object, actions can be blocking, they are not executed
   * on Vertx event loop.
   *
   * @param vertx the vertx object
   * @param ordered  if true then if when tasks are scheduled several times on the same context, the executions
   *                 for that context will be executed serially, not in parallel. if false then they will be no ordering
   *                 guarantees
   * @return the scheduler
   */
  public static Scheduler blockingScheduler(Vertx vertx, boolean ordered) {
    return new ContextScheduler(vertx, true, ordered);
  }

  /**
   * Create a scheduler for a {@link io.vertx.core.WorkerExecutor} object, actions are executed on the threads of this executor.
   *
   * @param executor the worker executor object
   * @return the scheduler
   */
  public static Scheduler blockingScheduler(WorkerExecutor executor) {
    return new ContextScheduler(executor, false);
  }

  public static <T> Subscriber<T> toSubscriber(WriteStream<T> stream, Consumer<Throwable> onError, Runnable onComplete) {
    return toSubscriber(stream, Function.identity(), onError, onComplete);
  }

  public static <R, T> Subscriber<R> toSubscriber(WriteStream<T> stream, Function<R, T> adapter, Consumer<Throwable> onError, Runnable onComplete) {
    return new WriteStreamSubscriber<>(stream, adapter, onError, onComplete);
  }

  public static <T> Observer<T> toObserver(WriteStream<T> stream, Consumer<Throwable> onError, Runnable onComplete) {
    return toObserver(stream, Function.identity(), onError, onComplete);
  }

  public static <R, T> Observer<R> toObserver(WriteStream<T> stream, Function<R, T> adapter, Consumer<Throwable> onError, Runnable onComplete) {
    return new WriteStreamObserver<>(stream, adapter, onError, onComplete);
  }
}
