package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.codegen.testmodel.TestDataObject}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.TestDataObject} original class using Vert.x codegen.
 */
public class TestDataObjectConverter implements JsonCodec<TestDataObject, JsonObject> {

  public static final TestDataObjectConverter INSTANCE = new TestDataObjectConverter();

  @Override public JsonObject encode(TestDataObject value) { return (value != null) ? value.toJson() : null; }

  @Override public TestDataObject decode(JsonObject value) { return (value != null) ? new TestDataObject(value) : null; }

  @Override public Class<TestDataObject> getTargetClass() { return TestDataObject.class; }
}
