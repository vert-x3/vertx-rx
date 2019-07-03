package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@VertxGen
public interface JsonCodecTCK {

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

  void methodWithHandlerAsyncResultTypeToIntegerParam(Handler<AsyncResult<MyPojoToInteger>> myPojoToIntegerHandler);
  void methodWithHandlerAsyncResultListOfTypeToIntegerParam(Handler<AsyncResult<List<MyPojoToInteger>>> myPojoToIntegerListHandler);
  void methodWithHandlerAsyncResultSetOfTypeToIntegerParam(Handler<AsyncResult<Set<MyPojoToInteger>>> myPojoToIntegerSetHandler);
  void methodWithHandlerAsyncResultMapOfTypeToIntegerParam(Handler<AsyncResult<Map<String, MyPojoToInteger>>> myPojoToIntegerMapHandler);

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

  void methodWithHandlerAsyncResultTypeToStringParam(Handler<AsyncResult<ZonedDateTime>> zonedDateTimeHandler);
  void methodWithHandlerAsyncResultListOfTypeToStringParam(Handler<AsyncResult<List<ZonedDateTime>>> zonedDateTimeListHandler);
  void methodWithHandlerAsyncResultSetOfTypeToStringParam(Handler<AsyncResult<Set<ZonedDateTime>>> zonedDateTimeSetHandler);
  void methodWithHandlerAsyncResultMapOfTypeToStringParam(Handler<AsyncResult<Map<String, ZonedDateTime>>> zonedDateTimeMapHandler);

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

  void methodWithHandlerAsyncResultTypeToJsonArrayParam(Handler<AsyncResult<MyPojoToJsonArray>> myPojoToJsonArrayHandler);
  void methodWithHandlerAsyncResultListOfTypeToJsonArrayParam(Handler<AsyncResult<List<MyPojoToJsonArray>>> myPojoToJsonArrayListHandler);
  void methodWithHandlerAsyncResultSetOfTypeToJsonArrayParam(Handler<AsyncResult<Set<MyPojoToJsonArray>>> myPojoToJsonArraySetHandler);
  void methodWithHandlerAsyncResultMapOfTypeToJsonArrayParam(Handler<AsyncResult<Map<String, MyPojoToJsonArray>>> myPojoToJsonArrayMapHandler);

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

  void methodWithHandlerAsyncResultTypeToJsonObjectParam(Handler<AsyncResult<MyPojoToJsonObject>> myPojoToJsonObjectHandler);
  void methodWithHandlerAsyncResultListOfTypeToJsonObjectParam(Handler<AsyncResult<List<MyPojoToJsonObject>>> myPojoToJsonObjectListHandler);
  void methodWithHandlerAsyncResultSetOfTypeToJsonObjectParam(Handler<AsyncResult<Set<MyPojoToJsonObject>>> myPojoToJsonObjectSetHandler);
  void methodWithHandlerAsyncResultMapOfTypeToJsonObjectParam(Handler<AsyncResult<Map<String, MyPojoToJsonObject>>> myPojoToJsonObjectMapHandler);

}
