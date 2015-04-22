package io.vertx.rx.java.test;

import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.ReadStreamAdapter;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import org.junit.Test;
import rx.Observable;
import rx.Producer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BackPressureTest extends AbstractReadStreamAdapterTest<Buffer> {


  @Override
  protected Observable<Buffer> toObservable(BufferReadStreamImpl stream) {
    return Observable.create(new ReadStreamAdapter<>(stream));
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
    AtomicInteger paused = new AtomicInteger();
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl pause() {
        paused.incrementAndGet();
        return this;
      }
      @Override
      public BufferReadStreamImpl resume() {
        paused.decrementAndGet();
        return this;
      }
    };
    Observable<Buffer> observable = toObservable(stream);
    AtomicReference<Producer> producer = new AtomicReference<>();
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
      @Override
      public void setProducer(Producer p) {
        super.setProducer(p);
        producer.set(p);
      }
    };
    observable.subscribe(subscriber);
    subscriber.assertEmpty();
    assertEquals(1, paused.get());
    stream.handler.handle(buffer("0"));
    assertEquals(1, paused.get());
    subscriber.assertEmpty();
    producer.get().request(1);
    subscriber.assertItem(buffer("0")).assertEmpty();
    assertEquals(1, paused.get());
  }

  @Test
  public void testNoPauseWhenRequestingOne() {
    AtomicInteger pauses = new AtomicInteger();
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl pause() {
        pauses.incrementAndGet();
        return this;
      }
    };
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(1);
      }
      @Override
      public void onNext(Buffer buffer) {
        super.onNext(buffer);
        request(1);
      }
    };
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    stream.handler.handle(buffer("0"));
    stream.handler.handle(buffer("1"));
    stream.handler.handle(buffer("2"));
    stream.endHandler.handle(null);
    assertEquals(0, pauses.get());
  }

  @Test
  public void testUnsubscribeFromBufferedDeliveredWhileRequesting() {
    for (int i = 1;i <= 3;i++) {
      BufferReadStreamImpl stream = new BufferReadStreamImpl() {
        @Override
        public BufferReadStreamImpl pause() {
          return this;
        }
        @Override
        public BufferReadStreamImpl resume() {
          return this;
        }
      };
      MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
        @Override
        public void onStart() {
          request(0);
        }
        @Override
        public void onNext(Buffer buffer) {
          super.onNext(buffer);
          unsubscribe();
        }
      };
      Observable<Buffer> observable = toObservable(stream);
      observable.subscribe(subscriber);
      stream.handler.handle(buffer("0"));
      stream.handler.handle(buffer("1"));
      subscriber.getProducer().request(i);
      subscriber.assertItem(Buffer.buffer("0")).assertCompleted().assertEmpty();
    }
  }

  @Test
  public void testNoResumeWhenRequestingBuffered() {
    AtomicBoolean resumed = new AtomicBoolean();
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl pause() {
        return this;
      }
      @Override
      public BufferReadStreamImpl resume() {
        resumed.set(true);
        return this;
      }
    };
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
    };
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    stream.handler.handle(buffer("0"));
    subscriber.getProducer().request(1);
    assertEquals(false, resumed.get());
  }

  @Test
  public void testEndDuringRequestResume() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl pause() {
        return this;
      }
      @Override
      public BufferReadStreamImpl resume() {
        endHandler.handle(null);
        return this;
      }
    };
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
    };
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    subscriber.getProducer().request(1);
    subscriber.assertCompleted().assertEmpty();
  }

}
