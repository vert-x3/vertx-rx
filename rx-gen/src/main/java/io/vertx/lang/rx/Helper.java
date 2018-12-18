package io.vertx.lang.rx;

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
  public static <T> Class<T> unwrap(Class<T> type) {
    if (type != null) {
      RxGen rxgen = type.getAnnotation(RxGen.class);
      if (rxgen != null) {
        // Unsafe-ish
        return rxgen.value();
      }
    }
    return type;
  }
}
