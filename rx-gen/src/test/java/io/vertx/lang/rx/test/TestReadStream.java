package io.vertx.lang.rx.test;

import io.vertx.core.Handler;
import io.vertx.core.impl.Arguments;
import io.vertx.core.streams.ReadStream;
import org.junit.Assert;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestReadStream<T> implements ReadStream<T> {

  private final long highWaterMark = 16L;
  private Handler<Throwable> exceptionHandler;
  private Handler<T> itemHandler;
  private Handler<Void> endHandler;
  private final Deque<T> pending;
  private long demand = Long.MAX_VALUE;
  private boolean ended;
  private boolean overflow;

  private Runnable onResume;

  public TestReadStream() {
    pending = new ArrayDeque<>();
  }

  public void assertPaused() {
    Assert.assertEquals(0L, demand);
  }

  public void assertResumed() {
    Assert.assertTrue(demand > 0L);
  }

  public TestReadStream<T> expectPause() {
    return this;
  }

  public TestReadStream<T> expectPause(Runnable action) {
    return this;
  }

  public TestReadStream<T> expectResume() {
    return this;
  }

  public TestReadStream<T> onResume(Runnable action) {
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
    Assert.assertTrue(itemHandler != null);
  }

  public void assertHasNoItemHandler() {
    Assert.assertTrue(itemHandler == null);
  }

  public void assertHasExceptionHandler() {
    Assert.assertNotNull(exceptionHandler);
  }

  public void assertHasNoExceptionHandler() {
    Assert.assertNull(exceptionHandler);
  }

  public void assertHasEndHandler() {
    Assert.assertNotNull(endHandler);
  }

  public void assertHasNoEndHandler() {
    Assert.assertNull(endHandler);
  }

  @Override
  public TestReadStream<T> exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }

  public TestReadStream<T> untilPaused(Runnable action) {
    while (demand > 0L) {
      action.run();
    }
    return this;
  }

  public TestReadStream<T> untilResumed(Runnable action) {
    while (demand == 0L) {
      action.run();
    }
    return this;
  }

  public boolean emit(T first) {
    if (ended) {
      throw new IllegalStateException();
    }
    pending.add(first);
    checkPending();
    boolean writable = pending.size() <= highWaterMark;
    overflow |= !writable;
    return writable;
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
    if (pending.isEmpty()) {
      Handler<Void> handler = endHandler;
      if (handler != null) {
        handler.handle(null);
      }
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
  public TestReadStream<T> handler(Handler<T> handler) {
    this.itemHandler = handler;
    return this;
  }

  @Override
  public TestReadStream<T> pause() {
    demand = 0L;
    return this;
  }

  private void checkPending() {
    T elt;
    while (demand > 0L && (elt = pending.poll()) != null) {
      if (demand != Long.MAX_VALUE) {
        demand--;
      }
      Handler<T> handler = itemHandler;
      if (handler != null) {
        handler.handle(elt);
      }
    }
    if (pending.isEmpty() && overflow) {
      overflow = false;
      if (onResume != null) {
        onResume.run();
      }
    }
  }

  @Override
  public TestReadStream<T> fetch(long amount) {
    Arguments.require(amount > 0L, "Fetch amount must be > 0L");
    demand += amount;
    if (demand < 0L) {
      demand = Long.MAX_VALUE;
    }
    checkPending();
    if (pending.isEmpty() && ended) {
      Handler<Void> handler = endHandler;
      if (handler != null) {
        handler.handle(null);
      }
    }
    return this;
  }

  @Override
  public TestReadStream<T> resume() {
    return fetch(Long.MAX_VALUE);
  }

  @Override
  public TestReadStream<T> endHandler(Handler<Void> endHandler) {
    this.endHandler = endHandler;
    return this;
  }

  public void check() {
  }
}
