package io.vertx.reactivex;

import io.reactivex.rxjava3.core.Scheduler;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.streams.WriteStream;
import io.vertx.reactivex.impl.WriteStreamObserverImpl;
import io.vertx.reactivex.impl.WriteStreamSubscriberImpl;
import org.reactivestreams.Subscriber;

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
   * After subscription, the original {@link WriteStream} handlers should not be used anymore as they will be used by the adapter.
   *
   * @param stream the stream to adapt
   *
   * @return the adapted {@link Subscriber}
   */
  public static <T> WriteStreamSubscriber<T> toSubscriber(WriteStream<T> stream) {
    return toSubscriber(stream, Function.identity());
  }

  /**
   * Like {@link #toSubscriber(WriteStream)}, except the provided {@code mapping} function is applied to each {@link io.reactivex.rxjava3.core.Flowable} item.
   */
  public static <R, T> WriteStreamSubscriber<R> toSubscriber(WriteStream<T> stream, Function<R, T> mapping) {
    return new WriteStreamSubscriberImpl<>(stream, mapping);
  }

  /**
   * Adapts a Vert.x {@link WriteStream} to an RxJava {@link io.reactivex.rxjava3.core.Observer}.
   * <p>
   * After subscription, the original {@link WriteStream} handlers should not be used anymore as they will be used by the adapter.
   *
   * @param stream the stream to adapt
   *
   * @return the adapted {@link io.reactivex.rxjava3.core.Observer}
   */
  public static <T> WriteStreamObserver<T> toObserver(WriteStream<T> stream) {
    return toObserver(stream, Function.identity());
  }

  /**
   * Like {@link #toObserver(WriteStream)}, except the provided {@code mapping} function is applied to each {@link io.reactivex.rxjava3.core.Observable} item.
   */
  public static <R, T> WriteStreamObserver<R> toObserver(WriteStream<T> stream, Function<R, T> mapping) {
    return new WriteStreamObserverImpl<>(stream, mapping);
  }
}
