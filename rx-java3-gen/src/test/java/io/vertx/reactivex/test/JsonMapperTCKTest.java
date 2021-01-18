package io.vertx.reactivex.test;

import io.vertx.codegen.testmodel.JsonMapperTCKImpl;
import io.vertx.codegen.testmodel.MyPojoToInteger;
import io.vertx.reactivex.codegen.testmodel.JsonMapperTCK;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonMapperTCKTest {

  @Test
  public void testInteger(){
    JsonMapperTCKImpl impl = new JsonMapperTCKImpl(); // Impl has asserts! So i reuse the same
    JsonMapperTCK test = JsonMapperTCK.newInstance(impl);
    impl.methodWithTypeToIntegerParam(
      test.rxMethodWithHandlerAsyncResultTypeToIntegerParam().blockingGet()
    );

    impl.methodWithListOfTypeToIntegerParam(
      test.rxMethodWithHandlerAsyncResultListOfTypeToIntegerParam().blockingGet()
    );

    impl.methodWithSetOfTypeToIntegerParam(
      test.rxMethodWithHandlerAsyncResultSetOfTypeToIntegerParam().blockingGet()
    );

    impl.methodWithMapOfTypeToIntegerParam(
      test.rxMethodWithHandlerAsyncResultMapOfTypeToIntegerParam().blockingGet()
    );
  }

  @Test
  public void testString(){
    JsonMapperTCKImpl impl = new JsonMapperTCKImpl(); // Impl has asserts! So i reuse the same
    JsonMapperTCK test = JsonMapperTCK.newInstance(impl);
    impl.methodWithTypeToStringParam(
      test.rxMethodWithHandlerAsyncResultTypeToStringParam().blockingGet()
    );

    impl.methodWithListOfTypeToStringParam(
      test.rxMethodWithHandlerAsyncResultListOfTypeToStringParam().blockingGet()
    );

    impl.methodWithSetOfTypeToStringParam(
      test.rxMethodWithHandlerAsyncResultSetOfTypeToStringParam().blockingGet()
    );

    impl.methodWithMapOfTypeToStringParam(
      test.rxMethodWithHandlerAsyncResultMapOfTypeToStringParam().blockingGet()
    );
  }

  @Test
  public void testJsonArray(){
    JsonMapperTCKImpl impl = new JsonMapperTCKImpl(); // Impl has asserts! So i reuse the same
    JsonMapperTCK test = JsonMapperTCK.newInstance(impl);
    impl.methodWithTypeToJsonArrayParam(
      test.rxMethodWithHandlerAsyncResultTypeToJsonArrayParam().blockingGet()
    );

    impl.methodWithListOfTypeToJsonArrayParam(
      test.rxMethodWithHandlerAsyncResultListOfTypeToJsonArrayParam().blockingGet()
    );

    impl.methodWithSetOfTypeToJsonArrayParam(
      test.rxMethodWithHandlerAsyncResultSetOfTypeToJsonArrayParam().blockingGet()
    );

    impl.methodWithMapOfTypeToJsonArrayParam(
      test.rxMethodWithHandlerAsyncResultMapOfTypeToJsonArrayParam().blockingGet()
    );
  }

  @Test
  public void testJsonObject(){
    JsonMapperTCKImpl impl = new JsonMapperTCKImpl(); // Impl has asserts! So i reuse the same
    JsonMapperTCK test = JsonMapperTCK.newInstance(impl);
    impl.methodWithTypeToJsonObjectParam(
      test.rxMethodWithHandlerAsyncResultTypeToJsonObjectParam().blockingGet()
    );

    impl.methodWithListOfTypeToJsonObjectParam(
      test.rxMethodWithHandlerAsyncResultListOfTypeToJsonObjectParam().blockingGet()
    );

    impl.methodWithSetOfTypeToJsonObjectParam(
      test.rxMethodWithHandlerAsyncResultSetOfTypeToJsonObjectParam().blockingGet()
    );

    impl.methodWithMapOfTypeToJsonObjectParam(
      test.rxMethodWithHandlerAsyncResultMapOfTypeToJsonObjectParam().blockingGet()
    );
  }

}
