package io.vertx.reactivex.test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StreamTest {

  @Test
  public void testStream() {
    io.vertx.codegen.extra.IterableWithStreamMethod bareInstance = new io.vertx.codegen.extra.IterableWithStreamMethod.Impl();
    io.vertx.reactivex.codegen.extra.IterableWithStreamMethod rxInstance = io.vertx.reactivex.codegen.extra.IterableWithStreamMethod.newInstance(bareInstance);
    assertEquals(2, rxInstance.stream().count());
  }
}
