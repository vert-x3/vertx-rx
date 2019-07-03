package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.codegen.testmodel.DataObjectWithListAdders}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithListAdders} original class using Vert.x codegen.
 */
public class DataObjectWithListAddersConverter implements JsonCodec<DataObjectWithListAdders, JsonObject> {

  public static final DataObjectWithListAddersConverter INSTANCE = new DataObjectWithListAddersConverter();

  @Override public JsonObject encode(DataObjectWithListAdders value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithListAdders decode(JsonObject value) { return (value != null) ? new DataObjectWithListAdders(value) : null; }

  @Override public Class<DataObjectWithListAdders> getTargetClass() { return DataObjectWithListAdders.class; }
}
