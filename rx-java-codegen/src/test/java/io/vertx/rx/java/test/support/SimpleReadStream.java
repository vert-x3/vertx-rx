package io.vertx.rx.java.test.support;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

import java.util.Collections;
import java.util.LinkedList;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SimpleReadStream<T> implements ReadStream<T> {

  private enum Kind {
    PAUSE,
    RESUME
  }

  private static class Expect {
    final Kind kind;
    final Runnable action;
    Expect(Kind kind, Runnable action) {
      this.kind = kind;
      this.action = action;
    }
    Expect(Kind kind) {
      this.kind = kind;
      this.action = null;
    }

    @Override
    public String toString() {
      return "Expected[kind=" + kind.name() + "]";
    }
  }

  private static final Expect RESUME = new Expect(Kind.RESUME);
  private static final Expect PAUSE = new Expect(Kind.PAUSE);

  private Handler<Throwable> exceptionHandler;
  private Handler<T> handler;
  private Handler<Void> endHandler;
  private boolean paused;
  private LinkedList<Expect> expects = new LinkedList<>();

  public void assertPaused() {
    assertTrue(paused);
  }

  public void assertResumed() {
    assertTrue(!paused);
  }

  public SimpleReadStream<T> expectPause() {
    expects.add(PAUSE);
    return this;
  }

  public SimpleReadStream<T> expectPause(Runnable action) {
    expects.add(new Expect(Kind.PAUSE, action));
    return this;
  }

  public SimpleReadStream<T> expectResume() {
    expects.add(RESUME);
    return this;
  }

  public SimpleReadStream<T> expectResume(Runnable action) {
    expects.add(new Expect(Kind.RESUME, action));
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
    assertNotNull(handler);
  }

  public void assertHasNoItemHandler() {
    assertNull(handler);
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
    while (!paused) {
      action.run();
    }
    return this;
  }

  public SimpleReadStream<T> untilResumed(Runnable action) {
    while (paused) {
      action.run();
    }
    return this;
  }

  public SimpleReadStream<T> emit(T... items) {
    for (T item : items) {
      handler.handle(item);
    }
    return this;
  }

  public void end() {
    endHandler.handle(null);
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
    this.handler = handler;
    return this;
  }

  private void expect(Kind kind) {
    Expect next = expects.poll();
    if (next == null || next.kind != kind) {
      throw new IllegalStateException();
    }
    if (next.action != null) {
      next.action.run();
    }
  }

  @Override
  public SimpleReadStream<T> pause() {
    expect(Kind.PAUSE);
    paused = true;
    return this;
  }

  @Override
  public SimpleReadStream<T> resume() {
    expect(Kind.RESUME);
    paused = false;
    return this;
  }

  @Override
  public SimpleReadStream<T> endHandler(Handler<Void> endHandler) {
    this.endHandler = endHandler;
    return this;
  }

  public void check() {
    assertEquals(Collections.emptyList(), expects);
  }
}
