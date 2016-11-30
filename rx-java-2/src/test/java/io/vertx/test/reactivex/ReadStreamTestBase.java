package io.vertx.test.reactivex;

import io.reactivex.Flowable;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.test.core.VertxTestBase;
import io.vertx.test.reactivex.stream.BufferReadStream;
import io.vertx.test.reactivex.stream.BufferReadStreamImpl;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class ReadStreamTestBase<B> extends VertxTestBase {

  protected abstract BufferReadStreamImpl stream();
  protected abstract Flowable<B> toObservable(BufferReadStreamImpl stream);
  protected abstract B buffer(String s);
  protected abstract String string(B buffer);

  @Test
  public void testReact() {
    BufferReadStreamImpl stream = stream();
    Flowable<B> observable = toObservable(stream);
    MySubscriber<B> subscriber = new MySubscriber<B>() {
      protected void assertEquals(Object expected, Object actual) {
        super.assertEquals(string((B) expected), string((B) actual));
      }
    };
    subscriber.subscribeTo(observable);
    stream.write(Buffer.buffer("foo"));
    subscriber.assertItem(buffer("foo")).assertEmpty();
    stream.write(Buffer.buffer("bar"));
    subscriber.assertItem(buffer("bar")).assertEmpty();
    stream.end();
    subscriber.assertCompleted().assertEmpty();
    assertFalse(subscriber.isSubscribed());
  }

  @Test
  public void testConcat() {
    vertx.runOnContext(v -> {
      BufferReadStreamImpl stream1 = stream();
      BufferReadStreamImpl stream2 = stream();
      Flowable<B> observable1 = toObservable(stream1);
      Flowable<B> observable2 = toObservable(stream2);
      Flowable<B> observable = Flowable.concat(observable1, observable2);
      MySubscriber<B> observer = new MySubscriber<B>() {
        public void onNext(B next) {
          switch (string(next)) {
            case "item1":
              stream1.end();
              stream2.write(Buffer.buffer("item2"));
              break;
            case "item2":
              stream2.end();
              break;
            default:
              fail();
          }
        }
        public void onError(Throwable e) {
          e.printStackTrace();
          fail();
        }
        public void onComplete() {
          testComplete();
        }
      };
      observer.subscribeTo(observable);
      stream1.write(Buffer.buffer("item1"));
    });
    await();
  }

/*
  @Test
  public void testOnSubscribeHandlerIsSetLast() {
    AtomicBoolean called = new AtomicBoolean();
    BufferReadStreamImpl stream = stream() {
      public BufferReadStreamImpl handler(Handler<Buffer> handler) {
        assertNotNull(endHandler());
        assertNotNull(exceptionHandler());
        called.set(true);
        return super.handler(handler);
      }
    };
    Flowable<B> observable = toObservable(stream);
    observable.subscribe(s -> {
    }, err -> {
    }, () -> {
    });
    assertTrue(called.get());
  }
*/

  @Test
  public void testBackPressureBuffer() {
    BufferReadStreamImpl stream = stream();
    Flowable<B> observable = toObservable(stream);
    MySubscriber<B> subscriber = new MySubscriber<B>() {
      protected void onSubscribe() {
        request(5);
      }
      protected void assertEquals(Object expected, Object actual) {
        super.assertEquals(string((B) expected), string((B) actual));
      }
    };
    subscriber.subscribeTo(observable);
    waitUntil(subscriber::isSubscribed);
    int count = 0;
    while (!stream.paused()) {
      stream.write(Buffer.buffer("" + count));
      count++;
    }
    for (int i = 0;i < 5;i++) {
      subscriber.assertItem(buffer("" + i));
      stream.write(Buffer.buffer("" + count));
      count++;
    }
    subscriber.assertEmpty();
    subscriber.request(count - 5);
    for (int i = 5;i < count; i++) {
      subscriber.assertItem(buffer("" + i));
    }
    subscriber.assertEmpty();
    stream.end();
    subscriber.assertCompleted().assertEmpty();
  }

/*
  @Test
  public void testEndWhenPaused() {
    BufferReadStreamImpl stream = stream();
    Flowable<B> observable = toObservable(stream);
    MySubscriber<B> subscriber = new MySubscriber<B>() {
      protected void onSubscribe() {
        request(5);
      }
      protected void assertEquals(Object expected, Object actual) {
        super.assertEquals(string((B) expected), string((B) actual));
      }
    };
    subscriber.subscribeTo(observable);
    waitUntil(subscriber::isSubscribed);
    int count = 0;
    while (!stream.paused()) {
      stream.write(Buffer.buffer("" + count));
      count++;
    }
    for (int i = 0;i < 5;i++) {
      subscriber.assertItem(buffer("" + i));
      stream.write(Buffer.buffer("" + count));
      count++;
    }
    subscriber.assertEmpty();
    stream.end();
    subscriber.assertCompleted().assertEmpty();
  }
*/

  @Test
  public void testChained() throws Exception {
    BufferReadStreamImpl stream = stream();
    Flowable<B> observable = toObservable(stream);
    AtomicBoolean subscribed = new AtomicBoolean();
    observable.subscribe(new Subscriber<B>() {
      final LinkedList<B> events = new LinkedList<>();
      public void onSubscribe(Subscription s) {
        s.request(1);
        subscribed.set(true);
      }
      public void onNext(B buffer) {
        events.add(buffer);
      }
      public void onError(Throwable e) {
        fail();
      }
      public void onComplete() {
        assertEquals(1, events.size());
        testComplete();
      }
    });
    waitUntil(subscribed::get);
    stream.write(Buffer.buffer("foo"));
    stream.end();
    await();
  }

  @Test
  public void testFlatMap() {
    BufferReadStreamImpl stream1 = stream();
    Flowable<B> obs1 = toObservable(stream1);
    BufferReadStreamImpl stream2 = stream();
    Flowable<B> obs2 = toObservable(stream2);
    obs1.flatMap(s -> obs2).subscribe(new Subscriber<B>() {
      final LinkedList<B> events = new LinkedList<>();
      public void onSubscribe(Subscription s) {
        s.request(1);
      }
      public void onNext(B buffer) {
        events.add(buffer);
      }
      public void onError(Throwable e) {
        fail();
      }
      public void onComplete() {
        assertEquals(1, events.size());
        testComplete();
      }
    });
    stream1.write(Buffer.buffer("foo"));
    stream1.end();
    stream2.write(Buffer.buffer("bar"));
    stream2.end();
    await();
  }

  @Test
  public void testCancelWhenSubscribedPropagatesToStream() {
    Buffer expected = Buffer.buffer("something");
    BufferReadStreamImpl stream = stream();
    Flowable<B> observable = toObservable(stream);
    observable.subscribe(new Subscriber<B>() {
      private Subscription sub;
      public void onSubscribe(Subscription s) {
        sub = s;
        s.request(1);
      }
      public void onNext(B b) {
        assertSame(b, expected);
        sub.cancel();
        assertNull(stream.handler());
        testComplete();
      }
      public void onError(Throwable t) {
        fail();
      }
      public void onComplete() {
        System.out.println("zoeijfoizjefoizjfeoij");
      }
    });
    stream.write(expected);
    await();
  }

  @Test
  public void testPausedInitially1() throws Exception {
    BufferReadStreamImpl stream = stream();
    assertFalse(stream.paused());
    Flowable<B> observable = toObservable(stream);
    assertTrue(stream.paused());
    CountDownLatch latch = new CountDownLatch(1);
    observable.subscribe(new Subscriber<B>() {
      LinkedList<B> events;
      @Override
      public void onSubscribe(Subscription s) {
        assertTrue(stream.paused());
        events = new LinkedList<>();
        s.request(1);
        assertFalse(stream.paused());
        latch.countDown();
      }
      @Override
      public void onNext(B b) {
        events.add(b);
      }

      @Override
      public void onError(Throwable t) {
        fail(t);
      }

      @Override
      public void onComplete() {
        assertEquals(buffer("item"), events.getFirst());
        assertEquals(1, events.size());
        testComplete();
      }
    });
    awaitLatch(latch);
    assertFalse(stream.paused());
    stream.write(Buffer.buffer("item"));
    waitUntil(stream::paused);
    stream.end();
    await();
  }

  @Test
  public void testPausedInitially2() {
    BufferReadStreamImpl stream = stream();
    stream.write(Buffer.buffer("item"));
    stream.end();
    assertFalse(stream.paused());
    Flowable<B> observable = toObservable(stream);
    assertTrue(stream.paused());
    observable.subscribe(new Subscriber<B>() {
      LinkedList<B> events;
      @Override
      public void onSubscribe(Subscription s) {
        assertTrue(stream.paused());
        events = new LinkedList<>();
        s.request(1);
        waitUntil(stream::paused);
      }
      @Override
      public void onNext(B b) {
        events.add(b);
      }

      @Override
      public void onError(Throwable t) {
        fail(t);
      }

      @Override
      public void onComplete() {
        assertEquals(buffer("item"), events.getFirst());
        assertEquals(1, events.size());
        testComplete();
      }
    });
    await();
  }
}