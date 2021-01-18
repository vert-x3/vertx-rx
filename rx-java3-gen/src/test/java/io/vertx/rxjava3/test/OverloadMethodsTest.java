package io.vertx.rxjava3.test;

import io.reactivex.rxjava3.core.Single;
import io.vertx.test.core.VertxTestBase;
import org.junit.Ignore;
import org.junit.Test;

import io.vertx.rxjava3.codegen.rxjava3.MethodWithFuture;
import io.vertx.rxjava3.codegen.rxjava3.MethodWithFunction;

import java.util.function.Function;


public class OverloadMethodsTest extends VertxTestBase {

  @Test
  public void testSingleSuccess() {
    Single<String> single = Single.just("foobar");
    assertTrue(MethodWithFuture.isSucceeded(single));
    assertEquals("foobar", MethodWithFuture.getResult(single));
  }

  @Test
  public void testSingleFailure() {
    Throwable error = new Throwable();
    Single<String> single = Single.error(error);
    assertTrue(MethodWithFuture.isFailed(single));
    assertEquals(error, MethodWithFuture.getCause(single));
  }

  @Test
  public void testFunctionReturningSingleSuccess() {
    Function<String, Single<Integer>> strLen = s -> Single.just(s).map(String::length);
    assertTrue(MethodWithFunction.isSucceeded("foobar", strLen));
    assertEquals(6, (int)MethodWithFunction.getResult("foobar", strLen));
  }

  @Test
  public void testFunctionReturningSingleFailure() {
    Throwable error = new Throwable();
    Function<String, Single<Integer>> strLen = s -> Single.<String>error(error).map(String::length);
    assertTrue(MethodWithFunction.isFailed("foobar", strLen));
    assertEquals(error, MethodWithFunction.getCause("foobar", strLen));
  }

  @Ignore
  @Test
  public void testFunctionReturningSingleFunctionFailure() {
    RuntimeException error = new RuntimeException();
    Function<String, Single<Integer>> strLen = s -> {
      throw error;
    };
    assertTrue(MethodWithFunction.isFailed("foobar", strLen));
    assertEquals(error, MethodWithFunction.getCause("foobar", strLen));
  }
}
