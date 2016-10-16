package io.vertx.reactivex;

import io.reactivex.Flowable;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.processors.UnicastProcessor;
import io.vertx.core.streams.ReadStream;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FlowableReadStream<T, U> extends Flowable<U> {

  private final ReadStream<T> stream;
  private final Function<T, U> f;
  private UnicastProcessor<U> processor;
  private BasicIntQueueSubscription basic;

  public FlowableReadStream(ReadStream<T> stream, Function<T, U> f) {

    // Pause until we have a subscription
    stream.pause();

    this.stream = stream;
    this.f = f;
  }

  @Override
  protected void subscribeActual(Subscriber<? super U> subscriber) {
    if (processor == null) {
      processor = UnicastProcessor.create();
    }
    processor.subscribe(new Subscriber<U>() {

      // Number of requested items
      // this number can be < 0 : the number of queued items - this happen
      // when a ReadStream has sent items and no claim has been done
      private long requested;

      public void onSubscribe(Subscription _) {
        BasicIntQueueSubscription<U> sub = (BasicIntQueueSubscription<U>) _;
        basic = new BasicIntQueueSubscription<U>() {
          public int requestFusion(int mode) {
            return sub.requestFusion(mode);
          }
          public U poll() throws Exception {
            return sub.poll();
          }
          public boolean isEmpty() {
            return sub.isEmpty();
          }
          public void clear() {
            sub.clear();
          }
          public void request(long n) {
            if (n == Long.MAX_VALUE) {
              requested = Long.MAX_VALUE;
            } else {
              requested += n;
            }
            if (requested > 0) {
              stream.resume();
            }
            sub.request(n);
          }
          public void cancel() {
            sub.cancel();
            processor = null;
            try {
              stream.handler(null);
              stream.exceptionHandler(null);
              stream.endHandler(null);
            } catch (Exception ignore) {
              // Todo : handle this case
              // happens with testObservableWebSocket
            }
          }
        };
        stream.endHandler(v -> processor.onComplete());
        stream.exceptionHandler(processor::onError);
        stream.handler(item -> {
          processor.onNext(f.apply(item));
          if (requested != Long.MAX_VALUE) {
            if (--requested <= 0) {
              stream.pause();
            }
          }
        });
      }
      public void onNext(U t) {
        subscriber.onNext(t);
      }
      public void onError(Throwable t) {
        subscriber.onError(t);
      }
      public void onComplete() {
        subscriber.onComplete();
      }
    });

    //
    subscriber.onSubscribe(basic);
  }
}