package io.vertx.rx.java.test.support;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
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
