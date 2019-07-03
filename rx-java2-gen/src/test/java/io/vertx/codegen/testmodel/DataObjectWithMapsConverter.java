package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.codegen.testmodel.DataObjectWithMaps}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithMaps} original class using Vert.x codegen.
 */
public class DataObjectWithMapsConverter implements JsonCodec<DataObjectWithMaps, JsonObject> {

  public static final DataObjectWithMapsConverter INSTANCE = new DataObjectWithMapsConverter();

  @Override public JsonObject encode(DataObjectWithMaps value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithMaps decode(JsonObject value) { return (value != null) ? new DataObjectWithMaps(value) : null; }

  @Override public Class<DataObjectWithMaps> getTargetClass() { return DataObjectWithMaps.class; }
}
