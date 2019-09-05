package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonMapper;

/**
 * Converter and mapper for {@link io.vertx.codegen.testmodel.DataObjectWithOnlyJsonObjectConstructor}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithOnlyJsonObjectConstructor} original class using Vert.x codegen.
 */
public class DataObjectWithOnlyJsonObjectConstructorConverter implements JsonMapper<DataObjectWithOnlyJsonObjectConstructor, JsonObject> {

  public static final DataObjectWithOnlyJsonObjectConstructorConverter INSTANCE = new DataObjectWithOnlyJsonObjectConstructorConverter();

  @Override public JsonObject serialize(DataObjectWithOnlyJsonObjectConstructor value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithOnlyJsonObjectConstructor deserialize(JsonObject value) { return (value != null) ? new DataObjectWithOnlyJsonObjectConstructor(value) : null; }

  @Override public Class<DataObjectWithOnlyJsonObjectConstructor> getTargetClass() { return DataObjectWithOnlyJsonObjectConstructor.class; }

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
