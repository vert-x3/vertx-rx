package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SimpleReadStream<T> implements ReadStream<T> {

  Handler<Throwable> exceptionHandler;
  Handler<T> handler;
  Handler<Void> endHandler;

  @Override
  public ReadStream<T> exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }

  @Override
  public ReadStream<T> handler(Handler<T> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public ReadStream<T> pause() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ReadStream<T> resume() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ReadStream<T> endHandler(Handler<Void> endHandler) {
    this.endHandler = endHandler;
    return this;
  }
}
