package io.vertx.reactivex;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.reactivex.impl.AsyncResultCompletable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CompletableHelper {

  /**
   * Returns a {@link Completable} that, when subscribed, uses the provided {@code handler} to adapt a callback-based asynchronous method.
   * <p>
   * For example:
   * <pre> {@code
   * io.vertx.core.Vertx vertx = Vertx.vertx();
   * // ... later
   * Completable undeploy = CompletableHelper.toCompletable(handler -> vertx.undeploy(deploymentId, handler));
   * }</pre>
   * <p>
   * This is useful when using RxJava without the Vert.x Rxified API or your own asynchronous methods.
   *
   * @param handler the code executed when the returned {@link Completable} is subscribed
   */
  public static Completable toCompletable(Consumer<Handler<AsyncResult<Void>>> handler) {
    return AsyncResultCompletable.toCompletable(handler);
  }

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
