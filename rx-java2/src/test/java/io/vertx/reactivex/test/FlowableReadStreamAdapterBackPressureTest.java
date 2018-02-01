package io.vertx.reactivex.test;

import io.reactivex.Flowable;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.FlowableHelper;
import io.vertx.rx.java.test.ReadStreamAdapterBackPressureTest;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import io.vertx.rx.java.test.support.SimpleSubscriber;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FlowableReadStreamAdapterBackPressureTest extends ReadStreamAdapterBackPressureTest<Flowable<Buffer>> {

  @Override
  protected Flowable<Buffer> toObservable(BufferReadStreamImpl stream, int maxBufferSize) {
    return FlowableHelper.toFlowable(stream, maxBufferSize);
  }

  @Override
  protected Flowable<Buffer> toObservable(BufferReadStreamImpl stream) {
    return FlowableHelper.toFlowable(stream);
  }

  @Override
  protected Flowable<Buffer> flatMap(Flowable<Buffer> obs, Function<Buffer, Flowable<Buffer>> f) {
    return obs.flatMap(f::apply);
  }

  @Override
  protected void subscribe(Flowable<Buffer> obs, SimpleSubscriber<Buffer> sub) {
    TestUtils.subscribe(obs, sub);
  }

  @Override
  protected Flowable<Buffer> concat(Flowable<Buffer> obs1, Flowable<Buffer> obs2) {
    return Flowable.concat(obs1, obs2);
  }

  @Test
  public void testSubscribeTwice() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Flowable<Buffer> observable = toObservable(stream);
    SimpleSubscriber<Buffer> subscriber1 = new SimpleSubscriber<Buffer>().prefetch(0);
    SimpleSubscriber<Buffer> subscriber2 = new SimpleSubscriber<Buffer>().prefetch(0);
    subscribe(observable, subscriber1);
    subscribe(observable, subscriber2);
    subscriber2.assertError(err -> {
      assertTrue(err instanceof IllegalStateException);
    });
    subscriber2.assertEmpty();
  }

  @Test
  public void testHandletIsSetInDoOnSubscribe() {
    AtomicBoolean hanlderSet = new AtomicBoolean();
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl handler(Handler<Buffer> handler) {
        hanlderSet.set(true);
        return super.handler(handler);
      }
    };
    Flowable<Buffer> observable = toObservable(stream).doOnSubscribe(disposable -> {
      assertTrue(hanlderSet.get());
    });
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<>();
    subscribe(observable, subscriber);
    subscriber.assertEmpty();
  }
}
