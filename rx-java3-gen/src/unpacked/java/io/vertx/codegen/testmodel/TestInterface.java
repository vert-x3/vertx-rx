package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

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

  Handler<String> methodWithHandlerStringReturn(String expected);
  <T> Handler<T> methodWithHandlerGenericReturn(Handler<T> handler);
  Handler<RefedInterface1> methodWithHandlerVertxGenReturn(String expected);

  Future<Byte> methodWithHandlerAsyncResultByte(boolean sendFailure);
  Future<Short> methodWithHandlerAsyncResultShort(boolean sendFailure);
  Future<Integer> methodWithHandlerAsyncResultInteger(boolean sendFailure);
  Future<Long> methodWithHandlerAsyncResultLong(boolean sendFailure);
  Future<Float> methodWithHandlerAsyncResultFloat(boolean sendFailure);
  Future<Double> methodWithHandlerAsyncResultDouble(boolean sendFailure);
  Future<Boolean> methodWithHandlerAsyncResultBoolean(boolean sendFailure);
  Future<Character> methodWithHandlerAsyncResultCharacter(boolean sendFailure);
  Future<String> methodWithHandlerAsyncResultString(boolean sendFailure);
  Future<TestDataObject> methodWithHandlerAsyncResultDataObject(boolean sendFailure);
  Future<TestStringDataObject> methodWithHandlerAsyncResultStringDataObject(boolean sendFailure);

  void methodWithUserTypes(RefedInterface1 refed);

  String methodWithOverloadedUserTypes(RefedInterface1 refed);

  String methodWithOverloadedUserTypes(RefedInterface2 refed);

  void methodWithObjectParam(String str, Object obj);

  void methodWithDataObjectParam(TestDataObject dataObject);

  void methodWithStringDataObjectParam(TestStringDataObject dataObject);

  void methodWithHandlerUserTypes(Handler<RefedInterface1> handler);

  Future<RefedInterface1> methodWithHandlerAsyncResultUserTypes();

  void methodWithConcreteHandlerUserTypeSubtype(ConcreteHandlerUserType handler);

  void methodWithAbstractHandlerUserTypeSubtype(AbstractHandlerUserType handler);

  void methodWithConcreteHandlerUserTypeSubtypeExtension(ConcreteHandlerUserTypeExtension handler);

  void methodWithHandlerVoid(Handler<Void> handler);

  Future<Void> methodWithHandlerAsyncResultVoid(boolean sendFailure);

  void methodWithHandlerThrowable(Handler<Throwable> handler);

  void methodWithHandlerDataObject(Handler<TestDataObject> handler);

  void methodWithHandlerStringDataObject(Handler<TestStringDataObject> handler);

  <U> void methodWithHandlerGenericUserType(U value, Handler<GenericRefedInterface<U>> handler);

  <U> Future<GenericRefedInterface<U>> methodWithHandlerAsyncResultGenericUserType(U value);

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

  RefedInterface1 methodWithVertxGenNullReturn();

  RefedInterface2 methodWithAbstractVertxGenReturn();

  TestDataObject methodWithDataObjectReturn();

  TestStringDataObject methodWithStringDataObjectReturn();

  TestDataObject methodWithDataObjectNullReturn();

  <U> GenericRefedInterface<U> methodWithGenericUserTypeReturn(U value);

  String overloadedMethod(String str, Handler<String> handler);

  String overloadedMethod(String str, RefedInterface1 refed);

  String overloadedMethod(String str, RefedInterface1 refed, Handler<String> handler);

  String overloadedMethod(String str, RefedInterface1 refed, long period, Handler<String> handler);

  <U> U methodWithGenericReturn(String type);

  <U> void methodWithGenericParam(String type, U u);

  <U> void methodWithGenericHandler(String type, Handler<U> handler);

  <U> Future<U> methodWithGenericHandlerAsyncResult(String type);

  @Fluent
  TestInterface fluentMethod(String str);

  static RefedInterface1 staticFactoryMethod(String foo) {
    RefedInterface1 refed = new RefedInterface1Impl();
    refed.setString(foo);
    return refed;
  }

  @CacheReturn
  RefedInterface1 methodWithCachedReturn(String foo);

  @CacheReturn
  int methodWithCachedReturnPrimitive(int arg);

  @CacheReturn
  List<RefedInterface1> methodWithCachedListReturn();

  JsonObject methodWithJsonObjectReturn();

  JsonObject methodWithNullJsonObjectReturn();

  JsonObject methodWithComplexJsonObjectReturn();

  JsonArray methodWithJsonArrayReturn();

  JsonArray methodWithNullJsonArrayReturn();

  JsonArray methodWithComplexJsonArrayReturn();

  void methodWithJsonParams(JsonObject jsonObject, JsonArray jsonArray);

  void methodWithNullJsonParams(@Nullable JsonObject jsonObject, @Nullable JsonArray jsonArray);

  void methodWithHandlerJson(Handler<JsonObject> jsonObjectHandler, Handler<JsonArray> jsonArrayHandler);

  void methodWithHandlerComplexJson(Handler<JsonObject> jsonObjectHandler, Handler<JsonArray> jsonArrayHandler);

  Future<JsonObject> methodWithHandlerAsyncResultJsonObject();

  Future<@Nullable JsonObject> methodWithHandlerAsyncResultNullJsonObject();

  Future<JsonObject> methodWithHandlerAsyncResultComplexJsonObject();

  Future<JsonArray> methodWithHandlerAsyncResultJsonArray();

  Future<@Nullable JsonArray> methodWithHandlerAsyncResultNullJsonArray();

  Future<JsonArray> methodWithHandlerAsyncResultComplexJsonArray();

  String methodWithEnumParam(String strVal, TestEnum weirdo);

  TestEnum methodWithEnumReturn(String strVal);

  String methodWithGenEnumParam(String strVal, TestGenEnum weirdo);

  TestGenEnum methodWithGenEnumReturn(String strVal);

  Throwable methodWithThrowableReturn(String strVal);

  String methodWithThrowableParam(Throwable t);

  int superMethodOverloadedBySubclass(String s);

}
