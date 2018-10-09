package io.vertx.lang.rx.test;

public class SimplePojo {

  public String foo;

  public SimplePojo(String foo) {
    this.foo = foo;
  }

  public SimplePojo() {
  }

  @Override
  public boolean equals(Object obj) {
    SimplePojo that = (SimplePojo) obj;
    return foo.equals(that.foo);
  }
}
