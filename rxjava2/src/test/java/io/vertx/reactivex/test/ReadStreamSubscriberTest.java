package io.vertx.reactivex.test;

import io.vertx.rx.java.test.ReadStreamSubscriberTestBase;
import org.reactivestreams.Subscription;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamSubscriberTest extends ReadStreamSubscriberTestBase {

  @Override
  protected Sender sender() {
    return new Sender() {

      private io.vertx.reactivex.core.impl.ReadStreamSubscriber<String, String> subscriber = new io.vertx.reactivex.core.impl.ReadStreamSubscriber<>(Function.identity());

      {
        stream = subscriber;
        subscriber.onSubscribe(new Subscription() {
          @Override
          public void request(long n) {
            requested += n;
          }
          @Override
          public void cancel() {
          }
        });
      }

      protected void emit() {
        subscriber.onNext("" + seq++);
      }

      protected void complete() {
        subscriber.onComplete();
      }

      protected void fail(Throwable cause) {
        subscriber.onError(cause);
      }

    };
  }
}
