package io.vertx.test.reactivex.stream;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BufferReadStreamImpl implements BufferReadStream {

  private final Deque<Buffer> events = new LinkedList<>();
  private Handler<Buffer> dataHandler;
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

  public synchronized Handler<Buffer> handler() {
    return dataHandler;
  }

  public void write(Buffer buffer) {
    synchronized (this) {
      events.addLast(buffer);
    }
    checkPending();
  }

  @Override
  public synchronized BufferReadStreamImpl exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return this;
  }

  @Override
  public BufferReadStreamImpl handler(Handler<Buffer> handler) {
    synchronized (this) {
      dataHandler = handler;
    }
    checkPending();
    return this;
  }

  @Override
  public synchronized BufferReadStreamImpl pause() {
    paused = true;
    return this;
  }

  private void checkPending() {
    while (true) {
      Runnable task;
      synchronized (this) {
        if (events.size() > 0) {
          Buffer event = events.peekFirst();
          if (event != null) {
            if (dataHandler != null) {
              events.removeFirst();
              task = () -> {
                dataHandler.handle(event);
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
  public BufferReadStreamImpl resume() {
    synchronized (this) {
      paused = false;
    }
    checkPending();
    return this;
  }

  @Override
  public BufferReadStreamImpl endHandler(Handler<Void> handler) {
    synchronized (this) {
      endHandler = handler;
    }
    checkPending();
    return this;
  }

  public void end() {
    synchronized (this) {
      events.addLast(null);
    }
    checkPending();
  }
}