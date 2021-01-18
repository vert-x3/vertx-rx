package io.vertx.rxjava3.test;

import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;
import io.vertx.lang.rx.test.ReadStreamAdapterTestBase;
import io.vertx.lang.rx.test.TestSubscriber;
import io.vertx.rxjava3.ObservableHelper;
import io.vertx.test.fakestream.FakeStream;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableReadStreamAdapterTest extends ReadStreamAdapterTestBase<Buffer, Observable<Buffer>> {

  @Override
  protected Observable<Buffer> toObservable(ReadStream<Buffer> stream) {
    return ObservableHelper.toObservable(stream);
  }

  @Override
  protected Buffer buffer(String s) {
    return Buffer.buffer(s);
  }

  @Override
  protected String string(Buffer buffer) {
    return buffer.toString();
  }

  @Override
  protected void subscribe(Observable<Buffer> obs, TestSubscriber<Buffer> sub) {
    TestUtils.subscribe(obs, sub);
  }

  @Override
  protected Observable<Buffer> concat(Observable<Buffer> obs1, Observable<Buffer> obs2) {
    return Observable.concat(obs1, obs2);
  }

  @Test
  public void testHandletIsSetInDoOnSubscribe() {
    AtomicBoolean hanlderSet = new AtomicBoolean();
    FakeStream<Buffer> stream = new FakeStream<Buffer>() {
      @Override
      public FakeStream<Buffer> handler(Handler<Buffer> handler) {
        hanlderSet.set(true);
        return super.handler(handler);
      }
    };
    Observable<Buffer> observable = toObservable(stream).doOnSubscribe(disposable -> {
      assertTrue(hanlderSet.get());
    });
    TestSubscriber<Buffer> subscriber = new TestSubscriber<>();
    subscribe(observable, subscriber);
    subscriber.assertEmpty();
  }
}
