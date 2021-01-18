package io.vertx.rxjava3.test;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.testmodel.NullableTCKImpl;
import io.vertx.codegen.testmodel.TestInterfaceImpl;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.rxjava3.codegen.rxjava3.MethodWithMultiCallback;
import io.vertx.rxjava3.codegen.rxjava3.MethodWithNullableTypeVariableParamByVoidArg;
import io.vertx.rxjava3.codegen.testmodel.NullableTCK;
import io.vertx.rxjava3.codegen.testmodel.TestInterface;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    Single<String> fut = obj.methodWithHandlerAsyncResultString(false);
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
    Completable failure = obj.methodWithHandlerAsyncResultVoid(true);
    AtomicInteger count = new AtomicInteger();
    failure.subscribe(Assert::fail, err -> {
      count.incrementAndGet();
    });
    assertEquals(1, count.getAndSet(0));
    Completable success = obj.methodWithHandlerAsyncResultVoid(false);
    success.subscribe(count::incrementAndGet, err -> fail());
    assertEquals(1, count.get());
  }

  @Test
  public void testMaybe() {
    NullableTCK obj = new NullableTCK(new NullableTCKImpl());
    List<String> result = new ArrayList<>();
    List<Throwable> failure = new ArrayList<>();
    AtomicInteger completions = new AtomicInteger();
    Maybe<String> maybeNotNull = obj.methodWithNullableStringHandlerAsyncResult(true);
    maybeNotNull.subscribe(result::add, failure::add, completions::incrementAndGet);
    assertEquals(Collections.singletonList("the_string_value"), result);
    assertEquals(Collections.emptyList(), failure);
    assertEquals(0, completions.get());
    result.clear();
    maybeNotNull = obj.methodWithNullableStringHandlerAsyncResult(false);
    maybeNotNull.subscribe(result::add, failure::add, completions::incrementAndGet);
    assertEquals(Collections.emptyList(), result);
    assertEquals(Collections.emptyList(), failure);
    assertEquals(1, completions.get());
    result.clear();
  }

  @Test
  public void testMultiCompletions() {
    MethodWithMultiCallback objectMethodWithMultiCompletable = MethodWithMultiCallback.newInstance(new io.vertx.codegen.rxjava3.MethodWithMultiCallback() {
      @Override
      public void multiCompletable(Handler<AsyncResult<Void>> handler) {
        handler.handle(Future.succeededFuture());
        handler.handle(Future.succeededFuture());
      }
      @Override
      public void multiMaybe(Handler<AsyncResult<@Nullable String>> handler) {
        handler.handle(Future.succeededFuture());
        handler.handle(Future.succeededFuture("foo"));
      }
      @Override
      public void multiSingle(Handler<AsyncResult<String>> handler) {
        handler.handle(Future.succeededFuture("foo"));
        handler.handle(Future.succeededFuture("foo"));
      }
    });
    AtomicInteger count = new AtomicInteger();
    objectMethodWithMultiCompletable.multiCompletable().subscribe(count::incrementAndGet);
    assertEquals(1, count.getAndSet(0));
    objectMethodWithMultiCompletable.multiMaybe().subscribe(s -> count.incrementAndGet(), err -> {}, count::incrementAndGet);
    assertEquals(1, count.getAndSet(0));
    objectMethodWithMultiCompletable.multiSingle().subscribe(s -> count.incrementAndGet());
    assertEquals(1, count.getAndSet(0));
  }

  @Test
  public void testNullableTypeVariableParamByVoidArg() {
    MethodWithNullableTypeVariableParamByVoidArg abc = MethodWithNullableTypeVariableParamByVoidArg.newInstance(handler -> handler.handle(Future.succeededFuture()));
    Maybe<Void> maybe = abc.doSomethingWithMaybeResult();
    AtomicInteger count = new AtomicInteger();
    maybe.subscribe(o -> fail(), err -> fail(err.getMessage()), count::incrementAndGet);
    assertEquals(1, count.get());
  }
}
