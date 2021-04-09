package io.vertx.reactivex.test;

import io.vertx.core.Future;
import io.vertx.reactivex.codegen.rxjava2.MethodWithFuture;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FutureTest {

  @Test
  public void testFuture() {
    io.vertx.codegen.rxjava2.MethodWithFuture delegate = new io.vertx.codegen.rxjava2.MethodWithFuture() {
    };
    Future<MethodWithFuture> ret = MethodWithFuture.withVertxGen(Future.succeededFuture(new MethodWithFuture(delegate)));
    assertTrue(ret.succeeded());
    assertSame(ret.result().getDelegate(), delegate);
  }
}
