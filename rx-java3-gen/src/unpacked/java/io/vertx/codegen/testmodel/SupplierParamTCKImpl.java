package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SupplierParamTCKImpl implements SupplierParamTCK {

  @Override
  public String methodWithBasicReturn(Supplier<Byte> byteFunc, Supplier<Short> shortFunc, Supplier<Integer> integerFunc, Supplier<Long> longFunc, Supplier<Float> floatFunc, Supplier<Double> doubleFunc, Supplier<Boolean> booleanFunc, Supplier<Character> charFunc, Supplier<String> stringFunc) {
    assertEquals(10, (int)(byte)byteFunc.get());
    assertEquals(1000, (int)(short)shortFunc.get());
    assertEquals(100000, (int)integerFunc.get());
    assertEquals(10000000000L, (long)longFunc.get());
    assertEquals(0.01f, floatFunc.get(), 0.001);
    assertEquals(0.00001D, doubleFunc.get(), 0.000001);
    assertEquals(true, booleanFunc.get());
    assertEquals('C', (char)charFunc.get());
    assertEquals("the-return", stringFunc.get());
    return "ok";
  }

  @Override
  public String methodWithJsonReturn(Supplier<JsonObject> objectFunc, Supplier<JsonArray> arrayFunc) {
    assertEquals(new JsonObject().put("foo", "foo_value").put("bar", 10).put("wibble", 0.1), objectFunc.get());
    assertEquals(new JsonArray().add("one").add("two").add("three"), arrayFunc.get());
    return "ok";
  }

  @Override
  public String methodWithObjectReturn(Supplier<Object> func) {
    assertEquals("the-string", func.get());
    assertEquals(123, ((Number)func.get()).intValue());
    assertEquals(true, func.get());
    assertEquals(new JsonObject().put("foo", "foo_value"), func.get());
    assertEquals(new JsonArray().add("foo").add("bar"), func.get());
    return "ok";
  }

  @Override
  public String methodWithDataObjectReturn(Supplier<TestDataObject> func) {
    TestDataObject val = func.get();
    assertEquals("wasabi", val.getFoo());
    assertEquals(6, val.getBar());
    assertEquals(0.01D, val.getWibble(), 0.001D);
    return "ok";
  }

  @Override
  public String methodWithEnumReturn(Supplier<TestEnum> func) {
    assertEquals(TestEnum.NICK,func.get());
    return "ok";
  }

  @Override
  public String methodWithListReturn(Supplier<List<String>> func) {
    assertEquals(Arrays.asList("one", "two", "three"),func.get());
    return "ok";
  }

  @Override
  public String methodWithSetReturn(Supplier<Set<String>> func) {
    assertEquals(new HashSet<>(Arrays.asList("one", "two", "three")),func.get());
    return "ok";
  }

  @Override
  public String methodWithMapReturn(Supplier<Map<String, String>> func) {
    Map<String, String> expected = new HashMap<>();
    expected.put("one", "one");
    expected.put("two", "two");
    expected.put("three", "three");
    assertEquals(expected,func.get());
    return "ok";
  }

  @Override
  public <T> String methodWithGenericReturn(Supplier<T> func) {
    return methodWithObjectReturn(func::get);
  }

  @Override
  public <T> String methodWithGenericUserTypeReturn(Supplier<GenericRefedInterface<T>> func) {
    GenericRefedInterfaceImpl<T> impl = new GenericRefedInterfaceImpl<>();
    assertEquals(impl , func.get());
    return "ok";
  }

  @Override
  public String methodWithNullableListReturn(Supplier<@Nullable List<String>> func) {
    assertEquals(null, func.get());
    return "ok";
  }
}
