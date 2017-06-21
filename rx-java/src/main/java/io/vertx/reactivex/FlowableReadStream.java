package io.vertx.reactivex;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.processors.UnicastProcessor;
import io.vertx.core.streams.ReadStream;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FlowableReadStream<T, U> extends Flowable<U> {

  private final ReadStream<T> stream;
  private final Function<T, U> f;
  private final AtomicReference<UnicastProcessor<U>> processor = new AtomicReference<>();
  private final long highWaterMark;
  private final long lowWaterMark;
  private boolean subscribed;
  private long pending;
  private boolean paused;

  public FlowableReadStream(ReadStream<T> stream, long maxBufferSize, Function<T, U> f) {
    this.stream = stream;
    this.f = f;
    this.highWaterMark = maxBufferSize;
    this.lowWaterMark = maxBufferSize / 2;
  }

  @Override
  protected void subscribeActual(Subscriber<? super U> subscriber) {
    UnicastProcessor<U> p = UnicastProcessor.create();
    if (!processor.compareAndSet(null, p)) {
      return;
    }
    p.subscribe(new FlowableSubscriber<U>() {
      public void onSubscribe(Subscription _) {
        BasicIntQueueSubscription<U> sub = (BasicIntQueueSubscription<U>) _;
        BasicIntQueueSubscription basic = new BasicIntQueueSubscription<U>() {
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
            if (p == processor.get()) {
              if (n == Long.MAX_VALUE) {
                pending = Long.MIN_VALUE;
              } else {
                pending -= n;
              }
              if (subscribed) {
                if (paused && pending < lowWaterMark) {
                  paused = false;
                  stream.resume();
                }
              }
              sub.request(n);
            }
          }
          public void cancel() {
            sub.cancel();
            release();
          }
        };
        stream.endHandler(v -> p.onComplete());
        stream.exceptionHandler(p::onError);
        stream.handler(item -> {
          p.onNext(f.apply(item));
          if (++pending >= highWaterMark && !paused) {
            paused = true;
            stream.pause();
          }
        });
        subscriber.onSubscribe(basic);
        subscribed = true;
      }
      public void onNext(U t) {
        subscriber.onNext(t);
      }
      public void onError(Throwable t) {
        release();
        subscriber.onError(t);
      }
      public void onComplete() {
        release();
        subscriber.onComplete();
      }
      private void release() {
        subscribed = false;
        processor.set(null);
        pending = 0;
        try {
          stream.exceptionHandler(null);
          stream.endHandler(null);
          stream.handler(null);
        } catch (Exception ignore) {
          // Todo : handle this case
          // happens with testObservableWebSocket
        }
        if (paused) {
          paused = false;
          stream.resume();
        }
      }
    });
  }
}
