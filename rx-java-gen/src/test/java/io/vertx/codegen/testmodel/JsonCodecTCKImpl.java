package io.vertx.codegen.testmodel;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonCodecTCKImpl implements JsonCodecTCK {
  @Override
  public void methodWithTypeToIntegerParam(MyPojoToInteger myPojoToInteger) {
    assertEquals(1, myPojoToInteger.getA());
  }

  @Override
  public void methodWithListOfTypeToIntegerParam(List<MyPojoToInteger> myPojoToIntegerList) {
    assertEquals(2, myPojoToIntegerList.size());
    assertEquals(1, myPojoToIntegerList.get(0).getA());
    assertEquals(2, myPojoToIntegerList.get(1).getA());
  }

  @Override
  public void methodWithSetOfTypeToIntegerParam(Set<MyPojoToInteger> myPojoToIntegerSet) {
    assertEquals(2, myPojoToIntegerSet.size());
    assertTrue(myPojoToIntegerSet.contains(new MyPojoToInteger(1)));
    assertTrue(myPojoToIntegerSet.contains(new MyPojoToInteger(2)));
  }

  @Override
  public void methodWithMapOfTypeToIntegerParam(Map<String, MyPojoToInteger> myPojoToIntegerMap) {
    assertEquals(2, myPojoToIntegerMap.size());
    assertEquals(1, myPojoToIntegerMap.get("a").getA());
    assertEquals(2, myPojoToIntegerMap.get("b").getA());
  }

  @Override
  public MyPojoToInteger methodWithTypeToIntegerReturn() {
    return new MyPojoToInteger(1);
  }

  @Override
  public List<MyPojoToInteger> methodWithListOfTypeToIntegerReturn() {
    return Arrays.asList(new MyPojoToInteger(1), new MyPojoToInteger(2));
  }

  @Override
  public Set<MyPojoToInteger> methodWithSetOfTypeToIntegerReturn() {
    return new HashSet<>(methodWithListOfTypeToIntegerReturn());
  }

  @Override
  public Map<String, MyPojoToInteger> methodWithMapOfTypeToIntegerReturn() {
    Map<String, MyPojoToInteger> map = new HashMap<>();
    map.put("a", new MyPojoToInteger(1));
    map.put("b", new MyPojoToInteger(2));
    return map;
  }

  @Override
  public void methodWithHandlerTypeToIntegerParam(Handler<MyPojoToInteger> myPojoToIntegerHandler) {
    myPojoToIntegerHandler.handle(methodWithTypeToIntegerReturn());
  }

  @Override
  public void methodWithHandlerListOfTypeToIntegerParam(Handler<List<MyPojoToInteger>> myPojoToIntegerListHandler) {
    myPojoToIntegerListHandler.handle(methodWithListOfTypeToIntegerReturn());
  }

  @Override
  public void methodWithHandlerSetOfTypeToIntegerParam(Handler<Set<MyPojoToInteger>> myPojoToIntegerSetHandler) {
    myPojoToIntegerSetHandler.handle(methodWithSetOfTypeToIntegerReturn());
  }

  @Override
  public void methodWithHandlerMapOfTypeToIntegerParam(Handler<Map<String, MyPojoToInteger>> myPojoToIntegerMapHandler) {
    myPojoToIntegerMapHandler.handle(methodWithMapOfTypeToIntegerReturn());
  }

  @Override
  public void methodWithHandlerAsyncResultTypeToIntegerParam(Handler<AsyncResult<MyPojoToInteger>> myPojoToIntegerHandler) {
    myPojoToIntegerHandler.handle(Future.succeededFuture(methodWithTypeToIntegerReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultListOfTypeToIntegerParam(Handler<AsyncResult<List<MyPojoToInteger>>> myPojoToIntegerListHandler) {
    myPojoToIntegerListHandler.handle(Future.succeededFuture(methodWithListOfTypeToIntegerReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultSetOfTypeToIntegerParam(Handler<AsyncResult<Set<MyPojoToInteger>>> myPojoToIntegerSetHandler) {
    myPojoToIntegerSetHandler.handle(Future.succeededFuture(methodWithSetOfTypeToIntegerReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultMapOfTypeToIntegerParam(Handler<AsyncResult<Map<String, MyPojoToInteger>>> myPojoToIntegerMapHandler) {
    myPojoToIntegerMapHandler.handle(Future.succeededFuture(methodWithMapOfTypeToIntegerReturn()));
  }

  @Override
  public void methodWithTypeToStringParam(ZonedDateTime zonedDateTime) {
    assertEquals(ZonedDateTime.parse("2019-04-03T14:30:05.083+02:00[Europe/Rome]"), zonedDateTime);
  }

  @Override
  public void methodWithListOfTypeToStringParam(List<ZonedDateTime> zonedDateTimeList) {
    assertEquals(2, zonedDateTimeList.size());
    assertEquals(ZonedDateTime.parse("2019-04-03T14:30:05.083+02:00[Europe/Rome]"), zonedDateTimeList.get(0));
    assertEquals(ZonedDateTime.parse("2019-04-04T14:30:05.083+02:00[Europe/Rome]"), zonedDateTimeList.get(1));
  }

  @Override
  public void methodWithSetOfTypeToStringParam(Set<ZonedDateTime> zonedDateTimeSet) {
    assertEquals(2, zonedDateTimeSet.size());
    assertTrue(zonedDateTimeSet.contains(ZonedDateTime.parse("2019-04-03T14:30:05.083+02:00[Europe/Rome]")));
    assertTrue(zonedDateTimeSet.contains(ZonedDateTime.parse("2019-04-04T14:30:05.083+02:00[Europe/Rome]")));
  }

  @Override
  public void methodWithMapOfTypeToStringParam(Map<String, ZonedDateTime> zonedDateTimeMap) {
    assertEquals(2, zonedDateTimeMap.size());
    assertEquals(ZonedDateTime.parse("2019-04-03T14:30:05.083+02:00[Europe/Rome]"), zonedDateTimeMap.get("a"));
    assertEquals(ZonedDateTime.parse("2019-04-04T14:30:05.083+02:00[Europe/Rome]"), zonedDateTimeMap.get("b"));
  }

  @Override
  public ZonedDateTime methodWithTypeToStringReturn() {
    return ZonedDateTime.parse("2019-04-03T14:30:05.083+02:00[Europe/Rome]");
  }

  @Override
  public List<ZonedDateTime> methodWithListOfTypeToStringReturn() {
    return Arrays.asList(ZonedDateTime.parse("2019-04-03T14:30:05.083+02:00[Europe/Rome]"), ZonedDateTime.parse("2019-04-04T14:30:05.083+02:00[Europe/Rome]"));
  }

  @Override
  public Set<ZonedDateTime> methodWithSetOfTypeToStringReturn() {
    return new HashSet<>(methodWithListOfTypeToStringReturn());
  }

  @Override
  public Map<String, ZonedDateTime> methodWithMapOfTypeToStringReturn() {
    Map<String, ZonedDateTime> zonedDateTimeMap = new HashMap<>();
    zonedDateTimeMap.put("a", ZonedDateTime.parse("2019-04-03T14:30:05.083+02:00[Europe/Rome]"));
    zonedDateTimeMap.put("b", ZonedDateTime.parse("2019-04-04T14:30:05.083+02:00[Europe/Rome]"));
    return zonedDateTimeMap;
  }

  @Override
  public void methodWithHandlerTypeToStringParam(Handler<ZonedDateTime> zonedDateTimeHandler) {
    zonedDateTimeHandler.handle(methodWithTypeToStringReturn());
  }

  @Override
  public void methodWithHandlerListOfTypeToStringParam(Handler<List<ZonedDateTime>> zonedDateTimeListHandler) {
    zonedDateTimeListHandler.handle(methodWithListOfTypeToStringReturn());
  }

  @Override
  public void methodWithHandlerSetOfTypeToStringParam(Handler<Set<ZonedDateTime>> zonedDateTimeSetHandler) {
    zonedDateTimeSetHandler.handle(methodWithSetOfTypeToStringReturn());
  }

  @Override
  public void methodWithHandlerMapOfTypeToStringParam(Handler<Map<String, ZonedDateTime>> zonedDateTimeMapHandler) {
    zonedDateTimeMapHandler.handle(methodWithMapOfTypeToStringReturn());
  }

  @Override
  public void methodWithHandlerAsyncResultTypeToStringParam(Handler<AsyncResult<ZonedDateTime>> zonedDateTimeHandler) {
    zonedDateTimeHandler.handle(Future.succeededFuture(methodWithTypeToStringReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultListOfTypeToStringParam(Handler<AsyncResult<List<ZonedDateTime>>> zonedDateTimeListHandler) {
    zonedDateTimeListHandler.handle(Future.succeededFuture(methodWithListOfTypeToStringReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultSetOfTypeToStringParam(Handler<AsyncResult<Set<ZonedDateTime>>> zonedDateTimeSetHandler) {
    zonedDateTimeSetHandler.handle(Future.succeededFuture(methodWithSetOfTypeToStringReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultMapOfTypeToStringParam(Handler<AsyncResult<Map<String, ZonedDateTime>>> zonedDateTimeMapHandler) {
    zonedDateTimeMapHandler.handle(Future.succeededFuture(methodWithMapOfTypeToStringReturn()));
  }

  @Override
  public void methodWithTypeToJsonArrayParam(MyPojoToJsonArray myPojoToJsonArray) {
    assertEquals(new MyPojoToJsonArray(Arrays.asList(1, 2, 3)), myPojoToJsonArray);
  }

  @Override
  public void methodWithListOfTypeToJsonArrayParam(List<MyPojoToJsonArray> myPojoToJsonArrayList) {
    assertEquals(2, myPojoToJsonArrayList.size());
    assertEquals(new MyPojoToJsonArray(Arrays.asList(1, 2, 3)), myPojoToJsonArrayList.get(0));
    assertEquals(new MyPojoToJsonArray(Arrays.asList(4, 5, 6)), myPojoToJsonArrayList.get(1));
  }

  @Override
  public void methodWithSetOfTypeToJsonArrayParam(Set<MyPojoToJsonArray> myPojoToJsonArraySet) {
    assertEquals(2, myPojoToJsonArraySet.size());
    assertTrue(myPojoToJsonArraySet.contains(new MyPojoToJsonArray(Arrays.asList(1, 2, 3))));
    assertTrue(myPojoToJsonArraySet.contains(new MyPojoToJsonArray(Arrays.asList(4, 5, 6))));
  }

  @Override
  public void methodWithMapOfTypeToJsonArrayParam(Map<String, MyPojoToJsonArray> myPojoToJsonArrayMap) {
    assertEquals(2, myPojoToJsonArrayMap.size());
    assertEquals(new MyPojoToJsonArray(Arrays.asList(1, 2, 3)), myPojoToJsonArrayMap.get("a"));
    assertEquals(new MyPojoToJsonArray(Arrays.asList(4, 5, 6)), myPojoToJsonArrayMap.get("b"));
  }

  @Override
  public MyPojoToJsonArray methodWithTypeToJsonArrayReturn() {
    return new MyPojoToJsonArray(Arrays.asList(1, 2, 3));
  }

  @Override
  public List<MyPojoToJsonArray> methodWithListOfTypeToJsonArrayReturn() {
    return Arrays.asList(new MyPojoToJsonArray(Arrays.asList(1, 2, 3)), new MyPojoToJsonArray(Arrays.asList(4, 5, 6)));
  }

  @Override
  public Set<MyPojoToJsonArray> methodWithSetOfTypeToJsonArrayReturn() {
    return new HashSet<>(methodWithListOfTypeToJsonArrayReturn());
  }

  @Override
  public Map<String, MyPojoToJsonArray> methodWithMapOfTypeToJsonArrayReturn() {
    Map<String, MyPojoToJsonArray> map = new HashMap<>();
    map.put("a", new MyPojoToJsonArray(Arrays.asList(1, 2, 3)));
    map.put("b", new MyPojoToJsonArray(Arrays.asList(4, 5, 6)));
    return map;
  }

  @Override
  public void methodWithHandlerTypeToJsonArrayParam(Handler<MyPojoToJsonArray> myPojoToJsonArrayHandler) {
    myPojoToJsonArrayHandler.handle(methodWithTypeToJsonArrayReturn());
  }

  @Override
  public void methodWithHandlerListOfTypeToJsonArrayParam(Handler<List<MyPojoToJsonArray>> myPojoToJsonArrayListHandler) {
    myPojoToJsonArrayListHandler.handle(methodWithListOfTypeToJsonArrayReturn());
  }

  @Override
  public void methodWithHandlerSetOfTypeToJsonArrayParam(Handler<Set<MyPojoToJsonArray>> myPojoToJsonArraySetHandler) {
    myPojoToJsonArraySetHandler.handle(methodWithSetOfTypeToJsonArrayReturn());
  }

  @Override
  public void methodWithHandlerMapOfTypeToJsonArrayParam(Handler<Map<String, MyPojoToJsonArray>> myPojoToJsonArrayMapHandler) {
    myPojoToJsonArrayMapHandler.handle(methodWithMapOfTypeToJsonArrayReturn());
  }

  @Override
  public void methodWithHandlerAsyncResultTypeToJsonArrayParam(Handler<AsyncResult<MyPojoToJsonArray>> myPojoToJsonArrayHandler) {
    myPojoToJsonArrayHandler.handle(Future.succeededFuture(methodWithTypeToJsonArrayReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultListOfTypeToJsonArrayParam(Handler<AsyncResult<List<MyPojoToJsonArray>>> myPojoToJsonArrayListHandler) {
    myPojoToJsonArrayListHandler.handle(Future.succeededFuture(methodWithListOfTypeToJsonArrayReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultSetOfTypeToJsonArrayParam(Handler<AsyncResult<Set<MyPojoToJsonArray>>> myPojoToJsonArraySetHandler) {
    myPojoToJsonArraySetHandler.handle(Future.succeededFuture(methodWithSetOfTypeToJsonArrayReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultMapOfTypeToJsonArrayParam(Handler<AsyncResult<Map<String, MyPojoToJsonArray>>> myPojoToJsonArrayMapHandler) {
    myPojoToJsonArrayMapHandler.handle(Future.succeededFuture(methodWithMapOfTypeToJsonArrayReturn()));
  }

  @Override
  public void methodWithTypeToJsonObjectParam(MyPojoToJsonObject myPojoToJsonObject) {
    assertEquals(new MyPojoToJsonObject(1), myPojoToJsonObject);
  }

  @Override
  public void methodWithListOfTypeToJsonObjectParam(List<MyPojoToJsonObject> myPojoToJsonObjectList) {
    assertEquals(2, myPojoToJsonObjectList.size());
    assertEquals(new MyPojoToJsonObject(1), myPojoToJsonObjectList.get(0));
    assertEquals(new MyPojoToJsonObject(2), myPojoToJsonObjectList.get(1));
  }

  @Override
  public void methodWithSetOfTypeToJsonObjectParam(Set<MyPojoToJsonObject> myPojoToJsonObjectSet) {
    assertEquals(2, myPojoToJsonObjectSet.size());
    assertTrue(myPojoToJsonObjectSet.contains(new MyPojoToJsonObject(1)));
    assertTrue(myPojoToJsonObjectSet.contains(new MyPojoToJsonObject(2)));
  }

  @Override
  public void methodWithMapOfTypeToJsonObjectParam(Map<String, MyPojoToJsonObject> myPojoToJsonObjectMap) {
    assertEquals(2, myPojoToJsonObjectMap.size());
    assertEquals(new MyPojoToJsonObject(1), myPojoToJsonObjectMap.get("a"));
    assertEquals(new MyPojoToJsonObject(2), myPojoToJsonObjectMap.get("b"));
  }

  @Override
  public MyPojoToJsonObject methodWithTypeToJsonObjectReturn() {
    return new MyPojoToJsonObject(1);
  }

  @Override
  public List<MyPojoToJsonObject> methodWithListOfTypeToJsonObjectReturn() {
    return Arrays.asList(new MyPojoToJsonObject(1), new MyPojoToJsonObject(2));
  }

  @Override
  public Set<MyPojoToJsonObject> methodWithSetOfTypeToJsonObjectReturn() {
    return new HashSet<>(methodWithListOfTypeToJsonObjectReturn());
  }

  @Override
  public Map<String, MyPojoToJsonObject> methodWithMapOfTypeToJsonObjectReturn() {
    Map<String, MyPojoToJsonObject> map = new HashMap<>();
    map.put("a", new MyPojoToJsonObject(1));
    map.put("b", new MyPojoToJsonObject(2));
    return map;
  }

  @Override
  public void methodWithHandlerTypeToJsonObjectParam(Handler<MyPojoToJsonObject> myPojoToJsonObjectHandler) {
    myPojoToJsonObjectHandler.handle(methodWithTypeToJsonObjectReturn());
  }

  @Override
  public void methodWithHandlerListOfTypeToJsonObjectParam(Handler<List<MyPojoToJsonObject>> myPojoToJsonObjectListHandler) {
    myPojoToJsonObjectListHandler.handle(methodWithListOfTypeToJsonObjectReturn());
  }

  @Override
  public void methodWithHandlerSetOfTypeToJsonObjectParam(Handler<Set<MyPojoToJsonObject>> myPojoToJsonObjectSetHandler) {
    myPojoToJsonObjectSetHandler.handle(methodWithSetOfTypeToJsonObjectReturn());
  }

  @Override
  public void methodWithHandlerMapOfTypeToJsonObjectParam(Handler<Map<String, MyPojoToJsonObject>> myPojoToJsonObjectMapHandler) {
    myPojoToJsonObjectMapHandler.handle(methodWithMapOfTypeToJsonObjectReturn());
  }

  @Override
  public void methodWithHandlerAsyncResultTypeToJsonObjectParam(Handler<AsyncResult<MyPojoToJsonObject>> myPojoToJsonObjectHandler) {
    myPojoToJsonObjectHandler.handle(Future.succeededFuture(methodWithTypeToJsonObjectReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultListOfTypeToJsonObjectParam(Handler<AsyncResult<List<MyPojoToJsonObject>>> myPojoToJsonObjectListHandler) {
    myPojoToJsonObjectListHandler.handle(Future.succeededFuture(methodWithListOfTypeToJsonObjectReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultSetOfTypeToJsonObjectParam(Handler<AsyncResult<Set<MyPojoToJsonObject>>> myPojoToJsonObjectSetHandler) {
    myPojoToJsonObjectSetHandler.handle(Future.succeededFuture(methodWithSetOfTypeToJsonObjectReturn()));
  }

  @Override
  public void methodWithHandlerAsyncResultMapOfTypeToJsonObjectParam(Handler<AsyncResult<Map<String, MyPojoToJsonObject>>> myPojoToJsonObjectMapHandler) {
    myPojoToJsonObjectMapHandler.handle(Future.succeededFuture(methodWithMapOfTypeToJsonObjectReturn()));
  }
}
