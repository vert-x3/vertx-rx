package io.vertx.rx.java.test;

import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.ObservableReadStream;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import io.vertx.rx.java.test.support.SimpleSubscriber;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class ReadStreamAdapterBackPressureTest<O> extends ReadStreamAdapterTestBase<Buffer, O> {

  protected abstract O toObservable(BufferReadStreamImpl stream, int maxBufferSize);

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
    O observable = toObservable(stream);
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    subscribe(observable, subscriber);
    subscriber.assertEmpty();
    stream.expectPause();
    for (int i = 0; i < ObservableReadStream.DEFAULT_MAX_BUFFER_SIZE; i++) {
      stream.emit(buffer("" + i));
    }
    stream.check();
    subscriber.assertEmpty();
    subscriber.request(1);
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
    O observable = toObservable(stream);
    subscribe(observable, subscriber);
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
      O observable = toObservable(stream, 2);
      subscribe(observable, subscriber);
      stream.emit(buffer("0"), buffer("1"));
      stream.assertPaused();
      subscriber.request(i);
      subscriber.assertItem(Buffer.buffer("0")).assertEmpty();
      stream.check();
    }
  }

  @Test
  public void testEndWithoutRequest() {
    testEndOrFailWithoutRequest(null);
  }

  @Test
  public void testFailWithoutRequest() {
    testEndOrFailWithoutRequest(new RuntimeException());
  }

  private void testEndOrFailWithoutRequest(Throwable err) {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    O observable = toObservable(stream, 2);
    subscribe(observable, subscriber);
    if (err == null) {
      stream.end();
      subscriber.assertCompleted();
    } else {
      stream.fail(err);
      subscriber.assertError(err);
    }
    stream.check();
    subscriber.assertEmpty();
  }

  @Test
  public void testNoResumeWhenRequestingBuffered() {
    AtomicBoolean resumed = new AtomicBoolean();
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    stream.expectPause();
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    O observable = toObservable(stream, 2);
    subscribe(observable, subscriber);
    stream.emit(buffer("0"), buffer("1"));
    subscriber.request(1);
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
    O observable = toObservable(stream, num);
    subscribe(observable, subscriber);
    for (int i = 0;i < num;i++) {
      stream.emit(Buffer.buffer("" + i));
    }
    subscriber.request(num);
    for (int i = 0;i < num;i++) {
      subscriber.assertItem(Buffer.buffer("" + i));
    }
    subscriber.assertCompleted().assertEmpty();
    stream.check();
  }

  @Test
  public void testDeliverEndWhenPaused() {
    testDeliverEndOrFailWhenPaused(null);
  }

  @Test
  public void testDeliverFailWhenPaused() {
    testDeliverEndOrFailWhenPaused(new RuntimeException());
  }

  private void testDeliverEndOrFailWhenPaused(Throwable err) {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    stream.expectPause();
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    O observable = toObservable(stream, 2);
    subscribe(observable, subscriber);
    stream.emit(buffer("0"), buffer("1"));
    stream.check();
    // We send events even though we are paused
    if (err == null) {
      stream.end();
    } else {
      stream.fail(err);
    }
    subscriber.request(2);
    subscriber.assertItems(buffer("0"), buffer("1"));
    if (err == null) {
      subscriber.assertCompleted();
    } else {
      subscriber.assertError(err);
    }
    subscriber.assertEmpty();
    stream.check();
  }

  @Test
  public void testEndWhenPaused() {
    testEndOrFailWhenPaused(null);
  }

  @Test
  public void testFailWhenPaused() {
    testEndOrFailWhenPaused(new RuntimeException());
  }

  private void testEndOrFailWhenPaused(Throwable err) {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    stream.expectPause();
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    O observable = toObservable(stream, 2);
    subscribe(observable, subscriber);
    stream.emit(buffer("0"), buffer("1"));
    stream.assertPaused();
    if (err == null) {
      stream.end();
    } else {
      stream.fail(err);
    }
    subscriber.request(2);
    subscriber.assertItems(buffer("0"), buffer("1"));
    if (err == null) {
      subscriber.assertCompleted();
    } else {
      subscriber.assertError(err);
    }
    subscriber.assertEmpty();
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
    O observable = toObservable(stream);
    subscribe(observable, subscriber);
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
    O observable = toObservable(stream, 2);
    subscribe(observable, subscriber);
    stream.emit(Buffer.buffer("0"));
    stream.emit(Buffer.buffer("1"));
    subscriber.request(2);
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
    O observable = toObservable(stream, num);
    subscribe(observable, subscriber);
    for (int i = 0;i < num;i++) {
      stream.emit(Buffer.buffer("" + i));
    }
    subscriber.request(num);
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
    O observable = toObservable(stream, 2);
    subscribe(observable, subscriber);
    stream.emit(buffer("0"), buffer("1"));
    subscriber.request(2);
    subscriber.assertItem(buffer("0")).assertItem(buffer("1")).assertEmpty();
    stream.check();
  }

  @Test
  public void testFoo() {
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    O observable = toObservable(stream);
    subscribe(observable, subscriber);
    stream.emit(buffer("0"));
    stream.end();
    subscriber.request(1);
    subscriber.assertItem(buffer("0")).assertCompleted().assertEmpty();
  }

  @Test
  public void testBar() {
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    stream.expectPause();
    O observable = toObservable(stream);
    subscribe(observable, subscriber);
    for (int i = 0; i < ObservableReadStream.DEFAULT_MAX_BUFFER_SIZE; i++) {
      stream.emit(buffer("" + i));
    }
    stream.end();
    subscriber.request(1);
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
    O observable = toObservable(stream);
    subscribe(observable, subscriber);
    stream.emit(buffer("0"));
  }

  @Test
  public void testResubscribe() {
    SimpleSubscriber<Buffer> subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    O observable = toObservable(stream, 2);
    subscribe(observable, subscriber);
    stream.expectPause();
    stream.emit(buffer("0"), buffer("1"));
    stream.check();
    stream.expectResume();
    subscriber.unsubscribe();
    stream.check();
    subscriber = new SimpleSubscriber<Buffer>().prefetch(0);
    subscribe(observable, subscriber);
    stream.emit(buffer("2"));
    stream.expectPause();
    stream.emit(buffer("3"));
    subscriber.assertEmpty();
    stream.check();
    stream.expectResume();
    subscriber.request(2);
    subscriber.assertItems(buffer("2"), buffer("3"));
    RuntimeException cause = new RuntimeException();
    stream.fail(cause);
    subscriber.assertError(cause);
    assertTrue(subscriber.isUnsubscribed());
    subscriber = new SimpleSubscriber<>();
    subscribe(observable, subscriber);
    stream.end();
    subscriber.assertCompleted();
    stream.check();
  }
}
