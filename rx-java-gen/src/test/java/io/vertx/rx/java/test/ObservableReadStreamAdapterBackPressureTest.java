package io.vertx.rx.java.test;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;
import io.vertx.lang.rx.test.ReadStreamAdapterBackPressureTest;
import io.vertx.lang.rx.test.TestSubscriber;
import io.vertx.rx.java.ObservableReadStream;
import io.vertx.rx.java.RxHelper;
import io.vertx.test.fakestream.FakeStream;
import org.junit.Test;
import rx.Observable;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableReadStreamAdapterBackPressureTest extends ReadStreamAdapterBackPressureTest<Observable<Buffer>> {

  @Override
  protected long defaultMaxBufferSize() {
    return ObservableReadStream.DEFAULT_MAX_BUFFER_SIZE;
  }

  @Override
  protected Observable<Buffer> toObservable(ReadStream<Buffer> stream, int maxBufferSize) {
    return RxHelper.toObservable(stream, maxBufferSize);
  }

  @Override
  protected Observable<Buffer> toObservable(ReadStream<Buffer> stream) {
    return RxHelper.toObservable(stream);
  }

  @Override
  protected void subscribe(Observable<Buffer> obs, TestSubscriber<Buffer> sub) {
    TestUtils.subscribe(obs, sub);
  }

  @Override
  protected Observable<Buffer> concat(Observable<Buffer> obs1, Observable<Buffer> obs2) {
    return Observable.concat(obs1, obs2);
  }

  @Override
  protected Observable<Buffer> flatMap(Observable<Buffer> obs, Function<Buffer, Observable<Buffer>> f) {
    return obs.flatMap(f::apply);
  }

  @Test
  public void testDisableBackPressure() {
    FakeStream<Buffer> stream = new FakeStream<>();
    ObservableReadStream<Buffer, Buffer> adapter = new ObservableReadStream<>(stream, Function.identity());
    Observable<Buffer> observable = Observable.create(adapter);
    TestSubscriber<Buffer> subscriber = new TestSubscriber<>();
    TestUtils.subscribe(observable, subscriber);
    assertEquals(Long.MAX_VALUE, adapter.getRequested());
    stream.emit(buffer("0"));
    assertEquals(Long.MAX_VALUE, adapter.getRequested());
  }

  @Test
  public void testImplicitBackPressureActivation() {
    FakeStream<Buffer> stream = new FakeStream<>();
    ObservableReadStream<Buffer, Buffer> adapter = new ObservableReadStream<Buffer, Buffer>(stream, Function.identity());
    Observable<Buffer> observable = Observable.create(adapter);
    TestSubscriber<Buffer> subscriber = new TestSubscriber<Buffer>() {
      @Override
      public void onNext(Buffer o) {
        super.onNext(o);
        request(2);
      }
    }.prefetch(Long.MAX_VALUE - 1);
    TestUtils.subscribe(observable, subscriber);
    assertEquals(Long.MAX_VALUE - 1, adapter.getRequested());
    stream.emit(buffer("0"));
    assertEquals(Long.MAX_VALUE, adapter.getRequested());
  }
}
