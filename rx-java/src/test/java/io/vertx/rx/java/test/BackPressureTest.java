package io.vertx.rx.java.test;

import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.ReadStreamAdapter;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(0);
      }
    };
    observable.subscribe(subscriber);
    subscriber.assertEmpty();
    assertEquals(1, paused.get());
    stream.handler.handle(buffer("0"));
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

  @Test
  public void testDisableBackPressure() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    ReadStreamAdapter<Buffer> adapter = new ReadStreamAdapter<>(stream);
    Observable<Buffer> observable = Observable.create(adapter);
    MySubscriber<Buffer> subscriber = new MySubscriber<>();
    observable.subscribe(subscriber);
    assertEquals(Long.MAX_VALUE, adapter.getExpected());
    stream.handler.handle(buffer("0"));
    assertEquals(Long.MAX_VALUE, adapter.getExpected());
  }

  @Test
  public void testImplicitBackPressureActivation() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    ReadStreamAdapter<Buffer> adapter = new ReadStreamAdapter<>(stream);
    Observable<Buffer> observable = Observable.create(adapter);
    MySubscriber<Buffer> subscriber = new MySubscriber<Buffer>() {
      @Override
      public void onStart() {
        request(Long.MAX_VALUE - 1);
      }
      @Override
      public void onNext(Buffer o) {
        super.onNext(o);
        request(Long.MAX_VALUE + 1 - (Long.MAX_VALUE - 1 - 1));
      }
    };
    observable.subscribe(subscriber);
    assertEquals(Long.MAX_VALUE - 1, adapter.getExpected());
    stream.handler.handle(buffer("0"));
    assertEquals(Long.MAX_VALUE, adapter.getExpected());
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
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    subscriber.getProducer().request(1);
    subscriber.assertItem(buffer("0")).assertEmpty();
  }

  @Test
  public void testEndDuringResume() {
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
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    subscriber.getProducer().request(1);
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
        handler.handle(buffer("0"));
        assertEquals(2, pauses.get());
        handler.handle(buffer("1"));
        return this;
      }
    };
    Observable<Buffer> observable = toObservable(stream);
    observable.subscribe(subscriber);
    subscriber.getProducer().request(1);
    subscriber.assertItem(buffer("0")).assertEmpty();
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
    assertEquals(1, pauses.get());
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
    stream.handler.handle(buffer("0"));
    stream.handler.handle(buffer("1"));
    stream.endHandler.handle(null);
    subscriber.getProducer().request(1);
    assertEquals(1, pauses.get());
    subscriber.assertItem(buffer("0")).assertEmpty();
  }
}
