package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@VertxGen
public interface JsonMapperTCK {

  @GenIgnore
  static Integer serializeMyPojoToInteger(MyPojoToInteger value) {
    return value.getA();
  }

  @GenIgnore
  static MyPojoToInteger deserializeMyPojoToInteger(Integer value) {
    return new MyPojoToInteger(value);
  }

  @GenIgnore
  static JsonObject serializeMyPojoToJsonObject(MyPojoToJsonObject value) {
    return new JsonObject().put("v", value.getV());
  }

  @GenIgnore
  static MyPojoToJsonObject deserializeMyPojoToJsonObject(JsonObject value) {
    return new MyPojoToJsonObject(value.getInteger("v"));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @GenIgnore
  static JsonArray serializeMyPojoToJsonArray(MyPojoToJsonArray value) {
    return new JsonArray((List)value.stuff);
  }

  @GenIgnore
  static MyPojoToJsonArray deserializeMyPojoToJsonArray(JsonArray value) {
    return new MyPojoToJsonArray(value.stream().map(j -> (Integer)j).collect(Collectors.toList()));
  }

  @GenIgnore
  static String serializeZonedDateTime(ZonedDateTime value) {
    return value.toString();
  }

  @GenIgnore
  static ZonedDateTime deserializeZonedDateTime(String value) {
    return ZonedDateTime.parse(value);
  }

  @GenIgnore
  static String serializeCustomEnum(TestCustomEnum value) {
    return (value != null) ? value.getShortName() : null;
  }

  @GenIgnore
  static TestCustomEnum deserializeCustomEnum(String value) {
    return (value != null) ? TestCustomEnum.of(value) : null;
  }

  // Java Type <-> Integer

  void methodWithTypeToIntegerParam(MyPojoToInteger myPojoToInteger);
  void methodWithListOfTypeToIntegerParam(List<MyPojoToInteger> myPojoToIntegerList);
  void methodWithSetOfTypeToIntegerParam(Set<MyPojoToInteger> myPojoToIntegerSet);
  void methodWithMapOfTypeToIntegerParam(Map<String, MyPojoToInteger> myPojoToIntegerMap);

  MyPojoToInteger methodWithTypeToIntegerReturn();
  List<MyPojoToInteger> methodWithListOfTypeToIntegerReturn();
  Set<MyPojoToInteger> methodWithSetOfTypeToIntegerReturn();
  Map<String, MyPojoToInteger> methodWithMapOfTypeToIntegerReturn();

  void methodWithHandlerTypeToIntegerParam(Handler<MyPojoToInteger> myPojoToIntegerHandler);
  void methodWithHandlerListOfTypeToIntegerParam(Handler<List<MyPojoToInteger>> myPojoToIntegerListHandler);
  void methodWithHandlerSetOfTypeToIntegerParam(Handler<Set<MyPojoToInteger>> myPojoToIntegerSetHandler);
  void methodWithHandlerMapOfTypeToIntegerParam(Handler<Map<String, MyPojoToInteger>> myPojoToIntegerMapHandler);

  Future<MyPojoToInteger> methodWithHandlerAsyncResultTypeToIntegerParam();
  Future<List<MyPojoToInteger>> methodWithHandlerAsyncResultListOfTypeToIntegerParam();
  Future<Set<MyPojoToInteger>> methodWithHandlerAsyncResultSetOfTypeToIntegerParam();
  Future<Map<String, MyPojoToInteger>> methodWithHandlerAsyncResultMapOfTypeToIntegerParam();

  // Java type <-> String

  void methodWithTypeToStringParam(ZonedDateTime zonedDateTime);
  void methodWithListOfTypeToStringParam(List<ZonedDateTime> zonedDateTimeList);
  void methodWithSetOfTypeToStringParam(Set<ZonedDateTime> zonedDateTimeSet);
  void methodWithMapOfTypeToStringParam(Map<String, ZonedDateTime> zonedDateTimeMap);

  ZonedDateTime methodWithTypeToStringReturn();
  List<ZonedDateTime> methodWithListOfTypeToStringReturn();
  Set<ZonedDateTime> methodWithSetOfTypeToStringReturn();
  Map<String, ZonedDateTime> methodWithMapOfTypeToStringReturn();

  void methodWithHandlerTypeToStringParam(Handler<ZonedDateTime> zonedDateTimeHandler);
  void methodWithHandlerListOfTypeToStringParam(Handler<List<ZonedDateTime>> zonedDateTimeListHandler);
  void methodWithHandlerSetOfTypeToStringParam(Handler<Set<ZonedDateTime>> zonedDateTimeSetHandler);
  void methodWithHandlerMapOfTypeToStringParam(Handler<Map<String, ZonedDateTime>> zonedDateTimeMapHandler);

  Future<ZonedDateTime> methodWithHandlerAsyncResultTypeToStringParam();
  Future<List<ZonedDateTime>> methodWithHandlerAsyncResultListOfTypeToStringParam();
  Future<Set<ZonedDateTime>> methodWithHandlerAsyncResultSetOfTypeToStringParam();
  Future<Map<String, ZonedDateTime>> methodWithHandlerAsyncResultMapOfTypeToStringParam();

  // Java type <-> MyPojoToJsonArray

  void methodWithTypeToJsonArrayParam(MyPojoToJsonArray myPojoToJsonArray);
  void methodWithListOfTypeToJsonArrayParam(List<MyPojoToJsonArray> myPojoToJsonArrayList);
  void methodWithSetOfTypeToJsonArrayParam(Set<MyPojoToJsonArray> myPojoToJsonArraySet);
  void methodWithMapOfTypeToJsonArrayParam(Map<String, MyPojoToJsonArray> myPojoToJsonArrayMap);

  MyPojoToJsonArray methodWithTypeToJsonArrayReturn();
  List<MyPojoToJsonArray> methodWithListOfTypeToJsonArrayReturn();
  Set<MyPojoToJsonArray> methodWithSetOfTypeToJsonArrayReturn();
  Map<String, MyPojoToJsonArray> methodWithMapOfTypeToJsonArrayReturn();

  void methodWithHandlerTypeToJsonArrayParam(Handler<MyPojoToJsonArray> myPojoToJsonArrayHandler);
  void methodWithHandlerListOfTypeToJsonArrayParam(Handler<List<MyPojoToJsonArray>> myPojoToJsonArrayListHandler);
  void methodWithHandlerSetOfTypeToJsonArrayParam(Handler<Set<MyPojoToJsonArray>> myPojoToJsonArraySetHandler);
  void methodWithHandlerMapOfTypeToJsonArrayParam(Handler<Map<String, MyPojoToJsonArray>> myPojoToJsonArrayMapHandler);

  Future<MyPojoToJsonArray> methodWithHandlerAsyncResultTypeToJsonArrayParam();
  Future<List<MyPojoToJsonArray>> methodWithHandlerAsyncResultListOfTypeToJsonArrayParam();
  Future<Set<MyPojoToJsonArray>> methodWithHandlerAsyncResultSetOfTypeToJsonArrayParam();
  Future<Map<String, MyPojoToJsonArray>> methodWithHandlerAsyncResultMapOfTypeToJsonArrayParam();

  // Java type <-> MyPojoToJsonObject

  void methodWithTypeToJsonObjectParam(MyPojoToJsonObject myPojoToJsonObject);
  void methodWithListOfTypeToJsonObjectParam(List<MyPojoToJsonObject> myPojoToJsonObjectList);
  void methodWithSetOfTypeToJsonObjectParam(Set<MyPojoToJsonObject> myPojoToJsonObjectSet);
  void methodWithMapOfTypeToJsonObjectParam(Map<String, MyPojoToJsonObject> myPojoToJsonObjectMap);

  MyPojoToJsonObject methodWithTypeToJsonObjectReturn();
  List<MyPojoToJsonObject> methodWithListOfTypeToJsonObjectReturn();
  Set<MyPojoToJsonObject> methodWithSetOfTypeToJsonObjectReturn();
  Map<String, MyPojoToJsonObject> methodWithMapOfTypeToJsonObjectReturn();

  void methodWithHandlerTypeToJsonObjectParam(Handler<MyPojoToJsonObject> myPojoToJsonObjectHandler);
  void methodWithHandlerListOfTypeToJsonObjectParam(Handler<List<MyPojoToJsonObject>> myPojoToJsonObjectListHandler);
  void methodWithHandlerSetOfTypeToJsonObjectParam(Handler<Set<MyPojoToJsonObject>> myPojoToJsonObjectSetHandler);
  void methodWithHandlerMapOfTypeToJsonObjectParam(Handler<Map<String, MyPojoToJsonObject>> myPojoToJsonObjectMapHandler);

  Future<MyPojoToJsonObject> methodWithHandlerAsyncResultTypeToJsonObjectParam();
  Future<List<MyPojoToJsonObject>> methodWithHandlerAsyncResultListOfTypeToJsonObjectParam();
  Future<Set<MyPojoToJsonObject>> methodWithHandlerAsyncResultSetOfTypeToJsonObjectParam();
  Future<Map<String, MyPojoToJsonObject>> methodWithHandlerAsyncResultMapOfTypeToJsonObjectParam();

  // CustomEnum <-> String

  void methodWithCustomEnumToStringParam(TestCustomEnum customEnum);
  void methodWithListOfCustomEnumToStringParam(List<TestCustomEnum> customEnumList);
  void methodWithSetOfCustomEnumToStringParam(Set<TestCustomEnum> customEnumSet);
  void methodWithMapOfCustomEnumToStringParam(Map<String, TestCustomEnum> customEnumMap);

  TestCustomEnum methodWithCustomEnumToStringReturn();
  List<TestCustomEnum> methodWithListOfCustomEnumToStringReturn();
  Set<TestCustomEnum> methodWithSetOfCustomEnumToStringReturn();
  Map<String, TestCustomEnum> methodWithMapOfCustomEnumToStringReturn();

  void methodWithHandlerCustomEnumToStringParam(Handler<TestCustomEnum> customEnumHandler);
  void methodWithHandlerListOfCustomEnumToStringParam(Handler<List<TestCustomEnum>> customEnumListHandler);
  void methodWithHandlerSetOfCustomEnumToStringParam(Handler<Set<TestCustomEnum>> customEnumSetHandler);
  void methodWithHandlerMapOfCustomEnumToStringParam(Handler<Map<String, TestCustomEnum>> customEnumMapHandler);

  Future<TestCustomEnum> methodWithHandlerAsyncResultCustomEnumToStringParam();
  Future<List<TestCustomEnum>> methodWithHandlerAsyncResultListOfCustomEnumToStringParam();
  Future<Set<TestCustomEnum>> methodWithHandlerAsyncResultSetOfCustomEnumToStringParam();
  Future<Map<String, TestCustomEnum>> methodWithHandlerAsyncResultMapOfCustomEnumToStringParam();

}
