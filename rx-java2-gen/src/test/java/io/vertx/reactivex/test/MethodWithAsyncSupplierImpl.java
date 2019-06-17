package io.vertx.reactivex.test;

import io.vertx.codegen.rxjava2.MethodWithAsyncFunction;
import io.vertx.codegen.rxjava2.MethodWithAsyncSupplier;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.function.Function;

public class MethodWithAsyncSupplierImpl implements MethodWithAsyncSupplier {

  @Override
  public void method(Handler<Future<String>> fn, Handler<AsyncResult<String>> resultHandler) {
    Future<String> fut = Future.future();
    fut.setHandler(resultHandler);
    fn.handle(fut);
  }
}
