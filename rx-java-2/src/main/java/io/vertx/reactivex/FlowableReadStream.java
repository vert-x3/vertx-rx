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

  static final int DEFAULT_WRITE_QUEUE_MAX_SIZE = 32;

  final ReadStream<T> stream;
  final UnicastProcessor<U> processor;
  final Function<T, U> f;

  public FlowableReadStream(ReadStream<T> stream, Function<T, U> f) {
    this.stream = stream;
    this.f = f;
    this.processor = UnicastProcessor.create();
  }

  @Override
  protected void subscribeActual(Subscriber<? super U> subscriber) {



    processor.subscribe(new Subscriber<U>() {
      private int size;
      public void onSubscribe(Subscription _) {
        BasicIntQueueSubscription<U> sub = (BasicIntQueueSubscription<U>) _;
        BasicIntQueueSubscription<U> basic = new BasicIntQueueSubscription<U>() {
          public int requestFusion(int mode) {
            return sub.requestFusion(mode);
          }
          public U poll() throws Exception {
            U value = sub.poll();
            if (value != null && --size < DEFAULT_WRITE_QUEUE_MAX_SIZE) {
              stream.resume();
            }
            return value;
          }
          public boolean isEmpty() {
            return sub.isEmpty();
          }
          public void clear() {
            sub.clear();
          }
          public void request(long n) {
            sub.request(n);
          }
          public void cancel() {
            sub.cancel();
            stream.handler(null);
            stream.exceptionHandler(null);
            stream.endHandler(null);
          }
        };
        subscriber.onSubscribe(basic);
        stream.endHandler(v -> processor.onComplete());
        stream.exceptionHandler(processor::onError);
        stream.handler(item -> {
          processor.onNext(f.apply(item));
          if (size++ > DEFAULT_WRITE_QUEUE_MAX_SIZE) {
            stream.pause();
          }
        });
      }
      public void onNext(U t) {
        subscriber.onNext(t);
        if (t != null && --size < DEFAULT_WRITE_QUEUE_MAX_SIZE) {
          stream.resume();
        }
      }
      public void onError(Throwable t) {
        subscriber.onError(t);
      }
      public void onComplete() {
        subscriber.onComplete();
      }
    });
  }
}