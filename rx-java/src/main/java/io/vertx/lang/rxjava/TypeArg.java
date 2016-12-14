package io.vertx.lang.rxjava;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TypeArg<T> {

  public static <T> TypeArg<T> unknown() {
    return new TypeArg<>(obj -> (T)obj, obj -> obj);
  }

  public final Function<Object, Object> wrap;
  public final Function<T, Object> unwrap;

  public TypeArg(Function<Object, Object> wrap, Function<T, Object> unwrap) {
    this.wrap = wrap;
    this.unwrap = unwrap;
  }

  public T wrap(Object o) {
    return o != null ? (T) wrap.apply(o) : null;
  }

  public Object unwrap(T o) {
    return o != null ? unwrap.apply(o) : null;
  }
}
