package io.vertx.codegen.rxjava;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

@VertxGen
public interface MethodWithAsyncSupplier {

  void method(Handler<Future<String>> fn, Handler<AsyncResult<String>> resultHandler);

}
