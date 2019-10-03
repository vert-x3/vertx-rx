package io.vertx.codegen.testmodel;

import java.util.Objects;

public class MyPojoToInteger {

  int a;

  public MyPojoToInteger() {
  }

  public MyPojoToInteger(int a) {
    this.a = a;
  }

  public int getA() {
    return a;
  }

  public void setA(int a) {
    this.a = a;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyPojoToInteger that = (MyPojoToInteger) o;
    return a == that.a;
  }

  @Override
  public int hashCode() {
    return Objects.hash(a);
  }
}
