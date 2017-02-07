package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.rx.java.test.stream.BufferReadStream;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SimpleReadStream<T> implements ReadStream<T> {

  private Handler<Throwable> exceptionHandler;
  private Handler<T> handler;
  private Handler<Void> endHandler;

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

  public SimpleReadStream<T> emit(T... items) {
    for (T item : items) {
      handler.handle(item);
    }
    return this;
  }

  public void end(T... items) {
    emit(items);
    endHandler.handle(null);
  }

  public void end() {
    endHandler.handle(null);
  }

  @Override
  public SimpleReadStream<T> handler(Handler<T> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public SimpleReadStream<T> pause() {
    throw new UnsupportedOperationException();
  }

  @Override
  public SimpleReadStream<T> resume() {
    throw new UnsupportedOperationException();
  }

  @Override
  public SimpleReadStream<T> endHandler(Handler<Void> endHandler) {
    this.endHandler = endHandler;
    return this;
  }
}
