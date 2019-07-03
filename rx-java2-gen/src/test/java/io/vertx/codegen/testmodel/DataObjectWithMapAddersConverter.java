package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.codegen.testmodel.DataObjectWithMapAdders}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithMapAdders} original class using Vert.x codegen.
 */
public class DataObjectWithMapAddersConverter implements JsonCodec<DataObjectWithMapAdders, JsonObject> {

  public static final DataObjectWithMapAddersConverter INSTANCE = new DataObjectWithMapAddersConverter();

  @Override public JsonObject encode(DataObjectWithMapAdders value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithMapAdders decode(JsonObject value) { return (value != null) ? new DataObjectWithMapAdders(value) : null; }

  @Override public Class<DataObjectWithMapAdders> getTargetClass() { return DataObjectWithMapAdders.class; }
}
