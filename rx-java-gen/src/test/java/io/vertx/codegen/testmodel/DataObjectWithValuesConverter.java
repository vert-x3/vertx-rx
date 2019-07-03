package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.codegen.testmodel.DataObjectWithValues}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithValues} original class using Vert.x codegen.
 */
public class DataObjectWithValuesConverter implements JsonCodec<DataObjectWithValues, JsonObject> {

  public static final DataObjectWithValuesConverter INSTANCE = new DataObjectWithValuesConverter();

  @Override public JsonObject encode(DataObjectWithValues value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithValues decode(JsonObject value) { return (value != null) ? new DataObjectWithValues(value) : null; }

  @Override public Class<DataObjectWithValues> getTargetClass() { return DataObjectWithValues.class; }
}
