package io.vertx.reactivex.test;

import io.reactivex.Flowable;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.RxHelper;
import io.vertx.rx.java.test.ReadStreamAdapterBackPressureTest;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import io.vertx.rx.java.test.support.SimpleSubscriber;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FlowableReadStreamAdapterBackPressureTest extends ReadStreamAdapterBackPressureTest<Flowable<Buffer>> {

  @Test
  @Override
  public void testReact() {
    super.testReact();
  }

  @Override
  protected Flowable<Buffer> toObservable(BufferReadStreamImpl stream, int maxBufferSize) {
    return RxHelper.toFlowable(stream, maxBufferSize);
  }

  @Override
  protected Flowable<Buffer> toObservable(BufferReadStreamImpl stream) {
    return RxHelper.toFlowable(stream);
  }

  @Override
  protected Flowable<Buffer> flatMap(Flowable<Buffer> obs, Function<Buffer, Flowable<Buffer>> f) {
    return obs.flatMap(f::apply);
  }

  @Override
  protected void subscribe(Flowable<Buffer> obs, SimpleSubscriber<Buffer> sub) {
    obs.subscribe(new Subscriber<Buffer>() {
      boolean unsubscribed;
      @Override
      public void onSubscribe(Subscription s) {
        sub.onSubscribe(new SimpleSubscriber.Subscription() {
          @Override
          public void fetch(long val) {
            if (val > 0) {
              s.request(val);
            }
          }
          @Override
          public void unsubscribe() {
            unsubscribed = true;
            s.cancel();
          }
          @Override
          public boolean isUnsubscribed() {
            return unsubscribed;
          }
        });

      }
      @Override
      public void onNext(Buffer buffer) {
        sub.onNext(buffer);
      }
      @Override
      public void onError(Throwable t) {
        unsubscribed = true;
        sub.onError(t);
      }
      @Override
      public void onComplete() {
        unsubscribed = true;
        sub.onCompleted();
      }
    });
  }

  @Override
  protected Flowable<Buffer> concat(Flowable<Buffer> obs1, Flowable<Buffer> obs2) {
    return Flowable.concat(obs1, obs2);
  }
}
