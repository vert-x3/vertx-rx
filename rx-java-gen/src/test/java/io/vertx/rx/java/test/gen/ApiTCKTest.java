package io.vertx.rx.java.test.gen;

import io.vertx.codegen.testmodel.RefedInterface1Impl;
import io.vertx.codegen.testmodel.TestDataObject;
import io.vertx.codegen.testmodel.TestInterfaceImpl;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.codegen.testmodel.Factory;
import io.vertx.rxjava.codegen.testmodel.RefedInterface1;
import io.vertx.rxjava.codegen.testmodel.RefedInterface2;
import io.vertx.rxjava.codegen.testmodel.TestInterface;
import com.acme.rxjava.pkg.MyInterface;
import com.acme.rxjava.pkg.sub.SubInterface;
import io.vertx.test.core.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import rx.Observable;
import rx.Single;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ApiTCKTest {

  final TestInterface obj = new TestInterface(new TestInterfaceImpl());

  @Test
  public void testMethodWithBasicParams() {
    obj.methodWithBasicParams((byte) 123, (short) 12345, 1234567, 1265615234l, 12.345f, 12.34566d, true, 'X', "foobar");
  }

  @Test
  public void testMethodWithBasicBoxedParams() {
    obj.methodWithBasicBoxedParams((byte) 123, (short) 12345, 1234567, 1265615234l, 12.345f, 12.34566d, true, 'X');
  }

  @Test
  public void testMethodWithHandlerBasicTypes() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerBasicTypes(
        checker.expectedResult((byte) 123),
        checker.expectedResult((short) 12345),
        checker.expectedResult(1234567),
        checker.expectedResult(1265615234l),
        checker.expectedResult(12.345f),
        checker.expectedResult(12.34566d),
        checker.expectedResult(true),
        checker.expectedResult('X'),
        checker.expectedResult("quux!")
    );
    assertEquals(9, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultBasicTypes() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultByte(false).onComplete(checker.asyncExpectedResult((byte) 123));
    obj.methodWithHandlerAsyncResultShort(false).onComplete(checker.asyncExpectedResult((short) 12345));
    obj.methodWithHandlerAsyncResultInteger(false).onComplete(checker.asyncExpectedResult(1234567));
    obj.methodWithHandlerAsyncResultLong(false).onComplete(checker.asyncExpectedResult(1265615234l));
    obj.methodWithHandlerAsyncResultFloat(false).onComplete(checker.asyncExpectedResult(12.345f));
    obj.methodWithHandlerAsyncResultDouble(false).onComplete(checker.asyncExpectedResult(12.34566d));
    obj.methodWithHandlerAsyncResultBoolean(false).onComplete(checker.asyncExpectedResult(true));
    obj.methodWithHandlerAsyncResultCharacter(false).onComplete(checker.asyncExpectedResult('X'));
    obj.methodWithHandlerAsyncResultString(false).onComplete(checker.asyncExpectedResult("quux!"));
    assertEquals(9, checker.count);
    checker.count = 0;
    obj.methodWithHandlerAsyncResultByte(true).onComplete(checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultShort(true).onComplete(checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultInteger(true).onComplete(checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultLong(true).onComplete(checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultFloat(true).onComplete(checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultDouble(true).onComplete(checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultBoolean(true).onComplete(checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultCharacter(true).onComplete(checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultString(true).onComplete(checker.failureAsserter("foobar!"));
    assertEquals(9, checker.count);
  }

  @Test
  public void testMethodWithFutureBasicTypes() throws Exception {
    assertEquals((byte) 123, (byte) get(obj.rxMethodWithHandlerAsyncResultByte(false)));
    assertEquals((short) 12345, (short) get(obj.rxMethodWithHandlerAsyncResultShort(false)));
    assertEquals(1234567, (int) get(obj.rxMethodWithHandlerAsyncResultInteger(false)));
    assertEquals(1265615234l, (long) get(obj.rxMethodWithHandlerAsyncResultLong(false)));
    assertEquals(12.345f, (float) get(obj.rxMethodWithHandlerAsyncResultFloat(false)), 0);
    assertEquals(12.34566d, (double) get(obj.rxMethodWithHandlerAsyncResultDouble(false)), 0);
    assertEquals(true, get(obj.rxMethodWithHandlerAsyncResultBoolean(false)));
    assertEquals('X', (char) get(obj.rxMethodWithHandlerAsyncResultCharacter(false)));
    assertEquals("quux!", get(obj.rxMethodWithHandlerAsyncResultString(false)));
    assertFailure("foobar!", obj.rxMethodWithHandlerAsyncResultByte(true));
    assertFailure("foobar!", obj.rxMethodWithHandlerAsyncResultShort(true));
    assertFailure("foobar!", obj.rxMethodWithHandlerAsyncResultInteger(true));
    assertFailure("foobar!", obj.rxMethodWithHandlerAsyncResultLong(true));
    assertFailure("foobar!", obj.rxMethodWithHandlerAsyncResultFloat(true));
    assertFailure("foobar!", obj.rxMethodWithHandlerAsyncResultDouble(true));
    assertFailure("foobar!", obj.rxMethodWithHandlerAsyncResultBoolean(true));
    assertFailure("foobar!", obj.rxMethodWithHandlerAsyncResultCharacter(true));
    assertFailure("foobar!", obj.rxMethodWithHandlerAsyncResultString(true));
  }

  static <T> T get(Observable<T> future) throws Exception {
    return future.toBlocking().first();
  }

  static <T> T get(Single<T> future) throws Exception {
    return future.toBlocking().value();
  }

  private <T> void assertFailure(String message, Observable<T> future) throws Exception {
    try {
      future.toBlocking().first();
    } catch (Exception e) {
//      assertEquals(message, e.getCause().getMessage());
    }
  }

  private <T> void assertFailure(String message, Single<T> future) throws Exception {
    try {
      future.toBlocking().value();
    } catch (Exception e) {
//      assertEquals(message, e.getCause().getMessage());
    }
  }

  @Test
  public void testMethodWithUserTypes() {
    RefedInterface1 refed = new RefedInterface1(new RefedInterface1Impl());
    refed.setString("aardvarks");
    obj.methodWithUserTypes(refed);
  }

  @Test
  public void testObjectParam() {
    obj.methodWithObjectParam("JsonObject", new JsonObject().put("foo", "hello").put("bar", 123));
    obj.methodWithObjectParam("JsonArray", new JsonArray().add("foo").add("bar").add("wib"));
  }

  @Test
  public void testDataObjectParam() {
    TestDataObject options = new TestDataObject();
    options.setFoo("hello");
    options.setBar(123);
    options.setWibble(1.23);
    obj.methodWithDataObjectParam(options);
  }

  @Test
  public void testMethodWithHandlerDataObject() {
    TestDataObject dataObject = new TestDataObject();
    dataObject.setFoo("foo");
    dataObject.setBar(123);
    AtomicInteger count = new AtomicInteger();
    obj.methodWithHandlerDataObject(it -> {
      assertEquals(dataObject.getFoo(), it.getFoo());
      assertEquals(dataObject.getBar(), it.getBar());
      count.incrementAndGet();
    });
    assertEquals(1, count.get());
  }

  @Test
  public void testMethodWithHandlerAsyncResultDataObject() {
    TestDataObject dataObject = new TestDataObject();
    dataObject.setFoo("foo");
    dataObject.setBar(123);
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultDataObject(false).onComplete(result -> {
      assertTrue(result.succeeded());
        assertFalse(result.failed());
        TestDataObject res = result.result();
        assertEquals(dataObject.getFoo(), res.getFoo());
        assertNull(result.cause());
        checker.count++;
    });
    obj.methodWithHandlerAsyncResultDataObject(true).onComplete(result -> {
      checker.assertAsyncFailure("foobar!", result);
    });
    assertEquals(2, checker.count);
  }

  @Test
  public void testMethodWithHandlerStringReturn() {
    Handler<String> handler = obj.methodWithHandlerStringReturn("the-result");
    handler.handle("the-result");
    boolean failed = false;
    try {
      handler.handle("not-expected");
    }  catch (Throwable ignore) {
      failed = true;
    }
    assertTrue(failed);
  }

  @Test
  public void testMethodWithHandlerGenericReturn() {
    AtomicReference<Object> result = new AtomicReference<>();
    obj.<String>methodWithHandlerGenericReturn(result::set).handle("the-result");
    assertEquals("the-result", result.get());
    obj.<TestInterface>methodWithHandlerGenericReturn(result::set).handle(obj);
    assertEquals(obj, result.get());
  }

  @Test
  public void testMethodWithHandlerVertxGenReturn() {
    obj.<String>methodWithHandlerVertxGenReturn("the-gen-result").handle(new RefedInterface1(new RefedInterface1Impl().setString("the-gen-result")));
  }

  @Test
  public void testMethodWithHandlerAsyncResultStringReturn() {
    Handler<AsyncResult<String>> succeedingHandler = obj.methodWithHandlerAsyncResultStringReturn("the-result", false);
    succeedingHandler.handle(Future.succeededFuture("the-result"));
    boolean failed = false;
    try {
      succeedingHandler.handle(Future.succeededFuture("not-expected"));
    }  catch (Throwable ignore) {
      failed = true;
    }
    assertTrue(failed);
    Handler<AsyncResult<String>> failingHandler = obj.methodWithHandlerAsyncResultStringReturn("an-error", true);
    failingHandler.handle(Future.failedFuture("an-error"));
    failed = false;
    try {
      failingHandler.handle(Future.succeededFuture("whatever"));
    } catch (Throwable ignore) {
      failed = true;
    }
    assertTrue(failed);
  }

  @Test
  public void testMethodWithHandlerAsyncResultGenericReturn() {
    AtomicReference<Object> result = new AtomicReference<>();
    Handler<AsyncResult<Object>> succeedingHandler = obj.methodWithHandlerAsyncResultGenericReturn(ar -> {
        result.set(ar.succeeded() ? ar.result() : ar.cause());
    });
    succeedingHandler.handle(Future.succeededFuture("the-result"));
    assertEquals("the-result", result.get());
    succeedingHandler.handle(Future.succeededFuture(obj));
    assertEquals(obj, result.get());
  }

  @Test
  public void testMethodWithHandlerAsyncResultVertxGenReturn() {
    obj.<String>methodWithHandlerAsyncResultVertxGenReturn("the-gen-result", false).handle(Future.succeededFuture(new RefedInterface1(new RefedInterface1Impl().setString("the-gen-result"))));
    obj.<String>methodWithHandlerAsyncResultVertxGenReturn("it-failed-dude", true).handle(Future.failedFuture(new Exception("it-failed-dude")));
  }

  @Test
  public void testMethodWithHandlerUserTypes() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerUserTypes(checker.<RefedInterface1>resultHandler(it -> assertEquals("echidnas", it.getString())));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithConcreteHandlerUserTypeSubtype() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithConcreteHandlerUserTypeSubtype(Factory.createConcreteHandlerUserType(checker.<RefedInterface1>resultHandler(it -> assertEquals("echidnas", it.getString()))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithAbstractHandlerUserTypeSubtype() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithAbstractHandlerUserTypeSubtype(Factory.createAbstractHandlerUserType(checker.<RefedInterface1>resultHandler(it -> assertEquals("echidnas", it.getString()))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithAbstractHandlerUserTypeSubtypeExtension() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithConcreteHandlerUserTypeSubtypeExtension(Factory.createConcreteHandlerUserTypeExtension(checker.<RefedInterface1>resultHandler(it -> assertEquals("echidnas", it.getString()))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultUserTypes() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultUserTypes().onComplete(checker.<RefedInterface1>asyncResultHandler(it -> assertEquals("cheetahs", it.getString())));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureUserTypes() throws Exception {
    RefedInterface1 result = get(obj.rxMethodWithHandlerAsyncResultUserTypes());
    assertEquals("cheetahs", result.getString());
  }

  @Test
  public void testMethodWithHandlerVoid() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerVoid(checker.<Void>resultHandler(Assert::assertNull));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultVoid() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultVoid(false).onComplete(checker.<Void>asyncResultHandler(Assert::assertNull));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureVoid() throws Exception {
    Void result = get(obj.rxMethodWithHandlerAsyncResultVoid(false));
    assertEquals(null, result);
  }

  @Test
  public void testMethodWithHandlerAsyncResultVoidFails() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultVoid(true).onComplete(checker.<Void>failureAsserter("foo!"));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureVoidFails() throws Exception {
    assertFailure("foo!", obj.rxMethodWithHandlerAsyncResultVoid(true));
  }

  @Test
  public void testMethodWithHandlerThrowable() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerThrowable(checker.<Throwable>resultHandler(t -> {
      assertTrue(t instanceof VertxException);
      assertEquals("cheese!", t.getMessage());
    }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithGenericParam() {
    obj.methodWithGenericParam("String", "foo");
    obj.methodWithGenericParam("Ref", new RefedInterface1Impl().setString("bar"));
    obj.methodWithGenericParam("JsonObject", new JsonObject().put("foo", "hello").put("bar", 123));
    obj.methodWithGenericParam("JsonArray", new JsonArray().add("foo").add("bar").add("wib"));
  }

  @Test
  public void testMethodWithGenericHandler() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithGenericHandler("String", checker.expectedResult("foo"));
    obj.methodWithGenericHandler("Ref", checker.<RefedInterface1Impl>resultHandler(it -> assertEquals("bar", it.getString())));
    obj.methodWithGenericHandler("JsonObject", checker.expectedResult(new JsonObject().put("foo", "hello").put("bar", 123)));
    obj.methodWithGenericHandler("JsonArray", checker.expectedResult(new JsonArray().add("foo").add("bar").add("wib")));
    assertEquals(4, checker.count);
  }

  @Test
  public void testMethodWithGenericHandlerAsyncResult() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithGenericHandlerAsyncResult("String").onComplete(checker.asyncExpectedResult("foo"));
    obj.<RefedInterface1Impl>methodWithGenericHandlerAsyncResult("Ref").onComplete(checker.asyncResultHandler(it -> assertEquals("bar", it.getString())));
    obj.methodWithGenericHandlerAsyncResult("JsonObject").onComplete(checker.asyncExpectedResult(new JsonObject().put("foo", "hello").put("bar", 123)));
    obj.methodWithGenericHandlerAsyncResult("JsonArray").onComplete(checker.asyncExpectedResult(new JsonArray().add("foo").add("bar").add("wib")));
    assertEquals(4, checker.count);
  }

  @Test
  public void testMethodWithGenericObservable() throws Exception {
    assertEquals("foo", get(obj.rxMethodWithGenericHandlerAsyncResult("String")));
    RefedInterface1Impl ref = get(obj.<RefedInterface1Impl>rxMethodWithGenericHandlerAsyncResult("Ref"));
    assertEquals("bar", ref.getString());
    assertEquals(new JsonObject().put("foo", "hello").put("bar", 123), get(obj.rxMethodWithGenericHandlerAsyncResult("JsonObject")));
    assertEquals(new JsonArray().add("foo").add("bar").add("wib"), get(obj.rxMethodWithGenericHandlerAsyncResult("JsonArray")));
  }

  @Test
  public void testBasicReturns() {
    assertEquals(123, obj.methodWithByteReturn());
    assertEquals(12345, obj.methodWithShortReturn());
    assertEquals(12345464, obj.methodWithIntReturn());
    assertEquals(65675123, obj.methodWithLongReturn());
    assertEquals(1.23f, obj.methodWithFloatReturn(), 0);
    assertEquals(3.34535, obj.methodWithDoubleReturn(), 0);
    assertEquals(true, obj.methodWithBooleanReturn());
    assertEquals('Y', obj.methodWithCharReturn());
    assertEquals("orangutan", obj.methodWithStringReturn());
  }

  @Test
  public void testVertxGenReturn() {
    RefedInterface1 r = obj.methodWithVertxGenReturn();
    assertEquals("chaffinch", r.getString());
  }

  @Test
  public void testVertxGenNullReturn() {
    RefedInterface1 r = obj.methodWithVertxGenNullReturn();
    assertNull(r);
  }

  @Test
  public void testAbstractVertxGenReturn() {
    RefedInterface2 r = obj.methodWithAbstractVertxGenReturn();
    assertEquals("abstractchaffinch", r.getString());
  }

  @Test
  public void testDataObjectReturn() {
    TestDataObject r = obj.methodWithDataObjectReturn();
    assertEquals("foo", r.getFoo());
    assertEquals(123, r.getBar());
  }

  @Test
  public void testDataObjectNullReturn() {
    TestDataObject r = obj.methodWithDataObjectNullReturn();
    assertNull(r);
  }

  @Test
  public void testOverloadedMethods() {
    RefedInterface1 refed = new RefedInterface1(new RefedInterface1Impl());
    refed.setString("dog");
    assertEquals("meth1", obj.overloadedMethod("cat", refed));
    AtomicBoolean called = new AtomicBoolean(false);
    assertEquals("meth2", obj.overloadedMethod("cat", refed, 12345, it -> { assertEquals("giraffe", it); called.set(true); }));
    assertTrue(called.getAndSet(false));
    assertEquals("meth3", obj.overloadedMethod("cat", it -> { assertEquals("giraffe", it); called.set(true); }));
    assertTrue(called.getAndSet(false));
    assertEquals("meth4", obj.overloadedMethod("cat", refed, it -> { assertEquals("giraffe", it); called.set(true); }));
    assertTrue(called.get());
  }

  @Test
  public void testSuperInterfaces() {
    obj.superMethodWithBasicParams((byte) 123, (short) 12345, 1234567, 1265615234l, 12.345f, 12.34566d, true, 'X', "foobar");
    obj.otherSuperMethodWithBasicParams((byte) 123, (short) 12345, 1234567, 1265615234l, 12.345f, 12.34566d, true, 'X', "foobar");
  }

  @Test
  public void testMethodWithGenericReturn() {
    Object ret = obj.methodWithGenericReturn("JsonObject");
    assertTrue("Was expecting " + ret + " to implement JsonObject", ret instanceof JsonObject);
    assertEquals(new JsonObject().put("foo", "hello").put("bar", 123), ret);
    ret = obj.methodWithGenericReturn("JsonArray");
    assertTrue("Was expecting " + ret + " to implement JsonArray", ret instanceof JsonArray);
    assertEquals(new JsonArray().add("foo").add("bar").add("wib"), ret);
  }

  @Test
  public void testFluentMethod() {
    TestInterface ret = obj.fluentMethod("bar");
    assertSame(obj, ret);
  }

  @Test
  public void testStaticFactoryMethod() {
    RefedInterface1 ret = TestInterface.staticFactoryMethod("bar");
    assertEquals("bar", ret.getString());
  }

  @Test
  public void testMethodWithCachedReturn() {
    RefedInterface1 ret1 = obj.methodWithCachedReturn("bar");
    assertEquals("bar", ret1.getString());
    RefedInterface1 ret2 = obj.methodWithCachedReturn("bar");
    assertSame(ret1, ret2);
    RefedInterface1 ret3 = obj.methodWithCachedReturn("bar");
    assertSame(ret1, ret3);
  }

  @Test
  public void testMethodWithCachedListReturn() {
    List<RefedInterface1> ret1 = obj.methodWithCachedListReturn();
    assertEquals(2, ret1.size());
    assertEquals("foo", ret1.get(0).getString());
    assertEquals("bar", ret1.get(1).getString());
    List<RefedInterface1> ret2 = obj.methodWithCachedListReturn();
    assertSame(ret1, ret2);
    List<RefedInterface1> ret3 = obj.methodWithCachedListReturn();
    assertSame(ret1, ret3);
  }

  @Test
  public void testMethodWithCachedReturnPrimitive() {
    int value = TestUtils.randomInt();
    assertEquals(value, obj.methodWithCachedReturnPrimitive(value));
    assertEquals(value, obj.methodWithCachedReturnPrimitive(value));
  }

  @Test
  public void testJsonReturns() {
    JsonObject ret1 = obj.methodWithJsonObjectReturn();
    assertEquals(new JsonObject().put("cheese", "stilton"), ret1);
    JsonArray ret2 = obj.methodWithJsonArrayReturn();
    assertEquals(new JsonArray().add("socks").add("shoes"), ret2);
  }

  @Test
  public void testNullJsonReturns() {
    JsonObject ret1 = obj.methodWithNullJsonObjectReturn();
    assertEquals(null, ret1);
    JsonArray ret2 = obj.methodWithNullJsonArrayReturn();
    assertEquals(null, ret2);
  }

  @Test
  public void testJsonParams() {
    obj.methodWithJsonParams(
        new JsonObject().put("cat", "lion").put("cheese", "cheddar"),
        new JsonArray().add("house").add("spider")
    );
  }

  @Test
  public void testNullJsonParams() {
    obj.methodWithNullJsonParams(
        null,
        null
    );
  }

  @Test
  public void testJsonHandlerParams() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerJson(
        checker.<JsonObject>resultHandler(it -> assertEquals(new JsonObject().put("cheese", "stilton"), it)),
        checker.<JsonArray>resultHandler(it -> assertEquals(new JsonArray().add("socks").add("shoes"), it)
    ));
    assertEquals(2, checker.count);
  }

  @Test
  public void testJsonHandlerAsyncResultParams() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultJsonObject().onComplete(checker.<JsonObject>asyncResultHandler(it -> assertEquals(new JsonObject().put("cheese", "stilton"), it)));
    obj.methodWithHandlerAsyncResultJsonArray().onComplete(checker.<JsonArray>asyncResultHandler(it -> assertEquals(new JsonArray().add("socks").add("shoes"), it)));
    assertEquals(2, checker.count);
  }

  @Test
  public void testNullJsonHandlerAsyncResultParams() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultNullJsonObject().onComplete(checker.<JsonObject>asyncResultHandler(it -> assertEquals(null, it)));
    obj.methodWithHandlerAsyncResultNullJsonArray().onComplete(checker.<JsonArray>asyncResultHandler(it -> assertEquals(null, it)));
    assertEquals(2, checker.count);
  }

  @Test
  public void testJsonFutureParams() throws Exception {
    assertEquals(new JsonObject().put("cheese", "stilton"), get(obj.rxMethodWithHandlerAsyncResultJsonObject()));
    assertEquals(new JsonArray().add("socks").add("shoes"), get(obj.rxMethodWithHandlerAsyncResultJsonArray()));
  }

  @Test
  public void testNullJsonFutureParams() throws Exception {
    assertEquals(null, get(obj.rxMethodWithHandlerAsyncResultNullJsonObject()));
    assertEquals(null, get(obj.rxMethodWithHandlerAsyncResultNullJsonArray()));
  }

  @Test
  public void testCustomModule() {
    MyInterface my = MyInterface.create();
    TestInterface testInterface = my.method();
    testInterface.methodWithBasicParams((byte) 123, (short) 12345, 1234567, 1265615234l, 12.345f, 12.34566d, true, 'X', "foobar");
    SubInterface sub = my.sub();
    assertEquals("olleh", sub.reverse("hello"));
  }

  @Test
  public void testThrowableParam() {
    assertEquals("throwable_message", obj.methodWithThrowableParam(new Exception("throwable_message")));
  }

  static <V> Map<String, V> map(String key1, V value1) {
    HashMap<String, V> map = new HashMap<>();
    map.put(key1, value1);
    return map;
  }

  static <V> Map<String, V> map(String key1, V value1, String key2, V value2) {
    HashMap<String, V> map = new HashMap<>();
    map.put(key1, value1);
    map.put(key2, value2);
    return map;
  }

  static <E> Set<E> set(E... elements) {
    return new HashSet<>(Arrays.asList(elements));
  }

  static <E> List<E> list(E... elements) {
    return Arrays.asList(elements);
  }
}
