package io.vertx.reactivex.test;

import io.reactivex.rxjava3.core.Flowable;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;
import io.vertx.lang.rx.test.TestSubscriber;
import io.vertx.reactivex.FlowableHelper;
import io.vertx.lang.rx.test.ReadStreamAdapterBackPressureTest;
import io.vertx.reactivex.impl.FlowableReadStream;
import io.vertx.test.fakestream.FakeStream;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FlowableReadStreamAdapterBackPressureTest extends ReadStreamAdapterBackPressureTest<Flowable<Buffer>> {

  @Override
  protected long defaultMaxBufferSize() {
    return FlowableReadStream.DEFAULT_MAX_BUFFER_SIZE;
  }

  @Override
  protected Flowable<Buffer> toObservable(ReadStream<Buffer> stream, int maxBufferSize) {
    return FlowableHelper.toFlowable(stream, maxBufferSize);
  }

  @Override
  protected Flowable<Buffer> toObservable(ReadStream<Buffer> stream) {
    return FlowableHelper.toFlowable(stream);
  }

  @Override
  protected Flowable<Buffer> flatMap(Flowable<Buffer> obs, Function<Buffer, Flowable<Buffer>> f) {
    return obs.flatMap(f::apply);
  }

  @Override
  protected void subscribe(Flowable<Buffer> obs, TestSubscriber<Buffer> sub) {
    TestUtils.subscribe(obs, sub);
  }

  @Override
  protected Flowable<Buffer> concat(Flowable<Buffer> obs1, Flowable<Buffer> obs2) {
    return Flowable.concat(obs1, obs2);
  }

  @Test
  public void testSubscribeTwice() {
    FakeStream<Buffer> stream = new FakeStream<>();
    Flowable<Buffer> observable = toObservable(stream);
    TestSubscriber<Buffer> subscriber1 = new TestSubscriber<Buffer>().prefetch(0);
    TestSubscriber<Buffer> subscriber2 = new TestSubscriber<Buffer>().prefetch(0);
    subscribe(observable, subscriber1);
    subscribe(observable, subscriber2);
    subscriber2.assertError(err -> {
      assertTrue(err instanceof IllegalStateException);
    });
    subscriber2.assertEmpty();
  }

  @Test
  public void testHandletIsSetInDoOnSubscribe() {
    AtomicBoolean handlerSet = new AtomicBoolean();
    FakeStream<Buffer> stream = new FakeStream<Buffer>() {
      @Override
      public FakeStream<Buffer> handler(Handler<Buffer> handler) {
        handlerSet.set(true);
        return super.handler(handler);
      }
    };
    Flowable<Buffer> observable = toObservable(stream).doOnSubscribe(disposable -> {
      assertTrue(handlerSet.get());
    });
    TestSubscriber<Buffer> subscriber = new TestSubscriber<>();
    subscribe(observable, subscriber);
    subscriber.assertEmpty();
  }
}
