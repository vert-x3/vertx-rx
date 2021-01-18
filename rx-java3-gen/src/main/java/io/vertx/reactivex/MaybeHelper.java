package io.vertx.reactivex;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.MaybeTransformer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.impl.AsyncResultMaybe;
import io.vertx.reactivex.impl.MaybeUnmarshaller;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MaybeHelper {

  /**
   * Returns a {@link Maybe} that, when subscribed, uses the provided {@code handler} to adapt a callback-based asynchronous method.
   * <p>
   * For example:
   * <pre> {@code
   * io.vertx.core.Vertx vertx = Vertx.vertx();
   * Maybe<String> blockingMethodResult = MaybeHelper.toMaybe(handler -> vertx.<String>executeBlocking(fut -> fut.complete(invokeBlocking()), handler));
   * }</pre>
   * <p>
   * This is useful when using RxJava without the Vert.x Rxified API or your own asynchronous methods.
   *
   * @param handler the code executed when the returned {@link Maybe} is subscribed
   */
  public static <T> Maybe<T> toMaybe(Consumer<Handler<AsyncResult<T>>> handler) {
    return AsyncResultMaybe.toMaybe(handler);
  }

  /**
   * Adapts an Vert.x {@code Handler<AsyncResult<T>>} to an RxJava2 {@link MaybeObserver}.
   * <p>
   * The returned observer can be subscribed to an {@link Maybe#subscribe(MaybeObserver)}.
   *
   * @param handler the handler to adapt
   * @return the observer
   */
  public static <T> MaybeObserver<T> toObserver(Handler<AsyncResult<T>> handler) {
    AtomicBoolean completed = new AtomicBoolean();
    return new MaybeObserver<T>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
      }
      @Override
      public void onComplete() {
        if (completed.compareAndSet(false, true)) {
          handler.handle(io.vertx.core.Future.succeededFuture());
        }
      }
      @Override
      public void onSuccess(@NonNull T item) {
        if (completed.compareAndSet(false, true)) {
          handler.handle(io.vertx.core.Future.succeededFuture(item));
        }
      }
      @Override
      public void onError(Throwable error) {
        if (completed.compareAndSet(false, true)) {
          handler.handle(io.vertx.core.Future.failedFuture(error));
        }
      }
    };
  }

  /**
   * Adapts an RxJava2 {@code Maybe<T>} to a Vert.x {@link Future<T>}.
   * <p>
   * The maybe will be immediately subscribed and the returned future will
   * be updated with the result of the single.
   *
   * @param maybe the single to adapt
   * @return the future
   */
  public static <T> Future<T> toFuture(Maybe<T> maybe) {
    Promise<T> promise = Promise.promise();
    maybe.subscribe(promise::complete, promise::fail, promise::complete);
    return promise.future();
  }

  /**
   * Like {@link MaybeHelper#toFuture(Maybe)} but with an {@code adapter} of the result.
   */
  public static <T, U> Future<U> toFuture(Maybe<T> maybe, Function<T, U> adapter) {
    return toFuture(maybe.map(adapter::apply));
  }

  public static <T> MaybeTransformer<Buffer, T> unmarshaller(Class<T> mappedType) {
    return new MaybeUnmarshaller<>(java.util.function.Function.identity(), mappedType);
  }

  public static <T> MaybeTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new MaybeUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef);
  }

  public static <T> MaybeTransformer<Buffer, T> unmarshaller(Class<T> mappedType, ObjectCodec mapper) {
    return new MaybeUnmarshaller<>(java.util.function.Function.identity(), mappedType, mapper);
  }

  public static <T> MaybeTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    return new MaybeUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef, mapper);
  }
}
