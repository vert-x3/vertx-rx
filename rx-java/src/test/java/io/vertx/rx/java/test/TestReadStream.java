package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestReadStream<T> implements ReadStream<T> {

  private Handler<Throwable> exceptionHandler;
  private Handler<T> handler;
  private Handler<Void> endHandler;
  private boolean paused;

  public boolean isPaused() {
    return paused;
  }

  public void emit(T t) {
    handler.handle(t);
  }

  @Override
  public TestReadStream<T> exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }

  @Override
  public TestReadStream<T> handler(Handler<T> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public TestReadStream<T> pause() {
    paused = true;
    return this;
  }

  @Override
  public TestReadStream<T> resume() {
    paused = false;
    return this;
  }

  @Override
  public TestReadStream<T> endHandler(Handler<Void> endHandler) {
    this.endHandler = endHandler;
    return this;
  }
}
