package io.vertx.codegen.testmodel;

import java.util.Objects;

public class MyPojoToJsonObject {

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
