package io.vertx.codegen.testmodel;

import io.vertx.core.spi.json.JsonMapper;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

public class MyPojoToJsonObject {

  public static class MyPojoToJsonObjectMapper implements JsonMapper<MyPojoToJsonObject, JsonObject> {

    public static final MyPojoToJsonObjectMapper INSTANCE = new MyPojoToJsonObjectMapper();

    @Override
    public MyPojoToJsonObject deserialize(JsonObject value) throws IllegalArgumentException {
      return new MyPojoToJsonObject(value.getInteger("v"));
    }

    @Override
    public JsonObject serialize(MyPojoToJsonObject value) throws IllegalArgumentException {
      return new JsonObject().put("v", value.getV());
    }

    @Override
    public Class<MyPojoToJsonObject> getTargetClass() {
      return MyPojoToJsonObject.class;
    }
  }

  int v;

  public MyPojoToJsonObject() { }

  public MyPojoToJsonObject(int v) {
    this.v = v;
  }

  public int getV() {
    return v;
  }

  public void setV(int v) {
    this.v = v;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyPojoToJsonObject that = (MyPojoToJsonObject) o;
    return v == that.v;
  }

  @Override
  public int hashCode() {
    return Objects.hash(v);
  }
}
