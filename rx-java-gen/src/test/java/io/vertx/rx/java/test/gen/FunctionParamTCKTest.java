package io.vertx.rx.java.test.gen;

import io.vertx.codegen.testmodel.FunctionParamTCKImpl;
import io.vertx.codegen.testmodel.RefedInterface1Impl;
import io.vertx.codegen.testmodel.TestDataObject;
import io.vertx.codegen.testmodel.TestEnum;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.codegen.testmodel.FunctionParamTCK;
import io.vertx.rxjava.codegen.testmodel.RefedInterface1;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FunctionParamTCKTest {

  final FunctionParamTCK obj = new FunctionParamTCK(new FunctionParamTCKImpl());

  @Test
  public void testBasicParam() {
    List<String> ret = obj.methodWithBasicParam(
        arg -> { assertEquals((Byte)(byte)100, arg); return "ok0"; },
        arg -> { assertEquals((Short)(short)1000, arg); return "ok1"; },
        arg -> { assertEquals((Integer)(int)100000, arg); return "ok2"; },
        arg -> { assertEquals((Long)10000000000L, arg); return "ok3"; },
        arg -> { assertEquals((Float)(float)3.5f, arg); return "ok4"; },
        arg -> { assertEquals((Double)(double)0.01, arg); return "ok5"; },
        arg -> { assertEquals(true, arg); return "ok6"; },
        arg -> { assertEquals((Character)(char)'F', arg); return "ok7"; },
        arg -> { assertEquals("wibble", arg); return "ok8"; }
    );
    assertEquals(Arrays.asList("ok0","ok1","ok2","ok3","ok4","ok5","ok6","ok7","ok8"), ret);
  }

  @Test
  public void testJsonParam() {
    List<String> ret = obj.methodWithJsonParam(
        it -> { assertEquals(new JsonObject().put("one", 1).put("two", 2).put("three", 3), it); return "ok0"; },
        it -> { assertEquals(new JsonArray().add("one").add("two").add("three"), it); return "ok1"; });
    assertEquals(Arrays.asList("ok0","ok1"), ret);
  }

  @Test
  public void testVoidParam() {
    assertEquals("ok", obj.methodWithVoidParam(it -> { assertEquals(null, it); return "ok"; }));
  }

  @Test
  public void testUserTypeParam() {
    RefedInterface1 refed = new RefedInterface1(new RefedInterface1Impl());
    assertEquals("ok", obj.methodWithUserTypeParam(refed, it -> {
      it.setString("foobarjuu");
      assertEquals("foobarjuu", it.getString());
      return "ok";
    }));
  }

  @Test
  public void testObjectParam() {
    assertEquals("ok", obj.methodWithObjectParam(123, it -> {
      assertEquals(123, it);
      return "ok";
    }));
    assertEquals("ok", obj.methodWithObjectParam("the-string-arg", it -> {
      assertEquals("the-string-arg", it);
      return "ok";
    }));
  }

  @Test
  public void testDataObjectParam() {
    assertEquals("ok", obj.methodWithDataObjectParam(it -> {
      assertEquals("foo_value", it.getFoo());
      assertEquals(3, it.getBar());
      assertEquals(0.01, it.getWibble(), 0);
      return "ok";
    }));
  }

  @Test
  public void testEnumParam() {
    assertEquals("ok", obj.methodWithEnumParam(it -> {
      assertEquals(TestEnum.TIM, it);
      return "ok";
    }));
  }

  @Test
  public void testListParam() {
    assertEquals("ok", obj.methodWithListParam(it -> {
      assertEquals(Arrays.asList("one", "two", "three"), it);
      return "ok";
    }));
  }

  @Test
  public void testSetParam() {
    assertEquals("ok", obj.methodWithSetParam(it -> {
      assertEquals(new HashSet<>(Arrays.asList("one", "two", "three")), it);
      return "ok";
    }));
  }

  @Test
  public void testMapParam() {
    assertEquals("ok", obj.methodWithMapParam( it -> {
      Map<String, String> expected = new HashMap<>();
      expected.put("one", "one");
      expected.put("two", "two");
      expected.put("three", "three");
      assertEquals(expected, it);
      return "ok";
    }));
  }

  @Test
  public void testGenericParam() {
    assertEquals("ok", obj.methodWithGenericParam(123, it -> {
      assertEquals((Integer)123, it);
      return "ok";
    }));
    assertEquals("ok", obj.methodWithGenericParam("the-string-arg", it -> {
      assertEquals("the-string-arg", it);
      return "ok";
    }));
  }
  @Test
  public void testGenericUserTypeParam() {
    assertEquals("ok", obj.methodWithGenericUserTypeParam(123, it -> {
      assertEquals((Integer)123, it.getValue());
      return "ok";
    }));
    assertEquals("ok", obj.methodWithGenericUserTypeParam("the-string-arg", it -> {
      assertEquals("the-string-arg", it.getValue());
      return "ok";
    }));
  }

  @Test
  public void testNullableListParam() {
    assertEquals("ok", obj.methodWithNullableListParam(it -> {
      assertEquals(null, it);
      return "ok";
    }));
  }

  @Test
  public void testBasicReturn() {
    assertEquals("ok", obj.methodWithBasicReturn(
        it -> (byte)10,
        it -> (short)1000,
        it -> 100000,
        it -> 10000000000L,
        it -> (float)0.01,
        it -> 0.00001,
        it -> true,
        it -> 'C',
        it -> "the-return"
    ));
  }

  @Test
  public void testJsonReturn() {
    assertEquals("ok", obj.methodWithJsonReturn(
        it -> new JsonObject().put("foo", "foo_value").put("bar", 10).put("wibble", 0.1),
        it -> new JsonArray(Arrays.asList("one", "two", "three"))
    ));
  }

  @Test
  public void testObjectReturn() {
    assertEquals("ok", obj.methodWithObjectReturn(it -> {
          switch (it) {
            case 0:
              return "the-string";
            case 1:
              return 123;
            case 2:
              return true;
            case 3:
              return new JsonObject().put("foo", "foo_value");
            case 4:
              return new JsonArray(Arrays.asList("foo", "bar"));
            default:
              throw new RuntimeException();
          }
        }
    ));
  }

  @Test
  public void testDataObjectReturn() {
    assertEquals("ok", obj.methodWithDataObjectReturn(it -> new TestDataObject().setFoo("wasabi").setBar(6).setWibble(0.01)));
  }

  @Test
  public void testEnumReturn() {
    assertEquals("ok", obj.methodWithEnumReturn(it -> TestEnum.NICK));
  }

  @Test
  public void testListReturn() {
    assertEquals("ok", obj.methodWithListReturn(it -> Arrays.asList("one", "two", "three")));
  }

  @Test
  public void testSetReturn() {
    assertEquals("ok", obj.methodWithSetReturn(it -> new HashSet<>(Arrays.asList("one", "two", "three"))));
  }

  @Test
  public void testMapReturn() {
    Map<String, String> expected = new HashMap<>();
    expected.put("one", "one");
    expected.put("two", "two");
    expected.put("three", "three");
    assertEquals("ok", obj.methodWithMapReturn(it -> expected));
  }

  @Test
  public void testGenericReturn() {
    assertEquals("ok", obj.methodWithGenericReturn(it -> {
          switch (it) {
            case 0:
              return "the-string";
            case 1:
              return 123;
            case 2:
              return true;
            case 3:
              return new JsonObject().put("foo", "foo_value");
            case 4:
              return new JsonArray(Arrays.asList("foo", "bar"));
            default:
              throw new RuntimeException();
          }
        }
    ));
  }

  @Test
  public void testGenericUserTypeReturn() {
    assertEquals("ok", obj.methodWithGenericUserTypeReturn(it -> it));
  }

  @Test
  public void testNullableListReturn() {
    assertEquals("ok", obj.methodWithNullableListReturn(it -> null));
  }
}
