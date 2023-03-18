package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The TCK for @Nullable.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface NullableTCK {

  // Test @Nullable Byte type
  boolean methodWithNonNullableByteParam(Byte param);
  void methodWithNullableByteParam(boolean expectNull, @Nullable Byte param);
  void methodWithNullableByteHandler(boolean notNull, Handler<@Nullable Byte> handler);
  Future<@Nullable Byte> methodWithNullableByteHandlerAsyncResult(boolean notNull);
  @Nullable Byte methodWithNullableByteReturn(boolean notNull);

  // Test @Nullable Short type
  boolean methodWithNonNullableShortParam(Short param);
  void methodWithNullableShortParam(boolean expectNull, @Nullable Short param);
  void methodWithNullableShortHandler(boolean notNull, Handler<@Nullable Short> handler);
  Future<@Nullable Short> methodWithNullableShortHandlerAsyncResult(boolean notNull);
  @Nullable Short methodWithNullableShortReturn(boolean notNull);

  // Test @Nullable Integer type
  boolean methodWithNonNullableIntegerParam(Integer param);
  void methodWithNullableIntegerParam(boolean expectNull, @Nullable Integer param);
  void methodWithNullableIntegerHandler(boolean notNull, Handler<@Nullable Integer> handler);
  Future<@Nullable Integer> methodWithNullableIntegerHandlerAsyncResult(boolean notNull);
  @Nullable Integer methodWithNullableIntegerReturn(boolean notNull);

  // Test @Nullable Long type
  boolean methodWithNonNullableLongParam(Long param);
  void methodWithNullableLongParam(boolean expectNull, @Nullable Long param);
  void methodWithNullableLongHandler(boolean notNull, Handler<@Nullable Long> handler);
  Future<@Nullable Long> methodWithNullableLongHandlerAsyncResult(boolean notNull);
  @Nullable Long methodWithNullableLongReturn(boolean notNull);

  // Test @Nullable Float type
  boolean methodWithNonNullableFloatParam(Float param);
  void methodWithNullableFloatParam(boolean expectNull, @Nullable Float param);
  void methodWithNullableFloatHandler(boolean notNull, Handler<@Nullable Float> handler);
  Future<@Nullable Float> methodWithNullableFloatHandlerAsyncResult(boolean notNull);
  @Nullable Float methodWithNullableFloatReturn(boolean notNull);

  // Test @Nullable Double type
  boolean methodWithNonNullableDoubleParam(Double param);
  void methodWithNullableDoubleParam(boolean expectNull, @Nullable Double param);
  void methodWithNullableDoubleHandler(boolean notNull, Handler<@Nullable Double> handler);
  Future<@Nullable Double> methodWithNullableDoubleHandlerAsyncResult(boolean notNull);
  @Nullable Double methodWithNullableDoubleReturn(boolean notNull);

  // Test @Nullable Boolean type
  boolean methodWithNonNullableBooleanParam(Boolean param);
  void methodWithNullableBooleanParam(boolean expectNull, @Nullable Boolean param);
  void methodWithNullableBooleanHandler(boolean notNull, Handler<@Nullable Boolean> handler);
  Future<@Nullable Boolean> methodWithNullableBooleanHandlerAsyncResult(boolean notNull);
  @Nullable Boolean methodWithNullableBooleanReturn(boolean notNull);

  // Test @Nullable String type
  boolean methodWithNonNullableStringParam(String param);
  void methodWithNullableStringParam(boolean expectNull, @Nullable String param);
  void methodWithNullableStringHandler(boolean notNull, Handler<@Nullable String> handler);
  Future<@Nullable String> methodWithNullableStringHandlerAsyncResult(boolean notNull);
  @Nullable String methodWithNullableStringReturn(boolean notNull);

  // Test @Nullable Char type
  boolean methodWithNonNullableCharParam(Character param);
  void methodWithNullableCharParam(boolean expectNull, @Nullable Character param);
  void methodWithNullableCharHandler(boolean notNull, Handler<@Nullable Character> handler);
  Future<@Nullable Character> methodWithNullableCharHandlerAsyncResult(boolean notNull);
  @Nullable Character methodWithNullableCharReturn(boolean notNull);

  // Test @Nullable JsonObject type
  boolean methodWithNonNullableJsonObjectParam(JsonObject param);
  void methodWithNullableJsonObjectParam(boolean expectNull, @Nullable JsonObject param);
  void methodWithNullableJsonObjectHandler(boolean notNull, Handler<@Nullable JsonObject> handler);
  Future<@Nullable JsonObject> methodWithNullableJsonObjectHandlerAsyncResult(boolean notNull);
  @Nullable JsonObject methodWithNullableJsonObjectReturn(boolean notNull);

  // Test @Nullable JsonArray type
  boolean methodWithNonNullableJsonArrayParam(JsonArray param);
  void methodWithNullableJsonArrayParam(boolean expectNull, @Nullable JsonArray param);
  void methodWithNullableJsonArrayHandler(boolean notNull, Handler<@Nullable JsonArray> handler);
  Future<@Nullable JsonArray> methodWithNullableJsonArrayHandlerAsyncResult(boolean notNull);
  @Nullable JsonArray methodWithNullableJsonArrayReturn(boolean notNull);

  // Test @Nullable Api type
  boolean methodWithNonNullableApiParam(RefedInterface1 param);
  void methodWithNullableApiParam(boolean expectNull, @Nullable RefedInterface1 param);
  void methodWithNullableApiHandler(boolean notNull, Handler<@Nullable RefedInterface1> handler);
  Future<@Nullable RefedInterface1> methodWithNullableApiHandlerAsyncResult(boolean notNull);
  @Nullable RefedInterface1 methodWithNullableApiReturn(boolean notNull);

  // Test @Nullable DataObject type
  boolean methodWithNonNullableDataObjectParam(TestDataObject param);
  void methodWithNullableDataObjectParam(boolean expectNull, @Nullable TestDataObject param);
  void methodWithNullableDataObjectHandler(boolean notNull, Handler<@Nullable TestDataObject> handler);
  Future<@Nullable TestDataObject> methodWithNullableDataObjectHandlerAsyncResult(boolean notNull);
  @Nullable TestDataObject methodWithNullableDataObjectReturn(boolean notNull);

  // Test @Nullable json-mapped type
  boolean methodWithNonNullableJsonMappedParam(ZonedDateTime param);
  void methodWithNullableJsonMappedParam(boolean expectNull, @Nullable ZonedDateTime param);
  void methodWithNullableJsonMappedHandler(boolean notNull, Handler<@Nullable ZonedDateTime> handler);
  Future<@Nullable ZonedDateTime> methodWithNullableJsonMappedHandlerAsyncResult(boolean notNull);
  @Nullable ZonedDateTime methodWithNullableJsonMappedReturn(boolean notNull);

  // Test @Nullable Enum type
  boolean methodWithNonNullableEnumParam(TestEnum param);
  void methodWithNullableEnumParam(boolean expectNull, @Nullable TestEnum param);
  void methodWithNullableEnumHandler(boolean notNull, Handler<@Nullable TestEnum> handler);
  Future<@Nullable TestEnum> methodWithNullableEnumHandlerAsyncResult(boolean notNull);
  @Nullable TestEnum methodWithNullableEnumReturn(boolean notNull);

  // Test @Nullable Enum type
  boolean methodWithNonNullableGenEnumParam(TestGenEnum param);
  void methodWithNullableGenEnumParam(boolean expectNull, @Nullable TestGenEnum param);
  void methodWithNullableGenEnumHandler(boolean notNull, Handler<@Nullable TestGenEnum> handler);
  Future<@Nullable TestGenEnum> methodWithNullableGenEnumHandlerAsyncResult(boolean notNull);
  @Nullable TestGenEnum methodWithNullableGenEnumReturn(boolean notNull);

  // Test @Nullable type variable
  <T> void methodWithNullableTypeVariableParam(boolean expectNull, T param);
  <T> void methodWithNullableTypeVariableHandler(boolean notNull, T value, Handler<T> handler);
  <T> Future<T> methodWithNullableTypeVariableHandlerAsyncResult(boolean notNull, T value);
  <T> @Nullable T methodWithNullableTypeVariableReturn(boolean notNull, T value);

  // Test @Nullable Object
  void methodWithNullableObjectParam(boolean expectNull, Object param);

  // Test @Nullable List<Byte> type
  boolean methodWithNonNullableListByteParam(List<Byte> param);
  void methodWithNullableListByteParam(boolean expectNull, @Nullable List<Byte> param);
  void methodWithNullableListByteHandler(boolean notNull, Handler<@Nullable List<Byte>> handler);
  Future<@Nullable List<Byte>> methodWithNullableListByteHandlerAsyncResult(boolean notNull);
  @Nullable List<Byte> methodWithNullableListByteReturn(boolean notNull);

  // Test @Nullable List<Short> type
  boolean methodWithNonNullableListShortParam(List<Short> param);
  void methodWithNullableListShortParam(boolean expectNull, @Nullable List<Short> param);
  void methodWithNullableListShortHandler(boolean notNull, Handler<@Nullable List<Short>> handler);
  Future<@Nullable List<Short>> methodWithNullableListShortHandlerAsyncResult(boolean notNull);
  @Nullable List<Short> methodWithNullableListShortReturn(boolean notNull);

  // Test @Nullable List<Integer> type
  boolean methodWithNonNullableListIntegerParam(List<Integer> param);
  void methodWithNullableListIntegerParam(boolean expectNull, @Nullable List<Integer> param);
  void methodWithNullableListIntegerHandler(boolean notNull, Handler<@Nullable List<Integer>> handler);
  Future<@Nullable List<Integer>> methodWithNullableListIntegerHandlerAsyncResult(boolean notNull);
  @Nullable List<Integer> methodWithNullableListIntegerReturn(boolean notNull);

  // Test @Nullable List<Long> type
  boolean methodWithNonNullableListLongParam(List<Long> param);
  void methodWithNullableListLongParam(boolean expectNull, @Nullable List<Long> param);
  void methodWithNullableListLongHandler(boolean notNull, Handler<@Nullable List<Long>> handler);
  Future<@Nullable List<Long>> methodWithNullableListLongHandlerAsyncResult(boolean notNull);
  @Nullable List<Long> methodWithNullableListLongReturn(boolean notNull);

  // Test @Nullable List<Float> type
  boolean methodWithNonNullableListFloatParam(List<Float> param);
  void methodWithNullableListFloatParam(boolean expectNull, @Nullable List<Float> param);
  void methodWithNullableListFloatHandler(boolean notNull, Handler<@Nullable List<Float>> handler);
  Future<@Nullable List<Float>> methodWithNullableListFloatHandlerAsyncResult(boolean notNull);
  @Nullable List<Float> methodWithNullableListFloatReturn(boolean notNull);

  // Test @Nullable List<Double> type
  boolean methodWithNonNullableListDoubleParam(List<Double> param);
  void methodWithNullableListDoubleParam(boolean expectNull, @Nullable List<Double> param);
  void methodWithNullableListDoubleHandler(boolean notNull, Handler<@Nullable List<Double>> handler);
  Future<@Nullable List<Double>> methodWithNullableListDoubleHandlerAsyncResult(boolean notNull);
  @Nullable List<Double> methodWithNullableListDoubleReturn(boolean notNull);

  // Test @Nullable List<Boolean> type
  boolean methodWithNonNullableListBooleanParam(List<Boolean> param);
  void methodWithNullableListBooleanParam(boolean expectNull, @Nullable List<Boolean> param);
  void methodWithNullableListBooleanHandler(boolean notNull, Handler<@Nullable List<Boolean>> handler);
  Future<@Nullable List<Boolean>> methodWithNullableListBooleanHandlerAsyncResult(boolean notNull);
  @Nullable List<Boolean> methodWithNullableListBooleanReturn(boolean notNull);

  // Test @Nullable List<String> type
  boolean methodWithNonNullableListStringParam(List<String> param);
  void methodWithNullableListStringParam(boolean expectNull, @Nullable List<String> param);
  void methodWithNullableListStringHandler(boolean notNull, Handler<@Nullable List<String>> handler);
  Future<@Nullable List<String>> methodWithNullableListStringHandlerAsyncResult(boolean notNull);
  @Nullable List<String> methodWithNullableListStringReturn(boolean notNull);

  // Test @Nullable List<Character> type
  boolean methodWithNonNullableListCharParam(List<Character> param);
  void methodWithNullableListCharParam(boolean expectNull, @Nullable List<Character> param);
  void methodWithNullableListCharHandler(boolean notNull, Handler<@Nullable List<Character>> handler);
  Future<@Nullable List<Character>> methodWithNullableListCharHandlerAsyncResult(boolean notNull);
  @Nullable List<Character> methodWithNullableListCharReturn(boolean notNull);

  // Test @Nullable List<JsonObject> type
  boolean methodWithNonNullableListJsonObjectParam(List<JsonObject> param);
  void methodWithNullableListJsonObjectParam(boolean expectNull, @Nullable List<JsonObject> param);
  void methodWithNullableListJsonObjectHandler(boolean notNull, Handler<@Nullable List<JsonObject>> handler);
  Future<@Nullable List<JsonObject>> methodWithNullableListJsonObjectHandlerAsyncResult(boolean notNull);
  @Nullable List<JsonObject> methodWithNullableListJsonObjectReturn(boolean notNull);

  // Test @Nullable List<JsonArray> type
  boolean methodWithNonNullableListJsonArrayParam(List<JsonArray> param);
  void methodWithNullableListJsonArrayParam(boolean expectNull, @Nullable List<JsonArray> param);
  void methodWithNullableListJsonArrayHandler(boolean notNull, Handler<@Nullable List<JsonArray>> handler);
  Future<@Nullable List<JsonArray>> methodWithNullableListJsonArrayHandlerAsyncResult(boolean notNull);
  @Nullable List<JsonArray> methodWithNullableListJsonArrayReturn(boolean notNull);

  // Test @Nullable List<Api> type
  boolean methodWithNonNullableListApiParam(List<RefedInterface1> param);
  void methodWithNullableListApiParam(boolean expectNull, @Nullable List<RefedInterface1> param);
  void methodWithNullableListApiHandler(boolean notNull, Handler<@Nullable List<RefedInterface1>> handler);
  Future<@Nullable List<RefedInterface1>> methodWithNullableListApiHandlerAsyncResult(boolean notNull);
  @Nullable List<RefedInterface1> methodWithNullableListApiReturn(boolean notNull);

  // Test @Nullable List<DataObject> type
  boolean methodWithNonNullableListDataObjectParam(List<TestDataObject> param);
  void methodWithNullableListDataObjectParam(boolean expectNull, @Nullable List<TestDataObject> param);
  void methodWithNullableListDataObjectHandler(boolean notNull, Handler<@Nullable List<TestDataObject>> handler);
  Future<@Nullable List<TestDataObject>> methodWithNullableListDataObjectHandlerAsyncResult(boolean notNull);
  @Nullable List<TestDataObject> methodWithNullableListDataObjectReturn(boolean notNull);

  // Test @Nullable List<Enum> type
  boolean methodWithNonNullableListEnumParam(List<TestEnum> param);
  void methodWithNullableListEnumParam(boolean expectNull, @Nullable List<TestEnum> param);
  void methodWithNullableListEnumHandler(boolean notNull, Handler<@Nullable List<TestEnum>> handler);
  Future<@Nullable List<TestEnum>> methodWithNullableListEnumHandlerAsyncResult(boolean notNull);
  @Nullable List<TestEnum> methodWithNullableListEnumReturn(boolean notNull);

  // Test @Nullable List<GenEnum> type
  boolean methodWithNonNullableListGenEnumParam(List<TestGenEnum> param);
  void methodWithNullableListGenEnumParam(boolean expectNull, @Nullable List<TestGenEnum> param);
  void methodWithNullableListGenEnumHandler(boolean notNull, Handler<@Nullable List<TestGenEnum>> handler);
  Future<@Nullable List<TestGenEnum>> methodWithNullableListGenEnumHandlerAsyncResult(boolean notNull);
  @Nullable List<TestGenEnum> methodWithNullableListGenEnumReturn(boolean notNull);

  // Test @Nullable Set<Byte> type
  boolean methodWithNonNullableSetByteParam(Set<Byte> param);
  void methodWithNullableSetByteParam(boolean expectNull, @Nullable Set<Byte> param);
  void methodWithNullableSetByteHandler(boolean notNull, Handler<@Nullable Set<Byte>> handler);
  Future<@Nullable Set<Byte>> methodWithNullableSetByteHandlerAsyncResult(boolean notNull);
  @Nullable Set<Byte> methodWithNullableSetByteReturn(boolean notNull);

  // Test @Nullable Set<Short> type
  boolean methodWithNonNullableSetShortParam(Set<Short> param);
  void methodWithNullableSetShortParam(boolean expectNull, @Nullable Set<Short> param);
  void methodWithNullableSetShortHandler(boolean notNull, Handler<@Nullable Set<Short>> handler);
  Future<@Nullable Set<Short>> methodWithNullableSetShortHandlerAsyncResult(boolean notNull);
  @Nullable Set<Short> methodWithNullableSetShortReturn(boolean notNull);

  // Test @Nullable Set<Integer> type
  boolean methodWithNonNullableSetIntegerParam(Set<Integer> param);
  void methodWithNullableSetIntegerParam(boolean expectNull, @Nullable Set<Integer> param);
  void methodWithNullableSetIntegerHandler(boolean notNull, Handler<@Nullable Set<Integer>> handler);
  Future<@Nullable Set<Integer>> methodWithNullableSetIntegerHandlerAsyncResult(boolean notNull);
  @Nullable Set<Integer> methodWithNullableSetIntegerReturn(boolean notNull);

  // Test @Nullable Set<Long> type
  boolean methodWithNonNullableSetLongParam(Set<Long> param);
  void methodWithNullableSetLongParam(boolean expectNull, @Nullable Set<Long> param);
  void methodWithNullableSetLongHandler(boolean notNull, Handler<@Nullable Set<Long>> handler);
  Future<@Nullable Set<Long>> methodWithNullableSetLongHandlerAsyncResult(boolean notNull);
  @Nullable Set<Long> methodWithNullableSetLongReturn(boolean notNull);

  // Test @Nullable Set<Float> type
  boolean methodWithNonNullableSetFloatParam(Set<Float> param);
  void methodWithNullableSetFloatParam(boolean expectNull, @Nullable Set<Float> param);
  void methodWithNullableSetFloatHandler(boolean notNull, Handler<@Nullable Set<Float>> handler);
  Future<@Nullable Set<Float>> methodWithNullableSetFloatHandlerAsyncResult(boolean notNull);
  @Nullable Set<Float> methodWithNullableSetFloatReturn(boolean notNull);

  // Test @Nullable Set<Double> type
  boolean methodWithNonNullableSetDoubleParam(Set<Double> param);
  void methodWithNullableSetDoubleParam(boolean expectNull, @Nullable Set<Double> param);
  void methodWithNullableSetDoubleHandler(boolean notNull, Handler<@Nullable Set<Double>> handler);
  Future<@Nullable Set<Double>> methodWithNullableSetDoubleHandlerAsyncResult(boolean notNull);
  @Nullable Set<Double> methodWithNullableSetDoubleReturn(boolean notNull);

  // Test @Nullable Set<Boolean> type
  boolean methodWithNonNullableSetBooleanParam(Set<Boolean> param);
  void methodWithNullableSetBooleanParam(boolean expectNull, @Nullable Set<Boolean> param);
  void methodWithNullableSetBooleanHandler(boolean notNull, Handler<@Nullable Set<Boolean>> handler);
  Future<@Nullable Set<Boolean>> methodWithNullableSetBooleanHandlerAsyncResult(boolean notNull);
  @Nullable Set<Boolean> methodWithNullableSetBooleanReturn(boolean notNull);

  // Test @Nullable Set<String> type
  boolean methodWithNonNullableSetStringParam(Set<String> param);
  void methodWithNullableSetStringParam(boolean expectNull, @Nullable Set<String> param);
  void methodWithNullableSetStringHandler(boolean notNull, Handler<@Nullable Set<String>> handler);
  Future<@Nullable Set<String>> methodWithNullableSetStringHandlerAsyncResult(boolean notNull);
  @Nullable Set<String> methodWithNullableSetStringReturn(boolean notNull);

  // Test @Nullable Set<Character> type
  boolean methodWithNonNullableSetCharParam(Set<Character> param);
  void methodWithNullableSetCharParam(boolean expectNull, @Nullable Set<Character> param);
  void methodWithNullableSetCharHandler(boolean notNull, Handler<@Nullable Set<Character>> handler);
  Future<@Nullable Set<Character>> methodWithNullableSetCharHandlerAsyncResult(boolean notNull);
  @Nullable Set<Character> methodWithNullableSetCharReturn(boolean notNull);

  // Test @Nullable Set<JsonObject> type
  boolean methodWithNonNullableSetJsonObjectParam(Set<JsonObject> param);
  void methodWithNullableSetJsonObjectParam(boolean expectNull, @Nullable Set<JsonObject> param);
  void methodWithNullableSetJsonObjectHandler(boolean notNull, Handler<@Nullable Set<JsonObject>> handler);
  Future<@Nullable Set<JsonObject>> methodWithNullableSetJsonObjectHandlerAsyncResult(boolean notNull);
  @Nullable Set<JsonObject> methodWithNullableSetJsonObjectReturn(boolean notNull);

  // Test @Nullable Set<JsonArray> type
  boolean methodWithNonNullableSetJsonArrayParam(Set<JsonArray> param);
  void methodWithNullableSetJsonArrayParam(boolean expectNull, @Nullable Set<JsonArray> param);
  void methodWithNullableSetJsonArrayHandler(boolean notNull, Handler<@Nullable Set<JsonArray>> handler);
  Future<@Nullable Set<JsonArray>> methodWithNullableSetJsonArrayHandlerAsyncResult(boolean notNull);
  @Nullable Set<JsonArray> methodWithNullableSetJsonArrayReturn(boolean notNull);

  // Test @Nullable Set<Api> type
  boolean methodWithNonNullableSetApiParam(Set<RefedInterface1> param);
  void methodWithNullableSetApiParam(boolean expectNull, @Nullable Set<RefedInterface1> param);
  void methodWithNullableSetApiHandler(boolean notNull, Handler<@Nullable Set<RefedInterface1>> handler);
  Future<@Nullable Set<RefedInterface1>> methodWithNullableSetApiHandlerAsyncResult(boolean notNull);
  @Nullable Set<RefedInterface1> methodWithNullableSetApiReturn(boolean notNull);

  // Test @Nullable Set<DataObject> type
  boolean methodWithNonNullableSetDataObjectParam(Set<TestDataObject> param);
  void methodWithNullableSetDataObjectParam(boolean expectNull, @Nullable Set<TestDataObject> param);
  void methodWithNullableSetDataObjectHandler(boolean notNull, Handler<@Nullable Set<TestDataObject>> handler);
  Future<@Nullable Set<TestDataObject>> methodWithNullableSetDataObjectHandlerAsyncResult(boolean notNull);
  @Nullable Set<TestDataObject> methodWithNullableSetDataObjectReturn(boolean notNull);

  // Test @Nullable Set<Enum> type
  boolean methodWithNonNullableSetEnumParam(Set<TestEnum> param);
  void methodWithNullableSetEnumParam(boolean expectNull, @Nullable Set<TestEnum> param);
  void methodWithNullableSetEnumHandler(boolean notNull, Handler<@Nullable Set<TestEnum>> handler);
  Future<@Nullable Set<TestEnum>> methodWithNullableSetEnumHandlerAsyncResult(boolean notNull);
  @Nullable Set<TestEnum> methodWithNullableSetEnumReturn(boolean notNull);

  // Test @Nullable Set<GenEnum> type
  boolean methodWithNonNullableSetGenEnumParam(Set<TestGenEnum> param);
  void methodWithNullableSetGenEnumParam(boolean expectNull, @Nullable Set<TestGenEnum> param);
  void methodWithNullableSetGenEnumHandler(boolean notNull, Handler<@Nullable Set<TestGenEnum>> handler);
  Future<@Nullable Set<TestGenEnum>> methodWithNullableSetGenEnumHandlerAsyncResult(boolean notNull);
  @Nullable Set<TestGenEnum> methodWithNullableSetGenEnumReturn(boolean notNull);

  // Test @Nullable Map<String, Byte> type
  boolean methodWithNonNullableMapByteParam(Map<String, Byte> param);
  void methodWithNullableMapByteParam(boolean expectNull, @Nullable Map<String, Byte> param);
  void methodWithNullableMapByteHandler(boolean notNull, Handler<@Nullable Map<String, Byte>> handler);
  Future<@Nullable Map<String, Byte>> methodWithNullableMapByteHandlerAsyncResult(boolean notNull);
  @Nullable Map<String, Byte> methodWithNullableMapByteReturn(boolean notNull);

  // Test @Nullable Map<String, Short> type
  boolean methodWithNonNullableMapShortParam(Map<String, Short> param);
  void methodWithNullableMapShortParam(boolean expectNull, @Nullable Map<String, Short> param);
  void methodWithNullableMapShortHandler(boolean notNull, Handler<@Nullable Map<String, Short>> handler);
  Future<@Nullable Map<String, Short>> methodWithNullableMapShortHandlerAsyncResult(boolean notNull);
  @Nullable Map<String, Short> methodWithNullableMapShortReturn(boolean notNull);

  // Test @Nullable Map<String, Integer> type
  boolean methodWithNonNullableMapIntegerParam(Map<String, Integer> param);
  void methodWithNullableMapIntegerParam(boolean expectNull, @Nullable Map<String, Integer> param);
  void methodWithNullableMapIntegerHandler(boolean notNull, Handler<@Nullable Map<String, Integer>> handler);
  Future<@Nullable Map<String, Integer>> methodWithNullableMapIntegerHandlerAsyncResult(boolean notNull);
  @Nullable Map<String, Integer> methodWithNullableMapIntegerReturn(boolean notNull);

  // Test @Nullable Map<String, Long> type
  boolean methodWithNonNullableMapLongParam(Map<String, Long> param);
  void methodWithNullableMapLongParam(boolean expectNull, @Nullable Map<String, Long> param);
  void methodWithNullableMapLongHandler(boolean notNull, Handler<@Nullable Map<String, Long>> handler);
  Future<@Nullable Map<String, Long>> methodWithNullableMapLongHandlerAsyncResult(boolean notNull);
  @Nullable Map<String, Long> methodWithNullableMapLongReturn(boolean notNull);

  // Test @Nullable Map<String, Float> type
  boolean methodWithNonNullableMapFloatParam(Map<String, Float> param);
  void methodWithNullableMapFloatParam(boolean expectNull, @Nullable Map<String, Float> param);
  void methodWithNullableMapFloatHandler(boolean notNull, Handler<@Nullable Map<String, Float>> handler);
  Future<@Nullable Map<String, Float>> methodWithNullableMapFloatHandlerAsyncResult(boolean notNull);
  @Nullable Map<String, Float> methodWithNullableMapFloatReturn(boolean notNull);

  // Test @Nullable Map<String, Double> type
  boolean methodWithNonNullableMapDoubleParam(Map<String, Double> param);
  void methodWithNullableMapDoubleParam(boolean expectNull, @Nullable Map<String, Double> param);
  void methodWithNullableMapDoubleHandler(boolean notNull, Handler<@Nullable Map<String, Double>> handler);
  Future<@Nullable Map<String, Double>> methodWithNullableMapDoubleHandlerAsyncResult(boolean notNull);
  @Nullable Map<String, Double> methodWithNullableMapDoubleReturn(boolean notNull);

  // Test @Nullable Map<String, Boolean> type
  boolean methodWithNonNullableMapBooleanParam(Map<String, Boolean> param);
  void methodWithNullableMapBooleanParam(boolean expectNull, @Nullable Map<String, Boolean> param);
  void methodWithNullableMapBooleanHandler(boolean notNull, Handler<@Nullable Map<String, Boolean>> handler);
  Future<@Nullable Map<String, Boolean>> methodWithNullableMapBooleanHandlerAsyncResult(boolean notNull);
  @Nullable Map<String, Boolean> methodWithNullableMapBooleanReturn(boolean notNull);

  // Test @Nullable Map<String, String> type
  boolean methodWithNonNullableMapStringParam(Map<String, String> param);
  void methodWithNullableMapStringParam(boolean expectNull, @Nullable Map<String, String> param);
  void methodWithNullableMapStringHandler(boolean notNull, Handler<@Nullable Map<String, String>> handler);
  Future<@Nullable Map<String, String>> methodWithNullableMapStringHandlerAsyncResult(boolean notNull);
  @Nullable Map<String, String> methodWithNullableMapStringReturn(boolean notNull);

  // Test @Nullable Map<String, Character> type
  boolean methodWithNonNullableMapCharParam(Map<String, Character> param);
  void methodWithNullableMapCharParam(boolean expectNull, @Nullable Map<String, Character> param);
  void methodWithNullableMapCharHandler(boolean notNull, Handler<@Nullable Map<String, Character>> handler);
  Future<@Nullable Map<String, Character>> methodWithNullableMapCharHandlerAsyncResult(boolean notNull);
  @Nullable Map<String, Character> methodWithNullableMapCharReturn(boolean notNull);

  // Test @Nullable Map<String, JsonObject> type
  boolean methodWithNonNullableMapJsonObjectParam(Map<String, JsonObject> param);
  void methodWithNullableMapJsonObjectParam(boolean expectNull, @Nullable Map<String, JsonObject> param);
  void methodWithNullableMapJsonObjectHandler(boolean notNull, Handler<@Nullable Map<String, JsonObject>> handler);
  Future<@Nullable Map<String, JsonObject>> methodWithNullableMapJsonObjectHandlerAsyncResult(boolean notNull);
  @Nullable Map<String, JsonObject> methodWithNullableMapJsonObjectReturn(boolean notNull);

  // Test @Nullable Map<String, JsonArray> type
  boolean methodWithNonNullableMapJsonArrayParam(Map<String, JsonArray> param);
  void methodWithNullableMapJsonArrayParam(boolean expectNull, @Nullable Map<String, JsonArray> param);
  void methodWithNullableMapJsonArrayHandler(boolean notNull, Handler<@Nullable Map<String, JsonArray>> handler);
  Future<@Nullable Map<String, JsonArray>> methodWithNullableMapJsonArrayHandlerAsyncResult(boolean notNull);
  @Nullable Map<String, JsonArray> methodWithNullableMapJsonArrayReturn(boolean notNull);

  // Test @Nullable Map<String, Api> type
  boolean methodWithNonNullableMapApiParam(Map<String, RefedInterface1> param);
  void methodWithNullableMapApiParam(boolean expectNull, @Nullable Map<String, RefedInterface1> param);

  // Test List<@Nullable Byte> type
  void methodWithListNullableByteParam(List<@Nullable Byte> param);
  void methodWithListNullableByteHandler(Handler<List<@Nullable Byte>> handler);
  Future<List<@Nullable Byte>> methodWithListNullableByteHandlerAsyncResult();
  List<@Nullable Byte> methodWithListNullableByteReturn();

  // Test List<@Nullable Short> type
  void methodWithListNullableShortParam(List<@Nullable Short> param);
  void methodWithListNullableShortHandler(Handler<List<@Nullable Short>> handler);
  Future<List<@Nullable Short>> methodWithListNullableShortHandlerAsyncResult();
  List<@Nullable Short> methodWithListNullableShortReturn();

  // Test List<@Nullable Integer> type
  void methodWithListNullableIntegerParam(List<@Nullable Integer> param);
  void methodWithListNullableIntegerHandler(Handler<List<@Nullable Integer>> handler);
  Future<List<@Nullable Integer>> methodWithListNullableIntegerHandlerAsyncResult();
  List<@Nullable Integer> methodWithListNullableIntegerReturn();

  // Test List<@Nullable Long> type
  void methodWithListNullableLongParam(List<@Nullable Long> param);
  void methodWithListNullableLongHandler(Handler<List<@Nullable Long>> handler);
  Future<List<@Nullable Long>> methodWithListNullableLongHandlerAsyncResult();
  List<@Nullable Long> methodWithListNullableLongReturn();

  // Test List<@Nullable Boolean> type
  void methodWithListNullableBooleanParam(List<@Nullable Boolean> param);
  void methodWithListNullableBooleanHandler(Handler<List<@Nullable Boolean>> handler);
  Future<List<@Nullable Boolean>> methodWithListNullableBooleanHandlerAsyncResult();
  List<@Nullable Boolean> methodWithListNullableBooleanReturn();

  // Test List<@Nullable Float> type
  void methodWithListNullableFloatParam(List<@Nullable Float> param);
  void methodWithListNullableFloatHandler(Handler<List<@Nullable Float>> handler);
  Future<List<@Nullable Float>> methodWithListNullableFloatHandlerAsyncResult();
  List<@Nullable Float> methodWithListNullableFloatReturn();

  // Test List<@Nullable Double> type
  void methodWithListNullableDoubleParam(List<@Nullable Double> param);
  void methodWithListNullableDoubleHandler(Handler<List<@Nullable Double>> handler);
  Future<List<@Nullable Double>> methodWithListNullableDoubleHandlerAsyncResult();
  List<@Nullable Double> methodWithListNullableDoubleReturn();

  // Test List<@Nullable String> type
  void methodWithListNullableStringParam(List<@Nullable String> param);
  void methodWithListNullableStringHandler(Handler<List<@Nullable String>> handler);
  Future<List<@Nullable String>> methodWithListNullableStringHandlerAsyncResult();
  List<@Nullable String> methodWithListNullableStringReturn();

  // Test List<@Nullable Character> type
  void methodWithListNullableCharParam(List<@Nullable Character> param);
  void methodWithListNullableCharHandler(Handler<List<@Nullable Character>> handler);
  Future<List<@Nullable Character>> methodWithListNullableCharHandlerAsyncResult();
  List<@Nullable Character> methodWithListNullableCharReturn();

  // Test List<@Nullable JsonObject> type
  void methodWithListNullableJsonObjectParam(List<@Nullable JsonObject> param);
  void methodWithListNullableJsonObjectHandler(Handler<List<@Nullable JsonObject>> handler);
  Future<List<@Nullable JsonObject>> methodWithListNullableJsonObjectHandlerAsyncResult();
  List<@Nullable JsonObject> methodWithListNullableJsonObjectReturn();

  // Test List<@Nullable String> type
  void methodWithListNullableJsonArrayParam(List<@Nullable JsonArray> param);
  void methodWithListNullableJsonArrayHandler(Handler<List<@Nullable JsonArray>> handler);
  Future<List<@Nullable JsonArray>> methodWithListNullableJsonArrayHandlerAsyncResult();
  List<@Nullable JsonArray> methodWithListNullableJsonArrayReturn();

  // Test List<@Nullable Api> type
  void methodWithListNullableApiParam(List<@Nullable RefedInterface1> param);
  void methodWithListNullableApiHandler(Handler<List<@Nullable RefedInterface1>> handler);
  Future<List<@Nullable RefedInterface1>> methodWithListNullableApiHandlerAsyncResult();
  List<@Nullable RefedInterface1> methodWithListNullableApiReturn();

  // Test List<@Nullable DataObject> type
  void methodWithListNullableDataObjectParam(List<@Nullable TestDataObject> param);
  void methodWithListNullableDataObjectHandler(Handler<List<@Nullable TestDataObject>> handler);
  Future<List<@Nullable TestDataObject>> methodWithListNullableDataObjectHandlerAsyncResult();
  List<@Nullable TestDataObject> methodWithListNullableDataObjectReturn();

  // Test List<@Nullable Enum> type
  void methodWithListNullableEnumParam(List<@Nullable TestEnum> param);
  void methodWithListNullableEnumHandler(Handler<List<@Nullable TestEnum>> handler);
  Future<List<@Nullable TestEnum>> methodWithListNullableEnumHandlerAsyncResult();
  List<@Nullable TestEnum> methodWithListNullableEnumReturn();

  // Test List<@Nullable GenEnum> type
  void methodWithListNullableGenEnumParam(List<@Nullable TestGenEnum> param);
  void methodWithListNullableGenEnumHandler(Handler<List<@Nullable TestGenEnum>> handler);
  Future<List<@Nullable TestGenEnum>> methodWithListNullableGenEnumHandlerAsyncResult();
  List<@Nullable TestGenEnum> methodWithListNullableGenEnumReturn();

  // Test Set<@Nullable Byte> type
  void methodWithSetNullableByteParam(Set<@Nullable Byte> param);
  void methodWithSetNullableByteHandler(Handler<Set<@Nullable Byte>> handler);
  Future<Set<@Nullable Byte>> methodWithSetNullableByteHandlerAsyncResult();
  Set<@Nullable Byte> methodWithSetNullableByteReturn();

  // Test Set<@Nullable Short> type
  void methodWithSetNullableShortParam(Set<@Nullable Short> param);
  void methodWithSetNullableShortHandler(Handler<Set<@Nullable Short>> handler);
  Future<Set<@Nullable Short>> methodWithSetNullableShortHandlerAsyncResult();
  Set<@Nullable Short> methodWithSetNullableShortReturn();

  // Test Set<@Nullable Integer> type
  void methodWithSetNullableIntegerParam(Set<@Nullable Integer> param);
  void methodWithSetNullableIntegerHandler(Handler<Set<@Nullable Integer>> handler);
  Future<Set<@Nullable Integer>> methodWithSetNullableIntegerHandlerAsyncResult();
  Set<@Nullable Integer> methodWithSetNullableIntegerReturn();

  // Test Set<@Nullable Long> type
  void methodWithSetNullableLongParam(Set<@Nullable Long> param);
  void methodWithSetNullableLongHandler(Handler<Set<@Nullable Long>> handler);
  Future<Set<@Nullable Long>> methodWithSetNullableLongHandlerAsyncResult();
  Set<@Nullable Long> methodWithSetNullableLongReturn();

  // Test Set<@Nullable Boolean> type
  void methodWithSetNullableBooleanParam(Set<@Nullable Boolean> param);
  void methodWithSetNullableBooleanHandler(Handler<Set<@Nullable Boolean>> handler);
  Future<Set<@Nullable Boolean>> methodWithSetNullableBooleanHandlerAsyncResult();
  Set<@Nullable Boolean> methodWithSetNullableBooleanReturn();

  // Test Set<@Nullable Float> type
  void methodWithSetNullableFloatParam(Set<@Nullable Float> param);
  void methodWithSetNullableFloatHandler(Handler<Set<@Nullable Float>> handler);
  Future<Set<@Nullable Float>> methodWithSetNullableFloatHandlerAsyncResult();
  Set<@Nullable Float> methodWithSetNullableFloatReturn();

  // Test Set<@Nullable Double> type
  void methodWithSetNullableDoubleParam(Set<@Nullable Double> param);
  void methodWithSetNullableDoubleHandler(Handler<Set<@Nullable Double>> handler);
  Future<Set<@Nullable Double>> methodWithSetNullableDoubleHandlerAsyncResult();
  Set<@Nullable Double> methodWithSetNullableDoubleReturn();

  // Test Set<@Nullable String> type
  void methodWithSetNullableStringParam(Set<@Nullable String> param);
  void methodWithSetNullableStringHandler(Handler<Set<@Nullable String>> handler);
  Future<Set<@Nullable String>> methodWithSetNullableStringHandlerAsyncResult();
  Set<@Nullable String> methodWithSetNullableStringReturn();

  // Test Set<@Nullable Character> type
  void methodWithSetNullableCharParam(Set<@Nullable Character> param);
  void methodWithSetNullableCharHandler(Handler<Set<@Nullable Character>> handler);
  Future<Set<@Nullable Character>> methodWithSetNullableCharHandlerAsyncResult();
  Set<@Nullable Character> methodWithSetNullableCharReturn();

  // Test Set<@Nullable JsonObject> type
  void methodWithSetNullableJsonObjectParam(Set<@Nullable JsonObject> param);
  void methodWithSetNullableJsonObjectHandler(Handler<Set<@Nullable JsonObject>> handler);
  Future<Set<@Nullable JsonObject>> methodWithSetNullableJsonObjectHandlerAsyncResult();
  Set<@Nullable JsonObject> methodWithSetNullableJsonObjectReturn();

  // Test Set<@Nullable String> type
  void methodWithSetNullableJsonArrayParam(Set<@Nullable JsonArray> param);
  void methodWithSetNullableJsonArrayHandler(Handler<Set<@Nullable JsonArray>> handler);
  Future<Set<@Nullable JsonArray>> methodWithSetNullableJsonArrayHandlerAsyncResult();
  Set<@Nullable JsonArray> methodWithSetNullableJsonArrayReturn();

  // Test Set<@Nullable Api> type
  void methodWithSetNullableApiParam(Set<@Nullable RefedInterface1> param);
  void methodWithSetNullableApiHandler(Handler<Set<@Nullable RefedInterface1>> handler);
  Future<Set<@Nullable RefedInterface1>> methodWithSetNullableApiHandlerAsyncResult();
  Set<@Nullable RefedInterface1> methodWithSetNullableApiReturn();

  // Test Set<@Nullable DataObject> type
  void methodWithSetNullableDataObjectParam(Set<@Nullable TestDataObject> param);
  void methodWithSetNullableDataObjectHandler(Handler<Set<@Nullable TestDataObject>> handler);
  Future<Set<@Nullable TestDataObject>> methodWithSetNullableDataObjectHandlerAsyncResult();
  Set<@Nullable TestDataObject> methodWithSetNullableDataObjectReturn();

  // Test Set<@Nullable Enum> type
  void methodWithSetNullableEnumParam(Set<@Nullable TestEnum> param);
  void methodWithSetNullableEnumHandler(Handler<Set<@Nullable TestEnum>> handler);
  Future<Set<@Nullable TestEnum>> methodWithSetNullableEnumHandlerAsyncResult();
  Set<@Nullable TestEnum> methodWithSetNullableEnumReturn();

  // Test Set<@Nullable GenEnum> type
  void methodWithSetNullableGenEnumParam(Set<@Nullable TestGenEnum> param);
  void methodWithSetNullableGenEnumHandler(Handler<Set<@Nullable TestGenEnum>> handler);
  Future<Set<@Nullable TestGenEnum>> methodWithSetNullableGenEnumHandlerAsyncResult();
  Set<@Nullable TestGenEnum> methodWithSetNullableGenEnumReturn();

  // Test Map<String, @Nullable Byte> type
  void methodWithMapNullableByteParam(Map<String, @Nullable Byte> param);
  void methodWithMapNullableByteHandler(Handler<Map<String, @Nullable Byte>> handler);
  Future<Map<String, @Nullable Byte>> methodWithMapNullableByteHandlerAsyncResult();
  Map<String, @Nullable Byte> methodWithMapNullableByteReturn();

  // Test Map<String, @Nullable Short> type
  void methodWithMapNullableShortParam(Map<String, @Nullable Short> param);
  void methodWithMapNullableShortHandler(Handler<Map<String, @Nullable Short>> handler);
  Future<Map<String, @Nullable Short>> methodWithMapNullableShortHandlerAsyncResult();
  Map<String, @Nullable Short> methodWithMapNullableShortReturn();

  // Test Map<String, @Nullable Integer> type
  void methodWithMapNullableIntegerParam(Map<String, @Nullable Integer> param);
  void methodWithMapNullableIntegerHandler(Handler<Map<String, @Nullable Integer>> handler);
  Future<Map<String, @Nullable Integer>> methodWithMapNullableIntegerHandlerAsyncResult();
  Map<String, @Nullable Integer> methodWithMapNullableIntegerReturn();

  // Test Map<String, @Nullable Long> type
  void methodWithMapNullableLongParam(Map<String, @Nullable Long> param);
  void methodWithMapNullableLongHandler(Handler<Map<String, @Nullable Long>> handler);
  Future<Map<String, @Nullable Long>> methodWithMapNullableLongHandlerAsyncResult();
  Map<String, @Nullable Long> methodWithMapNullableLongReturn();

  // Test Map<String, @Nullable Boolean> type
  void methodWithMapNullableBooleanParam(Map<String, @Nullable Boolean> param);
  void methodWithMapNullableBooleanHandler(Handler<Map<String, @Nullable Boolean>> handler);
  Future<Map<String, @Nullable Boolean>> methodWithMapNullableBooleanHandlerAsyncResult();
  Map<String, @Nullable Boolean> methodWithMapNullableBooleanReturn();

  // Test Map<String, @Nullable Float> type
  void methodWithMapNullableFloatParam(Map<String, @Nullable Float> param);
  void methodWithMapNullableFloatHandler(Handler<Map<String, @Nullable Float>> handler);
  Future<Map<String, @Nullable Float>> methodWithMapNullableFloatHandlerAsyncResult();
  Map<String, @Nullable Float> methodWithMapNullableFloatReturn();

  // Test Map<String, @Nullable Double> type
  void methodWithMapNullableDoubleParam(Map<String, @Nullable Double> param);
  void methodWithMapNullableDoubleHandler(Handler<Map<String, @Nullable Double>> handler);
  Future<Map<String, @Nullable Double>> methodWithMapNullableDoubleHandlerAsyncResult();
  Map<String, @Nullable Double> methodWithMapNullableDoubleReturn();

  // Test Map<String, @Nullable String> type
  void methodWithMapNullableStringParam(Map<String, @Nullable String> param);
  void methodWithMapNullableStringHandler(Handler<Map<String, @Nullable String>> handler);
  Future<Map<String, @Nullable String>> methodWithMapNullableStringHandlerAsyncResult();
  Map<String, @Nullable String> methodWithMapNullableStringReturn();

  // Test Map<String, @Nullable Character> type
  void methodWithMapNullableCharParam(Map<String, @Nullable Character> param);
  void methodWithMapNullableCharHandler(Handler<Map<String, @Nullable Character>> handler);
  Future<Map<String, @Nullable Character>> methodWithMapNullableCharHandlerAsyncResult();
  Map<String, @Nullable Character> methodWithMapNullableCharReturn();

  // Test Map<String, @Nullable JsonObject> type
  void methodWithMapNullableJsonObjectParam(Map<String, @Nullable JsonObject> param);
  void methodWithMapNullableJsonObjectHandler(Handler<Map<String, @Nullable JsonObject>> handler);
  Future<Map<String, @Nullable JsonObject>> methodWithMapNullableJsonObjectHandlerAsyncResult();
  Map<String, @Nullable JsonObject> methodWithMapNullableJsonObjectReturn();

  // Test Map<String, @Nullable String> type
  void methodWithMapNullableJsonArrayParam(Map<String, @Nullable JsonArray> param);
  void methodWithMapNullableJsonArrayHandler(Handler<Map<String, @Nullable JsonArray>> handler);
  Future<Map<String, @Nullable JsonArray>> methodWithMapNullableJsonArrayHandlerAsyncResult();
  Map<String, @Nullable JsonArray> methodWithMapNullableJsonArrayReturn();

  // Test Map<String, @Nullable Api> type
  void methodWithMapNullableApiParam(Map<String, @Nullable RefedInterface1> param);

  // Test @Nullable handlers
  void methodWithNullableHandler(boolean expectNull, @Nullable Handler<String> handler);
  @Nullable Future<String> methodWithNullableHandlerAsyncResult(boolean expectNull);

}
