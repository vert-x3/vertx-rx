package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonArray;
import io.vertx.core.spi.json.JsonMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MyPojoToJsonArray {

  public static class MyPojoToJsonArrayMapper implements JsonMapper<MyPojoToJsonArray, JsonArray> {

    public static final MyPojoToJsonArrayMapper INSTANCE = new MyPojoToJsonArrayMapper();

    @Override
    public MyPojoToJsonArray deserialize(JsonArray value) throws IllegalArgumentException {
      return new MyPojoToJsonArray(value.stream().map(j -> (Integer)j).collect(Collectors.toList()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonArray serialize(MyPojoToJsonArray value) throws IllegalArgumentException {
      return new JsonArray((List)value.stuff);
    }

    @Override
    public Class<MyPojoToJsonArray> getTargetClass() {
      return MyPojoToJsonArray.class;
    }
  }

  List<Integer> stuff;

  public MyPojoToJsonArray() { }

  public MyPojoToJsonArray(List<Integer> stuff) {
    this.stuff = stuff;
  }

  public List<Integer> getStuff() {
    return stuff;
  }

  public void setStuff(List<Integer> stuff) {
    this.stuff = stuff;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyPojoToJsonArray that = (MyPojoToJsonArray) o;
    return Objects.equals(stuff, that.stuff);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stuff);
  }
}
