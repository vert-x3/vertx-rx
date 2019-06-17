package io.vertx.reactivex.test;

import io.reactivex.Single;
import io.vertx.reactivex.codegen.rxjava2.MethodWithAsyncFunction;
import io.vertx.reactivex.codegen.rxjava2.MethodWithAsyncSupplier;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

public class AsyncLambdaTest extends VertxTestBase {

  @Test
  public void methodWithAsyncFunction() {
    MethodWithAsyncFunction api = new MethodWithAsyncFunction(new MethodWithAsyncFunctionImpl());
    api.rxMethod(s -> Single.just(s + " francesco")).subscribe(res -> {
      assertEquals("hello francesco", res);
      testComplete();
    }, this::fail);
    await();
  }

  @Test
  public void methodWithAsyncSupplier() {
    MethodWithAsyncSupplier api = new MethodWithAsyncSupplier(new MethodWithAsyncSupplierImpl());
    api.rxMethod(() -> Single.just("francesco")).subscribe(res -> {
      assertEquals("francesco", res);
      testComplete();
    }, this::fail);
    await();
  }

}
