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
 * @author <a href="slinkydeveloper.com">Francesco Guardiani</a>
 */
@VertxGen
public interface AsyncSupplierParamTCK {

  List<String> methodWithBasicParam(
    Handler<Future<String>> byteFunc,
    Handler<Future<String>> shortFunc,
    Handler<Future<String>> integerFunc,
    Handler<Future<String>> longFunc,
    Handler<Future<String>> floatFunc,
    Handler<Future<String>> doubleFunc,
    Handler<Future<String>> booleanFunc,
    Handler<Future<String>> charFunc,
    Handler<Future<String>> stringFunc
  );

  List<String> methodWithJsonParam(Handler<Future<String>> objectFunc, Handler<Future<String>> arrayFunc);

  String methodWithVoidParam(Handler<Future<String>> func);
  String methodWithUserTypeParam(RefedInterface1 arg, Handler<Future<String>> func);
  String methodWithObjectParam(Object arg, Handler<Future<String>> func);
  String methodWithDataObjectParam(Handler<Future<String>> func);
  String methodWithEnumParam(Handler<Future<String>> func);
  String methodWithListParam(Handler<Future<String>> stringFunc);
  String methodWithSetParam(Handler<Future<String>> func);
  String methodWithMapParam(Handler<Future<Map<String, String>>> func);

  <T> String methodWithGenericParam(T t, Handler<Future<String>> func);
  <T> String methodWithGenericUserTypeParam(T t, Handler<Future<String>> func);

  String methodWithBasicReturn(
    Handler<Future<Byte>> byteFunc,
    Handler<Future<Short>> shortFunc,
    Handler<Future<Integer>> integerFunc,
    Handler<Future<Long>> longFunc,
    Handler<Future<Float>> floatFunc,
    Handler<Future<Double>> doubleFunc,
    Handler<Future<Boolean>> booleanFunc,
    Handler<Future<Character>> charFunc,
    Handler<Future<String>> stringFunc
  );

  String methodWithJsonReturn(Handler<Future<JsonObject>> objectFunc, Handler<Future<JsonArray>> arrayFunc);
  String methodWithDataObjectReturn(Handler<Future<TestDataObject>> func);
  String methodWithEnumReturn(Handler<Future<TestEnum>> func);
  String methodWithListReturn(Handler<Future<List<String>>> func);
  String methodWithSetReturn(Handler<Future<Set<String>>> func);
  String methodWithMapReturn(Handler<Future<Map<String, String>>> func);
  <T> String methodWithGenericReturn(Handler<Future<T>> func);
  <T> String methodWithGenericUserTypeReturn(Handler<Future<GenericRefedInterface<T>>> func);

}
