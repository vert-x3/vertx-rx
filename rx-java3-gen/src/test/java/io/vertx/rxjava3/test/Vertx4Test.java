package io.vertx.rxjava3.test;

import io.vertx.rxjava3.codegen.vertx4.TestModel;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Vertx4Test extends VertxTestBase {

  @Test
  public void testFuture1() {
    TestModel
      .futureMethod1()
      .subscribe(v -> {
      assertTrue(v);
      testComplete();
    });
    await();
  }

  @Test
  public void testFuture2() {
    TestModel
      .futureMethod2("hello world")
      .subscribe(v -> {
        assertEquals(11, (int)v);
        testComplete();
      });
    await();
  }
}
