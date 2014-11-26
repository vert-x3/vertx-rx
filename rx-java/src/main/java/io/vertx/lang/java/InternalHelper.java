package io.vertx.lang.java;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class InternalHelper {

  public static <V> AsyncResult<V> result(V value) {
    return Future.succeededFuture(value);
  }

  public static <V> AsyncResult<V> failure(Throwable t) {
    return Future.failedFuture(t);
  }

  public static <V> Handler<AsyncResult<V>> asyncResultHandler(CompletableFuture<V> f) {
    return event -> {
      if (event.succeeded()) {
        f.complete(event.result());
      } else {
        f.completeExceptionally(event.cause());
      }
    };
  }

}
