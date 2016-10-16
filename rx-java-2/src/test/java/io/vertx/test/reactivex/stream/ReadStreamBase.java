package io.vertx.test.reactivex.stream;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamBase<T, Self extends ReadStreamBase<T, Self>> implements ReadStream<T> {

  private final Deque<T> events = new LinkedList<>();
  private Handler<T> dataHandler;
  private Handler<Throwable> exceptionHandler;
  private Handler<Void> endHandler;
  private boolean paused;
  private boolean ended;
  private final Context context;
  private final Self self;

  public ReadStreamBase(Context context) {
    this.self = (Self) this;
    this.context = context;
  }

  public ReadStreamBase() {
    this.self = (Self) this;
    this.context = null;
  }

  public synchronized boolean paused() {
    return paused;
  }

  public synchronized Handler<Void> endHandler() {
    return endHandler;
  }

  public synchronized Handler<Throwable> exceptionHandler() {
    return exceptionHandler;
  }

  public synchronized Handler<T> handler() {
    return dataHandler;
  }

  public void write(T buffer) {
    synchronized (this) {
      if (ended) {
        throw new IllegalStateException();
      }
      events.addLast(buffer);
    }
    checkPending();
  }

  @Override
  public synchronized Self exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return self;
  }

  @Override
  public Self handler(Handler<T> handler) {
    synchronized (this) {
      dataHandler = handler;
    }
    checkPending();
    return self;
  }

  @Override
  public synchronized Self pause() {
    paused = true;
    return self;
  }

  private Handler<Void> poll() {
    Handler<Void> task;
    synchronized (this) {
      if (events.size() > 0) {
        if (paused) {
          task = null;
        } else {
          T next = events.peekFirst();
          if (dataHandler != null) {
            events.removeFirst();
            task = v -> {
              dataHandler.handle(next);
            };
          } else {
            task = null;
          }
        }
      } else {
        if (ended && endHandler != null) {
          task = endHandler;
          endHandler = null;
        } else {
          task = null;
        }
      }
    }
    return task;
  }

  private void checkPending() {
    if (context != null) {
      Handler<Void> task = poll();
      if (task != null) {
        context.runOnContext(v -> {
          task.handle(null);
          checkPending();
        });
      }
    } else {
      Handler<Void> task;
      while ((task = poll()) != null) {
        task.handle(null);
      }
    }
  }

  @Override
  public Self resume() {
    synchronized (this) {
      paused = false;
    }
    checkPending();
    return self;
  }

  @Override
  public Self endHandler(Handler<Void> handler) {
    synchronized (this) {
      if (handler != null) {
        if (ended && events.isEmpty()) {
          if (context != null) {
            context.runOnContext(handler);
          } else {
            handler.handle(null);
          }
          return self;
        }
        endHandler = handler;
      } else {
        endHandler = null;
        return self;
      }
    }
    checkPending();
    return self;
  }

  public void end() {
    synchronized (this) {
      if (ended) {
        throw new IllegalStateException();
      }
      ended = true;
    }
    checkPending();
  }
}