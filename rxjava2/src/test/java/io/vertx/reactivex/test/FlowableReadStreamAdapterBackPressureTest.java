package io.vertx.reactivex.test;

import io.reactivex.Flowable;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.core.RxHelper;
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
    TestUtils.subscribe(obs, sub);
  }

  @Override
  protected Flowable<Buffer> concat(Flowable<Buffer> obs1, Flowable<Buffer> obs2) {
    return Flowable.concat(obs1, obs2);
  }
}
