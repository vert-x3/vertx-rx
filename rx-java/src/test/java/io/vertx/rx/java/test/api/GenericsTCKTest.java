package io.vertx.rx.java.test.api;

import io.vertx.codegen.testmodel.GenericsTCKImpl;
import io.vertx.codegen.testmodel.RefedInterface1Impl;
import io.vertx.rx.java.test.AsyncResultChecker;
import io.vertx.rxjava.codegen.testmodel.GenericNullableRefedInterface;
import io.vertx.rxjava.codegen.testmodel.GenericRefedInterface;
import io.vertx.rxjava.codegen.testmodel.GenericsTCK;
import io.vertx.rxjava.codegen.testmodel.RefedInterface1;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GenericsTCKTest {

  final GenericsTCK obj = new GenericsTCK(new GenericsTCKImpl());

  @Test
  public void testMethodWithUserTypeParameterizedReturn() {
    GenericRefedInterface<RefedInterface1> gen = obj.methodWithUserTypeParameterizedReturn();
    RefedInterface1 refed = gen.getValue();
    assertEquals("foo", refed.getString());
    refed = new RefedInterface1(new RefedInterface1Impl());
    refed.setString("the_string");
    gen.setValue(refed);
    refed = gen.getValue();
    assertEquals("the_string", refed.getString());
  }

  @Test
  public void testMethodWithGenericNullableApiHandler() throws Exception {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerGenericNullableApi(false, checker.resultHandler(ret -> {
      assertEquals(null, ret.getValue());
    }));
  }

  @Test
  public void testMethodWithGenericNullableApiHandlerAsyncResult() throws Exception {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultGenericNullableApi(false, checker.resultHandler(ret -> {
      assertEquals(null, ret.result().getValue());
    }));
  }

  @Test
  public void testMethodWithGenericNullableApiReturn() throws Exception {
    GenericNullableRefedInterface<RefedInterface1> ret = obj.methodWithGenericNullableApiReturn(false);
    assertEquals(null, ret.getValue());
  }
}
