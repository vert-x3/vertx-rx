package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonMapper;

/**
 * Converter and mapper for {@link io.vertx.codegen.testmodel.DataObjectWithBuffer}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithBuffer} original class using Vert.x codegen.
 */
public class DataObjectWithBufferConverter implements JsonMapper<DataObjectWithBuffer, JsonObject> {

  public static final DataObjectWithBufferConverter INSTANCE = new DataObjectWithBufferConverter();

  @Override public JsonObject serialize(DataObjectWithBuffer value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithBuffer deserialize(JsonObject value) { return (value != null) ? new DataObjectWithBuffer(value) : null; }

  @Override public Class<DataObjectWithBuffer> getTargetClass() { return DataObjectWithBuffer.class; }

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DataObjectWithBuffer obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "buffer":
          if (member.getValue() instanceof String) {
            obj.setBuffer(io.vertx.core.buffer.Buffer.buffer(java.util.Base64.getDecoder().decode((String)member.getValue())));
          }
          break;
      }
    }
  }

  public static void toJson(DataObjectWithBuffer obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DataObjectWithBuffer obj, java.util.Map<String, Object> json) {
    if (obj.getBuffer() != null) {
      json.put("buffer", java.util.Base64.getEncoder().encodeToString(obj.getBuffer().getBytes()));
    }
  }
}
