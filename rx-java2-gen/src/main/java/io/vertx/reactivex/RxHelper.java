package io.vertx.reactivex;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.vertx.core.Handler;
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

  /**
   * Adapts a Vert.x {@link WriteStream} to an RxJava {@link Subscriber}.
   * <p>
   * The {@link WriteStream#end()} method is not invoked when the {@link io.reactivex.Flowable} terminates.
   * <p>
   * Also, after subscription, the original {@link WriteStream} handlers should not be used anymore as they will be used by the adapter.
   *
   * @param stream the stream to adapt
   * @param onError callback invoked when the {@link io.reactivex.Flowable} terminates with an error, or the {@link WriteStream#exceptionHandler(Handler)} throws an error
   * @param onComplete callback invoked when the {@link io.reactivex.Flowable} terminates successfully
   *
   * @return the adapted {@link Subscriber}
   */
  public static <T> Subscriber<T> toSubscriber(WriteStream<T> stream, Consumer<Throwable> onError, Runnable onComplete) {
    return toSubscriber(stream, Function.identity(), onError, onComplete);
  }

  /**
   * Like {@link #toSubscriber(WriteStream, Consumer, Runnable)}, except the provided {@code mapping} function is applied to each {@link io.reactivex.Flowable} item.
   */
  public static <R, T> Subscriber<R> toSubscriber(WriteStream<T> stream, Function<R, T> mapping, Consumer<Throwable> onError, Runnable onComplete) {
    return new WriteStreamSubscriber<>(stream, mapping, onError, onComplete);
  }

  /**
   * Adapts a Vert.x {@link WriteStream} to an RxJava {@link Observer}.
   * <p>
   * The {@link WriteStream#end()} method is not invoked when the {@link io.reactivex.Observable} terminates.
   * <p>
   * Also, after subscription, the original {@link WriteStream} handlers should not be used anymore as they will be used by the adapter.
   *
   * @param stream the stream to adapt
   * @param onError callback invoked when the {@link io.reactivex.Observable} terminates with an error, or the {@link WriteStream#exceptionHandler(Handler)} throws an error
   * @param onComplete callback invoked when the {@link io.reactivex.Observable} terminates successfully
   *
   * @return the adapted {@link Observer}
   */
  public static <T> Observer<T> toObserver(WriteStream<T> stream, Consumer<Throwable> onError, Runnable onComplete) {
    return toObserver(stream, Function.identity(), onError, onComplete);
  }

  /**
   * Like {@link #toObserver(WriteStream, Consumer, Runnable)}, except the provided {@code mapping} function is applied to each {@link io.reactivex.Observable} item.
   */
  public static <R, T> Observer<R> toObserver(WriteStream<T> stream, Function<R, T> mapping, Consumer<Throwable> onError, Runnable onComplete) {
    return new WriteStreamObserver<>(stream, mapping, onError, onComplete);
  }
}
