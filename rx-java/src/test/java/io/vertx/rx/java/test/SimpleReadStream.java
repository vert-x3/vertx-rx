package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.rx.java.test.stream.BufferReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SimpleReadStream<T> implements ReadStream<T> {

  public Handler<Throwable> exceptionHandler;
  public Handler<T> handler;
  public Handler<Void> endHandler;

  @Override
  public SimpleReadStream<T> exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
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
