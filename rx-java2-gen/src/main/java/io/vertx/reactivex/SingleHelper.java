package io.vertx.reactivex;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.SingleObserver;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.impl.SingleUnmarshaller;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SingleHelper {

  /**
   * Adapts an Vert.x {@code Handler<AsyncResult<T>>} to an RxJava2 {@link SingleObserver}.
   * <p>
   * The returned observer can be subscribed to an {@link Single#subscribe(SingleObserver)}.
   *
   * @param handler the handler to adapt
   * @return the observer
   */
  public static <T> SingleObserver<T> toObserver(Handler<AsyncResult<T>> handler) {
    AtomicBoolean completed = new AtomicBoolean();
    return new SingleObserver<T>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
      }
      @Override
      public void onSuccess(@NonNull T item) {
        if (completed.compareAndSet(false, true)) {
          handler.handle(Future.succeededFuture(item));
        }
      }
      @Override
      public void onError(Throwable error) {
        if (completed.compareAndSet(false, true)) {
          handler.handle(Future.failedFuture(error));
        }
      }
    };
  }

  public static <T> SingleTransformer<Buffer, T> unmarshaller(Class<T> mappedType) {
    return new SingleUnmarshaller<>(java.util.function.Function.identity(), mappedType);
  }

  public static <T> SingleTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new SingleUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef);
  }

  public static <T> SingleTransformer<Buffer, T> unmarshaller(Class<T> mappedType, ObjectMapper mapper) {
    return new SingleUnmarshaller<>(java.util.function.Function.identity(), mappedType, mapper);
  }

  public static <T> SingleTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef, ObjectMapper mapper) {
    return new SingleUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef, mapper);
  }
}
