package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.codegen.testmodel.DataObjectWithOnlyJsonObjectConstructor}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithOnlyJsonObjectConstructor} original class using Vert.x codegen.
 */
public class DataObjectWithOnlyJsonObjectConstructorConverter implements JsonCodec<DataObjectWithOnlyJsonObjectConstructor, JsonObject> {

  public static final DataObjectWithOnlyJsonObjectConstructorConverter INSTANCE = new DataObjectWithOnlyJsonObjectConstructorConverter();

  @Override public JsonObject encode(DataObjectWithOnlyJsonObjectConstructor value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithOnlyJsonObjectConstructor decode(JsonObject value) { return (value != null) ? new DataObjectWithOnlyJsonObjectConstructor(value) : null; }

  @Override public Class<DataObjectWithOnlyJsonObjectConstructor> getTargetClass() { return DataObjectWithOnlyJsonObjectConstructor.class; }
}
