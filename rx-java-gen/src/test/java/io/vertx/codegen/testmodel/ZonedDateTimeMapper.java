package io.vertx.codegen.testmodel;

import io.vertx.core.spi.json.JsonMapper;

import java.time.ZonedDateTime;

public class ZonedDateTimeMapper implements JsonMapper<ZonedDateTime, String> {

  public static final ZonedDateTimeMapper INSTANCE = new ZonedDateTimeMapper();

  @Override
  public String serialize(ZonedDateTime value) {
    return value.toString();
  }

  @Override
  public Class<ZonedDateTime> getTargetClass() {
    return ZonedDateTime.class;
  }

  @Override
  public ZonedDateTime deserialize(String value) {
    return ZonedDateTime.parse(value);
  }
}
