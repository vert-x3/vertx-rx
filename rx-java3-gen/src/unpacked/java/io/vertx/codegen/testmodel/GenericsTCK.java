package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface GenericsTCK {

  GenericRefedInterface<Byte> methodWithByteParameterizedReturn();
  GenericRefedInterface<Short> methodWithShortParameterizedReturn();
  GenericRefedInterface<Integer> methodWithIntegerParameterizedReturn();
  GenericRefedInterface<Long> methodWithLongParameterizedReturn();
  GenericRefedInterface<Float> methodWithFloatParameterizedReturn();
  GenericRefedInterface<Double> methodWithDoubleParameterizedReturn();
  GenericRefedInterface<Boolean> methodWithBooleanParameterizedReturn();
  GenericRefedInterface<Character> methodWithCharacterParameterizedReturn();
  GenericRefedInterface<String> methodWithStringParameterizedReturn();
  GenericRefedInterface<JsonObject> methodWithJsonObjectParameterizedReturn();
  GenericRefedInterface<JsonArray> methodWithJsonArrayParameterizedReturn();
  GenericRefedInterface<TestDataObject> methodWithDataObjectParameterizedReturn();
  GenericRefedInterface<TestEnum> methodWithEnumParameterizedReturn();
  GenericRefedInterface<TestGenEnum> methodWithGenEnumParameterizedReturn();
  GenericRefedInterface<RefedInterface1> methodWithUserTypeParameterizedReturn();
  GenericRefedInterface<List<Object>> methodWithListOfObjectsParameterizedReturn();
  GenericRefedInterface<Set<Object>> methodWithSetOfObjectsParameterizedReturn();
  GenericRefedInterface<Map<String, Object>> methodWithMapOfObjectsParameterizedReturn();

  void methodWithHandlerByteParameterized(Handler<GenericRefedInterface<Byte>> handler);
  void methodWithHandlerShortParameterized(Handler<GenericRefedInterface<Short>> handler);
  void methodWithHandlerIntegerParameterized(Handler<GenericRefedInterface<Integer>> handler);
  void methodWithHandlerLongParameterized(Handler<GenericRefedInterface<Long>> handler);
  void methodWithHandlerFloatParameterized(Handler<GenericRefedInterface<Float>> handler);
  void methodWithHandlerDoubleParameterized(Handler<GenericRefedInterface<Double>> handler);
  void methodWithHandlerBooleanParameterized(Handler<GenericRefedInterface<Boolean>> handler);
  void methodWithHandlerCharacterParameterized(Handler<GenericRefedInterface<Character>> handler);
  void methodWithHandlerStringParameterized(Handler<GenericRefedInterface<String>> handler);
  void methodWithHandlerJsonObjectParameterized(Handler<GenericRefedInterface<JsonObject>> handler);
  void methodWithHandlerJsonArrayParameterized(Handler<GenericRefedInterface<JsonArray>> handler);
  void methodWithHandlerDataObjectParameterized(Handler<GenericRefedInterface<TestDataObject>> handler);
  void methodWithHandlerEnumParameterized(Handler<GenericRefedInterface<TestEnum>> handler);
  void methodWithHandlerGenEnumParameterized(Handler<GenericRefedInterface<TestGenEnum>> handler);
  void methodWithHandlerUserTypeParameterized(Handler<GenericRefedInterface<RefedInterface1>> handler);
  void methodWithHandlerListOfObjectsParameterized(Handler<GenericRefedInterface<List<Object>>> handler);
  void methodWithHandlerSetOfObjectsParameterized(Handler<GenericRefedInterface<Set<Object>>> handler);
  void methodWithHandlerMapOfObjectsParameterized(Handler<GenericRefedInterface<Map<String, Object>>> handler);

  Future<GenericRefedInterface<Byte>> methodWithHandlerAsyncResultByteParameterized();
  Future<GenericRefedInterface<Short>> methodWithHandlerAsyncResultShortParameterized();
  Future<GenericRefedInterface<Integer>> methodWithHandlerAsyncResultIntegerParameterized();
  Future<GenericRefedInterface<Long>> methodWithHandlerAsyncResultLongParameterized();
  Future<GenericRefedInterface<Float>> methodWithHandlerAsyncResultFloatParameterized();
  Future<GenericRefedInterface<Double>> methodWithHandlerAsyncResultDoubleParameterized();
  Future<GenericRefedInterface<Boolean>> methodWithHandlerAsyncResultBooleanParameterized();
  Future<GenericRefedInterface<Character>> methodWithHandlerAsyncResultCharacterParameterized();
  Future<GenericRefedInterface<String>> methodWithHandlerAsyncResultStringParameterized();
  Future<GenericRefedInterface<JsonObject>> methodWithHandlerAsyncResultJsonObjectParameterized();
  Future<GenericRefedInterface<JsonArray>> methodWithHandlerAsyncResultJsonArrayParameterized();
  Future<GenericRefedInterface<TestDataObject>> methodWithHandlerAsyncResultDataObjectParameterized();
  Future<GenericRefedInterface<TestEnum>> methodWithHandlerAsyncResultEnumParameterized();
  Future<GenericRefedInterface<TestGenEnum>> methodWithHandlerAsyncResultGenEnumParameterized();
  Future<GenericRefedInterface<RefedInterface1>> methodWithHandlerAsyncResultUserTypeParameterized();
  Future<GenericRefedInterface<List<Object>>> methodWithHandlerAsyncResultListOfObjectsParameterized();
  Future<GenericRefedInterface<Set<Object>>> methodWithHandlerAsyncResultSetOfObjectsParameterized();
  Future<GenericRefedInterface<Map<String, Object>>> methodWithHandlerAsyncResultMapOfObjectsParameterized();

  void methodWithFunctionParamByteParameterized(Function<GenericRefedInterface<Byte>, String> handler);
  void methodWithFunctionParamShortParameterized(Function<GenericRefedInterface<Short>, String> handler);
  void methodWithFunctionParamIntegerParameterized(Function<GenericRefedInterface<Integer>, String> handler);
  void methodWithFunctionParamLongParameterized(Function<GenericRefedInterface<Long>, String> handler);
  void methodWithFunctionParamFloatParameterized(Function<GenericRefedInterface<Float>, String> handler);
  void methodWithFunctionParamDoubleParameterized(Function<GenericRefedInterface<Double>, String> handler);
  void methodWithFunctionParamBooleanParameterized(Function<GenericRefedInterface<Boolean>, String> handler);
  void methodWithFunctionParamCharacterParameterized(Function<GenericRefedInterface<Character>, String> handler);
  void methodWithFunctionParamStringParameterized(Function<GenericRefedInterface<String>, String> handler);
  void methodWithFunctionParamJsonObjectParameterized(Function<GenericRefedInterface<JsonObject>, String> handler);
  void methodWithFunctionParamJsonArrayParameterized(Function<GenericRefedInterface<JsonArray>, String> handler);
  void methodWithFunctionParamDataObjectParameterized(Function<GenericRefedInterface<TestDataObject>, String> handler);
  void methodWithFunctionParamEnumParameterized(Function<GenericRefedInterface<TestEnum>, String> handler);
  void methodWithFunctionParamGenEnumParameterized(Function<GenericRefedInterface<TestGenEnum>, String> handler);
  void methodWithFunctionParamUserTypeParameterized(Function<GenericRefedInterface<RefedInterface1>, String> handler);
  void methodWithFunctionParamListOfObjectsParameterized(Function<GenericRefedInterface<List<Object>>, String> handler);
  void methodWithFunctionParamSetOfObjectsParameterized(Function<GenericRefedInterface<Set<Object>>, String> handler);
  void methodWithFunctionParamMapOfObjectsParameterized(Function<GenericRefedInterface<Map<String, Object>>, String> handler);

  <U> GenericRefedInterface<U> methodWithClassTypeParameterizedReturn(Class<U> type);
  <U> void methodWithHandlerClassTypeParameterized(Class<U> type, Handler<GenericRefedInterface<U>> handler);
  <U> Future<GenericRefedInterface<U>> methodWithHandlerAsyncResultClassTypeParameterized(Class<U> type);
  <U> void methodWithFunctionParamClassTypeParameterized(Class<U> type, Function<GenericRefedInterface<U>, String> handler);

  <U> void methodWithClassTypeParam(Class<U> type, U u);
  <U> U methodWithClassTypeReturn(Class<U> type);
  <U> void methodWithClassTypeHandler(Class<U> type, Handler<U> f);
  <U> Future<U> methodWithClassTypeHandlerAsyncResult(Class<U> type);
  <U> void methodWithClassTypeFunctionParam(Class<U> type, Function<U, String> f);
  <U> void methodWithClassTypeFunctionReturn(Class<U> type, Function<String, U> f);

  InterfaceWithApiArg interfaceWithApiArg(RefedInterface1 value);
  InterfaceWithStringArg interfaceWithStringArg(String value);
  <T, U> InterfaceWithVariableArg<T, U> interfaceWithVariableArg(T value1, Class<U> type, U value2);

  // Test GenericNullableRefedInterface can return a null value
  // todo : add other types ?
  void methodWithHandlerGenericNullableApi(boolean notNull, Handler<GenericNullableRefedInterface<RefedInterface1>> handler);
  Future<GenericNullableRefedInterface<RefedInterface1>> methodWithHandlerAsyncResultGenericNullableApi(boolean notNull);
  GenericNullableRefedInterface<RefedInterface1> methodWithGenericNullableApiReturn(boolean notNull);

  <T> GenericRefedInterface<T> methodWithParamInferedReturn(GenericRefedInterface<T> param);
  <T> void methodWithHandlerParamInfered(GenericRefedInterface<T> param, Handler<GenericRefedInterface<T>> handler);
  <T> Future<GenericRefedInterface<T>> methodWithHandlerAsyncResultParamInfered(GenericRefedInterface<T> param);

}
