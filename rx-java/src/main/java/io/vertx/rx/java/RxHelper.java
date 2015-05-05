package io.vertx.rx.java;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.Pump;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.reactivestreams.ReactiveWriteStream;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import rx.Observable;
import rx.Observer;
import rx.RxReactiveStreams;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.plugins.RxJavaSchedulersHook;

/**
 * A set of helpers for RxJava and Vert.x.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxHelper {


  /**
   * Adapts a Vert.x {@link io.vertx.core.streams.ReadStream<T>} to an RxJava {@link Observable<T>}. After
   * the stream is adapted to an observable, the original stream handlers should not be used anymore
   * as they will be used by the observable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T> Observable<T> toObservable(ReadStream<T> stream, Vertx vertx) {
    ReactiveWriteStream<T> ws = ReactiveWriteStream.writeStream(vertx);
    Observable<T> obs = RxReactiveStreams.toObservable(new Publisher<T>() {
      @Override
      public void subscribe(org.reactivestreams.Subscriber<? super T> subscriber) {
        org.reactivestreams.Subscriber<T> subscriberProxy = new org.reactivestreams.Subscriber<T>() {
          Pump pump = Pump.pump(stream, ws);
          @Override
          public void onSubscribe(Subscription subscription) {
            Subscription subscriptionProxy = new Subscription() {
              @Override
              public void request(long n) {
                subscription.request(n);
              }
              @Override
              public void cancel() {
                vertx.runOnContext(v -> {
                  subscriber.onComplete();
                });
                pump.stop();
                subscription.cancel();
//                ws.close(); ?
              }
            };
            stream.endHandler(done -> {
              subscriptionProxy.cancel();
            });
            stream.exceptionHandler(this::onError);
            pump.start();
            subscriber.onSubscribe(subscriptionProxy);
          }

          @Override
          public void onNext(T event) {
            subscriber.onNext(event);
          }

          @Override
          public void onError(Throwable t) {
            subscriber.onError(t);
          }

          @Override
          public void onComplete() {
            subscriber.onComplete();
          }
        };
        ws.subscribe(subscriberProxy);
      }
    });
    return obs;
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
    return new ObservableHandler<>();
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
   * Adapt a {@link Subscriber} as a {@code Handler<T>;}.
   *
   * @param observer the subscriber to adapt
   * @return a {@code Handler<T>}
   */
  public static <T> Handler<T> toHandler(Observer<T> observer) {
    ObservableHandler<T> observable = RxHelper.<T>observableHandler();
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
    ObservableHandler<T> observable = RxHelper.<T>observableHandler();
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
}
