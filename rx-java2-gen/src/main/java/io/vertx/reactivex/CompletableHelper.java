package io.vertx.reactivex;

import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CompletableHelper {

  /**
   * Adapts an Vert.x {@code Handler<AsyncResult<T>>} to an RxJava2 {@link SingleObserver}.
   * <p>
   * The returned observer can be subscribed to an {@link Single#subscribe(SingleObserver)}.
   *
   * @param handler the handler to adapt
   * @return the observer
   */
  public static <T> CompletableObserver toObserver(Handler<AsyncResult<T>> handler) {
    AtomicBoolean completed = new AtomicBoolean();
    return new CompletableObserver() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
      }
      @Override
      public void onComplete() {
        if (completed.compareAndSet(false, true)) {
          handler.handle(io.vertx.core.Future.succeededFuture());
        }
      }
      public void onSuccess() {
        if (completed.compareAndSet(false, true)) {
          handler.handle(io.vertx.core.Future.succeededFuture());
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
}
