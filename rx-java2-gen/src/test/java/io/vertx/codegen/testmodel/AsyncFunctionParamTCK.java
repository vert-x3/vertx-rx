package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author <a href="slinkydeveloper.com">Francesco Guardiani</a>
 */
@VertxGen
public interface AsyncFunctionParamTCK {

  List<String> methodWithBasicParam(
    Function<Byte, Future<String>> byteFunc,
    Function<Short, Future<String>> shortFunc,
    Function<Integer, Future<String>> integerFunc,
    Function<Long, Future<String>> longFunc,
    Function<Float, Future<String>> floatFunc,
    Function<Double, Future<String>> doubleFunc,
    Function<Boolean, Future<String>> booleanFunc,
    Function<Character, Future<String>> charFunc,
    Function<String, Future<String>> stringFunc
  );

  List<String> methodWithJsonParam(Function<JsonObject, Future<String>> objectFunc, Function<JsonArray, Future<String>> arrayFunc);

  String methodWithVoidParam(Function<Void, Future<String>> func);
  String methodWithUserTypeParam(RefedInterface1 arg, Function<RefedInterface1, Future<String>> func);
  String methodWithObjectParam(Object arg, Function<Object, Future<String>> func);
  String methodWithDataObjectParam(Function<TestDataObject, Future<String>> func);
  String methodWithEnumParam(Function<TestEnum, Future<String>> func);
  String methodWithListParam(Function<List<String>, Future<String>> stringFunc);
  String methodWithSetParam(Function<Set<String>, Future<String>> func);
  String methodWithMapParam(Function<Map<String, String>, Future<String>> func);

  <T> String methodWithGenericParam(T t, Function<T, Future<String>> func);
  <T> String methodWithGenericUserTypeParam(T t, Function<GenericRefedInterface<T>, Future<String>> func);

  String methodWithBasicReturn(
    Function<String, Future<Byte>> byteFunc,
    Function<String, Future<Short>> shortFunc,
    Function<String, Future<Integer>> integerFunc,
    Function<String, Future<Long>> longFunc,
    Function<String, Future<Float>> floatFunc,
    Function<String, Future<Double>> doubleFunc,
    Function<String, Future<Boolean>> booleanFunc,
    Function<String, Future<Character>> charFunc,
    Function<String, Future<String>> stringFunc
  );

  String methodWithJsonReturn(Function<String, Future<JsonObject>> objectFunc, Function<String, Future<JsonArray>> arrayFunc);
  String methodWithObjectReturn(Function<Integer, Future<Object>> func);
  String methodWithDataObjectReturn(Function<String, Future<TestDataObject>> func);
  String methodWithEnumReturn(Function<String, Future<TestEnum>> func);
  String methodWithListReturn(Function<String, Future<List<String>>> func);
  String methodWithSetReturn(Function<String, Future<Set<String>>> func);
  String methodWithMapReturn(Function<String, Future<Map<String, String>>> func);
  <T> String methodWithGenericReturn(Function<Integer, Future<T>> func);
  <T> String methodWithGenericUserTypeReturn(Function<GenericRefedInterface<T>, Future<GenericRefedInterface<T>>> func);

}
