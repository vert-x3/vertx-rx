package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface SupplierParamTCK {

  String methodWithBasicReturn(
      Supplier<Byte> byteSupplier,
      Supplier<Short> shortSupplier,
      Supplier<Integer> integerSupplier,
      Supplier<Long> longSupplier,
      Supplier<Float> floatSupplier,
      Supplier<Double> doubleSupplier,
      Supplier<Boolean> booleanSupplier,
      Supplier<Character> charSupplier,
      Supplier<String> stringSupplier
  );

  String methodWithJsonReturn(Supplier<JsonObject> objectSupplier, Supplier<JsonArray> arraySupplier);
  String methodWithObjectReturn(Supplier<Object> Supplier);
  String methodWithDataObjectReturn(Supplier<TestDataObject> Supplier);
  String methodWithEnumReturn(Supplier<TestEnum> Supplier);
  String methodWithListReturn(Supplier<List<String>> Supplier);
  String methodWithSetReturn(Supplier<Set<String>> Supplier);
  String methodWithMapReturn(Supplier<Map<String, String>> Supplier);
  <T> String methodWithGenericReturn(Supplier<T> Supplier);
  <T> String methodWithGenericUserTypeReturn(Supplier<GenericRefedInterface<T>> Supplier);

  String methodWithNullableListReturn(Supplier<@Nullable List<String>> Supplier);

}
