package io.vertx.rx.java.test.gen;

import io.vertx.codegen.testmodel.CollectionTCKImpl;
import io.vertx.codegen.testmodel.RefedInterface1Impl;
import io.vertx.codegen.testmodel.TestDataObject;
import io.vertx.codegen.testmodel.TestEnum;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.codegen.testmodel.CollectionTCK;
import io.vertx.rxjava.codegen.testmodel.RefedInterface1;
import io.vertx.rxjava.codegen.testmodel.RefedInterface2;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

import static io.vertx.rx.java.test.gen.ApiTCKTest.get;
import static io.vertx.rx.java.test.gen.ApiTCKTest.set;
import static io.vertx.rx.java.test.gen.ApiTCKTest.list;
import static io.vertx.rx.java.test.gen.ApiTCKTest.map;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CollectionTCKTest {

  final CollectionTCK obj = new CollectionTCK(new CollectionTCKImpl());

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
    assertEquals(Arrays.asList("foo", "bar", "wibble"), get(obj.methodWithHandlerAsyncResultListStringObservable()));
    assertEquals(Arrays.asList(5, 12, 100), get(obj.methodWithHandlerAsyncResultListIntegerObservable()));
    assertEquals(new HashSet<>(Arrays.asList("foo", "bar", "wibble")), get(obj.methodWithHandlerAsyncResultSetStringObservable()));
    assertEquals(new HashSet<>(Arrays.asList(5, 12, 100)), get(obj.methodWithHandlerAsyncResultSetIntegerObservable()));
  }

  @Test
  public void testMethodWithHandlerListVertxGen() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListVertxGen(checker.<List<RefedInterface1>>resultHandler(it ->
        assertEquals(Arrays.asList("foo", "bar"), it.stream().map(RefedInterface1::getString).collect(Collectors.toList()))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerListAbstractVertxGen() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListAbstractVertxGen(checker.<List<RefedInterface2>>resultHandler(it ->
        assertEquals(Arrays.asList("abstractfoo", "abstractbar"), it.stream().map(RefedInterface2::getString).collect(Collectors.toList()))));
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
  public void testMethodWithHandlerAsyncResultListAbstractVertxGen() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultListAbstractVertxGen(
        checker.<List<RefedInterface2>>asyncResultHandler(event -> {
          assertEquals(2, event.size());
          assertEquals("abstractfoo", event.get(0).getString());
          assertEquals("abstractbar", event.get(1).getString());
        }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureListVertxGen() throws Exception {
    List<RefedInterface1> result = get(obj.methodWithHandlerAsyncResultListVertxGenObservable());
    assertEquals(2, result.size());
    assertEquals("foo", result.get(0).getString());
    assertEquals("bar", result.get(1).getString());
  }

  @Test
  public void testMethodWithHandlerSetVertxGen() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetVertxGen(checker.<Set<RefedInterface1>>resultHandler(event -> {
      List<String> list = event.stream().map(it -> it.getString()).collect(Collectors.toList());
      Collections.sort(list);
      assertEquals(Arrays.asList("bar", "foo"), list);
    }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerSetAbstractVertxGen() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetAbstractVertxGen(checker.<Set<RefedInterface2>>resultHandler(event -> {
      List<String> list = event.stream().map(it -> it.getString()).collect(Collectors.toList());
      Collections.sort(list);
      assertEquals(Arrays.asList("abstractbar", "abstractfoo"), list);
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
  public void testMethodWithHandlerAsyncResultSetAbstractVertxGen() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultSetAbstractVertxGen(
        checker.<Set<RefedInterface2>>asyncResultHandler(event -> {
          List<String> list = event.stream().map(RefedInterface2::getString).collect(Collectors.toList());
          Collections.sort(list);
          assertEquals(Arrays.asList("abstractbar", "abstractfoo"), list);
        }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureSetVertxGen() throws Exception {
    Set<RefedInterface1> result = get(obj.methodWithHandlerAsyncResultSetVertxGenObservable());
    List<String> list = result.stream().map(RefedInterface1::getString).collect(Collectors.toList());
    Collections.sort(list);
    assertEquals(Arrays.asList("bar", "foo"), list);
  }

  @Test
  public void testMethodWithHandlerListJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListJsonObject(checker.expectedResult(
        Arrays.asList(new JsonObject().put("cheese", "stilton"), new JsonObject().put("socks", "tartan"))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultListJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultListJsonObject(
        checker.asyncExpectedResult(Arrays.asList(new JsonObject().put("cheese", "stilton"), new JsonObject().put("socks", "tartan")))
    );
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureListJsonObject() throws Exception {
    List<JsonObject> result = get(obj.methodWithHandlerAsyncResultListJsonObjectObservable());
    assertEquals(Arrays.asList(new JsonObject().put("cheese", "stilton"), new JsonObject().put("socks", "tartan")), result);
  }

  @Test
  public void testMethodWithHandlerSetJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetJsonObject(checker.<Set<JsonObject>>resultHandler(r -> {
      assertEquals(Arrays.asList(new JsonObject().put("cheese", "stilton"), new JsonObject().put("socks", "tartan")), new ArrayList<>(r));
    }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultSetJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultSetJsonObject(checker.<Set<JsonObject>>asyncResultHandler(it ->
        assertEquals(Arrays.asList(new JsonObject().put("cheese", "stilton"), new JsonObject().put("socks", "tartan")), new ArrayList<>(it))
    ));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureSetJsonObject() throws Exception {
    Set<JsonObject> result = get(obj.methodWithHandlerAsyncResultSetJsonObjectObservable());
    assertEquals(Arrays.asList(new JsonObject().put("cheese", "stilton"), new JsonObject().put("socks", "tartan")), new ArrayList<>(result));
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
  public void testMethodWithFutureListJsonArray() throws Exception {
    List<JsonArray> result = get(obj.methodWithHandlerAsyncResultListJsonArrayObservable());
    assertEquals(result, Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple")));
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
  public void testMethodWithFutureSetJsonArray() throws Exception {
    Set<JsonArray> result = get(obj.methodWithHandlerAsyncResultSetJsonArrayObservable());
    assertEquals(Arrays.asList(new JsonArray().add("green").add("blue"), new JsonArray().add("yellow").add("purple")), new ArrayList<>(result));
  }

  @Test
  public void testMethodWithHandlerListDataObject() throws Exception {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListDataObject(checker.<List<TestDataObject>>resultHandler(list -> {
      assertEquals(2, list.size());
      assertEquals("String 1", list.get(0).getFoo());
      assertEquals(1, list.get(0).getBar());
      assertEquals(1.1, list.get(0).getWibble(), 0);
      assertEquals("String 2", list.get(1).getFoo());
      assertEquals(2, list.get(1).getBar());
      assertEquals(2.2, list.get(1).getWibble(), 0);
    }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerSetDataObject() throws Exception {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetDataObject(checker.<Set<TestDataObject>>resultHandler(set -> {
      List<TestDataObject> list = new ArrayList<>(set);
      Collections.sort(list, (c1, c2) -> ((Integer) c1.getBar()).compareTo(c2.getBar()));
      assertEquals("String 1", list.get(0).getFoo());
      assertEquals(1, list.get(0).getBar());
      assertEquals(1.1, list.get(0).getWibble(), 0);
      assertEquals("String 2", list.get(1).getFoo());
      assertEquals(2, list.get(1).getBar());
      assertEquals(2.2, list.get(1).getWibble(), 0);
    }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultListDataObject() throws Exception {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultListDataObject(checker.<List<TestDataObject>>asyncResultHandler(list -> {
      assertEquals(2, list.size());
      assertEquals("String 1", list.get(0).getFoo());
      assertEquals(1, list.get(0).getBar());
      assertEquals(1.1, list.get(0).getWibble(), 0);
      assertEquals("String 2", list.get(1).getFoo());
      assertEquals(2, list.get(1).getBar());
      assertEquals(2.2, list.get(1).getWibble(), 0);
    }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultSetDataObject() throws Exception {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultSetDataObject(checker.<Set<TestDataObject>>asyncResultHandler(set -> {
      List<TestDataObject> list = new ArrayList<>(set);
      Collections.sort(list, (c1, c2) -> ((Integer) c1.getBar()).compareTo(c2.getBar()));
      assertEquals("String 1", list.get(0).getFoo());
      assertEquals(1, list.get(0).getBar());
      assertEquals(1.1, list.get(0).getWibble(), 0);
      assertEquals("String 2", list.get(1).getFoo());
      assertEquals(2, list.get(1).getBar());
      assertEquals(2.2, list.get(1).getWibble(), 0);
    }));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerListEnum() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerListEnum(checker.expectedResult(Arrays.asList(TestEnum.TIM, TestEnum.JULIEN)));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerSetEnum() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerSetEnum(checker.expectedResult(set(TestEnum.TIM, TestEnum.JULIEN)));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerListAsyncResultEnum() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultListEnum(checker.asyncExpectedResult(Arrays.asList(TestEnum.TIM, TestEnum.JULIEN)));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultSetEnum() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithHandlerAsyncResultSetEnum(checker.asyncExpectedResult(set(TestEnum.TIM, TestEnum.JULIEN)));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodListParams() {
    RefedInterface1 refed1 = new RefedInterface1(new RefedInterface1Impl());
    refed1.setString("foo");
    RefedInterface1 refed2 = new RefedInterface1(new RefedInterface1Impl());
    refed2.setString("bar");
    obj.methodWithListParams(Arrays.asList("foo", "bar"), Arrays.asList((byte) 2, (byte) 3), Arrays.asList((short) 12, (short) 13),
        Arrays.asList(1234, 1345), Arrays.asList(123l, 456l), Arrays.asList(new JsonObject().put("foo", "bar"), new JsonObject().put("eek", "wibble")),
        Arrays.asList(new JsonArray().add("foo"), new JsonArray().add("blah")), Arrays.asList(refed1, refed2),
        Arrays.asList(new TestDataObject().setFoo("String 1").setBar(1).setWibble(1.1), new TestDataObject().setFoo("String 2").setBar(2).setWibble(2.2)),
        Arrays.asList(TestEnum.JULIEN, TestEnum.TIM),
        Arrays.asList("foo", 4, 3.4, true, new JsonObject().put("wibble", "eek"), new JsonArray().add("one").add(2))
    );
  }

  @Test
  public void testMethodSetParams() {
    RefedInterface1 refed1 = new RefedInterface1(new RefedInterface1Impl());
    refed1.setString("foo");
    RefedInterface1 refed2 = new RefedInterface1(new RefedInterface1Impl());
    refed2.setString("bar");
    obj.methodWithSetParams(set("foo", "bar"), set((byte) 2, (byte) 3), set((short) 12, (short) 13),
        set(1234, 1345), set(123l, 456l), set(new JsonObject().put("foo", "bar"), new JsonObject().put("eek", "wibble")),
        set(new JsonArray().add("foo"), new JsonArray().add("blah")), set(refed1, refed2),
        set(new TestDataObject().setFoo("String 1").setBar(1).setWibble(1.1), new TestDataObject().setFoo("String 2").setBar(2).setWibble(2.2)),
        set(TestEnum.TIM, TestEnum.JULIEN),
        set("foo", 4, 3.4, true, new JsonObject().put("wibble", "eek"), new JsonArray().add("one").add(2)));
  }

  @Test
  public void testMethodMapParams() {
    RefedInterface1 refed1 = new RefedInterface1(new RefedInterface1Impl());
    refed1.setString("foo");
    RefedInterface1 refed2 = new RefedInterface1(new RefedInterface1Impl());
    refed2.setString("bar");
    Map<String, Object> m = new HashMap<>();
    m.put("string", "foo");
    m.put("integer", 4);
    m.put("float", 3.4);
    m.put("boolean", true);
    m.put("object", new JsonObject().put("wibble", "eek"));
    m.put("array", new JsonArray().add("one").add(2));
    obj.methodWithMapParams(map("foo", "bar", "eek", "wibble"), map("foo", (byte) 2, "eek", (byte) 3),
        map("foo", (short) 12, "eek", (short) 13),
        map("foo", 1234, "eek", 1345), map("foo", 123l, "eek", 456l), map("foo", new JsonObject().put("foo", "bar"), "eek", new JsonObject().put("eek", "wibble")),
        map("foo", new JsonArray().add("foo"), "eek", new JsonArray().add("blah")), map("foo", refed1, "eek", refed2),
        m
      );
  }

  @Test
  public void testListStringReturn() {
    assertEquals(Arrays.asList("foo", "bar", "wibble"), obj.methodWithListStringReturn());
  }

  @Test
  public void testListLongReturn() {
    assertEquals(list(123l, 456l), obj.methodWithListLongReturn());
  }

  @Test
  public void testListJsonObjectReturn() {
    List<JsonObject> list = obj.methodWithListJsonObjectReturn();
    assertEquals(2, list.size());
    JsonObject json1 = list.get(0);
    assertEquals("bar", json1.getValue("foo"));
    JsonObject json2 = list.get(1);
    assertEquals("eek", json2.getValue("blah"));
  }

  @Test
  public void testListJsonArrayReturn() {
    List<JsonArray> list = obj.methodWithListJsonArrayReturn();
    assertEquals(2, list.size());
    JsonArray json1 = list.get(0);
    assertEquals("foo", json1.getValue(0));
    JsonArray json2 = list.get(1);
    assertEquals("blah", json2.getValue(0));
  }

  @Test
  public void testListVertxGenReturn() {
    List<RefedInterface1> list = obj.methodWithListVertxGenReturn();
    assertEquals(2, list.size());
    RefedInterface1 refed1 = list.get(0);
    RefedInterface1 refed2 = list.get(1);
    assertEquals("foo", refed1.getString());
    assertEquals("bar", refed2.getString());
  }

  @Test
  public void testListDataObjectReturn() {
    List<TestDataObject> list = obj.methodWithListDataObjectReturn();
    assertEquals(2, list.size());
    assertEquals("String 1", list.get(0).getFoo());
    assertEquals(1, list.get(0).getBar());
    assertEquals(1.1, list.get(0).getWibble(), 0);
    assertEquals("String 2", list.get(1).getFoo());
    assertEquals(2, list.get(1).getBar());
    assertEquals(2.2, list.get(1).getWibble(), 0);
  }

  @Test
  public void testSetStringReturn() {
    assertEquals(new HashSet<>(Arrays.asList("foo", "bar", "wibble")), obj.methodWithSetStringReturn());
  }

  @Test
  public void testSetLongReturn() {
    assertEquals(set(123l, 456l), obj.methodWithSetLongReturn());
  }

  @Test
  public void testSetJsonObjectReturn() {
    Set<JsonObject> set = obj.methodWithSetJsonObjectReturn();
    assertEquals(2, set.size());
    JsonObject json1 = new JsonObject();
    json1.put("foo", "bar");
    assertTrue(set.contains(json1));
    JsonObject json2 = new JsonObject();
    json2.put("blah", "eek");
    assertTrue(set.contains(json2));
  }

  @Test
  public void testSetJsonArrayReturn() {
    Set<JsonArray> set = obj.methodWithSetJsonArrayReturn();
    assertEquals(2, set.size());
    JsonArray json1 = new JsonArray();
    json1.add("foo");
    assertTrue(set.contains(json1));
    JsonArray json2 = new JsonArray();
    json2.add("blah");
    assertTrue(set.contains(json2));
  }

  @Test
  public void testSetVertxGenReturn() {
    Set<RefedInterface1> set = obj.methodWithSetVertxGenReturn();
    assertEquals(2, set.size());
    RefedInterface1 refed1 = new RefedInterface1(new RefedInterface1Impl());
    refed1.setString("foo");
    RefedInterface1 refed2 = new RefedInterface1(new RefedInterface1Impl());
    refed2.setString("bar");
    List<RefedInterface1> list = new ArrayList<>(set);
    assertTrue((list.get(0).getString().equals("foo") && list.get(1).getString().equals("bar")) || (list.get(0).getString().equals("bar") && list.get(1).getString().equals("foo")));
  }

  @Test
  public void testSetDataObjectReturn() {
    Set<TestDataObject> set = obj.methodWithSetDataObjectReturn();
    List<TestDataObject> list = new ArrayList<>(set);
    Collections.sort(list, (c1, c2) -> ((Integer) c1.getBar()).compareTo(c2.getBar()));
    assertEquals("String 1", list.get(0).getFoo());
    assertEquals(1, list.get(0).getBar());
    assertEquals(1.1, list.get(0).getWibble(), 0);
    assertEquals("String 2", list.get(1).getFoo());
    assertEquals(2, list.get(1).getBar());
    assertEquals(2.2, list.get(1).getWibble(), 0);
  }

  @Test
  public void testMapStringReturn() {
    Map<String, String> map = obj.methodWithMapStringReturn(s -> {
    });
    assertEquals("bar", map.get("foo"));
  }

  @Test
  public void testMapLongReturn() {
    Map<String, Long> map = obj.methodWithMapLongReturn(s -> {
    });
    assertEquals(123l, (long)map.get("foo"));
  }

  @Test
  public void testMapJsonObjectReturn() {
    Map<String, JsonObject> map = obj.methodWithMapJsonObjectReturn(s -> {});
    JsonObject m = map.get("foo");
    assertEquals("eek", m.getValue("wibble"));
  }

  @Test
  public void testMapJsonArrayReturn() {
    Map<String, JsonArray> map = obj.methodWithMapJsonArrayReturn(s -> {
    });
    JsonArray m = map.get("foo");
    assertEquals("wibble", m.getValue(0));
  }

  @Test
  public void testMapObjectReturn() {
    Map<String, Object> map = obj.methodWithMapObjectReturn(s -> {
    });
    assertEquals("foo", map.get("string"));
    assertEquals(4, map.get("integer"));
    assertEquals(3.4f, map.get("float"));
    assertEquals(true, map.get("boolean"));
    assertEquals(new JsonObject().put("wibble", "eek"), map.get("object"));
    assertEquals(new JsonArray().add("one").add(2), map.get("array"));
  }

  @Test
  public void testListEnumReturn() {
    assertEquals(Arrays.asList(TestEnum.JULIEN, TestEnum.TIM), obj.methodWithListEnumReturn());
  }

  @Test
  public void testListObjectReturn() {
    List<Object> list = obj.methodWithListObjectReturn();
    assertEquals("foo", list.get(0));
    assertEquals(4, list.get(1));
    assertEquals(3.4f, list.get(2));
    assertEquals(true, list.get(3));
    assertEquals(new JsonObject().put("wibble", "eek"), list.get(4));
    assertEquals(new JsonArray().add("one").add(2), list.get(5));
  }

  @Test
  public void testsetEnumReturn() {
    assertEquals(set(TestEnum.JULIEN, TestEnum.TIM), obj.methodWithSetEnumReturn());
  }

  @Test
  public void testSetObjectReturn() {
    List<Object> list = new ArrayList<>(obj.methodWithSetObjectReturn());
    assertEquals("foo", list.get(0));
    assertEquals(4, list.get(1));
    assertEquals(3.4f, list.get(2));
    assertEquals(true, list.get(3));
    assertEquals(new JsonObject().put("wibble", "eek"), list.get(4));
    assertEquals(new JsonArray().add("one").add(2), list.get(5));
  }
}
