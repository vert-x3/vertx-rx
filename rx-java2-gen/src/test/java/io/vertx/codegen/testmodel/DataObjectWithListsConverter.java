package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.codegen.testmodel.DataObjectWithLists}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithLists} original class using Vert.x codegen.
 */
public class DataObjectWithListsConverter implements JsonCodec<DataObjectWithLists, JsonObject> {

  public static final DataObjectWithListsConverter INSTANCE = new DataObjectWithListsConverter();

  @Override public JsonObject encode(DataObjectWithLists value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithLists decode(JsonObject value) { return (value != null) ? new DataObjectWithLists(value) : null; }

  @Override public Class<DataObjectWithLists> getTargetClass() { return DataObjectWithLists.class; }
}
