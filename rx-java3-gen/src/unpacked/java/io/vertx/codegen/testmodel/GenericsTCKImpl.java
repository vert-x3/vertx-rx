package io.vertx.codegen.testmodel;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GenericsTCKImpl implements GenericsTCK {

  @Override
  public GenericRefedInterface<Byte> methodWithByteParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn((byte)123);
  }

  @Override
  public GenericRefedInterface<Short> methodWithShortParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn((short)1234);
  }

  @Override
  public GenericRefedInterface<Integer> methodWithIntegerParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(123456);
  }

  @Override
  public GenericRefedInterface<Long> methodWithLongParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(123456789L);
  }

  @Override
  public GenericRefedInterface<Float> methodWithFloatParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(0.34F);
  }

  @Override
  public GenericRefedInterface<Double> methodWithDoubleParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(0.314D);
  }

  @Override
  public GenericRefedInterface<Boolean> methodWithBooleanParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(true);
  }

  @Override
  public GenericRefedInterface<Character> methodWithCharacterParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn('F');
  }

  @Override
  public GenericRefedInterface<String> methodWithStringParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn("zoumbawe");
  }

  @Override
  public GenericRefedInterface<JsonObject> methodWithJsonObjectParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(new JsonObject().put("cheese", "stilton"));
  }

  @Override
  public GenericRefedInterface<JsonArray> methodWithJsonArrayParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(new JsonArray().add("cheese").add("stilton"));
  }

  @Override
  public GenericRefedInterface<TestDataObject> methodWithDataObjectParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(new TestDataObject().setWibble(3.14).setFoo("foo_value").setBar(123456));
  }

  @Override
  public GenericRefedInterface<TestEnum> methodWithEnumParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(TestEnum.WESTON);
  }

  @Override
  public GenericRefedInterface<TestGenEnum> methodWithGenEnumParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(TestGenEnum.LELAND);
  }

  @Override
  public GenericRefedInterface<RefedInterface1> methodWithUserTypeParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(new RefedInterface1Impl().setString("foo"));
  }

  @Override
  public GenericRefedInterface<List<Object>> methodWithListOfObjectsParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(Arrays.asList(0, "foo", true));
  }

  @Override
  public GenericRefedInterface<Set<Object>> methodWithSetOfObjectsParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(new HashSet<>(Arrays.asList(0, "foo", true)));
  }

  @Override
  public GenericRefedInterface<Map<String, Object>> methodWithMapOfObjectsParameterizedReturn() {
    return methodWithClassTypeParameterizedReturn(Collections.singletonMap("foo", "bar"));
  }

  @Override
  public void methodWithHandlerByteParameterized(Handler<GenericRefedInterface<Byte>> handler) {
    handler.handle(methodWithByteParameterizedReturn());
  }

  @Override
  public void methodWithHandlerShortParameterized(Handler<GenericRefedInterface<Short>> handler) {
    handler.handle(methodWithShortParameterizedReturn());
  }

  @Override
  public void methodWithHandlerIntegerParameterized(Handler<GenericRefedInterface<Integer>> handler) {
    handler.handle(methodWithIntegerParameterizedReturn());
  }

  @Override
  public void methodWithHandlerLongParameterized(Handler<GenericRefedInterface<Long>> handler) {
    handler.handle(methodWithLongParameterizedReturn());
  }

  @Override
  public void methodWithHandlerFloatParameterized(Handler<GenericRefedInterface<Float>> handler) {
    handler.handle(methodWithFloatParameterizedReturn());
  }

  @Override
  public void methodWithHandlerDoubleParameterized(Handler<GenericRefedInterface<Double>> handler) {
    handler.handle(methodWithDoubleParameterizedReturn());
  }

  @Override
  public void methodWithHandlerBooleanParameterized(Handler<GenericRefedInterface<Boolean>> handler) {
    handler.handle(methodWithBooleanParameterizedReturn());
  }

  @Override
  public void methodWithHandlerCharacterParameterized(Handler<GenericRefedInterface<Character>> handler) {
    handler.handle(methodWithCharacterParameterizedReturn());
  }

  @Override
  public void methodWithHandlerStringParameterized(Handler<GenericRefedInterface<String>> handler) {
    handler.handle(methodWithStringParameterizedReturn());
  }

  @Override
  public void methodWithHandlerJsonObjectParameterized(Handler<GenericRefedInterface<JsonObject>> handler) {
    handler.handle(methodWithJsonObjectParameterizedReturn());
  }

  @Override
  public void methodWithHandlerJsonArrayParameterized(Handler<GenericRefedInterface<JsonArray>> handler) {
    handler.handle(methodWithJsonArrayParameterizedReturn());
  }

  @Override
  public void methodWithHandlerDataObjectParameterized(Handler<GenericRefedInterface<TestDataObject>> handler) {
    handler.handle(methodWithDataObjectParameterizedReturn());
  }

  @Override
  public void methodWithHandlerEnumParameterized(Handler<GenericRefedInterface<TestEnum>> handler) {
    handler.handle(methodWithEnumParameterizedReturn());
  }

  @Override
  public void methodWithHandlerGenEnumParameterized(Handler<GenericRefedInterface<TestGenEnum>> handler) {
    handler.handle(methodWithGenEnumParameterizedReturn());
  }

  @Override
  public void methodWithHandlerUserTypeParameterized(Handler<GenericRefedInterface<RefedInterface1>> handler) {
    handler.handle(methodWithUserTypeParameterizedReturn());
  }

  @Override
  public void methodWithHandlerListOfObjectsParameterized(Handler<GenericRefedInterface<List<Object>>> handler) {
    handler.handle(methodWithListOfObjectsParameterizedReturn());
  }

  @Override
  public void methodWithHandlerSetOfObjectsParameterized(Handler<GenericRefedInterface<Set<Object>>> handler) {
    handler.handle(methodWithSetOfObjectsParameterizedReturn());
  }

  @Override
  public void methodWithHandlerMapOfObjectsParameterized(Handler<GenericRefedInterface<Map<String, Object>>> handler) {
    handler.handle(methodWithMapOfObjectsParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<Byte>> methodWithHandlerAsyncResultByteParameterized() {
    return Future.succeededFuture(methodWithByteParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<Short>> methodWithHandlerAsyncResultShortParameterized() {
    return Future.succeededFuture(methodWithShortParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<Integer>> methodWithHandlerAsyncResultIntegerParameterized() {
    return Future.succeededFuture(methodWithIntegerParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<Long>> methodWithHandlerAsyncResultLongParameterized() {
    return Future.succeededFuture(methodWithLongParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<Float>> methodWithHandlerAsyncResultFloatParameterized() {
    return Future.succeededFuture(methodWithFloatParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<Double>> methodWithHandlerAsyncResultDoubleParameterized() {
    return Future.succeededFuture(methodWithDoubleParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<Boolean>> methodWithHandlerAsyncResultBooleanParameterized() {
    return Future.succeededFuture(methodWithBooleanParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<Character>> methodWithHandlerAsyncResultCharacterParameterized() {
    return Future.succeededFuture(methodWithCharacterParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<String>> methodWithHandlerAsyncResultStringParameterized() {
    return Future.succeededFuture(methodWithStringParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<JsonObject>> methodWithHandlerAsyncResultJsonObjectParameterized() {
    return Future.succeededFuture(methodWithJsonObjectParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<JsonArray>> methodWithHandlerAsyncResultJsonArrayParameterized() {
    return Future.succeededFuture(methodWithJsonArrayParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<TestDataObject>> methodWithHandlerAsyncResultDataObjectParameterized() {
    return Future.succeededFuture(methodWithDataObjectParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<TestEnum>> methodWithHandlerAsyncResultEnumParameterized() {
    return Future.succeededFuture(methodWithEnumParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<TestGenEnum>> methodWithHandlerAsyncResultGenEnumParameterized() {
    return Future.succeededFuture(methodWithGenEnumParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<RefedInterface1>> methodWithHandlerAsyncResultUserTypeParameterized() {
    return Future.succeededFuture(methodWithUserTypeParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<List<Object>>> methodWithHandlerAsyncResultListOfObjectsParameterized() {
    return Future.succeededFuture(methodWithListOfObjectsParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<Set<Object>>> methodWithHandlerAsyncResultSetOfObjectsParameterized() {
    return Future.succeededFuture(methodWithSetOfObjectsParameterizedReturn());
  }

  @Override
  public Future<GenericRefedInterface<Map<String, Object>>> methodWithHandlerAsyncResultMapOfObjectsParameterized() {
    return Future.succeededFuture(methodWithMapOfObjectsParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamByteParameterized(Function<GenericRefedInterface<Byte>, String> handler) {
    handler.apply(methodWithByteParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamShortParameterized(Function<GenericRefedInterface<Short>, String> handler) {
    handler.apply(methodWithShortParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamIntegerParameterized(Function<GenericRefedInterface<Integer>, String> handler) {
    handler.apply(methodWithIntegerParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamLongParameterized(Function<GenericRefedInterface<Long>, String> handler) {
    handler.apply(methodWithLongParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamFloatParameterized(Function<GenericRefedInterface<Float>, String> handler) {
    handler.apply(methodWithFloatParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamDoubleParameterized(Function<GenericRefedInterface<Double>, String> handler) {
    handler.apply(methodWithDoubleParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamBooleanParameterized(Function<GenericRefedInterface<Boolean>, String> handler) {
    handler.apply(methodWithBooleanParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamCharacterParameterized(Function<GenericRefedInterface<Character>, String> handler) {
    handler.apply(methodWithCharacterParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamStringParameterized(Function<GenericRefedInterface<String>, String> handler) {
    handler.apply(methodWithStringParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamJsonObjectParameterized(Function<GenericRefedInterface<JsonObject>, String> handler) {
    handler.apply(methodWithJsonObjectParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamJsonArrayParameterized(Function<GenericRefedInterface<JsonArray>, String> handler) {
    handler.apply(methodWithJsonArrayParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamDataObjectParameterized(Function<GenericRefedInterface<TestDataObject>, String> handler) {
    handler.apply(methodWithDataObjectParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamEnumParameterized(Function<GenericRefedInterface<TestEnum>, String> handler) {
    handler.apply(methodWithEnumParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamGenEnumParameterized(Function<GenericRefedInterface<TestGenEnum>, String> handler) {
    handler.apply(methodWithGenEnumParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamUserTypeParameterized(Function<GenericRefedInterface<RefedInterface1>, String> handler) {
    handler.apply(methodWithUserTypeParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamListOfObjectsParameterized(Function<GenericRefedInterface<List<Object>>, String> handler) {
    handler.apply(methodWithListOfObjectsParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamSetOfObjectsParameterized(Function<GenericRefedInterface<Set<Object>>, String> handler) {
    handler.apply(methodWithSetOfObjectsParameterizedReturn());
  }

  @Override
  public void methodWithFunctionParamMapOfObjectsParameterized(Function<GenericRefedInterface<Map<String, Object>>, String> handler) {
    handler.apply(methodWithMapOfObjectsParameterizedReturn());
  }

  @Override
  public void methodWithSupplierParamByteParameterized(Supplier<GenericRefedInterface<Byte>> handler) {
  }

  @Override
  public void methodWithSupplierParamShortParameterized(Supplier<GenericRefedInterface<Short>> handler) {
  }

  @Override
  public void methodWithSupplierParamIntegerParameterized(Supplier<GenericRefedInterface<Integer>> handler) {
  }

  @Override
  public void methodWithSupplierParamLongParameterized(Supplier<GenericRefedInterface<Long>> handler) {
  }

  @Override
  public void methodWithSupplierParamFloatParameterized(Supplier<GenericRefedInterface<Float>> handler) {
  }

  @Override
  public void methodWithSupplierParamDoubleParameterized(Supplier<GenericRefedInterface<Double>> handler) {
  }

  @Override
  public void methodWithSupplierParamBooleanParameterized(Supplier<GenericRefedInterface<Boolean>> handler) {
  }

  @Override
  public void methodWithSupplierParamCharacterParameterized(Supplier<GenericRefedInterface<Character>> handler) {
  }

  @Override
  public void methodWithSupplierParamStringParameterized(Supplier<GenericRefedInterface<String>> handler) {
  }

  @Override
  public void methodWithSupplierParamJsonObjectParameterized(Supplier<GenericRefedInterface<JsonObject>> handler) {
  }

  @Override
  public void methodWithSupplierParamJsonArrayParameterized(Supplier<GenericRefedInterface<JsonArray>> handler) {
  }

  @Override
  public void methodWithSupplierParamDataObjectParameterized(Supplier<GenericRefedInterface<TestDataObject>> handler) {
  }

  @Override
  public void methodWithSupplierParamEnumParameterized(Supplier<GenericRefedInterface<TestEnum>> handler) {
  }

  @Override
  public void methodWithSupplierParamGenEnumParameterized(Supplier<GenericRefedInterface<TestGenEnum>> handler) {
  }

  @Override
  public void methodWithSupplierParamUserTypeParameterized(Supplier<GenericRefedInterface<RefedInterface1>> handler) {
  }

  @Override
  public void methodWithSupplierParamListOfObjectsParameterized(Supplier<GenericRefedInterface<List<Object>>> handler) {
  }

  @Override
  public void methodWithSupplierParamSetOfObjectsParameterized(Supplier<GenericRefedInterface<Set<Object>>> handler) {
  }

  @Override
  public void methodWithSupplierParamMapOfObjectsParameterized(Supplier<GenericRefedInterface<Map<String, Object>>> handler) {
  }

  @Override
  public <U> GenericRefedInterface<U> methodWithClassTypeParameterizedReturn(Class<U> type) {
    if (type == Byte.class) {
      return (GenericRefedInterface<U>) methodWithByteParameterizedReturn();
    }
    if (type == Short.class) {
      return (GenericRefedInterface<U>) methodWithShortParameterizedReturn();
    }
    if (type == Integer.class) {
      return (GenericRefedInterface<U>) methodWithIntegerParameterizedReturn();
    }
    if (type == Long.class) {
      return (GenericRefedInterface<U>) methodWithLongParameterizedReturn();
    }
    if (type == Float.class) {
      return (GenericRefedInterface<U>) methodWithFloatParameterizedReturn();
    }
    if (type == Double.class) {
      return (GenericRefedInterface<U>) methodWithDoubleParameterizedReturn();
    }
    if (type == Boolean.class) {
      return (GenericRefedInterface<U>) methodWithBooleanParameterizedReturn();
    }
    if (type == Character.class) {
      return (GenericRefedInterface<U>) methodWithCharacterParameterizedReturn();
    }
    if (type == String.class) {
      return (GenericRefedInterface<U>) methodWithStringParameterizedReturn();
    }
    if (type == JsonObject.class) {
      return (GenericRefedInterface<U>) methodWithJsonObjectParameterizedReturn();
    }
    if (type == JsonArray.class) {
      return (GenericRefedInterface<U>) methodWithJsonArrayParameterizedReturn();
    }
    if (type == TestDataObject.class) {
      return (GenericRefedInterface<U>) methodWithDataObjectParameterizedReturn();
    }
    if (type == TestEnum.class) {
      return (GenericRefedInterface<U>) methodWithEnumParameterizedReturn();
    }
    if (type == TestGenEnum.class) {
      return (GenericRefedInterface<U>) methodWithGenEnumParameterizedReturn();
    }
    if (type == RefedInterface1.class) {
      return (GenericRefedInterface<U>) methodWithUserTypeParameterizedReturn();
    }
    throw new AssertionError("Unexpected type " + type);
  }

  @Override
  public <U> U methodWithClassTypeReturn(Class<U> type) {
    return methodWithClassTypeParameterizedReturn(type).getValue();
  }

  @Override
  public <U> void methodWithClassTypeParam(Class<U> type, U u) {
    GenericRefedInterface<U> gen = methodWithClassTypeParameterizedReturn(type);
    if (!u.equals(gen.getValue())) {
      throw new AssertionError("Unexpected value " + u + "/" + u.getClass() + " != " + gen.getValue() + "/" + gen.getValue().getClass());
    }
  }

  @Override
  public <U> void methodWithClassTypeHandler(Class<U> type, Handler<U> f) {
    f.handle(methodWithClassTypeReturn(type));
  }

  @Override
  public <U> Future<U> methodWithClassTypeHandlerAsyncResult(Class<U> type) {
    return Future.succeededFuture(methodWithClassTypeReturn(type));
  }

  @Override
  public <U> void methodWithClassTypeFunctionParam(Class<U> type, Function<U, String> f) {
    f.apply(methodWithClassTypeReturn(type));
  }

  @Override
  public <U> void methodWithClassTypeFunctionReturn(Class<U> type, Function<String, U> f) {
    methodWithClassTypeParam(type, f.apply("whatever"));
  }

  @Override
  public <U> void methodWithClassTypeSupplierReturn(Class<U> type, Supplier<U> f) {
    methodWithClassTypeParam(type, f.get());
  }

  @Override
  public <U> void methodWithHandlerClassTypeParameterized(Class<U> type, Handler<GenericRefedInterface<U>> handler) {
    handler.handle(methodWithClassTypeParameterizedReturn(type));
  }

  @Override
  public <U> Future<GenericRefedInterface<U>> methodWithHandlerAsyncResultClassTypeParameterized(Class<U> type) {
    return Future.succeededFuture(methodWithClassTypeParameterizedReturn(type));
  }

  @Override
  public <U> void methodWithFunctionParamClassTypeParameterized(Class<U> type, Function<GenericRefedInterface<U>, String> function) {
    function.apply(methodWithClassTypeParameterizedReturn(type));
  }

  @Override
  public <U> void methodWithSupplierParamClassTypeParameterized(Class<U> type, Supplier<GenericRefedInterface<U>> handler) {
  }

  private <U> GenericRefedInterface<U> methodWithClassTypeParameterizedReturn(U val) {
    GenericRefedInterfaceImpl<U> obj = new GenericRefedInterfaceImpl<>();
    obj.setValue(val);
    return obj;
  }

  @Override
  public InterfaceWithApiArg interfaceWithApiArg(RefedInterface1 value) {
    return new InterfaceWithApiArg() {
      private RefedInterface1 val = value;
      @Override
      public void meth() {
      }
      @Override
      public GenericRefedInterface<RefedInterface1> setValue(RefedInterface1 value) {
        val = value;
        return this;
      }
      @Override
      public RefedInterface1 getValue() {
        return val;
      }
    };
  }

  @Override
  public InterfaceWithStringArg interfaceWithStringArg(String value) {
    return new InterfaceWithStringArg() {
      private String val = value;
      @Override
      public void meth() {
      }
      @Override
      public GenericRefedInterface<String> setValue(String value) {
        val = value;
        return this;
      }
      @Override
      public String getValue() {
        return val;
      }
    };
  }

  @Override
  public <T, U> InterfaceWithVariableArg<T, U> interfaceWithVariableArg(T value1, Class<U> type, U value2) {
    return new InterfaceWithVariableArg<T, U>() {
      private T val1 = value1;
      private U val2 = value2;
      @Override
      public void setOtherValue(T value) {
        val1 = value;
      }
      @Override
      public T getOtherValue() {
        return val1;
      }
      @Override
      public GenericRefedInterface<U> setValue(U value) {
        val2 = value;
        return this;
      }
      @Override
      public U getValue() {
        return val2;
      }
    };
  }

  @Override
  public GenericNullableRefedInterface<RefedInterface1> methodWithGenericNullableApiReturn(boolean notNull) {
    return new GenericNullableRefedInterface<RefedInterface1>() {
      @Override
      public RefedInterface1 getValue() {
        return notNull ? new RefedInterface1Impl().setString("the_string_value") : null;
      }
    };
  }

  @Override
  public void methodWithHandlerGenericNullableApi(boolean notNull, Handler<GenericNullableRefedInterface<RefedInterface1>> handler) {
    handler.handle(methodWithGenericNullableApiReturn(notNull));
  }

  @Override
  public Future<GenericNullableRefedInterface<RefedInterface1>> methodWithHandlerAsyncResultGenericNullableApi(boolean notNull) {
    return Future.succeededFuture(methodWithGenericNullableApiReturn(notNull));
  }

  @Override
  public <T> GenericRefedInterface<T> methodWithParamInferedReturn(GenericRefedInterface<T> param) {
    return param;
  }

  @Override
  public <T> void methodWithHandlerParamInfered(GenericRefedInterface<T> param, Handler<GenericRefedInterface<T>> handler) {
    handler.handle(param);
  }

  @Override
  public <T> Future<GenericRefedInterface<T>> methodWithHandlerAsyncResultParamInfered(GenericRefedInterface<T> param) {
    return Future.succeededFuture(param);
  }
}
