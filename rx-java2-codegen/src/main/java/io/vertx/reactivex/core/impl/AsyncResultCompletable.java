package io.vertx.reactivex.core.impl;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultCompletable extends Completable {

  private final Handler<Handler<AsyncResult<Void>>> method;

  public AsyncResultCompletable(Handler<Handler<AsyncResult<Void>>> method) {
    this.method = method;
  }

  @Override
  protected void subscribeActual(CompletableObserver observer) {
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
                observer.onComplete();
              } catch (Throwable ignore) {
              }
            } else {
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
