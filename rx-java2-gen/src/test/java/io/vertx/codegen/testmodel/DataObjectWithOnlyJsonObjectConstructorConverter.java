package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link io.vertx.codegen.testmodel.DataObjectWithOnlyJsonObjectConstructor}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithOnlyJsonObjectConstructor} original class using Vert.x codegen.
 */
public class DataObjectWithOnlyJsonObjectConstructorConverter {


  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DataObjectWithOnlyJsonObjectConstructor obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "foo":
          break;
      }
    }
  }

  public static void toJson(DataObjectWithOnlyJsonObjectConstructor obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DataObjectWithOnlyJsonObjectConstructor obj, java.util.Map<String, Object> json) {
    if (obj.getFoo() != null) {
      json.put("foo", obj.getFoo());
    }
  }
}
