package io.vertx.rx.java.test;

import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.ObservableReadStream;
import io.vertx.rx.java.RxHelper;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import io.vertx.rx.java.test.support.SimpleSubscriber;
import org.junit.Test;
import rx.Observable;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableReadStreamAdapterBackPressureTest extends ReadStreamAdapterBackPressureTest<Observable<Buffer>> {

  @Override
  protected Observable<Buffer> toObservable(BufferReadStreamImpl stream, int maxBufferSize) {
    return RxHelper.toObservable(stream, maxBufferSize);
  }

  @Override
  protected Observable<Buffer> toObservable(BufferReadStreamImpl stream) {
    return RxHelper.toObservable(stream);
  }

  @Override
  protected void subscribe(Observable<Buffer> observable, SimpleSubscriber<Buffer> subscriber) {
    SimpleSubscriber.subscribe(observable, subscriber);
  }

  @Override
  protected Observable<Buffer> concat(Observable<Buffer> observable1, Observable<Buffer> observable2) {
    return Observable.concat(observable1, observable2);
  }

  @Test
  public void testDisableBackPressure() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    ObservableReadStream<Buffer, Buffer> adapter = new ObservableReadStream<>(stream, Function.identity());
    Observable<Buffer> observable = Observable.create(adapter);
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<>();
    SimpleSubscriber.subscribe(observable, subscriber);
    assertEquals(Long.MAX_VALUE, adapter.getRequested());
    stream.emit(buffer("0"));
    assertEquals(Long.MAX_VALUE, adapter.getRequested());
  }

  @Test
  public void testImplicitBackPressureActivation() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    ObservableReadStream<Buffer, Buffer> adapter = new ObservableReadStream<Buffer, Buffer>(stream, Function.identity());
    Observable<Buffer> observable = Observable.create(adapter);
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>() {
      @Override
      public void onNext(Buffer o) {
        super.onNext(o);
        request(2);
      }
    }.prefetch(Long.MAX_VALUE - 1);
    SimpleSubscriber.subscribe(observable, subscriber);
    assertEquals(Long.MAX_VALUE - 1, adapter.getRequested());
    stream.emit(buffer("0"));
    assertEquals(Long.MAX_VALUE, adapter.getRequested());
  }
}
