package io.vertx.rx.java;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.plugins.RxJavaSchedulersHook;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * A set of helpers for RxJava and Vert.x.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxHelper {

  /**
   * Adapts an Vert.x {@code Handler<AsyncResult<T>>} to an RxJava {@link Subscriber}.
   * <p>
   * The returned subscriber can be subscribed to an {@link Observable#subscribe(Subscriber)} or
   * {@link rx.Single#subscribe(Subscriber)}.
   *
   * @param handler the handler to adapt
   * @return the subscriber
   */
  public static <T> Subscriber<T> toSubscriber(Handler<AsyncResult<T>> handler) {
    AtomicBoolean completed = new AtomicBoolean();
    return new Subscriber<T>() {
      @Override
      public void onCompleted() {
        if (completed.compareAndSet(false, true)) {
          handler.handle(Future.succeededFuture());
        }
      }
      @Override
      public void onError(Throwable error) {
        if (completed.compareAndSet(false, true)) {
          handler.handle(Future.failedFuture(error));
        }
      }
      @Override
      public void onNext(T item) {
        if (completed.compareAndSet(false, true)) {
          handler.handle(Future.succeededFuture(item));
        }
      }
    };
  }

  /**
   * Adapts an RxJava {@link Observable<T>} to a Vert.x {@link io.vertx.core.streams.ReadStream<T>}. The returned
   * readstream will be subscribed to the {@link Observable<T>}.<p>
   *
   * @param observable the observable to adapt
   * @return the adapted stream
   */
  public static <T> ReadStream<T> toReadStream(Observable<T> observable) {
    return ReadStreamSubscriber.asReadStream(observable, Function.identity());
  }

  /**
   * Adapts a Vert.x {@link io.vertx.core.streams.ReadStream<T>} to an RxJava {@link Observable<T>}. After
   * the stream is adapted to an observable, the original stream handlers should not be used anymore
   * as they will be used by the observable adapter.
   * <p>
   * The adapter supports <a href="https://github.com/ReactiveX/RxJava/wiki/Backpressure">reactive pull</a>
   * back-pressure.
   * <p>
   * When back-pressure is enabled, a buffer of {@link ObservableReadStream#DEFAULT_MAX_BUFFER_SIZE}
   * items is maintained:
   * <ul>
   *   <li>When the buffer is full, the stream is paused</li>
   *   <li>When the buffer is half empty, the stream is resumed</li>
   * </ul>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T> Observable<T> toObservable(ReadStream<T> stream) {
    return toObservable(stream, Function.identity());
  }

  /**
   * Adapts a Vert.x {@link io.vertx.core.streams.ReadStream<T>} to an RxJava {@link Observable<T>}. After
   * the stream is adapted to an observable, the original stream handlers should not be used anymore
   * as they will be used by the observable adapter.
   * <p>
   * The adapter supports <a href="https://github.com/ReactiveX/RxJava/wiki/Backpressure">reactive pull</a>
   * back-pressure.
   * <p>
   * When back-pressure is enabled, a buffer of {@code maxBufferSize} items is maintained:
   * <ul>
   *   <li>When the buffer is full, the stream is paused</li>
   *   <li>When the buffer is half empty, the stream is resumed</li>
   * </ul>
   *
   * @param stream the stream to adapt
   * @param maxBufferSize the max size of the buffer used when back-pressure is active
   * @return the adapted observable
   */
  public static <T> Observable<T> toObservable(ReadStream<T> stream, int maxBufferSize) {
    return toObservable(stream, Function.identity(), maxBufferSize);
  }

  /**
   * Like {@link #toObservable(ReadStream)} but with a function that adapts the items.
   */
  public static <T, R> Observable<R> toObservable(ReadStream<T> stream, Function<T, R> adapter) {
    return Observable.create(new ObservableReadStream<>(stream, adapter));
  }

  /**
   * Like {@link #toObservable(ReadStream, int)} but with a function that adapts the items.
   */
  public static <T, R> Observable<R> toObservable(ReadStream<T> stream, Function<T, R> adapter, int maxBufferSize) {
    return Observable.create(new ObservableReadStream<>(stream, adapter, maxBufferSize));
  }

  /**
   * Create a new {@code ObservableFuture<T>} object: an {@link rx.Observable} implementation
   * implementing {@code Handler<AsyncResult<T>>}. When the async result handler completes, the observable
   * will produce the result and complete immediatly after, when it fails it will signal the error.
   *
   * @return the observable future.
   */
  public static <T> ObservableFuture<T> observableFuture() {
    return new ObservableFuture<>();
  }

  /**
   * Create a new {@code ObservableHandler<T>} object: an {@link rx.Observable} implementation
   * implementing {@code Handler<T>}. When the event handler completes, the observable
   * will produce the event and complete immediatly after.
   *
   * @return the observable future.
   */
  public static <T> ObservableHandler<T> observableHandler() {
    return observableHandler(false);
  }

  /**
   * Create a new {@code ObservableHandler<T>} object: an {@link rx.Observable} implementation
   * implementing {@code Handler<T>}.<p/>
   *
   * When {@literal} parameter is false and the event handler completes, the observable
   * will produce the event and complete immediatly after, as a single event is expected.
   *
   * @param multi true if the handler can emit multiple events
   * @return the observable future.
   */
  public static <T> ObservableHandler<T> observableHandler(boolean multi) {
    return new ObservableHandler<>(multi);
  }

  /**
   * Adapt a {@link Subscriber} as a {@code Handler<AsyncResult<T>>;}.
   *
   * @param observer the subscriber to adapt
   * @return a {@code Handler<AsyncResult<T>>}
   */
  public static <T> Handler<AsyncResult<T>> toFuture(Observer<T> observer) {
    ObservableFuture<T> observable = RxHelper.<T>observableFuture();
    observable.subscribe(observer);
    return observable.toHandler();
  }

  /**
   * Adapt a {@link Subscriber} as a {@code Handler<T>;}.<p/>
   *
   * When the event handler completes, the observer
   * will complete immediatly after the event is received, as a single event is expected.
   *
   * @param observer the subscriber to adapt
   * @return a {@code Handler<T>}
   */
  public static <T> Handler<T> toHandler(Observer<T> observer) {
    return toHandler(observer, false);
  }

  /**
   * Adapt a {@link Subscriber} as a {@code Handler<T>;}.<p/>
   *
   * When {@literal} parameter is false and the event handler completes, the observer
   * will complete immediatly after the event is received, as a single event is expected.
   *
   * @param observer the subscriber to adapt
   * @param multi true if the handler can emit multiple events
   * @return a {@code Handler<T>}
   */
  public static <T> Handler<T> toHandler(Observer<T> observer, boolean multi) {
    ObservableHandler<T> observable = RxHelper.<T>observableHandler(multi);
    observable.subscribe(observer);
    return observable.toHandler();
  }

  /**
   * Adapt an item callback as a {@code Handler<AsyncResult<T>>}.
   *
   * @param onNext the {@code Action1<T>} you have designed to accept the resolution from the {@code Handler<AsyncResult<T>>}
   * @return a {@code Handler<AsyncResult<T>>}
   */
  public static <T> Handler<AsyncResult<T>> toFuture(Action1<T> onNext) {
    ObservableFuture<T> observable = RxHelper.<T>observableFuture();
    observable.subscribe(onNext);
    return observable.toHandler();
  }

  /**
   * Adapt an item callback as a {@code Handler<T>}.
   *
   * @param onNext the {@code Action1<T>} you have designed to accept the resolution from the {@code Handler<T>}
   * @return a {@code Handler<T>}
   */
  public static <T> Handler<T> toHandler(Action1<T> onNext) {
    ObservableHandler<T> observable = RxHelper.<T>observableHandler(true);
    observable.subscribe(onNext);
    return observable.toHandler();
  }

  /**
   * Adapt an item callback and an error callback as a {@code Handler<AsyncResult<T>>}.
   *
   * @param onNext the {@code Action1<T>} you have designed to accept the resolution from the {@code Handler<AsyncResult<T>>}
   * @param onError the {@code Action1<Throwable>} you have designed to accept the eventual failure from the {@code Handler<AsyncResult<T>>}
   * @return a {@code Handler<AsyncResult<T>>}
   */
  public static <T> Handler<AsyncResult<T>> toFuture(Action1<T> onNext, Action1<Throwable> onError) {
    ObservableFuture<T> observable = RxHelper.<T>observableFuture();
    observable.subscribe(onNext, onError);
    return observable.toHandler();
  }

  /**
   * Adapt an item callback and an error callback as a {@code Handler<AsyncResult<T>>}.
   *
   * @param onNext the {@code Action1<T>} you have designed to accept the resolution from the {@code Handler<AsyncResult<T>>}
   * @param onError the {@code Action1<Throwable>} you have designed to accept the eventual failure from the {@code Handler<AsyncResult<T>>}
   * @param onComplete the {@code Action0} you have designed to accept a completion notification from the {@code Handler<AsyncResult<T>>}
   * @return a {@code Handler<AsyncResult<T>>}
   */
  public static <T> Handler<AsyncResult<T>> toFuture(Action1<T> onNext, Action1<Throwable> onError, Action0 onComplete) {
    ObservableFuture<T> observable = RxHelper.<T>observableFuture();
    observable.subscribe(onNext, onError, onComplete);
    return observable.toHandler();
  }

  /**
   * Create a scheduler for a {@link Vertx} object, actions are executed on the event loop.
   *
   * @param vertx the vertx object
   * @return the scheduler
   */
  public static Scheduler scheduler(Vertx vertx) {
    return new ContextScheduler(vertx, false);
  }

  /**
   * Create a scheduler for a {@link Context}, actions are executed on the event loop of this context.
   *
   * @param context the context object
   * @return the scheduler
   */
  public static Scheduler scheduler(Context context) {
    return new ContextScheduler(context, false);
  }

  /**
   * Create a scheduler for a {@link Vertx} object, actions can be blocking, they are not executed
   * on Vertx event loop.
   *
   * @param vertx the vertx object
   * @return the scheduler
   */
  public static Scheduler blockingScheduler(Vertx vertx) {
    return new ContextScheduler(vertx, true);
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
    return new ContextScheduler(vertx, true, ordered);
  }

  /**
   * Create a scheduler hook for a {@link Context} object, the {@link rx.plugins.RxJavaSchedulersHook#getIOScheduler()}
   * uses a blocking scheduler.
   *
   * @param context the context object
   * @return the scheduler hook
   */
  public static RxJavaSchedulersHook schedulerHook(Context context) {
    return new RxJavaSchedulersHook() {
      @Override
      public Scheduler getComputationScheduler() {
        return scheduler(context);
      }
      @Override
      public Scheduler getIOScheduler() {
        return blockingScheduler(context.owner());
      }
      @Override
      public Scheduler getNewThreadScheduler() {
        return scheduler(context);
      }
    };
  }

  /**
   * Create a scheduler hook for a {@link Vertx} object, the {@link rx.plugins.RxJavaSchedulersHook#getIOScheduler()}
   * uses a blocking scheduler.
   *
   * @param vertx the vertx object
   * @return the scheduler hook
   */
  public static RxJavaSchedulersHook schedulerHook(Vertx vertx) {
    return new RxJavaSchedulersHook() {
      @Override
      public Scheduler getComputationScheduler() {
        return scheduler(vertx);
      }
      @Override
      public Scheduler getIOScheduler() {
        return blockingScheduler(vertx);
      }
      @Override
      public Scheduler getNewThreadScheduler() {
        return scheduler(vertx);
      }
    };
  }

  /**
   * Returns a json unmarshaller for the specified java type as a {@link rx.Observable.Operator} instance.<p/>
   *
   * The marshaller can be used with the {@link Observable#lift(rx.Observable.Operator)} method to transform
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
      public Buffer unwrap(Buffer buffer) {
        return buffer;
      }
    };
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
   * @param mappedTypeRef the type reference to unmarshall
   * @return the unmarshaller operator
   */
  public static <T> Observable.Operator<T, Buffer> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new UnmarshallerOperator<T, Buffer>(mappedTypeRef) {
      @Override
      public Buffer unwrap(Buffer buffer) {
        return buffer;
      }
    };
  }

  /**
   * Clearing handlers after stream closed causes issues for some (eg AsyncFile) so silently drop errors.
   */
  static void setNullHandlers(ReadStream<?> stream) {
    try {
      stream.exceptionHandler(null);
    }
    catch(Exception ignore) {
    }
    try {
      stream.endHandler(null);
    }
    catch(Exception ignore) {
    }
    try {
      stream.handler(null);
    }
    catch(Exception ignore) {
    }
  }
}
