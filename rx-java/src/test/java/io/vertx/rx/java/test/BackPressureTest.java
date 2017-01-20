package io.vertx.rx.java.test;

import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.ObservableReadStream;
import io.vertx.rx.java.RxHelper;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BackPressureTest extends AbstractReadStreamAdapterTest<Buffer> {

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
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
    };
    observable.subscribe(subscriber);
    subscriber.assertEmpty();
    assertEquals(0, paused.get());
    for (int i = 0; i < ObservableReadStream.DEFAULT_MAX_BUFFER_SIZE; i++) {
      stream.handler.handle(buffer("" + i));
    }
    assertEquals(1, paused.get());
    subscriber.assertEmpty();
    subscriber.getProducer().request(1);
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
      subscriber.assertItem(Buffer.buffer("0")).assertEmpty();
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
    int num = 10;
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
    Observable<Buffer> observable = toObservable(stream, num);
    observable.subscribe(subscriber);
    for (int i = 0;i < num;i++) {
      stream.handler.handle(Buffer.buffer("" + i));
    }
    subscriber.getProducer().request(num);
    for (int i = 0;i < num;i++) {
      subscriber.assertItem(Buffer.buffer("" + i));
    }
    subscriber.assertCompleted().assertEmpty();
  }

  @Test
  public void testDisableBackPressure() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    ObservableReadStream<Buffer, Buffer> adapter = new ObservableReadStream<>(stream, Function.identity());
    Observable<Buffer> observable = Observable.create(adapter);
    MySubscriber<Buffer> subscriber = new MySubscriber<>();
    observable.subscribe(subscriber);
    assertEquals(Long.MAX_VALUE, adapter.getRequested());
    stream.handler.handle(buffer("0"));
    assertEquals(Long.MAX_VALUE, adapter.getRequested());
  }

  @Test
  public void testImplicitBackPressureActivation() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    ObservableReadStream<Buffer, Buffer> adapter = new ObservableReadStream<Buffer, Buffer>(stream, Function.identity());
    Observable<Buffer> observable = Observable.create(adapter);
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(Long.MAX_VALUE - 1);
      }
      @Override
      public void onNext(Buffer o) {
        super.onNext(o);
        request(2);
      }
    };
    observable.subscribe(subscriber);
    assertEquals(Long.MAX_VALUE - 1, adapter.getRequested());
    stream.handler.handle(buffer("0"));
    assertEquals(Long.MAX_VALUE, adapter.getRequested());
  }

  @Test
  public void testDeliverEventWhenPaused() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl pause() {
        return this;
      }
    };
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(1);
      }
    };
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    stream.handler.handle(buffer("0")); // We send an event even though we are paused
    stream.endHandler.handle(null);
    subscriber.getProducer().request(1);
    subscriber.assertItem(buffer("0")).assertCompleted().assertEmpty();
  }

  @Test
  public void testEndWhenPaused() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl pause() {
        return this;
      }
    };
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(1);
      }
    };
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    stream.endHandler.handle(null);
    subscriber.assertCompleted().assertEmpty();
  }

  @Test
  public void testRequestDuringOnNext() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
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
    subscriber.assertItem(buffer("0")).assertEmpty();
    stream.handler.handle(buffer("1"));
    subscriber.assertItem(buffer("1")).assertEmpty();
    stream.handler.handle(buffer("2"));
    subscriber.assertItem(buffer("2")).assertEmpty();
    stream.endHandler.handle(null);
    subscriber.assertCompleted().assertEmpty();
  }

  @Test
  public void testDeliverDuringResume() {
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
    };
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl pause() {
        return this;
      }
      @Override
      public BufferReadStreamImpl resume() {
        handler.handle(buffer("0"));
        return this;
      }
    };
    Observable<Buffer> observable = toObservable(stream, 2);
    observable.subscribe(subscriber);
    stream.handler.handle(Buffer.buffer("0"));
    subscriber.getProducer().request(1);
    subscriber.assertItem(buffer("0")).assertEmpty();
  }

  @Test
  public void testEndDuringResume() {
    int num = 4;
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
    };
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
    Observable<Buffer> observable = toObservable(stream, num);
    observable.subscribe(subscriber);
    for (int i = 0;i < num;i++) {
      stream.handler.handle(Buffer.buffer("" + i));
    }
    subscriber.getProducer().request(num);
    for (int i = 0;i < num;i++) {
      subscriber.assertItem(Buffer.buffer("" + i));
    }
    subscriber.assertCompleted().assertEmpty();
  }

  @Test
  public void testBufferDuringResume() {
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
    };
    AtomicInteger pauses = new AtomicInteger();
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl pause() {
        pauses.incrementAndGet();
        return this;
      }
      @Override
      public BufferReadStreamImpl resume() {
        assertEquals(1, pauses.get());
        handler.handle(buffer("2"));
        handler.handle(buffer("3"));
        assertEquals(1, pauses.get());
        return this;
      }
    };
    Observable<Buffer> observable = toObservable(stream, 2);
    observable.subscribe(subscriber);
    stream.handler.handle(buffer("0"));
    stream.handler.handle(buffer("1"));
    subscriber.getProducer().request(2);
    subscriber.assertItem(buffer("0")).assertItem(buffer("1")).assertEmpty();
  }

  @Test
  public void testFoo() {
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
    };
    AtomicInteger pauses = new AtomicInteger();
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl pause() {
        pauses.incrementAndGet();
        return this;
      }
    };
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    stream.handler.handle(buffer("0"));
    stream.endHandler.handle(null);
    subscriber.getProducer().request(1);
    subscriber.assertItem(buffer("0")).assertCompleted().assertEmpty();
  }

  @Test
  public void testBar() {
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
    };
    AtomicInteger pauses = new AtomicInteger();
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl pause() {
        pauses.incrementAndGet();
        return this;
      }
    };
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    for (int i = 0; i < ObservableReadStream.DEFAULT_MAX_BUFFER_SIZE; i++) {
      stream.handler.handle(buffer("" + i));
    }
    assertEquals(1, pauses.get());
    stream.endHandler.handle(null);
    subscriber.getProducer().request(1);
    assertEquals(1, pauses.get());
    subscriber.assertItem(buffer("0")).assertEmpty();
  }

  @Test
  public void testUnsubscribeDuringOnNext() {
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onNext(Buffer buffer) {
        super.onNext(buffer);
        unsubscribe();
      }
    };
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    stream.handler.handle(buffer("0"));
  }

  @Test
  public void testResubscribe() {
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
    };
    AtomicInteger resumed = new AtomicInteger();
    AtomicInteger paused = new AtomicInteger();
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl resume() {
        resumed.getAndIncrement();
        return this;
      }
      @Override
      public BufferReadStreamImpl pause() {
        paused.getAndIncrement();
        return this;
      }
    };
    Observable<Buffer> observable = toObservable(stream, 2);
    Subscription sub = observable.subscribe(subscriber);
    stream.handler.handle(buffer("0"));
    stream.handler.handle(buffer("1"));
    assertEquals(0, resumed.get());
    assertEquals(1, paused.get());
    sub.unsubscribe();
    assertEquals(1, resumed.get());
    assertEquals(1, paused.get());
    subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
    };
    sub = observable.subscribe(subscriber);
    assertEquals(1, resumed.get());
    assertEquals(1, paused.get());
    stream.handler.handle(buffer("2"));
    stream.handler.handle(buffer("3"));
    assertEquals(1, resumed.get());
    assertEquals(2, paused.get());
  }
}
