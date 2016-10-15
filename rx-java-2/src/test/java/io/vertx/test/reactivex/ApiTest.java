package io.vertx.test.reactivex;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.codegen.testmodel.TestInterfaceImpl;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.codegen.testmodel.TestInterface;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ApiTest {

  @Test
  public void testSingle() {
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

  @Test
  public void testCompletable() {
    TestInterface obj = new TestInterface(new TestInterfaceImpl());
    Completable failure = obj.methodWithHandlerAsyncResultVoidCompletable(true);
    AtomicInteger count = new AtomicInteger();
    failure.subscribe(Assert::fail, err -> {
      count.incrementAndGet();
    });
    assertEquals(1, count.getAndSet(0));
    Completable success = obj.methodWithHandlerAsyncResultVoidCompletable(false);
    success.subscribe(count::incrementAndGet, err -> fail());
    assertEquals(1, count.get());
  }
}
