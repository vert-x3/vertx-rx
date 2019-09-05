package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonMapper;

/**
 * Converter and mapper for {@link io.vertx.codegen.testmodel.DataObjectWithNestedBuffer}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithNestedBuffer} original class using Vert.x codegen.
 */
public class DataObjectWithNestedBufferConverter implements JsonMapper<DataObjectWithNestedBuffer, JsonObject> {

  public static final DataObjectWithNestedBufferConverter INSTANCE = new DataObjectWithNestedBufferConverter();

  @Override public JsonObject serialize(DataObjectWithNestedBuffer value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithNestedBuffer deserialize(JsonObject value) { return (value != null) ? new DataObjectWithNestedBuffer(value) : null; }

  @Override public Class<DataObjectWithNestedBuffer> getTargetClass() { return DataObjectWithNestedBuffer.class; }

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DataObjectWithNestedBuffer obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "buffer":
          if (member.getValue() instanceof String) {
            obj.setBuffer(io.vertx.core.buffer.Buffer.buffer(java.util.Base64.getDecoder().decode((String)member.getValue())));
          }
          break;
        case "buffers":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.core.buffer.Buffer> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add(io.vertx.core.buffer.Buffer.buffer(java.util.Base64.getDecoder().decode((String)item)));
            });
            obj.setBuffers(list);
          }
          break;
        case "nested":
          if (member.getValue() instanceof JsonObject) {
            obj.setNested(new io.vertx.codegen.testmodel.DataObjectWithBuffer((JsonObject)member.getValue()));
          }
          break;
      }
    }
  }

  public static void toJson(DataObjectWithNestedBuffer obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DataObjectWithNestedBuffer obj, java.util.Map<String, Object> json) {
    if (obj.getBuffer() != null) {
      json.put("buffer", java.util.Base64.getEncoder().encodeToString(obj.getBuffer().getBytes()));
    }
    if (obj.getBuffers() != null) {
      JsonArray array = new JsonArray();
      obj.getBuffers().forEach(item -> array.add(java.util.Base64.getEncoder().encodeToString(item.getBytes())));
      json.put("buffers", array);
    }
    if (obj.getNested() != null) {
      json.put("nested", obj.getNested().toJson());
    }
  }
}
