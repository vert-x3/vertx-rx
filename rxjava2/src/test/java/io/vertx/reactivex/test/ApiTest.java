package io.vertx.reactivex.test;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.vertx.codegen.testmodel.NullableTCKImpl;
import io.vertx.codegen.testmodel.TestInterfaceImpl;
import io.vertx.reactivex.codegen.testmodel.NullableTCK;
import io.vertx.reactivex.codegen.testmodel.TestInterface;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ApiTest {

  @Test
  public void testSingle() {
    TestInterface obj = new TestInterface(new TestInterfaceImpl());
    Single<String> fut = obj.rxMethodWithHandlerAsyncResultString(false);
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
    Completable failure = obj.rxMethodWithHandlerAsyncResultVoid(true);
    AtomicInteger count = new AtomicInteger();
    failure.subscribe(Assert::fail, err -> {
      count.incrementAndGet();
    });
    assertEquals(1, count.getAndSet(0));
    Completable success = obj.rxMethodWithHandlerAsyncResultVoid(false);
    success.subscribe(count::incrementAndGet, err -> fail());
    assertEquals(1, count.get());
  }

  @Test
  public void testMaybe() {
    NullableTCK obj = new NullableTCK(new NullableTCKImpl());
    List<String> result = new ArrayList<>();
    List<Throwable> failure = new ArrayList<>();
    AtomicInteger completions = new AtomicInteger();
    Maybe<String> maybeNotNull = obj.rxMethodWithNullableStringHandlerAsyncResult(true);
    maybeNotNull.subscribe(result::add, failure::add, completions::incrementAndGet);
    assertEquals(Collections.singletonList("the_string_value"), result);
    assertEquals(Collections.emptyList(), failure);
    assertEquals(0, completions.get());
    result.clear();
    maybeNotNull = obj.rxMethodWithNullableStringHandlerAsyncResult(false);
    maybeNotNull.subscribe(result::add, failure::add, completions::incrementAndGet);
    assertEquals(Collections.emptyList(), result);
    assertEquals(Collections.emptyList(), failure);
    assertEquals(1, completions.get());
    result.clear();
  }
}
