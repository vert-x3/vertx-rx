package io.vertx.lang.rxjava;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.ReadStream;
import rx.Observable;
import rx.RxReactiveStreams;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

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

  public static <R, J> Observable<R> toObservable(ReadStream<J> stream, Vertx vertx, BiFunction<J, Vertx, R> adapter) {
    return RxReactiveStreams.toObservable(new PublisherImpl<>(stream, vertx, adapter));
  }
}
