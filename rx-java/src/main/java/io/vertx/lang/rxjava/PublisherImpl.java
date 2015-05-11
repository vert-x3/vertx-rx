package io.vertx.lang.rxjava;

import io.vertx.core.Vertx;
import io.vertx.core.streams.Pump;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.reactivestreams.ReactiveWriteStream;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class PublisherImpl<R, J> implements Publisher<R> {

  private final Vertx vertx;
  private final ReadStream<J> rs;
  private final ReactiveWriteStream<J> ws;
  private final AtomicBoolean started = new AtomicBoolean();
  private final CopyOnWriteArrayList<Subscriber<? super R>> subscribers = new CopyOnWriteArrayList<>();
  private final BiFunction<J, Vertx, R> adapter;

  public PublisherImpl(ReadStream<J> rs, Vertx vertx, BiFunction<J, Vertx, R> adapter) {
    this.rs = rs;
    this.vertx = vertx;
    this.ws = ReactiveWriteStream.writeStream(vertx);
    this.adapter = adapter;
  }

  private void lazyStart() {
    if (started.compareAndSet(false, true)) {
      Pump pump = Pump.pump(rs, ws);
      rs.endHandler(v1 -> {
        vertx.runOnContext(v2 -> {
          ws.close();
        });
      });
      rs.exceptionHandler(err -> {
        for (Subscriber<? super R> subscriber : subscribers) {
          subscriber.onError(err);
        }
      });
      pump.start();
    }
  }

  @Override
  public void subscribe(Subscriber<? super R> subscriber) {
    Subscriber<J> subscriberProxy = new Subscriber<J>() {
      @Override
      public void onSubscribe(Subscription subscription) {
        Subscription subscriptionProxy = new Subscription() {
          @Override
          public void request(long n) {
            subscription.request(n);
          }

          @Override
          public void cancel() {
            subscription.cancel();
            subscribers.remove(subscriber);
          }
        };
        subscriber.onSubscribe(subscriptionProxy);
        subscribers.add(subscriber);
      }

      @Override
      public void onNext(J event) {
        subscriber.onNext(adapter.apply(event, vertx));
      }

      @Override
      public void onError(Throwable t) {
        subscriber.onError(t);
      }

      @Override
      public void onComplete() {
        subscriber.onComplete();
      }
    };
    ws.subscribe(subscriberProxy);
    lazyStart();
  }
}
