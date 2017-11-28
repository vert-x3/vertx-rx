package io.vertx.reactivex.core.impl;

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
public class AsyncResultSingle<T> extends Single<T> {

  private final Handler<Handler<AsyncResult<T>>> method;

  public AsyncResultSingle(Handler<Handler<AsyncResult<T>>> method) {
    this.method = method;
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
        method.handle(ar -> {
          if (!disposed.getAndSet(true)) {
            if (ar.succeeded()) {
              try {
                observer.onSuccess(ar.result());
              } catch (Throwable ignore) {
              }
            } else if (ar.failed()) {
              try {
                observer.onError(ar.cause());
              } catch (Throwable ignore) {
              }
            }
          }
        });
      } catch (Exception e) {
        if (!disposed.getAndSet(true)) {
          try {
            observer.onError(e);
          } catch (Throwable ignore) {
          }
        }
      }
    }
  }
}
