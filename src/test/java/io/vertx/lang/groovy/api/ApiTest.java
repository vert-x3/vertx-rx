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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ApiTest {

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
    obj.methodWithHandlerAsyncResultBasicTypes(false,
        checker.asyncExpectedResult((byte) 123),
        checker.asyncExpectedResult((short) 12345),
        checker.asyncExpectedResult(1234567),
        checker.asyncExpectedResult(1265615234l),
        checker.asyncExpectedResult(12.345f),
        checker.asyncExpectedResult(12.34566d),
        checker.asyncExpectedResult(true),
        checker.asyncExpectedResult('X'),
        checker.asyncExpectedResult("quux!")
    );
    assertEquals(9, checker.count);
    checker.count = 0;
    obj.methodWithHandlerAsyncResultBasicTypes(true,
        checker.failureAsserter("foobar!"),
        checker.failureAsserter("foobar!"),
        checker.failureAsserter("foobar!"),
        checker.failureAsserter("foobar!"),
        checker.failureAsserter("foobar!"),
        checker.failureAsserter("foobar!"),
        checker.failureAsserter("foobar!"),
        checker.failureAsserter("foobar!"),
        checker.failureAsserter("foobar!")
    );
    assertEquals(9, checker.count);
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
    obj.methodWithHandlerAsyncResultListAndSet(
        checker.asyncExpectedResult(Arrays.asList("foo", "bar", "wibble")),
        checker.asyncExpectedResult(Arrays.asList(5, 12, 100)),
        checker.asyncExpectedResult(new HashSet<>(Arrays.asList("foo", "bar", "wibble"))),
        checker.asyncExpectedResult(new HashSet<>(Arrays.asList(5, 12, 100)))
    );
    assertEquals(4, checker.count);
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
  public void testMethodWithHandlerListJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListJsonObject(checker.expectedResult(
        Arrays.asList(new JsonObject().putString("cheese", "stilton"), new JsonObject().putString("socks", "tartan"))));
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
  public void testMethodWithHandlerSetJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetJsonObject(checker.<Set<JsonObject>>resultHandler(r -> {
      assertEquals(Arrays.asList(new JsonObject().putString("cheese", "stilton"), new JsonObject().putString("socks", "tartan")), new ArrayList<>(r));
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
  public void testMethodWithHandlerListJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListJsonArray(checker.expectedResult(
        Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple"))));
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
  public void testMethodWithHandlerSetJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetJsonArray(checker.<Set<JsonArray>>resultHandler(it ->
      assertEquals(Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple")), new ArrayList<>(it))
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
  public void testMethodWithHandlerAsyncResultVoidFails() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultVoid(true, checker.<Void>failureAsserter("foo!"));
    assertEquals(1, checker.count);
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
    obj.methodWithGenericParam("Ref", new RefedInterface1Impl().setString("foo"));
    obj.methodWithGenericParam("JsonObject", new JsonObject().putString("foo", "hello").putNumber("bar", 123));
    obj.methodWithGenericParam("JsonArray", new JsonArray().add("foo").add("bar").add("wib"));
  }

  @Test
  public void testMethodWithGenericHandler() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithGenericHandler("String", checker.expectedResult("handlerFoo"));
    obj.methodWithGenericHandler("Ref", checker.<RefedInterface1Impl>resultHandler( it -> assertEquals("bar", it.getString())));
    obj.methodWithGenericHandler("JsonObject", checker.expectedResult(new JsonObject().putString("foo", "hello").putNumber("bar", 123)));
    obj.methodWithGenericHandler("JsonArray", checker.expectedResult(new JsonArray().add("foo").add("bar").add("wib")));
    assertEquals(4, checker.count);
  }

  @Test
  public void testMethodWithGenericHandlerAsyncResult() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithGenericHandlerAsyncResult("String", checker.asyncExpectedResult("asyncResultHandlerFoo"));
    obj.methodWithGenericHandlerAsyncResult("Ref", checker.<RefedInterface1Impl>asyncResultHandler( it -> assertEquals("bar", it.getString())));
    obj.methodWithGenericHandlerAsyncResult("JsonObject", checker.asyncExpectedResult(new JsonObject().putString("foo", "hello").putNumber("bar", 123)));
    obj.methodWithGenericHandlerAsyncResult("JsonArray", checker.asyncExpectedResult(new JsonArray().add("foo").add("bar").add("wib")));
    assertEquals(4, checker.count);
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
    Object ret = obj.methodWithGenericReturn(true);
    assertTrue("Was expecting " + ret + " to implement JsonObject", ret instanceof JsonObject);
    assertEquals(Collections.<String, Object>singletonMap("foo", "bar"), ((JsonObject) ret).toMap());
    ret = obj.methodWithGenericReturn(false);
    assertTrue("Was expecting " + ret + " to implement JsonArray", ret instanceof JsonArray);
    assertEquals(Arrays.asList("foo","bar"), ((JsonArray) ret).toList());
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
    JsonObject ret1 = obj.methodwithJsonObjectReturn();
    assertEquals(Collections.<String, Object>singletonMap("cheese", "stilton"), ret1.toMap());
    JsonArray ret2 = obj.methodWithJsonArrayReturn();
    assertEquals(Arrays.asList("socks", "shoes"), ret2.toList());
  }

  @Test
  public void testJsonParams() {
    obj.methodWithJsonParams(
        new JsonObject().putString("cat", "lion").putString("cheese", "cheddar"),
        new JsonArray().add("house").add("spider")
    );
  }

  @Test
  public void testJsonHandlerParams() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerJson(
        checker.<JsonObject>resultHandler(it -> assertEquals(Collections.<String, Object>singletonMap("cheese", "stilton"), it.toMap())),
        checker.<JsonArray>resultHandler(it -> assertEquals(Arrays.asList("socks","shoes"), it.toList())
    ));
    assertEquals(2, checker.count);
  }

  @Test
  public void testJsonHandlerAsyncResultParams() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultJson(
        checker.<JsonObject>asyncResultHandler(it -> assertEquals(Collections.<String, Object>singletonMap("cheese", "stilton"), it.toMap())),
        checker.<JsonArray>asyncResultHandler(it -> assertEquals(Arrays.asList("socks","shoes"), it.toList())
        ));
    assertEquals(2, checker.count);
  }
}
