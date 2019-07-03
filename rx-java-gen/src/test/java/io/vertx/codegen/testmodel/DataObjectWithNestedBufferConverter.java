package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.codegen.testmodel.DataObjectWithNestedBuffer}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithNestedBuffer} original class using Vert.x codegen.
 */
public class DataObjectWithNestedBufferConverter implements JsonCodec<DataObjectWithNestedBuffer, JsonObject> {

  public static final DataObjectWithNestedBufferConverter INSTANCE = new DataObjectWithNestedBufferConverter();

  @Override public JsonObject encode(DataObjectWithNestedBuffer value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithNestedBuffer decode(JsonObject value) { return (value != null) ? new DataObjectWithNestedBuffer(value) : null; }

  @Override public Class<DataObjectWithNestedBuffer> getTargetClass() { return DataObjectWithNestedBuffer.class; }
}
