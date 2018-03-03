package io.vertx.rx.java.test.gen;

import io.vertx.codegen.testmodel.NullableTCKImpl;
import io.vertx.codegen.testmodel.TestDataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.codegen.testmodel.NullableTCK;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static io.vertx.rx.java.test.gen.ApiTCKTest.get;
import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class NullableTCKTest {

  private final List<TestDataObject> TEST_DATA_OBJECTS = Arrays.asList(new TestDataObject().setFoo("first").setBar(1).setWibble(1.1), null, new TestDataObject().setFoo("third").setBar(3).setWibble(3.3));
  private final List<JsonArray> EXPECTED1BILT = Arrays.asList(new JsonArray().add("foo").add("bar"), null, new JsonArray().add("juu"));
  private final List<JsonObject> EXPECTED = Arrays.asList(new JsonObject().put("foo", "bar"), null, new JsonObject().put("juu", 3));
  private final NullableTCK obj = new NullableTCK(new NullableTCKImpl());

  @Test
  public void testNullDataObjectParam() {
    obj.methodWithNullableDataObjectParam(true, null);
  }

  @Test
  public void testNullJsonHandlerParams() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithNullableJsonObjectHandler(true, checker.resultHandler(it -> assertEquals(new JsonObject().put("foo", "wibble").put("bar", 3), it)));
    obj.methodWithNullableJsonObjectHandler(false, checker.resultHandler(it -> assertEquals(null, it)));
    obj.methodWithNullableJsonArrayHandler(true, checker.resultHandler(it -> assertEquals(new JsonArray().add("one").add("two").add("three"), it)));
    obj.methodWithNullableJsonArrayHandler(false, checker.resultHandler(it -> assertEquals(null, it)));
    assertEquals(4, checker.count);
  }

  @Test
  public void testMethodWithHandlerListNullJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithListNullableJsonObjectHandler(checker.expectedResult(EXPECTED));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultListNullJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithListNullableJsonObjectHandlerAsyncResult(checker.asyncExpectedResult(EXPECTED));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureListNullJsonObject() throws Exception {
    List<JsonObject> result = get(obj.methodWithListNullableJsonObjectHandlerAsyncResultObservable());
    assertEquals(EXPECTED, result);
  }

  @Test
  public void testMethodWithHandlerSetNullJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithSetNullableJsonObjectHandler(checker.resultHandler(r -> assertEquals(EXPECTED, new ArrayList<>(r))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultSetNullJsonObject() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithSetNullableJsonObjectHandlerAsyncResult(checker.asyncResultHandler(it -> assertEquals(EXPECTED, new ArrayList<>(it))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureSetNullJsonObject() throws Exception {
    Set<JsonObject> result = get(obj.methodWithSetNullableJsonObjectHandlerAsyncResultObservable());
    assertEquals(EXPECTED, new ArrayList<>(result));
  }

  @Test
  public void testMethodWithHandlerListNullJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithListNullableJsonArrayHandler(checker.expectedResult(EXPECTED1BILT));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultListNullJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithListNullableJsonArrayHandlerAsyncResult(checker.asyncExpectedResult(EXPECTED1BILT));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureListNullJsonArray() throws Exception {
    List<JsonArray> result = get(obj.methodWithListNullableJsonArrayHandlerAsyncResultObservable());
    assertEquals(result, EXPECTED1BILT);
  }

  @Test
  public void testMethodWithHandlerSetNullJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithSetNullableJsonArrayHandler(checker.resultHandler(it -> assertEquals(EXPECTED1BILT, new ArrayList<>(it))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultSetNullJsonArray() {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithSetNullableJsonArrayHandlerAsyncResult(checker.asyncResultHandler(it -> assertEquals(EXPECTED1BILT, new ArrayList<>(it))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithFutureSetNullJsonArray() throws Exception {
    Set<JsonArray> result = get(obj.methodWithSetNullableJsonArrayHandlerAsyncResultObservable());
    assertEquals(EXPECTED1BILT, new ArrayList<>(result));
  }

  @Test
  public void testMethodWithHandlerNullListDataObject() throws Exception {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithListNullableDataObjectHandler(checker.resultHandler(list -> assertEquals(TEST_DATA_OBJECTS, list)));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultNullListDataObject() throws Exception {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithListNullableDataObjectHandlerAsyncResult(checker.asyncResultHandler(list -> assertEquals(TEST_DATA_OBJECTS, list)));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerNullSetDataObject() throws Exception {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithSetNullableDataObjectHandler(checker.resultHandler(set -> assertEquals(TEST_DATA_OBJECTS, new ArrayList<>(set))));
    assertEquals(1, checker.count);
  }

  @Test
  public void testMethodWithHandlerAsyncResultNullSetDataObject() throws Exception {
    AsyncResultChecker checker = new AsyncResultChecker();
    obj.methodWithSetNullableDataObjectHandlerAsyncResult(checker.asyncResultHandler(set -> assertEquals(TEST_DATA_OBJECTS, new ArrayList<>(set))));
    assertEquals(1, checker.count);
  }
}
