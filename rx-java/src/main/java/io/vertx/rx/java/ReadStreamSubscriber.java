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

  private static final Runnable NOOP_ACTION = () -> {};
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
    synchronized (this) {
      itemHandler = handler;
    }
    checkStatus();
    return this;
  }

  @Override
  public ReadStream<J> pause() {
    synchronized (this) {
      paused = true;
    }
    return this;
  }

  @Override
  public ReadStream<J> resume() {
    synchronized (this) {
      paused = false;
    }
    checkStatus();
    return this;
  }

  private void checkStatus() {
    Runnable action = NOOP_ACTION;
    while (true) {
      J adapted;
      Handler<J> handler;
      synchronized (this) {
        if (!paused && (handler = itemHandler) != null && pending.size() > 0) {
          requested--;
          R item = pending.poll();
          adapted = adapter.apply(item);
        } else {
          if (completed != null) {
            if (pending.isEmpty()) {
              Handler<Throwable> onError;
              Throwable result;
              if (completed != DONE_SENTINEL) {
                onError = exceptionHandler;
                result = completed;
                exceptionHandler = null;
              } else {
                onError = null;
                result = null;
              }
              Handler<Void> onCompleted = endHandler;
              endHandler = null;
              action = () -> {
                try {
                  if (onError != null) {
                    onError.handle(result);
                  }
                } finally {
                  if (onCompleted != null) {
                    onCompleted.handle(null);
                  }
                }
              };
            }
          } else if (requested < FETCH_SIZE / 2) {
            int request = FETCH_SIZE - requested;
            action = () -> request(request);
            requested = FETCH_SIZE;
          }
          break;
        }
      }
      handler.handle(adapted);
    }
    action.run();
  }

  @Override
  public ReadStream<J> endHandler(Handler<Void> handler) {
    synchronized (this) {
      if (completed == null || pending.size() > 0) {
        endHandler = handler;
      } else {
        if (handler != null) {
          throw new IllegalStateException();
        }
      }
    }
    return this;
  }

  @Override
  public ReadStream<J> exceptionHandler(Handler<Throwable> handler) {
    synchronized (this) {
      if (completed == null || pending.size() > 0) {
        exceptionHandler = handler;
      } else {
        if (handler != null) {
          throw new IllegalStateException();
        }
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
    synchronized (this) {
      if (completed != null) {
        return;
      }
      completed = e;
    }
    checkStatus();
  }

  @Override
  public void onNext(R item) {
    synchronized (this) {
      pending.add(item);
    }
    checkStatus();
  }
}
