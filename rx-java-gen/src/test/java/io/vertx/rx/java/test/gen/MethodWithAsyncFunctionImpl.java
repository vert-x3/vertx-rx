package io.vertx.rx.java.test.gen;

import io.vertx.codegen.rxjava.MethodWithAsyncFunction;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.function.Function;

public class MethodWithAsyncFunctionImpl implements MethodWithAsyncFunction {
  @Override
  public void method(Function<String, Future<String>> fn, Handler<AsyncResult<String>> resultHandler) {
    fn.apply("hello").setHandler(resultHandler);
  }
}
