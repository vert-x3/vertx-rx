package io.vertx.lang.rxjava;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TypeArg<T> {

  public static <T> TypeArg<T> unknown() {
    return new TypeArg<>(obj -> (T)obj, obj -> obj);
  }

  public final Function<Object, T> toJava; // wrap ???
  public final Function<T, Object> unwrap;

  public TypeArg(Function<Object, T> toJava, Function<T, Object> unwrap) {
    this.toJava = toJava;
    this.unwrap = unwrap;
  }
}
