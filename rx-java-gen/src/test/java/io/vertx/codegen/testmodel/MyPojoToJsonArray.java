package io.vertx.codegen.testmodel;

import java.util.List;
import java.util.Objects;

public class MyPojoToJsonArray {

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
