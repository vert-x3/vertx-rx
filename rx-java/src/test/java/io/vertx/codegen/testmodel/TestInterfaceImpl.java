package io.vertx.codegen.testmodel;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class TestInterfaceImpl implements TestInterface {

  private static <T> T assertInstanceOf(Class<T> expectedType, Object obj) {
    if (expectedType.isInstance(obj)) {
      return expectedType.cast(obj);
    } else {
      throw new AssertionError("Was expecting " + obj + " to be an instance of " + expectedType);
    }
  }

  @Override
  public void methodWithBasicParams(byte b, short s, int i, long l, float f, double d, boolean bool, char ch, String str) {
    assertEquals((byte) 123, b);
    assertEquals((short) 12345, s);
    assertEquals(1234567, i);
    assertEquals(1265615234l, l);
    assertEquals(12.345f, f, 0);
    assertEquals(12.34566d, d, 0);
    assertTrue(bool);
    assertEquals('X', ch);
    assertEquals("foobar", str);
  }

  @Override
  public void methodWithBasicBoxedParams(Byte b, Short s, Integer i, Long l, Float f, Double d, Boolean bool, Character ch) {
    assertEquals((byte) 123, b.byteValue());
    assertEquals((short) 12345, s.shortValue());
    assertEquals(1234567, i.intValue());
    assertEquals(1265615234l, l.longValue());
    assertEquals(12.345f, f.floatValue(), 0);
    assertEquals(12.34566d, d.doubleValue(), 0);
    assertTrue(bool);
    assertEquals('X', ch.charValue());
  }

  @Override
  public void methodWithHandlerBasicTypes(Handler<Byte> byteHandler, Handler<Short> shortHandler,
                                          Handler<Integer> intHandler, Handler<Long> longHandler,
                                          Handler<Float> floatHandler, Handler<Double> doubleHandler,
                                          Handler<Boolean> booleanHandler, Handler<Character> charHandler,
                                          Handler<String> stringHandler) {
    byteHandler.handle((byte)123);
    shortHandler.handle((short)12345);
    intHandler.handle(1234567);
    longHandler.handle(1265615234l);
    floatHandler.handle(12.345f);
    doubleHandler.handle(12.34566d);
    booleanHandler.handle(true);
    charHandler.handle('X');
    stringHandler.handle("quux!");
  }

  @Override
  public void methodWithHandlerAsyncResultByte(boolean sendFailure, Handler<AsyncResult<Byte>> handler) {
    if (sendFailure) {
      Exception e = new Exception("foobar!");
      handler.handle(Future.failedFuture(e));
    } else {
      handler.handle(Future.succeededFuture((byte) 123));
    }
  }

  @Override
  public void methodWithHandlerAsyncResultShort(boolean sendFailure, Handler<AsyncResult<Short>> handler) {
    if (sendFailure) {
      Exception e = new Exception("foobar!");
      handler.handle(Future.failedFuture(e));
    } else {
      handler.handle(Future.succeededFuture((short) 12345));
    }
  }

  @Override
  public void methodWithHandlerAsyncResultInteger(boolean sendFailure, Handler<AsyncResult<Integer>> handler) {
    if (sendFailure) {
      Exception e = new Exception("foobar!");
      handler.handle(Future.failedFuture(e));
    } else {
      handler.handle(Future.succeededFuture(1234567));
    }
  }

  @Override
  public void methodWithHandlerAsyncResultLong(boolean sendFailure, Handler<AsyncResult<Long>> handler) {
    if (sendFailure) {
      Exception e = new Exception("foobar!");
      handler.handle(Future.failedFuture(e));
    } else {
      handler.handle(Future.succeededFuture(1265615234l));
    }
  }

  @Override
  public void methodWithHandlerAsyncResultFloat(boolean sendFailure, Handler<AsyncResult<Float>> handler) {
    if (sendFailure) {
      Exception e = new Exception("foobar!");
      handler.handle(Future.failedFuture(e));
    } else {
      handler.handle(Future.succeededFuture(12.345f));
    }
  }

  @Override
  public void methodWithHandlerAsyncResultDouble(boolean sendFailure, Handler<AsyncResult<Double>> handler) {
    if (sendFailure) {
      Exception e = new Exception("foobar!");
      handler.handle(Future.failedFuture(e));
    } else {
      handler.handle(Future.succeededFuture(12.34566d));
    }
  }

  @Override
  public void methodWithHandlerAsyncResultBoolean(boolean sendFailure, Handler<AsyncResult<Boolean>> handler) {
    if (sendFailure) {
      Exception e = new Exception("foobar!");
      handler.handle(Future.failedFuture(e));
    } else {
      handler.handle(Future.succeededFuture(true));
    }
  }

  @Override
  public void methodWithHandlerAsyncResultCharacter(boolean sendFailure, Handler<AsyncResult<Character>> handler) {
    if (sendFailure) {
      Exception e = new Exception("foobar!");
      handler.handle(Future.failedFuture(e));
    } else {
      handler.handle(Future.succeededFuture('X'));
    }
  }

  @Override
  public void methodWithHandlerAsyncResultString(boolean sendFailure, Handler<AsyncResult<String>> handler) {
    if (sendFailure) {
      Exception e = new Exception("foobar!");
      handler.handle(Future.failedFuture(e));
    } else {
      handler.handle(Future.succeededFuture("quux!"));
    }
  }

  @Override
  public void methodWithUserTypes(RefedInterface1 refed) {
    assertEquals("aardvarks", refed.getString());
  }

  @Override
  public void methodWithObjectParam(String str, Object obj) {
    switch (str) {
      case "JsonObject" : {
        assertTrue(obj instanceof JsonObject);
        JsonObject jsonObject = (JsonObject)obj;
        assertEquals("hello", jsonObject.getString("foo"));
        assertEquals(123, jsonObject.getInteger("bar").intValue());
        break;
      }
      case "JsonArray": {
        assertTrue(obj instanceof JsonArray);
        JsonArray arr = (JsonArray)obj;
        assertEquals(3, arr.size());
        assertEquals("foo", arr.getString(0));
        assertEquals("bar", arr.getString(1));
        assertEquals("wib", arr.getString(2));
        break;
      }
      default: fail("invalid type");
    }
  }

  @Override
  public void methodWithOptionsParam(TestOptions options) {
    assertEquals("hello", options.getFoo());
    assertEquals(123, options.getBar());
    assertEquals(1.23, options.getWibble(), 0);
  }

  @Override
  public void methodWithNullOptionsParam(TestOptions options) {
    assertNull(options);
  }

  @Override
  public void methodWithHandlerListAndSet(Handler<List<String>> listStringHandler, Handler<List<Integer>> listIntHandler,
                                          Handler<Set<String>> setStringHandler, Handler<Set<Integer>> setIntHandler) {
    List<String> listString = Arrays.asList("foo", "bar", "wibble");
    List<Integer> listInt = Arrays.asList(5, 12, 100);
    Set<String> setString = new LinkedHashSet<>( Arrays.asList("foo", "bar", "wibble"));
    Set<Integer> setInt = new LinkedHashSet<>(Arrays.asList(new Integer[] {5, 12, 100}));
    listStringHandler.handle(listString);
    listIntHandler.handle(listInt);
    setStringHandler.handle(setString);
    setIntHandler.handle(setInt);
  }

  @Override
  public void methodWithHandlerAsyncResultListString(Handler<AsyncResult<List<String>>> handler) {
    List<String> listString = Arrays.asList("foo", "bar", "wibble");
    handler.handle(Future.succeededFuture(listString));
  }

  @Override
  public void methodWithHandlerAsyncResultListInteger(Handler<AsyncResult<List<Integer>>> handler) {
    List<Integer> listInt = Arrays.asList(5, 12, 100);
    handler.handle(Future.succeededFuture(listInt));
  }

  @Override
  public void methodWithHandlerAsyncResultSetString(Handler<AsyncResult<Set<String>>> handler) {
    Set<String> setString = new LinkedHashSet<>( Arrays.asList("foo", "bar", "wibble"));
    handler.handle(Future.succeededFuture(setString));
  }

  @Override
  public void methodWithHandlerAsyncResultSetInteger(Handler<AsyncResult<Set<Integer>>> handler) {
    Set<Integer> setInt = new LinkedHashSet<>(Arrays.asList(5, 12, 100));
    handler.handle(Future.succeededFuture(setInt));
  }

  @Override
  public void methodWithHandlerListVertxGen(Handler<List<RefedInterface1>> listHandler) {
    List<RefedInterface1> list = Arrays.asList(new RefedInterface1Impl().setString("foo"), new RefedInterface1Impl().setString("bar"));
    listHandler.handle(list);
  }

  @Override
  public void methodWithHandlerSetVertxGen(Handler<Set<RefedInterface1>> setHandler) {
    Set<RefedInterface1> list = new LinkedHashSet<>(Arrays.asList(new RefedInterface1Impl().setString("foo"), new RefedInterface1Impl().setString("bar")));
    setHandler.handle(list);
  }

  @Override
  public void methodWithHandlerListAbstractVertxGen(Handler<List<RefedInterface2>> listHandler) {
    List<RefedInterface2> list = Arrays.asList(new RefedInterface2Impl().setString("abstractfoo"), new RefedInterface2Impl().setString("abstractbar"));
    listHandler.handle(list);
  }

  @Override
  public void methodWithHandlerSetAbstractVertxGen(Handler<Set<RefedInterface2>> setHandler) {
    Set<RefedInterface2> list = new LinkedHashSet<>(Arrays.asList(new RefedInterface2Impl().setString("abstractfoo"), new RefedInterface2Impl().setString("abstractbar")));
    setHandler.handle(list);
  }

  @Override
  public void methodWithHandlerListJsonObject(Handler<List<JsonObject>> listHandler) {
    List<JsonObject> list = Arrays.asList(new JsonObject().put("cheese", "stilton"), new JsonObject().put("socks", "tartan"));
    listHandler.handle(list);
  }

  @Override
  public void methodWithHandlerListNullJsonObject(Handler<List<JsonObject>> listHandler) {
    List<JsonObject> list = Collections.singletonList(null);
    listHandler.handle(list);
  }

  @Override
  public void methodWithHandlerSetJsonObject(Handler<Set<JsonObject>> setHandler) {
    Set<JsonObject> set = new LinkedHashSet<>(Arrays.asList(new JsonObject().put("cheese", "stilton"), new JsonObject().put("socks", "tartan")));
    setHandler.handle(set);
  }

  @Override
  public void methodWithHandlerSetNullJsonObject(Handler<Set<JsonObject>> setHandler) {
    Set<JsonObject> set = Collections.singleton(null);
    setHandler.handle(set);
  }

  @Override
  public void methodWithHandlerListJsonArray(Handler<List<JsonArray>> listHandler) {
    List<JsonArray> list = Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple"));
    listHandler.handle(list);
  }

  @Override
  public void methodWithHandlerListNullJsonArray(Handler<List<JsonArray>> listHandler) {
    List<JsonArray> list = Collections.singletonList(null);
    listHandler.handle(list);
  }

  @Override
  public void methodWithHandlerSetJsonArray(Handler<Set<JsonArray>> listHandler) {
    Set<JsonArray> set = new LinkedHashSet<>(Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple")));
    listHandler.handle(set);
  }

  @Override
  public void methodWithHandlerSetNullJsonArray(Handler<Set<JsonArray>> listHandler) {
    Set<JsonArray> set = Collections.singleton(null);
    listHandler.handle(set);
  }

  @Override
  public void methodWithHandlerAsyncResultListVertxGen(Handler<AsyncResult<List<RefedInterface1>>> listHandler) {
    List<RefedInterface1> list = Arrays.asList(new RefedInterface1Impl().setString("foo"), new RefedInterface1Impl().setString("bar"));
    listHandler.handle(Future.succeededFuture(list));
  }

  @Override
  public void methodWithHandlerAsyncResultSetVertxGen(Handler<AsyncResult<Set<RefedInterface1>>> setHandler) {
    Set<RefedInterface1> list = new LinkedHashSet<>(Arrays.asList(new RefedInterface1Impl().setString("foo"), new RefedInterface1Impl().setString("bar")));
    setHandler.handle(Future.succeededFuture(list));
  }

  @Override
  public void methodWithHandlerAsyncResultListAbstractVertxGen(Handler<AsyncResult<List<RefedInterface2>>> listHandler) {
    List<RefedInterface2> list = Arrays.asList(new RefedInterface2Impl().setString("abstractfoo"), new RefedInterface2Impl().setString("abstractbar"));
    listHandler.handle(Future.succeededFuture(list));
  }

  @Override
  public void methodWithHandlerAsyncResultSetAbstractVertxGen(Handler<AsyncResult<Set<RefedInterface2>>> setHandler) {
    Set<RefedInterface2> list = new LinkedHashSet<>(Arrays.asList(new RefedInterface2Impl().setString("abstractfoo"), new RefedInterface2Impl().setString("abstractbar")));
    setHandler.handle(Future.succeededFuture(list));
  }

  @Override
  public void methodWithHandlerAsyncResultListJsonObject(Handler<AsyncResult<List<JsonObject>>> listHandler) {
    List<JsonObject> list = Arrays.asList(new JsonObject().put("cheese", "stilton"), new JsonObject().put("socks", "tartan"));
    listHandler.handle(Future.succeededFuture(list));
  }

  @Override
  public void methodWithHandlerAsyncResultListNullJsonObject(Handler<AsyncResult<List<JsonObject>>> listHandler) {
    List<JsonObject> list = Collections.singletonList(null);
    listHandler.handle(Future.succeededFuture(list));
  }

  @Override
  public void methodWithHandlerAsyncResultSetJsonObject(Handler<AsyncResult<Set<JsonObject>>> setHandler) {
    Set<JsonObject> set = new LinkedHashSet<>(Arrays.asList(new JsonObject().put("cheese", "stilton"), new JsonObject().put("socks", "tartan")));
    setHandler.handle(Future.succeededFuture(set));
  }

  @Override
  public void methodWithHandlerAsyncResultSetNullJsonObject(Handler<AsyncResult<Set<JsonObject>>> setHandler) {
    Set<JsonObject> set = Collections.singleton(null);
    setHandler.handle(Future.succeededFuture(set));
  }

  @Override
  public void methodWithHandlerAsyncResultListJsonArray(Handler<AsyncResult<List<JsonArray>>> listHandler) {
    List<JsonArray> list = Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple"));
    listHandler.handle(Future.succeededFuture(list));
  }

  @Override
  public void methodWithHandlerAsyncResultListNullJsonArray(Handler<AsyncResult<List<JsonArray>>> listHandler) {
    List<JsonArray> list = Collections.singletonList(null);
    listHandler.handle(Future.succeededFuture(list));
  }

  @Override
  public void methodWithHandlerAsyncResultSetJsonArray(Handler<AsyncResult<Set<JsonArray>>> listHandler) {
    Set<JsonArray> set = new LinkedHashSet<>(Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple")));
    listHandler.handle(Future.succeededFuture(set));
  }

  @Override
  public void methodWithHandlerAsyncResultSetNullJsonArray(Handler<AsyncResult<Set<JsonArray>>> listHandler) {
    Set<JsonArray> set = Collections.singleton(null);
    listHandler.handle(Future.succeededFuture(set));
  }

  @Override
  public void methodWithHandlerUserTypes(Handler<RefedInterface1> handler) {
    RefedInterface1 refed = new RefedInterface1Impl();
    refed.setString("echidnas");
    handler.handle(refed);
  }

  @Override
  public void methodWithHandlerAsyncResultUserTypes(Handler<AsyncResult<RefedInterface1>> handler) {
    RefedInterface1 refed = new RefedInterface1Impl();
    refed.setString("cheetahs");
    handler.handle(Future.succeededFuture(refed));
  }

  @Override
  public void methodWithHandlerVoid(Handler<Void> handler) {
    handler.handle(null);
  }

  @Override
  public void methodWithHandlerAsyncResultVoid(boolean sendFailure, Handler<AsyncResult<Void>> handler) {
    if (sendFailure) {
      handler.handle(Future.failedFuture(new VertxException("foo!")));
    } else {
      handler.handle(Future.succeededFuture((Void) null));
    }
  }

  @Override
  public void methodWithHandlerThrowable(Handler<Throwable> handler) {
    handler.handle(new VertxException("cheese!"));
  }

  @Override
  public <U> void methodWithHandlerGenericUserType(U value, Handler<GenericRefedInterface<U>> handler) {
    GenericRefedInterfaceImpl<U> userObj = new GenericRefedInterfaceImpl<>();
    userObj.setValue(value);
    handler.handle(userObj);
  }

  @Override
  public <U> void methodWithHandlerAsyncResultGenericUserType(U value, Handler<AsyncResult<GenericRefedInterface<U>>> handler) {
    GenericRefedInterfaceImpl<U> userObj = new GenericRefedInterfaceImpl<>();
    userObj.setValue(value);
    handler.handle(Future.succeededFuture(userObj));
  }

  @Override
  public byte methodWithByteReturn() {
    return (byte)123;
  }

  @Override
  public short methodWithShortReturn() {
    return (short)12345;
  }

  @Override
  public int methodWithIntReturn() {
    return 12345464;
  }

  @Override
  public long methodWithLongReturn() {
    return 65675123l;
  }

  @Override
  public float methodWithFloatReturn() {
    return 1.23f;
  }

  @Override
  public double methodWithDoubleReturn() {
    return 3.34535d;
  }

  @Override
  public boolean methodWithBooleanReturn() {
    return true;
  }

  @Override
  public char methodWithCharReturn() {
    return 'Y';
  }

  @Override
  public String methodWithStringReturn() {
    return "orangutan";
  }

  @Override
  public RefedInterface1 methodWithVertxGenReturn() {
    RefedInterface1 refed = new RefedInterface1Impl();
    refed.setString("chaffinch");
    return refed;
  }

  @Override
  public RefedInterface2 methodWithAbstractVertxGenReturn() {
    RefedInterface2 refed = new RefedInterface2Impl();
    refed.setString("abstractchaffinch");
    return refed;
  }

  @Override
  public List<String> methodWithListStringReturn() {
    return Arrays.asList("foo", "bar", "wibble");
  }

  @Override
  public Set<String> methodWithSetStringReturn() {
    return new LinkedHashSet<>( Arrays.asList("foo", "bar", "wibble"));
  }

  @Override
  public String overloadedMethod(String str, RefedInterface1 refed) {
    assertEquals("cat", str);
    assertEquals("dog", refed.getString());
    return "meth1";
  }

  @Override
  public String overloadedMethod(String str, RefedInterface1 refed, long period, Handler<String> handler) {
    assertEquals("cat", str);
    assertEquals("dog", refed.getString());
    assertEquals(12345l, period);
    assertNotNull(handler);
    handler.handle("giraffe");
    return "meth2";
  }

  @Override
  public String overloadedMethod(String str, Handler<String> handler) {
    assertEquals("cat", str);
    assertNotNull(handler);
    handler.handle("giraffe");
    return "meth3";
  }

  @Override
  public String overloadedMethod(String str, RefedInterface1 refed, Handler<String> handler) {
    assertEquals("cat", str);
    assertEquals("dog", refed.getString());
    assertNotNull(handler);
    handler.handle("giraffe");
    return "meth4";
  }

  @Override
  public void superMethodWithBasicParams(byte b, short s, int i, long l, float f, double d, boolean bool, char ch, String str) {
    assertEquals((byte) 123, b);
    assertEquals((short) 12345, s);
    assertEquals(1234567, i);
    assertEquals(1265615234l, l);
    assertEquals(12.345f, f, 0);
    assertEquals(12.34566d, d, 0);
    assertTrue(bool);
    assertEquals('X', ch);
    assertEquals("foobar", str);
  }

  @Override
  public void otherSuperMethodWithBasicParams(byte b, short s, int i, long l, float f, double d, boolean bool, char ch, String str) {
    superMethodWithBasicParams(b, s, i, l, f, d, bool, ch, str);
  }

  @Override
  public <U> U methodWithGenericReturn(String type) {
    switch (type) {
      case "Boolean": {
        return (U) Boolean.valueOf(true);
      }
      case "Byte": {
        return (U) Byte.valueOf((byte)123);
      }
      case "Short": {
        return (U) Short.valueOf((short)12345);
      }
      case "Integer": {
        return (U) Integer.valueOf(1234567);
      }
      case "Long": {
        return (U) Long.valueOf(1265615234);
      }
      case "Float": {
        return (U) Float.valueOf(12.345f);
      }
      case "Double": {
        return (U) Double.valueOf(12.34566d);
      }
      case "Character": {
        return (U) Character.valueOf('x');
      }
      case "String": {
        return (U) "foo";
      }
      case "Ref": {
        return (U) new RefedInterface1Impl().setString("bar");
      }
      case "JsonObject": {
        return (U) (new JsonObject().put("foo", "hello").put("bar", 123));
      }
      case "JsonObjectLong": {
        // Some languages will convert to Long
        return (U) (new JsonObject().put("foo", "hello").put("bar", 123L));
      }
      case "JsonArray": {
        return (U) (new JsonArray().add("foo").add("bar").add("wib"));
      }
      default:
        throw new AssertionError("Unexpected " + type);
    }
  }

  @Override
  public <U> void methodWithGenericParam(String type, U u) {
    Object expected = methodWithGenericReturn(type);
    assertEquals(expected.getClass(), u.getClass());
    assertEquals(expected, u);
  }

  @Override
  public <U> void methodWithGenericHandler(String type, Handler<U> handler) {
    U value = methodWithGenericReturn(type);
    handler.handle(value);
  }

  @Override
  public <U> void methodWithGenericHandlerAsyncResult(String type, Handler<AsyncResult<U>> asyncResultHandler) {
    U value = methodWithGenericReturn(type);
    asyncResultHandler.handle(Future.succeededFuture(value));
  }

  @Override
  public TestInterface fluentMethod(String str) {
    assertEquals("bar", str);
    return this;
  }

  @Override
  public RefedInterface1 methodWithCachedReturn(String foo) {
    RefedInterface1 refed = new RefedInterface1Impl();
    refed.setString(foo);
    return refed;
  }

  @Override
  public JsonObject methodWithJsonObjectReturn() {
    return new JsonObject().put("cheese", "stilton");
  }

  @Override
  public JsonObject methodWithNullJsonObjectReturn() {
    return null;
  }

  @Override
  public JsonArray methodWithJsonArrayReturn() {
    return new JsonArray().add("socks").add("shoes");
  }

  @Override
  public JsonArray methodWithNullJsonArrayReturn() {
    return null;
  }

  @Override
  public void methodWithJsonParams(JsonObject jsonObject, JsonArray jsonArray) {
    assertNotNull(jsonObject);
    assertNotNull(jsonArray);
    assertEquals("lion", jsonObject.getString("cat"));
    assertEquals("cheddar", jsonObject.getString("cheese"));
    assertEquals("house", jsonArray.getString(0));
    assertEquals("spider", jsonArray.getString(1));
  }

  @Override
  public void methodWithNullJsonParams(JsonObject jsonObject, JsonArray jsonArray) {
    assertNull(jsonObject);
    assertNull(jsonArray);
  }

  @Override
  public void methodWithHandlerJson(Handler<JsonObject> jsonObjectHandler, Handler<JsonArray> jsonArrayHandler) {
    assertNotNull(jsonObjectHandler);
    assertNotNull(jsonArrayHandler);
    jsonObjectHandler.handle(new JsonObject().put("cheese", "stilton"));
    jsonArrayHandler.handle(new JsonArray().add("socks").add("shoes"));
  }

  @Override
  public void methodWithHandlerNullJson(Handler<JsonObject> jsonObjectHandler, Handler<JsonArray> jsonArrayHandler) {
    assertNotNull(jsonObjectHandler);
    assertNotNull(jsonArrayHandler);
    jsonObjectHandler.handle(null);
    jsonArrayHandler.handle(null);
  }

  @Override
  public void methodWithHandlerAsyncResultJsonObject(Handler<AsyncResult<JsonObject>> handler) {
    assertNotNull(handler);
    handler.handle(Future.succeededFuture(new JsonObject().put("cheese", "stilton")));
  }

  @Override
  public void methodWithHandlerAsyncResultNullJsonObject(Handler<AsyncResult<JsonObject>> handler) {
    assertNotNull(handler);
    handler.handle(Future.succeededFuture(null));
  }

  @Override
  public void methodWithHandlerAsyncResultJsonArray(Handler<AsyncResult<JsonArray>> handler) {
    assertNotNull(handler);
    handler.handle(Future.succeededFuture(new JsonArray().add("socks").add("shoes")));
  }

  @Override
  public void methodWithHandlerAsyncResultNullJsonArray(Handler<AsyncResult<JsonArray>> handler) {
    assertNotNull(handler);
    handler.handle(Future.succeededFuture(null));
  }

  @Override
  public Map<String, String> methodWithMapReturn(Handler<String> handler) {
    return new HandlerTestMap<>(handler);
  }

  @Override
  public Map<String, JsonObject> methodWithMapJsonReturn(Handler<String> handler) {
    return new HandlerTestMap<>(handler);
  }

  @Override
  public Map<String, JsonArray> methodWithMapJsonArrayReturn(Handler<String> handler) {
    return new HandlerTestMap<>(handler);
  }

  @Override
  public Map<String, String> methodWithNullMapReturn() {
    return null;
  }

  @Override
  public String methodWithEnumParam(String strVal, TestEnum weirdo) {
    return strVal + weirdo;
  }

  @Override
  public TestEnum methodWithEnumReturn(String strVal) {
    return TestEnum.valueOf(strVal);
  }

  @Override
  public Throwable methodWithThrowableReturn(String strVal) {
    return new Exception(strVal);
  }

  private static class HandlerTestMap<V> implements Map<String, V> {
    private Handler<String> handler;
    private Map<String, V> map;

    private HandlerTestMap(Handler<String> handler) {
      map = new HashMap<>();
      this.handler = handler;
    }

    @Override
    public int size() {
      handler.handle("size()");
      return map.size();
    }

    @Override
    public boolean isEmpty() {
      handler.handle("isEmpty()");
      return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
      handler.handle("containsKey(" + key + ")");
      return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
      handler.handle("containsValue(" + value + ")");
      return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
      handler.handle("get(" + key + ")");
      return map.get(key);
    }

    @Override
    public V put(String key, V value) {
      handler.handle("put(" + key + "," + value + ")");
      return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
      handler.handle("remove(" + key + ")");
      return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
      handler.handle("putAll(m)");
      map.putAll(m);
    }

    @Override
    public void clear() {
      handler.handle("clear()");
      map.clear();
    }

    @Override
    public Set<String> keySet() {
      handler.handle("keySet()");
      return map.keySet();
    }

    @Override
    public Collection<V> values() {
      handler.handle("values()");
      return map.values();
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
      handler.handle("entrySet()");
      return map.entrySet();
    }
  }
}
