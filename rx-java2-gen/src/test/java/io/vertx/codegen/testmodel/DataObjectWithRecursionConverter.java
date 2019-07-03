package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.codegen.testmodel.DataObjectWithRecursion}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithRecursion} original class using Vert.x codegen.
 */
public class DataObjectWithRecursionConverter implements JsonCodec<DataObjectWithRecursion, JsonObject> {

  public static final DataObjectWithRecursionConverter INSTANCE = new DataObjectWithRecursionConverter();

  @Override public JsonObject encode(DataObjectWithRecursion value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithRecursion decode(JsonObject value) { return (value != null) ? new DataObjectWithRecursion(value) : null; }

  @Override public Class<DataObjectWithRecursion> getTargetClass() { return DataObjectWithRecursion.class; }
}
