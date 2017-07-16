package io.vertx.reactivex;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.streams.ReadStream;
import io.vertx.reactivex.core.impl.FlowableReadStream;
import io.vertx.reactivex.core.impl.ObservableReadStream;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxHelper {

  /**
   * Adapts a Vert.x {@link ReadStream <T>} to an RxJava {@link Observable <T>}. After
   * the stream is adapted to an observable, the original stream handlers should not be used anymore
   * as they will be used by the observable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T> Observable<T> toObservable(ReadStream<T> stream) {
    return new ObservableReadStream<T, T>(stream, Function.identity());
  }

  /**
   * Adapts a Vert.x {@link ReadStream<T>} to an RxJava {@link Observable<T>}. After
   * the stream is adapted to an observable, the original stream handlers should not be used anymore
   * as they will be used by the observable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T, U> Observable<U> toObservable(ReadStream<T> stream, Function<T, U> f) {
    return new ObservableReadStream<T, U>(stream, f);
  }

  /**
   * Adapts a Vert.x {@link ReadStream<T>} to an RxJava {@link Flowable <U>}. After
   * the stream is adapted to a flowable, the original stream handlers should not be used anymore
   * as they will be used by the flowable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T, U> Flowable<U> toFlowable(ReadStream<T> stream, Function<T, U> f) {
    return new FlowableReadStream<>(stream, FlowableReadStream.DEFAULT_MAX_BUFFER_SIZE, f);
  }

  /**
   * Adapts a Vert.x {@link ReadStream<T>} to an RxJava {@link Flowable<T>}. After
   * the stream is adapted to a flowable, the original stream handlers should not be used anymore
   * as they will be used by the flowable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T> Flowable<T> toFlowable(ReadStream<T> stream) {
    return new FlowableReadStream<>(stream, FlowableReadStream.DEFAULT_MAX_BUFFER_SIZE, Function.identity());
  }

  /**
   * Adapts a Vert.x {@link ReadStream<T>} to an RxJava {@link Flowable<T>}. After
   * the stream is adapted to a flowable, the original stream handlers should not be used anymore
   * as they will be used by the flowable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T> Flowable<T> toFlowable(ReadStream<T> stream, long maxBufferSize) {
    return new FlowableReadStream<>(stream, maxBufferSize, Function.identity());
  }

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

}
