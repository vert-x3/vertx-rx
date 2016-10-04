package io.vertx.reactivex;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultSource<T> implements SingleSource<T>, Handler<AsyncResult<T>> {

  private AsyncResult<T> asyncResult;
  private Subscription<T> sub;

  @Override
  public void handle(AsyncResult<T> event) {
    asyncResult = event;
    check();
  }

  @Override
  public void subscribe(SingleObserver<? super T> observer) {
    if (sub != null) {
      throw new UnsupportedOperationException("todo");
    }
    sub = new Subscription<>(observer);
    observer.onSubscribe(new Disposable() {
      @Override
      public void dispose() {
      }
      @Override
      public boolean isDisposed() {
        return false;
      }
    });
    check();
  }

  private void check() {
    if (asyncResult != null && sub != null) {
      if (asyncResult.succeeded()) {
        try {
          sub.observer.onSuccess(asyncResult.result());
        } catch (Exception e) {

        } finally {
          sub = null;
        }
      } else if (asyncResult.failed()) {
        try {
          sub.observer.onError(asyncResult.cause());
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          sub = null;
        }
      }
    }
  }

  private static class Subscription<T> {

    private final SingleObserver<? super T> observer;

    public Subscription(SingleObserver<? super T> observer) {
      this.observer = observer;
    }
  }
}
