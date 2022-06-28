package io.vertx.lang.rx;

import io.vertx.core.Handler;

import java.util.Objects;
import java.util.function.Function;

public class DelegatingHandler<U, V> implements Handler<U> {

  private final Handler<V> handler;
  private final Function<U, V> mapper;

  public DelegatingHandler(Handler<V> handler, Function<U, V> mapper) {
    this.handler = Objects.requireNonNull(handler);
    this.mapper = Objects.requireNonNull(mapper);
  }

  @Override
  public void handle(U event) {
    handler.handle(mapper.apply(event));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DelegatingHandler<?, ?> that = (DelegatingHandler<?, ?>) o;
    return handler.equals(that.handler);
  }

  @Override
  public int hashCode() {
    return handler.hashCode();
  }
}
