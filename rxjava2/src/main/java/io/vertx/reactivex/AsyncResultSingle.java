package io.vertx.reactivex;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultSingle<T> implements SingleSource<T> {

  private final Handler<Handler<AsyncResult<T>>> method;

  public AsyncResultSingle(Handler<Handler<AsyncResult<T>>> method) {
    this.method = method;
  }

  @Override
  public void subscribe(SingleObserver<? super T> observer) {
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
      method.handle(ar -> {
        if (!disposed.getAndSet(false)) {
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
    }
  }
}
