package io.vertx.lang.rxjava3;

import io.vertx.lang.rx.RxGen;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Helper {

  /**
   * Unwrap the type used in RxJava.
   *
   * @param type the type to unwrap
   * @return the unwrapped type
   */
  public static Class unwrap(Class<?> type) {
    if (type != null) {
      RxGen rxgen = type.getAnnotation(RxGen.class);
      if (rxgen != null) {
        return rxgen.value();
      }
    }
    return type;
  }
}
