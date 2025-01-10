package io.vertx.rx.java.test;

import io.vertx.rxjava.codegen.extra.DeprecatedMethod;
import io.vertx.rxjava.codegen.extra.DeprecatedType;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@SuppressWarnings("deprecation")
public class DeprecationTest {

  @Test
  public void testDeprecatedType() {
    assertNotNull(DeprecatedType.class.getAnnotation(Deprecated.class));
  }

  @Test
  public void testDeprecatedMethod() throws Exception {
    assertNotNull(DeprecatedMethod.class.getMethod("foo").getAnnotation(Deprecated.class));
  }
}
