package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.codegen.testmodel.DataObjectWithBuffer}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithBuffer} original class using Vert.x codegen.
 */
public class DataObjectWithBufferConverter implements JsonCodec<DataObjectWithBuffer, JsonObject> {

  public static final DataObjectWithBufferConverter INSTANCE = new DataObjectWithBufferConverter();

  @Override public JsonObject encode(DataObjectWithBuffer value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithBuffer decode(JsonObject value) { return (value != null) ? new DataObjectWithBuffer(value) : null; }

  @Override public Class<DataObjectWithBuffer> getTargetClass() { return DataObjectWithBuffer.class; }
}
