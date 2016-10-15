package io.vertx.test.reactivex.stream;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamBase<T, Self extends ReadStreamBase<T, Self>> implements ReadStream<T> {

  private static final Object END = new Object();

  private final Deque<Object> events = new LinkedList<>();
  private Handler<T> dataHandler;
  private Handler<Throwable> exceptionHandler;
  private Handler<Void> endHandler;
  private boolean paused;

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
      events.addLast(buffer);
    }
    checkPending();
  }

  @Override
  public synchronized Self exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return (Self) this;
  }

  @Override
  public Self handler(Handler<T> handler) {
    synchronized (this) {
      dataHandler = handler;
    }
    checkPending();
    return (Self) this;
  }

  @Override
  public synchronized Self pause() {
    paused = true;
    return (Self) this;
  }

  private void checkPending() {
    while (true) {
      Runnable task;
      synchronized (this) {
        if (events.size() > 0) {
          Object next = events.peekFirst();
          if (next != END) {
            if (dataHandler != null) {
              events.removeFirst();
              task = () -> {
                dataHandler.handle((T) next);
              };
            } else {
              break;
            }
          } else {
            if (endHandler != null) {
              events.removeFirst();
              task = () -> {
                endHandler.handle(null);
              };
            } else {
              break;
            }
          }
        } else {
          break;
        }
      }
      task.run();
    }
  }

  @Override
  public Self resume() {
    synchronized (this) {
      paused = false;
    }
    checkPending();
    return (Self) this;
  }

  @Override
  public Self endHandler(Handler<Void> handler) {
    synchronized (this) {
      endHandler = handler;
    }
    checkPending();
    return (Self) this;
  }

  public void end() {
    synchronized (this) {
      events.addLast(END);
    }
    checkPending();
  }
}