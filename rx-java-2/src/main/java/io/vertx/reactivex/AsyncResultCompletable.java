package io.vertx.reactivex;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultCompletable implements CompletableSource, Handler<AsyncResult<Void>> {

  private AsyncResult<Void> asyncResult;
  private CompletableObserver observer;

  @Override
  public void handle(AsyncResult<Void> event) {
    asyncResult = event;
    check();
  }

  @Override
  public void subscribe(CompletableObserver obj) {
    if (this.observer != null) {
      throw new UnsupportedOperationException("todo");
    }
    this.observer = obj;
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
    if (asyncResult != null && observer != null) {
      if (asyncResult.succeeded()) {
        try {
          observer.onComplete();
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
