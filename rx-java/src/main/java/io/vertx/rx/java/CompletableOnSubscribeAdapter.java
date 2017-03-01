package io.vertx.rx.java;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import rx.Completable;
import rx.CompletableSubscriber;
import rx.Single;
import rx.SingleSubscriber;
import rx.subscriptions.BooleanSubscription;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CompletableOnSubscribeAdapter implements Completable.OnSubscribe {

  protected final Consumer<Handler<AsyncResult<Void>>> consumer;

  public CompletableOnSubscribeAdapter(Consumer<Handler<AsyncResult<Void>>> consumer) {
    this.consumer = consumer;
  }

  // OnSubscribe

  @Override
  public void call(CompletableSubscriber sub) {
    consumer.accept(ar -> {
      if (ar.succeeded()) {
        sub.onCompleted();
      } else {
        sub.onError(ar.cause());
      }
    });
  }
}
