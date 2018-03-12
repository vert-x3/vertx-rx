package io.vertx.rx.java;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HandlerAdapter<T> extends ObservableOnSubscribeAdapter<T> implements Handler<AsyncResult<T>> {

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
