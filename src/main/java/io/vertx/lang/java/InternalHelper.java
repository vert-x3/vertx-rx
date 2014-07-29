package io.vertx.lang.java;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class InternalHelper {

  public static <V> AsyncResult<V> result(V value) {
    return Future.completedFuture(value);
  }

  public static <V> AsyncResult<V> failure(Throwable t) {
    return Future.completedFuture(t);
  }

}
