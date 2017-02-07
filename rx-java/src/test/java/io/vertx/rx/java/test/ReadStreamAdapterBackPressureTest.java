package io.vertx.rx.java.test;

import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.ObservableReadStream;
import io.vertx.rx.java.RxHelper;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import io.vertx.rx.java.test.support.SimpleSubscriber;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamAdapterBackPressureTest extends ReadStreamAdapterTestBase<Buffer> {

  protected Observable<Buffer> toObservable(BufferReadStreamImpl stream, int maxBufferSize) {
    return RxHelper.toObservable(stream, maxBufferSize);
  }

  @Override
  protected Observable<Buffer> toObservable(BufferReadStreamImpl stream) {
    return RxHelper.toObservable(stream);
  }

  @Override
  protected Buffer buffer(String s) {
    return Buffer.buffer(s);
  }

  @Override
  protected String string(Buffer buffer) {
    return buffer.toString("UTF-8");
  }

  @Test
  public void testPause() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Observable<Buffer> observable = toObservable(stream);
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    observable.subscribe(subscriber);
    subscriber.assertEmpty();
    stream.expectPause();
    for (int i = 0; i < ObservableReadStream.DEFAULT_MAX_BUFFER_SIZE; i++) {
      stream.emit(buffer("" + i));
    }
    stream.check();
    subscriber.assertEmpty();
    subscriber.getProducer().request(1);
    subscriber.assertItem(buffer("0")).assertEmpty();
  }

  @Test
  public void testNoPauseWhenRequestingOne() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>() {
      @Override
      public void onNext(Buffer buffer) {
        super.onNext(buffer);
        request(1);
      }
    }.prefetch(1);
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    stream.emit(buffer("0"), buffer("1"), buffer("2"));
    stream.check();
  }

  @Test
  public void testUnsubscribeOnFirstItemFromBufferedDeliveredWhileRequesting() {
    for (int i = 1;i <= 3;i++) {
      BufferReadStreamImpl stream = new BufferReadStreamImpl();
      stream.expectPause();
      stream.expectResume();
      SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>() {
        @Override
        public void onNext(Buffer buffer) {
          super.onNext(buffer);
          unsubscribe();
        }
      }.prefetch(0);
      Observable<Buffer> observable = toObservable(stream, 2);
      observable.subscribe(subscriber);
      stream.emit(buffer("0"), buffer("1"));
      stream.assertPaused();
      subscriber.getProducer().request(i);
      subscriber.assertItem(Buffer.buffer("0")).assertEmpty();
      stream.check();
    }
  }

  @Test
  public void testNoResumeWhenRequestingBuffered() {
    AtomicBoolean resumed = new AtomicBoolean();
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    stream.expectPause();
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    Observable<Buffer> observable = toObservable(stream, 2);
    observable.subscribe(subscriber);
    stream.emit(buffer("0"), buffer("1"));
    subscriber.getProducer().request(1);
    assertEquals(false, resumed.get());
    stream.check();
  }

  @Test
  public void testEndDuringRequestResume() {
    int num = 10;
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    stream.expectPause();
    stream.expectResume(stream::end);
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    Observable<Buffer> observable = toObservable(stream, num);
    observable.subscribe(subscriber);
    for (int i = 0;i < num;i++) {
      stream.emit(Buffer.buffer("" + i));
    }
    subscriber.getProducer().request(num);
    for (int i = 0;i < num;i++) {
      subscriber.assertItem(Buffer.buffer("" + i));
    }
    subscriber.assertCompleted().assertEmpty();
    stream.check();
  }

  @Test
  public void testDisableBackPressure() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    ObservableReadStream<Buffer, Buffer> adapter = new ObservableReadStream<>(stream, Function.identity());
    Observable<Buffer> observable = Observable.create(adapter);
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<>();
    observable.subscribe(subscriber);
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
    observable.subscribe(subscriber);
    assertEquals(Long.MAX_VALUE - 1, adapter.getRequested());
    stream.emit(buffer("0"));
    assertEquals(Long.MAX_VALUE, adapter.getRequested());
  }

  @Test
  public void testDeliverEventWhenPaused() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
    };
    stream.expectPause();
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    Observable<Buffer> observable = toObservable(stream, 2);
    observable.subscribe(subscriber);
    stream.end(buffer("0"), buffer("1")); // We send an event even though we are paused
    subscriber.getProducer().request(2);
    subscriber.assertItems(buffer("0"), buffer("1")).assertCompleted().assertEmpty();
    stream.check();
  }

  @Test
  public void testEndWhenPaused() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    stream.expectPause();
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    Observable<Buffer> observable = toObservable(stream, 2);
    observable.subscribe(subscriber);
    stream.emit(buffer("0"), buffer("1"));
    stream.assertPaused();
    stream.end();
    subscriber.getProducer().request(2);
    subscriber.assertItems(buffer("0"), buffer("1")).assertCompleted().assertEmpty();
    stream.check();
  }

  @Test
  public void testRequestDuringOnNext() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>() {
      @Override
      public void onNext(Buffer buffer) {
        super.onNext(buffer);
        request(1);
      }
    }.prefetch(1);
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    stream.emit(buffer("0"));
    subscriber.assertItem(buffer("0")).assertEmpty();
    stream.emit(buffer("1"));
    subscriber.assertItem(buffer("1")).assertEmpty();
    stream.emit(buffer("2"));
    subscriber.assertItem(buffer("2")).assertEmpty();
    stream.end();
    subscriber.assertCompleted().assertEmpty();
  }

  @Test
  public void testDeliverDuringResume() {
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    stream.expectPause();
    stream.expectResume(() -> stream.emit(buffer("2")));
    Observable<Buffer> observable = toObservable(stream, 2);
    observable.subscribe(subscriber);
    stream.emit(Buffer.buffer("0"));
    stream.emit(Buffer.buffer("1"));
    subscriber.getProducer().request(2);
    subscriber.assertItems(buffer("0"), buffer("1")).assertEmpty();
    stream.check();
  }

  @Test
  public void testEndDuringResume() {
    int num = 4;
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    stream.expectPause();
    stream.expectResume(stream::end);
    Observable<Buffer> observable = toObservable(stream, num);
    observable.subscribe(subscriber);
    for (int i = 0;i < num;i++) {
      stream.emit(Buffer.buffer("" + i));
    }
    subscriber.getProducer().request(num);
    for (int i = 0;i < num;i++) {
      subscriber.assertItem(Buffer.buffer("" + i));
    }
    subscriber.assertCompleted().assertEmpty();
    stream.check();
  }

  @Test
  public void testBufferDuringResume() {
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    stream.expectPause();
    stream.expectResume(() -> stream.emit(buffer("2"), buffer("3")));
    stream.expectPause();
    Observable<Buffer> observable = toObservable(stream, 2);
    observable.subscribe(subscriber);
    stream.emit(buffer("0"), buffer("1"));
    subscriber.getProducer().request(2);
    subscriber.assertItem(buffer("0")).assertItem(buffer("1")).assertEmpty();
    stream.check();
  }

  @Test
  public void testFoo() {
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    stream.emit(buffer("0"));
    stream.end();
    subscriber.getProducer().request(1);
    subscriber.assertItem(buffer("0")).assertCompleted().assertEmpty();
  }

  @Test
  public void testBar() {
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    stream.expectPause();
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    for (int i = 0; i < ObservableReadStream.DEFAULT_MAX_BUFFER_SIZE; i++) {
      stream.emit(buffer("" + i));
    }
    stream.end();
    subscriber.getProducer().request(1);
    subscriber.assertItem(buffer("0")).assertEmpty();
  }

  @Test
  public void testUnsubscribeDuringOnNext() {
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>() {
      @Override
      public void onNext(Buffer buffer) {
        super.onNext(buffer);
        unsubscribe();
      }
    };
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    stream.emit(buffer("0"));
  }

  @Test
  public void testResubscribe() {
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Observable<Buffer> observable = toObservable(stream, 2);
    Subscription sub = observable.subscribe(subscriber);
    stream.expectPause();
    stream.emit(buffer("0"), buffer("1"));
    stream.check();
    stream.expectResume();
    sub.unsubscribe();
    stream.check();
    subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    sub = observable.subscribe(subscriber);
    stream.emit(buffer("2"));
    stream.expectPause();
    stream.emit(buffer("3"));
    stream.check();
  }
}
