package io.vertx.test.reactivex.stream;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxInterfaceImpl implements RxInterface {

  public final AtomicInteger voidCalls = new AtomicInteger();
  public final AtomicInteger voidFailures = new AtomicInteger();
  public final AtomicInteger stringCalls = new AtomicInteger();
  public final AtomicInteger stringFailures = new AtomicInteger();

  @Override
  public void methodWithHandlerAsyncResultString(Future<String> fut, Handler<AsyncResult<String>> handler) {
    stringCalls.incrementAndGet();
    fut.setHandler(ar -> {
      try {
        if (ar.succeeded()) {
          handler.handle(Future.succeededFuture(ar.result()));
        } else {
          handler.handle(Future.failedFuture(ar.cause()));
        }
      } catch (Exception e) {
        stringFailures.getAndIncrement();
      }
    });
  }

  @Override
  public void methodWithHandlerAsyncResultVoid(Future<Void> fut, Handler<AsyncResult<Void>> handler) {
    voidCalls.incrementAndGet();
    try {
      fut.setHandler(ar -> {
        if (ar.succeeded()) {
          handler.handle(Future.succeededFuture());
        } else {
          handler.handle(Future.failedFuture(ar.cause()));
        }
      });
    } catch (Exception e) {
      voidFailures.incrementAndGet();
    }
  }
}
