package io.vertx.reactivex.test;

import io.reactivex.Maybe;
import io.vertx.reactivex.codegen.extra.InheritNullableFutureReturn;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NullReturnTypeInheritanceCovarianceTest {

  @Test
  public void testMaybe() {
    InheritNullableFutureReturn instance = InheritNullableFutureReturn.create();
    Maybe<String> maybe = instance.rxMethod("foo");
    assertEquals("foo", maybe.blockingGet());
    maybe = instance.rxMethod(null);
    assertEquals(null, maybe.blockingGet());
  }
}
