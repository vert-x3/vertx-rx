package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@VertxGen
public interface TestInterface extends SuperInterface1, SuperInterface2 {

  // Test params

  void methodWithBasicParams(byte b, short s, int i, long l, float f, double d, boolean bool, char ch, String str);

  void methodWithBasicBoxedParams(Byte b, Short s, Integer i, Long l, Float f, Double d, Boolean bool, Character ch);

  void methodWithHandlerBasicTypes(Handler<Byte> byteHandler, Handler<Short> shortHandler, Handler<Integer> intHandler,
                                   Handler<Long> longHandler, Handler<Float> floatHandler, Handler<Double> doubleHandler,
                                   Handler<Boolean> booleanHandler, Handler<Character> charHandler, Handler<String> stringHandler);

  void methodWithHandlerAsyncResultByte(boolean sendFailure, Handler<AsyncResult<Byte>> handler);
  void methodWithHandlerAsyncResultShort(boolean sendFailure, Handler<AsyncResult<Short>> handler);
  void methodWithHandlerAsyncResultInteger(boolean sendFailure, Handler<AsyncResult<Integer>> handler);
  void methodWithHandlerAsyncResultLong(boolean sendFailure, Handler<AsyncResult<Long>> handler);
  void methodWithHandlerAsyncResultFloat(boolean sendFailure, Handler<AsyncResult<Float>> handler);
  void methodWithHandlerAsyncResultDouble(boolean sendFailure, Handler<AsyncResult<Double>> handler);
  void methodWithHandlerAsyncResultBoolean(boolean sendFailure, Handler<AsyncResult<Boolean>> handler);
  void methodWithHandlerAsyncResultCharacter(boolean sendFailure, Handler<AsyncResult<Character>> handler);
  void methodWithHandlerAsyncResultString(boolean sendFailure, Handler<AsyncResult<String>> handler);

  void methodWithUserTypes(RefedInterface1 refed);

  void methodWithObjectParam(String str, Object obj);

  void methodWithOptionsParam(TestOptions options);

  void methodWithNullOptionsParam(TestOptions options);

  void methodWithListParams(List<String> listString, List<Byte> listByte, List<Short> listShort, List<Integer> listInt, List<Long> listLong, List<JsonObject> listJsonObject, List<JsonArray> listJsonArray, List<RefedInterface1> listVertxGen);

  void methodWithSetParams(Set<String> setString, Set<Byte> setByte, Set<Short> setShort, Set<Integer> setInt, Set<Long> setLong, Set<JsonObject> setJsonObject, Set<JsonArray> setJsonArray, Set<RefedInterface1> setVertxGen);

  void methodWithMapParams(Map<String, String> mapString, Map<String, Byte> mapByte, Map<String, Short> mapShort, Map<String, Integer> mapInt, Map<String, Long> mapLong, Map<String, JsonObject> mapJsonObject, Map<String, JsonArray> mapJsonArray, Map<String, RefedInterface1> mapVertxGen);

  void methodWithHandlerListAndSet(Handler<List<String>> listStringHandler, Handler<List<Integer>> listIntHandler,
                                   Handler<Set<String>> setStringHandler, Handler<Set<Integer>> setIntHandler);

  void methodWithHandlerAsyncResultListString(Handler<AsyncResult<List<String>>> handler);
  void methodWithHandlerAsyncResultListInteger(Handler<AsyncResult<List<Integer>>> handler);
  void methodWithHandlerAsyncResultSetString(Handler<AsyncResult<Set<String>>> handler);
  void methodWithHandlerAsyncResultSetInteger(Handler<AsyncResult<Set<Integer>>> handler);

  void methodWithHandlerListVertxGen(Handler<List<RefedInterface1>> listHandler);

  void methodWithHandlerSetVertxGen(Handler<Set<RefedInterface1>> listHandler);

  void methodWithHandlerListAbstractVertxGen(Handler<List<RefedInterface2>> listHandler);

  void methodWithHandlerSetAbstractVertxGen(Handler<Set<RefedInterface2>> listHandler);

  void methodWithHandlerListJsonObject(Handler<List<JsonObject>> listHandler);

  void methodWithHandlerListNullJsonObject(Handler<List<JsonObject>> listHandler);

  void methodWithHandlerSetJsonObject(Handler<Set<JsonObject>> listHandler);

  void methodWithHandlerSetNullJsonObject(Handler<Set<JsonObject>> listHandler);

  void methodWithHandlerListJsonArray(Handler<List<JsonArray>> listHandler);

  void methodWithHandlerListNullJsonArray(Handler<List<JsonArray>> listHandler);

  void methodWithHandlerSetJsonArray(Handler<Set<JsonArray>> listHandler);

  void methodWithHandlerSetNullJsonArray(Handler<Set<JsonArray>> listHandler);

  void methodWithHandlerAsyncResultListVertxGen(Handler<AsyncResult<List<RefedInterface1>>> listHandler);

  void methodWithHandlerAsyncResultSetVertxGen(Handler<AsyncResult<Set<RefedInterface1>>> listHandler);

  void methodWithHandlerAsyncResultListAbstractVertxGen(Handler<AsyncResult<List<RefedInterface2>>> listHandler);

  void methodWithHandlerAsyncResultSetAbstractVertxGen(Handler<AsyncResult<Set<RefedInterface2>>> listHandler);

  void methodWithHandlerAsyncResultListJsonObject(Handler<AsyncResult<List<JsonObject>>> listHandler);

  void methodWithHandlerAsyncResultListNullJsonObject(Handler<AsyncResult<List<JsonObject>>> listHandler);

  void methodWithHandlerAsyncResultSetJsonObject(Handler<AsyncResult<Set<JsonObject>>> listHandler);

  void methodWithHandlerAsyncResultSetNullJsonObject(Handler<AsyncResult<Set<JsonObject>>> listHandler);

  void methodWithHandlerAsyncResultListJsonArray(Handler<AsyncResult<List<JsonArray>>> listHandler);

  void methodWithHandlerAsyncResultListNullJsonArray(Handler<AsyncResult<List<JsonArray>>> listHandler);

  void methodWithHandlerAsyncResultSetJsonArray(Handler<AsyncResult<Set<JsonArray>>> listHandler);

  void methodWithHandlerAsyncResultSetNullJsonArray(Handler<AsyncResult<Set<JsonArray>>> listHandler);

  void methodWithHandlerUserTypes(Handler<RefedInterface1> handler);

  void methodWithHandlerAsyncResultUserTypes(Handler<AsyncResult<RefedInterface1>> handler);

  void methodWithHandlerVoid(Handler<Void> handler);

  void methodWithHandlerAsyncResultVoid(boolean sendFailure, Handler<AsyncResult<Void>> handler);

  void methodWithHandlerThrowable(Handler<Throwable> handler);

  <U> void methodWithHandlerGenericUserType(U value, Handler<GenericRefedInterface<U>> handler);

  <U> void methodWithHandlerAsyncResultGenericUserType(U value, Handler<AsyncResult<GenericRefedInterface<U>>> handler);

  byte methodWithByteReturn();

  short methodWithShortReturn();

  int methodWithIntReturn();

  long methodWithLongReturn();

  float methodWithFloatReturn();

  double methodWithDoubleReturn();

  boolean methodWithBooleanReturn();

  char methodWithCharReturn();

  String methodWithStringReturn();

  RefedInterface1 methodWithVertxGenReturn();

  RefedInterface2 methodWithAbstractVertxGenReturn();

  String overloadedMethod(String str, Handler<String> handler);

  String overloadedMethod(String str, RefedInterface1 refed);

  String overloadedMethod(String str, RefedInterface1 refed, Handler<String> handler);

  String overloadedMethod(String str, RefedInterface1 refed, long period, Handler<String> handler);

  <U> U methodWithGenericReturn(String type);

  <U> void methodWithGenericParam(String type, U u);

  <U> void methodWithGenericHandler(String type, Handler<U> handler);

  <U> void methodWithGenericHandlerAsyncResult(String type, Handler<AsyncResult<U>> asyncResultHandler);

  @Fluent
  TestInterface fluentMethod(String str);

  static RefedInterface1 staticFactoryMethod(String foo) {
    RefedInterface1 refed = new RefedInterface1Impl();
    refed.setString(foo);
    return refed;
  }

  @CacheReturn
  RefedInterface1 methodWithCachedReturn(String foo);

  JsonObject methodWithJsonObjectReturn();

  JsonObject methodWithNullJsonObjectReturn();

  JsonArray methodWithJsonArrayReturn();

  JsonArray methodWithNullJsonArrayReturn();

  void methodWithJsonParams(JsonObject jsonObject, JsonArray jsonArray);

  void methodWithNullJsonParams(JsonObject jsonObject, JsonArray jsonArray);

  void methodWithHandlerJson(Handler<JsonObject> jsonObjectHandler, Handler<JsonArray> jsonArrayHandler);

  void methodWithHandlerNullJson(Handler<JsonObject> jsonObjectHandler, Handler<JsonArray> jsonArrayHandler);

  void methodWithHandlerAsyncResultJsonObject(Handler<AsyncResult<JsonObject>> handler);

  void methodWithHandlerAsyncResultNullJsonObject(Handler<AsyncResult<JsonObject>> handler);

  void methodWithHandlerAsyncResultJsonArray(Handler<AsyncResult<JsonArray>> handler);

  void methodWithHandlerAsyncResultNullJsonArray(Handler<AsyncResult<JsonArray>> handler);

  Map<String, String> methodWithMapReturn(Handler<String> handler);

  Map<String, String> methodWithMapStringReturn(Handler<String> handler);

  Map<String, Long> methodWithMapLongReturn(Handler<String> handler);

  Map<String, JsonObject> methodWithMapJsonObjectReturn(Handler<String> handler);

  Map<String, JsonArray> methodWithMapJsonArrayReturn(Handler<String> handler);

  Map<String, String> methodWithNullMapReturn();



  List<String> methodWithListStringReturn();

  List<Long> methodWithListLongReturn();

  List<RefedInterface1> methodWithListVertxGenReturn();

  List<JsonObject> methodWithListJsonObjectReturn();

  List<JsonArray> methodWithListJsonArrayReturn();

  List<String> methodWithNullListReturn();



  Set<String> methodWithSetStringReturn();

  Set<Long> methodWithSetLongReturn();

  Set<RefedInterface1> methodWithSetVertxGenReturn();

  Set<JsonObject> methodWithSetJsonObjectReturn();

  Set<JsonArray> methodWithSetJsonArrayReturn();

  Set<String> methodWithNullSetReturn();


  String methodWithEnumParam(String strVal, TestEnum weirdo);

  TestEnum methodWithEnumReturn(String strVal);

  Throwable methodWithThrowableReturn(String strVal);
}
