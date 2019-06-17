package io.vertx.codegen.rxjava2;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.function.Function;

@VertxGen
public interface MethodWithAsyncFunction {

  void method(Function<String, Future<String>> fn, Handler<AsyncResult<String>> resultHandler);

}
