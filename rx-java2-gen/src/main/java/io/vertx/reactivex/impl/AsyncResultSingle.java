package io.vertx.reactivex.impl;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultSingle<T> extends Single<T> {

  private static final Logger log = LoggerFactory.getLogger(AsyncResultSingle.class);

  public static <T> Single<T> toSingle(Consumer<Handler<AsyncResult<T>>> subscriptionConsumer) {
    return RxJavaPlugins.onAssembly(new AsyncResultSingle<T>(subscriptionConsumer));
  }

  private final Consumer<Handler<AsyncResult<T>>> subscriptionConsumer;

  public AsyncResultSingle(Consumer<Handler<AsyncResult<T>>> subscriptionConsumer) {
    this.subscriptionConsumer = subscriptionConsumer;
  }

  @Override
  protected void subscribeActual(@NonNull SingleObserver<? super T> observer) {
    AtomicBoolean disposed = new AtomicBoolean();
    observer.onSubscribe(new Disposable() {
      @Override
      public void dispose() {
        disposed.set(true);
      }
      @Override
      public boolean isDisposed() {
        return disposed.get();
      }
    });
    if (!disposed.get()) {
      try {
        subscriptionConsumer.accept(ar -> {
          if (!disposed.getAndSet(true)) {
            if (ar.succeeded()) {
              try {
                observer.onSuccess(ar.result());
              } catch (Exception err) {
                log.error("Unexpected error", err);
              }
            } else if (ar.failed()) {
              try {
                observer.onError(ar.cause());
              } catch (Exception err) {
                log.error("Unexpected error", err);
              }
            }
          }
        });
      } catch (Exception e) {
        if (!disposed.getAndSet(true)) {
          try {
            observer.onError(e);
          } catch (Exception err) {
            log.error("Unexpected error", err);
          }
        }
      }
    }
  }
}
