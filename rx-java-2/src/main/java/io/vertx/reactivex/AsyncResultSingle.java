package io.vertx.reactivex;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultSingle<T> implements SingleSource<T>, Handler<AsyncResult<T>> {

  private AsyncResult<T> asyncResult;
  private SingleObserver<? super T> observer;

  @Override
  public void handle(AsyncResult<T> event) {
    asyncResult = event;
    check();
  }

  @Override
  public void subscribe(SingleObserver<? super T> obj) {
    if (this.observer != null) {
      throw new UnsupportedOperationException("todo");
    }
    this.observer = obj;
    obj.onSubscribe(new Disposable() {
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
    if (asyncResult != null && observer != null) {
      if (asyncResult.succeeded()) {
        try {
          observer.onSuccess(asyncResult.result());
        } catch (Exception e) {

        } finally {
          observer = null;
        }
      } else if (asyncResult.failed()) {
        try {
          observer.onError(asyncResult.cause());
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          observer = null;
        }
      }
    }
  }
}
