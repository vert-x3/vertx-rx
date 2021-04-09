package io.vertx.rxjava3.test;

import io.vertx.codegen.testmodel.JsonMapperTCKImpl;
import io.vertx.codegen.testmodel.MyPojoToInteger;
import io.vertx.rxjava3.codegen.testmodel.JsonMapperTCK;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonMapperTCKTest {

  @Test
  public void testInteger(){
    JsonMapperTCKImpl impl = new JsonMapperTCKImpl(); // Impl has asserts! So i reuse the same
    JsonMapperTCK test = JsonMapperTCK.newInstance(impl);
    impl.methodWithTypeToIntegerParam(
      test.methodWithHandlerAsyncResultTypeToIntegerParam().blockingGet()
    );

    impl.methodWithListOfTypeToIntegerParam(
      test.methodWithHandlerAsyncResultListOfTypeToIntegerParam().blockingGet()
    );

    impl.methodWithSetOfTypeToIntegerParam(
      test.methodWithHandlerAsyncResultSetOfTypeToIntegerParam().blockingGet()
    );

    impl.methodWithMapOfTypeToIntegerParam(
      test.methodWithHandlerAsyncResultMapOfTypeToIntegerParam().blockingGet()
    );
  }

  @Test
  public void testString(){
    JsonMapperTCKImpl impl = new JsonMapperTCKImpl(); // Impl has asserts! So i reuse the same
    JsonMapperTCK test = JsonMapperTCK.newInstance(impl);
    impl.methodWithTypeToStringParam(
      test.methodWithHandlerAsyncResultTypeToStringParam().blockingGet()
    );

    impl.methodWithListOfTypeToStringParam(
      test.methodWithHandlerAsyncResultListOfTypeToStringParam().blockingGet()
    );

    impl.methodWithSetOfTypeToStringParam(
      test.methodWithHandlerAsyncResultSetOfTypeToStringParam().blockingGet()
    );

    impl.methodWithMapOfTypeToStringParam(
      test.methodWithHandlerAsyncResultMapOfTypeToStringParam().blockingGet()
    );
  }

  @Test
  public void testJsonArray(){
    JsonMapperTCKImpl impl = new JsonMapperTCKImpl(); // Impl has asserts! So i reuse the same
    JsonMapperTCK test = JsonMapperTCK.newInstance(impl);
    impl.methodWithTypeToJsonArrayParam(
      test.methodWithHandlerAsyncResultTypeToJsonArrayParam().blockingGet()
    );

    impl.methodWithListOfTypeToJsonArrayParam(
      test.methodWithHandlerAsyncResultListOfTypeToJsonArrayParam().blockingGet()
    );

    impl.methodWithSetOfTypeToJsonArrayParam(
      test.methodWithHandlerAsyncResultSetOfTypeToJsonArrayParam().blockingGet()
    );

    impl.methodWithMapOfTypeToJsonArrayParam(
      test.methodWithHandlerAsyncResultMapOfTypeToJsonArrayParam().blockingGet()
    );
  }

  @Test
  public void testJsonObject(){
    JsonMapperTCKImpl impl = new JsonMapperTCKImpl(); // Impl has asserts! So i reuse the same
    JsonMapperTCK test = JsonMapperTCK.newInstance(impl);
    impl.methodWithTypeToJsonObjectParam(
      test.methodWithHandlerAsyncResultTypeToJsonObjectParam().blockingGet()
    );

    impl.methodWithListOfTypeToJsonObjectParam(
      test.methodWithHandlerAsyncResultListOfTypeToJsonObjectParam().blockingGet()
    );

    impl.methodWithSetOfTypeToJsonObjectParam(
      test.methodWithHandlerAsyncResultSetOfTypeToJsonObjectParam().blockingGet()
    );

    impl.methodWithMapOfTypeToJsonObjectParam(
      test.methodWithHandlerAsyncResultMapOfTypeToJsonObjectParam().blockingGet()
    );
  }

}
