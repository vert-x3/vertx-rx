package io.vertx.rx.java.test.support;

import io.vertx.core.Handler;
import io.vertx.core.queue.Queue;
import io.vertx.core.streams.ReadStream;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SimpleReadStream<T> implements ReadStream<T> {

  private Handler<Throwable> exceptionHandler;
  private Handler<T> itemHandler;
  private Handler<Void> endHandler;
  private boolean ended;
  private final Queue<T> queue;

  private Runnable onResume;

  public SimpleReadStream() {
    queue = Queue.queue();

    queue.writableHandler(v -> {
      resume();
    });
    queue.emptyHandler(v -> {
      if (ended) {
        if (endHandler != null) {
          endHandler.handle(null);
        }
      }
    });
    queue.handler(item -> {
      if (itemHandler != null) {
        itemHandler.handle(item);
      }
    });
  }

  public void assertPaused() {
    assertTrue(queue.isPaused());
  }

  public void assertResumed() {
    assertFalse(queue.isPaused());
  }

  public SimpleReadStream<T> expectPause() {
    return this;
  }

  public SimpleReadStream<T> expectPause(Runnable action) {
    return this;
  }

  public SimpleReadStream<T> expectResume() {
    return this;
  }

  public SimpleReadStream<T> onResume(Runnable action) {
    onResume = action;
    return this;
  }

  public void assertHasHandlers() {
    assertHasItemHandler();
    assertHasExceptionHandler();
    assertHasEndHandler();
  }

  public void assertHasNoHandlers() {
    assertHasNoItemHandler();
    assertHasNoExceptionHandler();
    assertHasNoEndHandler();
  }

  public void assertHasItemHandler() {
    assertTrue(itemHandler != null);
  }

  public void assertHasNoItemHandler() {
    assertTrue(itemHandler == null);
  }

  public void assertHasExceptionHandler() {
    assertNotNull(exceptionHandler);
  }

  public void assertHasNoExceptionHandler() {
    assertNull(exceptionHandler);
  }

  public void assertHasEndHandler() {
    assertNotNull(endHandler);
  }

  public void assertHasNoEndHandler() {
    assertNull(endHandler);
  }

  @Override
  public SimpleReadStream<T> exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }

  public SimpleReadStream<T> untilPaused(Runnable action) {
    while (!queue.isPaused()) {
      action.run();
    }
    return this;
  }

  public SimpleReadStream<T> untilResumed(Runnable action) {
    while (queue.isPaused()) {
      action.run();
    }
    return this;
  }

  public boolean emit(T first) {
    if (ended) {
      throw new IllegalStateException();
    }
    return queue.add(first);
  }

  public boolean emit(T first, T... other) {
    boolean full = emit(first);
    for (T item : other) {
      full &= emit(item);
    }
    return full;
  }

  public void end() {
    if (ended) {
      throw new IllegalStateException();
    }
    ended = true;
    if (queue.isEmpty()) {
      endHandler.handle(null);
    }
  }

  public void fail(Throwable err) {
    exceptionHandler.handle(err);
  }

  public void done(Throwable err) {
    if (err == null) {
      end();
    } else {
      fail(err);
    }
  }

  @Override
  public SimpleReadStream<T> handler(Handler<T> handler) {
    this.itemHandler = handler;
    return this;
  }

  @Override
  public SimpleReadStream<T> pause() {
    queue.pause();
    return this;
  }

  @Override
  public ReadStream<T> fetch(long amount) {
    assertNotNull(itemHandler);
    queue.take(amount);
    return this;
  }

  @Override
  public SimpleReadStream<T> resume() {
    if (!ended) {
      queue.resume();
      if (onResume != null) {
        onResume.run();
      }
    }
    return this;
  }

  @Override
  public SimpleReadStream<T> endHandler(Handler<Void> endHandler) {
    this.endHandler = endHandler;
    return this;
  }

  public void check() {
  }
}
