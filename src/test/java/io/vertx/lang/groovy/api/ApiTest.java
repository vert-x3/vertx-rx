package io.vertx.lang.groovy.api;

import io.vertx.codegen.testmodel.RefedInterface1Impl;
import io.vertx.codegen.testmodel.TestInterfaceImpl;
import io.vertx.codegen.testmodel.TestOptions;
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.java.codegen.testmodel.RefedInterface1;
import io.vertx.java.codegen.testmodel.TestInterface;
import io.vertx.lang.groovy.AsyncResultChecker;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ApiTest {

  final TestInterface<?> obj = new TestInterface(new TestInterfaceImpl());

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
    obj.methodWithHandlerAsyncResultByte(false, checker.asyncExpectedResult((byte) 123));
    obj.methodWithHandlerAsyncResultShort(false, checker.asyncExpectedResult((short) 12345));
    obj.methodWithHandlerAsyncResultInteger(false, checker.asyncExpectedResult(1234567));
    obj.methodWithHandlerAsyncResultLong(false, checker.asyncExpectedResult(1265615234l));
    obj.methodWithHandlerAsyncResultFloat(false, checker.asyncExpectedResult(12.345f));
    obj.methodWithHandlerAsyncResultDouble(false, checker.asyncExpectedResult(12.34566d));
    obj.methodWithHandlerAsyncResultBoolean(false, checker.asyncExpectedResult(true));
    obj.methodWithHandlerAsyncResultCharacter(false, checker.asyncExpectedResult('X'));
    obj.methodWithHandlerAsyncResultString(false, checker.asyncExpectedResult("quux!"));
    assertEquals(9, checker.count);
    checker.count = 0;
    obj.methodWithHandlerAsyncResultByte(true, checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultShort(true, checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultInteger(true, checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultLong(true, checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultFloat(true, checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultDouble(true, checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultBoolean(true, checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultCharacter(true, checker.failureAsserter("foobar!"));
    obj.methodWithHandlerAsyncResultString(true, checker.failureAsserter("foobar!"));
    assertEquals(9, checker.count);
  }

  @Test
  public void testMethodWithFutureBasicTypes() throws Exception {
    assertEquals((byte) 123, (byte) obj.methodWithHandlerAsyncResultByteFuture(false).get());
    assertEquals((short) 12345, (short) obj.methodWithHandlerAsyncResultShortFuture(false).get());
    assertEquals(1234567, (int) obj.methodWithHandlerAsyncResultIntegerFuture(false).get());
    assertEquals(1265615234l, (long) obj.methodWithHandlerAsyncResultLongFuture(false).get());
    assertEquals(12.345f, (float) obj.methodWithHandlerAsyncResultFloatFuture(false).get(), 0);
    assertEquals(12.34566d, (double) obj.methodWithHandlerAsyncResultDoubleFuture(false).get(), 0);
    assertEquals(true, obj.methodWithHandlerAsyncResultBooleanFuture(false).get());
    assertEquals('X', (char) obj.methodWithHandlerAsyncResultCharacterFuture(false).get());
    assertEquals("quux!", obj.methodWithHandlerAsyncResultStringFuture(false).get());
    assertFailure("foobar!", obj.methodWithHandlerAsyncResultByteFuture(true));
    assertFailure("foobar!", obj.methodWithHandlerAsyncResultShortFuture(true));
    assertFailure("foobar!", obj.methodWithHandlerAsyncResultIntegerFuture(true));
    assertFailure("foobar!", obj.methodWithHandlerAsyncResultLongFuture(true));
    assertFailure("foobar!", obj.methodWithHandlerAsyncResultFloatFuture(true));
    assertFailure("foobar!", obj.methodWithHandlerAsyncResultDoubleFuture(true));
    assertFailure("foobar!", obj.methodWithHandlerAsyncResultBooleanFuture(true));
    assertFailure("foobar!", obj.methodWithHandlerAsyncResultCharacterFuture(true));
    assertFailure("foobar!", obj.methodWithHandlerAsyncResultStringFuture(true));
  }

  private <T> void assertFailure(String message, CompletableFuture<T> future) throws Exception {
    try {
      future.get();
    } catch (ExecutionException e) {
      assertEquals(message, e.getCause().getMessage());
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
    obj.methodWithObjectParam("JsonObject", new JsonObject().putString("foo", "hello").putNumber("bar", 123));
    obj.methodWithObjectParam("JsonArray", new JsonArray().add("foo").add("bar").add("wib"));
  }

  @Test
  public void testOptionsParam() {
    TestOptions options = TestOptions.options();
    options.setFoo("hello");
    options.setBar(123);
    options.setWibble(1.23);
    obj.methodWithOptionsParam(options);
  }

  @Test
  public void testNullOptionsParam() {
    obj.methodWithNullOptionsParam(null);
  }

  @Test
  public void testMethodWithHandlerListAndSet() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListAndSet(
        checker.expectedResult(Arrays.asList("foo", "bar", "wibble")),
        checker.expectedResult(Arrays.asList(5, 12, 100)),
        checker.expectedResult(new HashSet<>(Arrays.asList("foo", "bar", "wibble"))),
        checker.expectedResult(new HashSet<>(Arrays.asList(5, 12, 100)))
    );
    assertEquals(4, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultListAndSet() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultListString(checker.asyncExpectedResult(Arrays.asList("foo", "bar", "wibble")));
    obj.methodWithHandlerAsyncResultListInteger(checker.asyncExpectedResult(Arrays.asList(5, 12, 100)));
    obj.methodWithHandlerAsyncResultSetString(checker.asyncExpectedResult(new HashSet<>(Arrays.asList("foo", "bar", "wibble"))));
    obj.methodWithHandlerAsyncResultSetInteger(checker.asyncExpectedResult(new HashSet<>(Arrays.asList(5, 12, 100))));
    assertEquals(4, checker.count);
  }

  @Test
  public void testMethodWithFutureListAndSet() throws Exception {
    assertEquals(Arrays.asList("foo", "bar", "wibble"), obj.methodWithHandlerAsyncResultListStringFuture().get());
    assertEquals(Arrays.asList(5, 12, 100), obj.methodWithHandlerAsyncResultListIntegerFuture().get());
    assertEquals(new HashSet<>(Arrays.asList("foo", "bar", "wibble")), obj.methodWithHandlerAsyncResultSetStringFuture().get());
    assertEquals(new HashSet<>(Arrays.asList(5, 12, 100)), obj.methodWithHandlerAsyncResultSetIntegerFuture().get());
  }

  @Test
  public void testMethodWithHandlerListVertxGen() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListVertxGen(checker.<List<RefedInterface1>>resultHandler(it ->
        assertEquals(Arrays.asList("foo", "bar"), it.stream().map(RefedInterface1::getString).collect(Collectors.toList()))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultListVertxGen() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultListVertxGen(
        checker.<List<RefedInterface1>>asyncResultHandler(event -> {
          assertEquals(2, event.size());
          assertEquals("foo", event.get(0).getString());
          assertEquals("bar", event.get(1).getString());
        }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureListVertxGen() throws Exception {
    List<RefedInterface1> result = obj.methodWithHandlerAsyncResultListVertxGenFuture().get();
    assertEquals(2, result.size());
    assertEquals("foo", result.get(0).getString());
    assertEquals("bar", result.get(1).getString());
  }

  @Test
  public void testMethodWithHandlerSetVertxGen() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetVertxGen(checker.<Set<RefedInterface1>>resultHandler( event -> {
      List<String> list = event.stream().map(it -> it.getString()).collect(Collectors.toList());
      Collections.sort(list);
      assertEquals(Arrays.asList("bar", "foo"), list);
    }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultSetVertxGen() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultSetVertxGen(
        checker.<Set<RefedInterface1>>asyncResultHandler(event -> {
          List<String> list = event.stream().map(RefedInterface1::getString).collect(Collectors.toList());
          Collections.sort(list);
          assertEquals(Arrays.asList("bar", "foo"), list);
        }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureSetVertxGen() throws Exception {
    Set<RefedInterface1> result = obj.methodWithHandlerAsyncResultSetVertxGenFuture().get();
    List<String> list = result.stream().map(RefedInterface1::getString).collect(Collectors.toList());
    Collections.sort(list);
    assertEquals(Arrays.asList("bar", "foo"), list);
  }

  @Test
  public void testMethodWithHandlerListJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListJsonObject(checker.expectedResult(
        Arrays.asList(new JsonObject().putString("cheese", "stilton"), new JsonObject().putString("socks", "tartan"))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerListNullJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListNullJsonObject(checker.expectedResult(
        Collections.singletonList(null)));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultListJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultListJsonObject(
        checker.asyncExpectedResult(Arrays.asList(new JsonObject().putString("cheese", "stilton"), new JsonObject().putString("socks", "tartan")))
    );
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultListNullJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultListNullJsonObject(
        checker.asyncExpectedResult(Collections.singletonList(null))
    );
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureListJsonObject() throws Exception {
    List<JsonObject> result = obj.methodWithHandlerAsyncResultListJsonObjectFuture().get();
    assertEquals(Arrays.asList(new JsonObject().putString("cheese", "stilton"), new JsonObject().putString("socks", "tartan")), result);
  }

  @Test
  public void testMethodWithFutureListNullJsonObject() throws Exception {
    List<JsonObject> result = obj.methodWithHandlerAsyncResultListNullJsonObjectFuture().get();
    assertEquals(Collections.singletonList(null), result);
  }

  @Test
  public void testMethodWithHandlerSetJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetJsonObject(checker.<Set<JsonObject>>resultHandler(r -> {
      assertEquals(Arrays.asList(new JsonObject().putString("cheese", "stilton"), new JsonObject().putString("socks", "tartan")), new ArrayList<>(r));
    }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerSetNullJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetNullJsonObject(checker.<Set<JsonObject>>resultHandler(r -> {
      assertEquals(Collections.singletonList(null), new ArrayList<>(r));
    }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultSetJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultSetJsonObject(checker.<Set<JsonObject>>asyncResultHandler(it ->
      assertEquals(Arrays.asList(new JsonObject().putString("cheese", "stilton"), new JsonObject().putString("socks", "tartan")), new ArrayList<>(it))
    ));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultSetNullJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultSetNullJsonObject(checker.<Set<JsonObject>>asyncResultHandler(it ->
            assertEquals(Collections.singletonList(null), new ArrayList<>(it))
    ));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureSetJsonObject() throws Exception {
    Set<JsonObject> result = obj.methodWithHandlerAsyncResultSetJsonObjectFuture().get();
    assertEquals(Arrays.asList(new JsonObject().putString("cheese", "stilton"), new JsonObject().putString("socks", "tartan")), new ArrayList<>(result));
  }

  @Test
  public void testMethodWithFutureSetNullJsonObject() throws Exception {
    Set<JsonObject> result = obj.methodWithHandlerAsyncResultSetNullJsonObjectFuture().get();
    assertEquals(Collections.singletonList(null), new ArrayList<>(result));
  }

  @Test
  public void testMethodWithHandlerListJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListJsonArray(checker.expectedResult(
        Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple"))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerListNullJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListNullJsonArray(checker.expectedResult(
        Collections.singletonList(null)));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultListJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultListJsonArray(checker.asyncExpectedResult(
        Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple"))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultListNullJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultListNullJsonArray(checker.asyncExpectedResult(
        Collections.singletonList(null)));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureListJsonArray() throws Exception {
    List<JsonArray> result = obj.methodWithHandlerAsyncResultListJsonArrayFuture().get();
    assertEquals(result, Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple")));
  }

  @Test
  public void testMethodWithFutureListNullJsonArray() throws Exception {
    List<JsonArray> result = obj.methodWithHandlerAsyncResultListNullJsonArrayFuture().get();
    assertEquals(result, Collections.singletonList(null));
  }

  @Test
  public void testMethodWithHandlerSetJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetJsonArray(checker.<Set<JsonArray>>resultHandler(it ->
      assertEquals(Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple")), new ArrayList<>(it))
    ));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerSetNullJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetNullJsonArray(checker.<Set<JsonArray>>resultHandler(it ->
            assertEquals(Collections.singletonList(null), new ArrayList<>(it))
    ));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultSetJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultSetJsonArray(checker.<Set<JsonArray>>asyncResultHandler(it ->
        assertEquals(Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple")), new ArrayList<>(it))
    ));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultSetNullJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultSetNullJsonArray(checker.<Set<JsonArray>>asyncResultHandler(it ->
            assertEquals(Collections.singletonList(null), new ArrayList<>(it))
    ));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureSetJsonArray() throws Exception {
    Set<JsonArray> result = obj.methodWithHandlerAsyncResultSetJsonArrayFuture().get();
    assertEquals(Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple")), new ArrayList<>(result));
  }

  @Test
  public void testMethodWithFutureSetNullJsonArray() throws Exception {
    Set<JsonArray> result = obj.methodWithHandlerAsyncResultSetNullJsonArrayFuture().get();
    assertEquals(Collections.singletonList(null), new ArrayList<>(result));
  }

  @Test
  public void testMethodWithHandlerUserTypes() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerUserTypes(checker.<RefedInterface1>resultHandler( it -> assertEquals("echidnas", it.getString())));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultUserTypes() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultUserTypes(checker.<RefedInterface1>asyncResultHandler( it -> assertEquals("cheetahs", it.getString())));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureUserTypes() throws Exception {
    RefedInterface1 result = obj.methodWithHandlerAsyncResultUserTypesFuture().get();
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
    obj.methodWithHandlerAsyncResultVoid(false, checker.<Void>asyncResultHandler(Assert::assertNull));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureVoid() throws Exception {
    Void result = obj.methodWithHandlerAsyncResultVoidFuture(false).get();
    assertEquals(null, result);
  }

  @Test
  public void testMethodWithHandlerAsyncResultVoidFails() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultVoid(true, checker.<Void>failureAsserter("foo!"));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureVoidFails() throws Exception {
    assertFailure("foo!", obj.methodWithHandlerAsyncResultVoidFuture(true));
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
    obj.methodWithGenericParam("JsonObject", new JsonObject().putString("foo", "hello").putNumber("bar", 123));
    obj.methodWithGenericParam("JsonArray", new JsonArray().add("foo").add("bar").add("wib"));
  }

  @Test
  public void testMethodWithGenericHandler() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithGenericHandler("String", checker.expectedResult("foo"));
    obj.methodWithGenericHandler("Ref", checker.<RefedInterface1Impl>resultHandler( it -> assertEquals("bar", it.getString())));
    obj.methodWithGenericHandler("JsonObject", checker.expectedResult(new JsonObject().putString("foo", "hello").putNumber("bar", 123)));
    obj.methodWithGenericHandler("JsonArray", checker.expectedResult(new JsonArray().add("foo").add("bar").add("wib")));
    assertEquals(4, checker.count);
  }

  @Test
  public void testMethodWithGenericHandlerAsyncResult() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithGenericHandlerAsyncResult("String", checker.asyncExpectedResult("foo"));
    obj.methodWithGenericHandlerAsyncResult("Ref", checker.<RefedInterface1Impl>asyncResultHandler( it -> assertEquals("bar", it.getString())));
    obj.methodWithGenericHandlerAsyncResult("JsonObject", checker.asyncExpectedResult(new JsonObject().putString("foo", "hello").putNumber("bar", 123)));
    obj.methodWithGenericHandlerAsyncResult("JsonArray", checker.asyncExpectedResult(new JsonArray().add("foo").add("bar").add("wib")));
    assertEquals(4, checker.count);
  }

  @Test
  public void testMethodWithGenericFuture() throws Exception {
    assertEquals("foo", obj.methodWithGenericHandlerAsyncResultFuture("String").get());
    RefedInterface1Impl ref = obj.<RefedInterface1Impl>methodWithGenericHandlerAsyncResultFuture("Ref").get();
    assertEquals("bar", ref.getString());
    assertEquals(new JsonObject().putString("foo", "hello").putNumber("bar", 123), obj.methodWithGenericHandlerAsyncResultFuture("JsonObject").get());
    assertEquals(new JsonArray().add("foo").add("bar").add("wib"), obj.methodWithGenericHandlerAsyncResultFuture("JsonArray").get());
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
  public void testListStringReturn() {
    assertEquals(Arrays.asList("foo", "bar", "wibble"), obj.methodWithListStringReturn());
  }

  @Test
  public void testSetStringReturn() {
    assertEquals(new HashSet<>(Arrays.asList("foo", "bar", "wibble")), obj.methodWithSetStringReturn());
  }

  @Test
  public void testOverloadedMethods() {
    RefedInterface1 refed = new RefedInterface1(new RefedInterface1Impl());
    refed.setString("dog");
    assertEquals("meth1", obj.overloadedMethod("cat", refed));
    assertEquals("meth2", obj.overloadedMethod("cat", refed, 12345));
    AtomicBoolean called = new AtomicBoolean(false);
    assertEquals("meth3", obj.overloadedMethod("cat", refed, 12345, it -> { assertEquals("giraffe", it); called.set(true); }));
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
    assertEquals(new JsonObject().putString("foo", "hello").putNumber("bar", 123), ret);
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
  public void testJsonReturns() {
    JsonObject ret1 = obj.methodWithJsonObjectReturn();
    assertEquals(new JsonObject().putString("cheese", "stilton"), ret1);
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
        new JsonObject().putString("cat", "lion").putString("cheese", "cheddar"),
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
        checker.<JsonObject>resultHandler(it -> assertEquals(new JsonObject().putString("cheese", "stilton"), it)),
        checker.<JsonArray>resultHandler(it -> assertEquals(new JsonArray().add("socks").add("shoes"), it)
    ));
    assertEquals(2, checker.count);
  }

  @Test
  public void testNullJsonHandlerParams() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerNullJson(
        checker.<JsonObject>resultHandler(it -> assertEquals(null, it)),
        checker.<JsonArray>resultHandler(it -> assertEquals(null, it)
        ));
    assertEquals(2, checker.count);
  }

  @Test
  public void testJsonHandlerAsyncResultParams() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultJsonObject(checker.<JsonObject>asyncResultHandler(it -> assertEquals(new JsonObject().putString("cheese", "stilton"), it)));
    obj.methodWithHandlerAsyncResultJsonArray(checker.<JsonArray>asyncResultHandler(it -> assertEquals(new JsonArray().add("socks").add("shoes"), it)));
    assertEquals(2, checker.count);
  }

  @Test
  public void testNullJsonHandlerAsyncResultParams() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultNullJsonObject(checker.<JsonObject>asyncResultHandler(it -> assertEquals(null, it)));
    obj.methodWithHandlerAsyncResultNullJsonArray(checker.<JsonArray>asyncResultHandler(it -> assertEquals(null, it)));
    assertEquals(2, checker.count);
  }

  @Test
  public void testJsonFutureParams() throws Exception {
    assertEquals(new JsonObject().putString("cheese", "stilton"), obj.methodWithHandlerAsyncResultJsonObjectFuture().get());
    assertEquals(new JsonArray().add("socks").add("shoes"), obj.methodWithHandlerAsyncResultJsonArrayFuture().get());
  }

  @Test
  public void testNullJsonFutureParams() throws Exception {
    assertEquals(null, obj.methodWithHandlerAsyncResultNullJsonObjectFuture().get());
    assertEquals(null, obj.methodWithHandlerAsyncResultNullJsonArrayFuture().get());
  }
}
