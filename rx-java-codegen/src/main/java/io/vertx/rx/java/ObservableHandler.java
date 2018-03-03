package io.vertx.rx.java;

import io.vertx.core.Handler;
import rx.Observable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableHandler<T> extends Observable<T> {

  private abstract static class HandlerAdapter<T> extends ObservableOnSubscribeAdapter<T> implements Handler<T> {

    private static final int STATUS_MULTI = 0;
    private static final int STATUS_SINGLE = 1;
    private static final int STATUS_DISPATCHED = 2;

    private boolean subscribed;
    private int status;

    private HandlerAdapter(boolean multi) {
      status = multi ? STATUS_MULTI : STATUS_SINGLE;
    }

    @Override
    public void onSubscribed() {
      subscribed = true;
      if (status == STATUS_DISPATCHED) {
        fireComplete();
      }
    }

    @Override
    public void handle(T event) {
      if (status == STATUS_MULTI) {
        if (subscribed) {
          dispatch(event);
        }
      } else if (status == STATUS_SINGLE) {
        if (subscribed) {
          dispatch(event);
          fireComplete();
        }
        status = STATUS_DISPATCHED;
      }
    }

    @Override
    protected void onUnsubscribed() {
      subscribed = false;
    }

    protected abstract void dispatch(T event);
  }

  public ObservableHandler() {
    this(true);
  }

  public ObservableHandler(boolean multi) {
    this(new HandlerAdapter<T>(multi) {
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
