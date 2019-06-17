package io.vertx.rx.java.test.gen;

import io.vertx.codegen.rxjava.MethodWithAsyncSupplier;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class MethodWithAsyncSupplierImpl implements MethodWithAsyncSupplier {

  @Override
  public void method(Handler<Future<String>> fn, Handler<AsyncResult<String>> resultHandler) {
    Future<String> fut = Future.future();
    fut.setHandler(resultHandler);
    fn.handle(fut);
  }
}
