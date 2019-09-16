package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link io.vertx.codegen.testmodel.TestDataObject}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.TestDataObject} original class using Vert.x codegen.
 */
public class TestDataObjectConverter {


  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TestDataObject obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "bar":
          if (member.getValue() instanceof Number) {
            obj.setBar(((Number)member.getValue()).intValue());
          }
          break;
        case "foo":
          if (member.getValue() instanceof String) {
            obj.setFoo((String)member.getValue());
          }
          break;
        case "wibble":
          if (member.getValue() instanceof Number) {
            obj.setWibble(((Number)member.getValue()).doubleValue());
          }
          break;
      }
    }
  }

  public static void toJson(TestDataObject obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(TestDataObject obj, java.util.Map<String, Object> json) {
    json.put("bar", obj.getBar());
    if (obj.getFoo() != null) {
      json.put("foo", obj.getFoo());
    }
    json.put("wibble", obj.getWibble());
  }
}
