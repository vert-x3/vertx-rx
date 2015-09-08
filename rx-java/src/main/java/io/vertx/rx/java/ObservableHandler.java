package io.vertx.rx.java;

import io.vertx.core.Handler;
import rx.Observable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableHandler<T> extends Observable<T> {

  private abstract static class HandlerAdapter<T> extends SingleOnSubscribeAdapter<T> implements Handler<T> {

    private boolean subscribed;

    @Override
    public void onSubscribed() {
      subscribed = true;
    }

    @Override
    public void handle(T event) {
      if (subscribed) {
        dispatch(event);
      }
    }

    @Override
    protected void onUnsubscribed() {
      subscribed = false;
    }

    protected abstract void dispatch(T event);
  }

  public ObservableHandler() {
    this(new HandlerAdapter<T>() {
      @Override
      protected void dispatch(T event) {
        this.fireNext(event);
      }
    });
  }

  private HandlerAdapter<T> adapter;

  private ObservableHandler(HandlerAdapter<T> adapter) {
    super(adapter);
    this.adapter = adapter;
  }

  public Handler<T> toHandler() {
    return adapter;
  }
}
