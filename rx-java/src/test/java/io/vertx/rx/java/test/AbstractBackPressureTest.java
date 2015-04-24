package io.vertx.rx.java.test;

import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.ReadStreamAdapter;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class AbstractBackPressureTest extends AbstractReadStreamAdapterTest<Buffer> {


  @Override
  protected Buffer buffer(String s) {
    return Buffer.buffer(s);
  }

  @Override
  protected String string(Buffer buffer) {
    return buffer.toString("UTF-8");
  }

  @Test
  public void testBackPressureBuffer() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Observable<Buffer> observable = toObservable(stream);
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(5);
      }
    };
    observable.subscribe(subscriber);
    waitUntil(() -> subscriber.getProducer() != null);
    int count = 0;
    while (!stream.isPaused()) {
      stream.write(buffer("" + count));
      count++;
    }
    for (int i = 0;i < 5;i++) {
      subscriber.assertItem(buffer("" + i));
      stream.write(buffer("" + count));
      count++;
    }
    subscriber.assertEmpty();
    subscriber.getProducer().request(count - 5);
    for (int i = 5;i < count; i++) {
      subscriber.assertItem(buffer("" + i));
    }
    subscriber.assertEmpty();
    stream.end();
    subscriber.assertCompleted().assertEmpty();
  }

  @Test
  public void testEndWhenPaused() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Observable<Buffer> observable = toObservable(stream);
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(5);
      }
    };
    observable.subscribe(subscriber);
    waitUntil(() -> subscriber.getProducer() != null);
    int count = 0;
    while (!stream.isPaused()) {
      stream.write(buffer("" + count));
      count++;
    }
    for (int i = 0;i < 5;i++) {
      subscriber.assertItem(buffer("" + i));
      stream.write(buffer("" + count));
      count++;
    }
    subscriber.assertEmpty();
    stream.end();
    subscriber.assertCompleted().assertEmpty();
  }
}
