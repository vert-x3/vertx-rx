package io.vertx.rx.java.test.gen;

import io.vertx.codegen.testmodel.GenericsTCKImpl;
import io.vertx.codegen.testmodel.RefedInterface1Impl;
import io.vertx.rxjava.codegen.testmodel.GenericNullableRefedInterface;
import io.vertx.rxjava.codegen.testmodel.GenericRefedInterface;
import io.vertx.rxjava.codegen.testmodel.GenericsTCK;
import io.vertx.rxjava.codegen.testmodel.RefedInterface1;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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

  @Test
  public void testMethodWithClassTypeParameterizedReturn() throws Exception {
    GenericRefedInterface<RefedInterface1> refed = obj.methodWithClassTypeParameterizedReturn(RefedInterface1.class);
    RefedInterface1 a = refed.getValue();
    assertEquals("foo", a.getString());
  }

  @Test
  public void testMethodWithParamInferedReturn() throws Exception {
    GenericRefedInterface<RefedInterface1> refed = obj.methodWithUserTypeParameterizedReturn();
    GenericRefedInterface<RefedInterface1> ret = obj.methodWithParamInferedReturn(refed);
    assertSame(refed.getDelegate(), ret.getDelegate());
  }

  @Test
  public void testMethodWithHandlerParamInfered() throws Exception {
    GenericRefedInterface<RefedInterface1> refed = obj.methodWithUserTypeParameterizedReturn();
    obj.methodWithHandlerParamInfered(refed, ret -> {
      assertSame(refed.getDelegate(), ret.getDelegate());
    });
  }

  @Test
  public void testMethodWithHandlerAsyncResultParamInfered() throws Exception {
    GenericRefedInterface<RefedInterface1> refed = obj.methodWithUserTypeParameterizedReturn();
    obj.methodWithHandlerAsyncResultParamInfered(refed, ret -> {
      assertSame(refed.getDelegate(), ret.result().getDelegate());
    });
  }

  @Test
  public void testMethodWithClassTypeReturn() throws Exception {
    RefedInterface1 refed = obj.methodWithClassTypeReturn(RefedInterface1.class);
    assertEquals("foo", refed.getString());
  }

  @Test
  public void testMethodWithClassTypeHandler() throws Exception {
    AtomicReference<RefedInterface1> refed = new AtomicReference<>();
    obj.methodWithClassTypeHandler(RefedInterface1.class, refed::set);
    assertEquals("foo", refed.get().getString());
  }

  @Test
  public void testMethodWithClassTypeHandlerAsyncResult() throws Exception {
    AtomicReference<RefedInterface1> refed = new AtomicReference<>();
    obj.methodWithClassTypeHandlerAsyncResult(RefedInterface1.class, ar -> refed.set(ar.result()));
    assertEquals("foo", refed.get().getString());
  }
}
