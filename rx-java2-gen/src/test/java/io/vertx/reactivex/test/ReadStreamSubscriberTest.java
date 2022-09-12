package io.vertx.reactivex.test;

import io.vertx.reactivex.impl.ReadStreamSubscriber;
import io.vertx.lang.rx.test.ReadStreamSubscriberTestBase;
import org.reactivestreams.Subscription;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamSubscriberTest extends ReadStreamSubscriberTestBase {

  @Override
  public long bufferSize() {
    return ReadStreamSubscriber.BUFFER_SIZE;
  }

  @Override
  protected Sender sender() {
    return new Sender() {

      private boolean cancelled;

      private ReadStreamSubscriber<String, String> subscriber = new ReadStreamSubscriber<>(Function.identity(), subscriber -> {
        subscriber.onSubscribe(new Subscription() {
          @Override
          public void request(long n) {
            requested += n;
          }
          @Override
          public void cancel() {
            cancelled = true;
          }
        });
      });

      {
        stream = subscriber;
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

      @Override
      protected boolean isUnsubscribed() {
        return cancelled;
      }
    };
  }
}
