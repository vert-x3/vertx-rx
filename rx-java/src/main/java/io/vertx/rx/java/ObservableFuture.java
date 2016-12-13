package io.vertx.rx.java;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import rx.Observable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableFuture<T> extends Observable<T> {

  private static class HandlerAdapter<T> extends ObservableOnSubscribeAdapter<T> implements Handler<AsyncResult<T>> {

    private AsyncResult<T> buffered;
    private boolean subscribed;

    @Override
    public void onSubscribed() {
      AsyncResult<T> result = buffered;
      if (result != null) {
        buffered = null;
        dispatch(result);
      } else {
        subscribed = true;
      }
    }

    @Override
    public void handle(AsyncResult<T> event) {
      if (subscribed) {
        subscribed = false;
        dispatch(event);
      } else {
        this.buffered = event;
      }
    }

    @Override
    protected void onUnsubscribed() {
      subscribed = false;
    }

    protected void dispatch(AsyncResult<T> event) {
      if (event.succeeded()) {
        this.fireNext(event.result());
        this.fireComplete();
      } else {
        this.fireError(event.cause());
      }
    }
  }

  public ObservableFuture() {
    this(new HandlerAdapter<T>() {
      @Override
      protected void dispatch(AsyncResult<T> event) {
        if (event.succeeded()) {
          this.fireNext(event.result());
          this.fireComplete();
        } else {
          this.fireError(event.cause());
        }
      }
    });
  }

  private HandlerAdapter<T> adapter;

  private ObservableFuture(HandlerAdapter<T> adapter) {
    super(adapter);
    this.adapter = adapter;
  }

  public Handler<AsyncResult<T>> toHandler() {
    return adapter;
  }
}
