package io.vertx.rx.java;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import rx.Single;
import rx.SingleSubscriber;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SingleOnSubscribeAdapter<T> implements Single.OnSubscribe<T> {

  protected final Consumer<Handler<AsyncResult<T>>> consumer;

  public SingleOnSubscribeAdapter(Consumer<Handler<AsyncResult<T>>> consumer) {
    this.consumer = consumer;
  }

  // OnSubscribe

  /**
   * Subscription
   */
  @Override
  public void call(SingleSubscriber<? super T> sub) {
    consumer.accept(ar -> {
      if (!sub.isUnsubscribed()) {
        if (ar.succeeded()) {
          sub.onSuccess(ar.result());
        } else {
          sub.onError(ar.cause());
        }
      }
    });
  }
}
