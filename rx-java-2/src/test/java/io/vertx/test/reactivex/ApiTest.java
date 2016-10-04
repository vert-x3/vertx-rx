package io.vertx.test.reactivex;

import io.reactivex.Single;
import io.vertx.codegen.testmodel.TestInterfaceImpl;
import io.vertx.reactivex.codegen.testmodel.TestInterface;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ApiTest {

  @Test
  public void testFoo() {
    TestInterface obj = new TestInterface(new TestInterfaceImpl());
    Single<String> fut = obj.methodWithHandlerAsyncResultStringSingle(false);
    AtomicInteger result = new AtomicInteger();
    AtomicInteger fail = new AtomicInteger();
    fut.subscribe(res -> {
      result.getAndIncrement();
    }, err -> {
      fail.getAndIncrement();
    });
    assertEquals(1, result.get());
    assertEquals(0, fail.get());
  }
}
