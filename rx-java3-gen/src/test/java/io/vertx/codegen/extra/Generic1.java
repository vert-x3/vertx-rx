package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;

@VertxGen
public interface Generic1<T> {

  T getValue();
  @Fluent
  Generic1<T> setValue(T value);

  @GenIgnore
  class Impl<T> implements Generic1<T> {
    T value;
    @Override
    public T getValue() {
      return value;
    }
    @Override
    public Impl<T> setValue(T value) {
      this.value = value;
      return this;
    }
  }
}
