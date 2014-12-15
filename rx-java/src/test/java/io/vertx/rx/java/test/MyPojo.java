package io.vertx.rx.java.test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MyPojo {

  public String foo;

  public MyPojo(String foo) {
    this.foo = foo;
  }

  public MyPojo() {
  }

  @Override
  public boolean equals(Object obj) {
    MyPojo that = (MyPojo) obj;
    return foo.equals(that.foo);
  }
}
