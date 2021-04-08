package io.vertx.rxjava3.test;

import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Future;
import io.vertx.rxjava3.codegen.rxjava3.MethodWithFuture;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FutureTest {

  @Test
  public void testFuture() {
    io.vertx.codegen.rxjava3.MethodWithFuture delegate = new io.vertx.codegen.rxjava3.MethodWithFuture() {
    };
    Future<MethodWithFuture> ret = MethodWithFuture.withVertxGen(Single.just(new MethodWithFuture(delegate)));
    assertTrue(ret.succeeded());
    assertSame(ret.result().getDelegate(), delegate);
  }
}
