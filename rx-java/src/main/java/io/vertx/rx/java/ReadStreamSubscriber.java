package io.vertx.rx.java;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import rx.Observable;
import rx.Subscriber;

import java.util.ArrayDeque;
import java.util.function.Function;

/**
 * todo: synchronization
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamSubscriber<R, J> extends Subscriber<R> implements ReadStream<J> {

  private static final Throwable DONE_SENTINEL = new Throwable();

  public static final int FETCH_SIZE = 16;

  public static <R, J> ReadStream<J> asReadStream(Observable<R> observable, Function<R, J> adapter) {
    ReadStreamSubscriber<R, J> observer = new ReadStreamSubscriber<R, J>(adapter);
    observable.subscribe(observer);
    return observer;
  }

  private final Function<R, J> adapter;
  private Handler<Void> endHandler;
  private Handler<Throwable> exceptionHandler;
  private Handler<J> itemHandler;
  private boolean paused = false;
  private Throwable completed;
  private ArrayDeque<R> pending = new ArrayDeque<>();
  private int requested = 0;

  public ReadStreamSubscriber(Function<R, J> adapter) {
    this.adapter = adapter;
    request(0);
  }

  @Override
  public ReadStream<J> handler(Handler<J> handler) {
    itemHandler = handler;
    checkStatus();
    return this;
  }

  @Override
  public ReadStream<J> pause() {
    paused = true;
    return this;
  }

  @Override
  public ReadStream<J> resume() {
    paused = false;
    checkStatus();
    return this;
  }

  private void checkStatus() {
    Handler<J> handler;
    while (!paused && (handler = itemHandler) != null) {
      if (pending.size() > 0) {
        requested--;
        R item = pending.poll();
        J adapted = adapter.apply(item);
        handler.handle(adapted);
      } else {
        break;
      }
    }
    if (completed != null) {
      if (pending.isEmpty()) {
        if (completed != DONE_SENTINEL) {
          Handler<Throwable> callback = exceptionHandler;
          if (callback != null) {
            exceptionHandler = null;
            callback.handle(completed);
          }
        }
        Handler<Void> callback = endHandler;
        if (callback != null) {
          endHandler = null;
          callback.handle(null);
        }
      }
    } else if (requested < FETCH_SIZE / 2) {
      request(FETCH_SIZE - requested);
      requested = FETCH_SIZE;
    }
  }

  @Override
  public ReadStream<J> endHandler(Handler<Void> handler) {
    if (completed == null || pending.size() > 0) {
      endHandler = handler;
    } else {
      if (handler != null) {
        throw new IllegalStateException();
      }
    }
    return this;
  }

  @Override
  public ReadStream<J> exceptionHandler(Handler<Throwable> handler) {
    if (completed == null || pending.size() > 0) {
      exceptionHandler = handler;
    } else {
      if (handler != null) {
        throw new IllegalStateException();
      }
    }
    return this;
  }

  @Override
  public void onCompleted() {
    onError(DONE_SENTINEL);
  }

  @Override
  public void onError(Throwable e) {
    if (completed == null) {
      completed = e;
      checkStatus();
    }
  }

  @Override
  public void onNext(R item) {
    pending.add(item);
    checkStatus();
  }
}
