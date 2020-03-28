package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;

@VertxGen
public interface Generic2<T, U> {

  T getValue1();
  @Fluent
  Generic2<T, U> setValue1(T value);
  U getValue2();
  @Fluent
  Generic2<T, U> setValue2(U value);

  @GenIgnore
  class Impl<T, U> implements Generic2<T, U> {
    T value1 = null;
    U value2 = null;
    @Override
    public T getValue1() {
      return value1;
    }
    @Override
    public Impl<T, U> setValue1(T value) {
      value1 = value;
      return this;
    }
    @Override
    public U getValue2() {
      return value2;
    }
    @Override
    public Impl<T, U> setValue2(U value) {
      value2 = value;
      return this;
    }
  }
}
