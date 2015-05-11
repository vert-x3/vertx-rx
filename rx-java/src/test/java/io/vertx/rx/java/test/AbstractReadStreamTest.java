package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

import java.util.LinkedList;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class AbstractReadStreamTest<B> extends VertxTestBase {

  
  protected abstract Observable<B> toObservable(BufferReadStreamImpl stream);
  protected abstract B buffer(String s);
  protected abstract String string(B buffer);

  @Test
  public void testReact() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Observable<B> observable = toObservable(stream);
    MySubscriber<B> subscriber = new MySubscriber<B>() {
      @Override
      protected void assertEquals(Object expected, Object actual) {
        super.assertEquals(string((B) expected), string((B) actual));
      }
    };
    Subscription subscription = observable.subscribe(subscriber);
    stream.write(Buffer.buffer("foo"));
    subscriber.assertItem(buffer("foo")).assertEmpty();
    stream.write(Buffer.buffer("bar"));
    subscriber.assertItem(buffer("bar")).assertEmpty();
    stream.end();
    subscriber.assertCompleted().assertEmpty();
    assertTrue(subscription.isUnsubscribed());
    testComplete();
  }

  @Test
  public void testConcat() {
    vertx.runOnContext(v -> {
      BufferReadStreamImpl stream1 = new BufferReadStreamImpl();
      BufferReadStreamImpl stream2 = new BufferReadStreamImpl();
      Observable<B> observable1 = toObservable(stream1);
      Observable<B> observable2 = toObservable(stream2);
      Observable<B> observable = Observable.concat(observable1, observable2);
      Observer<B> observer = new Subscriber<B>() {
        @Override
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
        @Override
        public void onError(Throwable e) {
          e.printStackTrace();
          fail();
        }
        @Override
        public void onCompleted() {
          testComplete();
        }
      };
      observable.subscribe(observer);
      stream1.write(Buffer.buffer("item1"));
    });
    await();
  }

  @Test
  public void testOnSubscribeHandlerIsSetLast() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl handler(Handler<Buffer> handler) {
        assertNotNull(getEndHandler());
        assertNotNull(getExceptionHandler());
        return super.handler(handler);
      }
    };
    Observable<B> observable = toObservable(stream);
    observable.subscribe(s -> {
    }, err -> {
    }, () -> {
    });
  }

  @Test
  public void testBackPressureBuffer() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Observable<B> observable = toObservable(stream);
    MySubscriber<B> subscriber = new MySubscriber<B>() {
      @Override
      public void onStart() {
        request(5);
      }
      protected void assertEquals(Object expected, Object actual) {
        super.assertEquals(string((B) expected), string((B) actual));
      }
    };
    observable.subscribe(subscriber);
    waitUntil(() -> subscriber.getProducer() != null);
    int count = 0;
    while (!stream.isPaused()) {
      stream.write(Buffer.buffer("" + count));
      count++;
    }
    for (int i = 0;i < 5;i++) {
      subscriber.assertItem(buffer("" + i));
      stream.write(Buffer.buffer("" + count));
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
    Observable<B> observable = toObservable(stream);
    MySubscriber<B> subscriber = new MySubscriber<B>() {
      @Override
      public void onStart() {
        request(5);
      }
      protected void assertEquals(Object expected, Object actual) {
        super.assertEquals(string((B) expected), string((B) actual));
      }
    };
    observable.subscribe(subscriber);
    waitUntil(() -> subscriber.getProducer() != null);
    int count = 0;
    while (!stream.isPaused()) {
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

  @Test
  public void testChained() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Observable<B> observable = toObservable(stream);
    observable.subscribe(new Subscriber<B>() {
      final LinkedList<B> events = new LinkedList<>();
      @Override
      public void onNext(B buffer) {
        events.add(buffer);
      }
      @Override
      public void onError(Throwable e) {
        fail();
      }
      @Override
      public void onCompleted() {
        assertEquals(1, events.size());
        testComplete();
      }
    });
    stream.write(Buffer.buffer("foo"));
    stream.end();
    await();
  }

/*
  @Test
  public void testFlatMap() {
    BufferReadStreamImpl stream1 = new BufferReadStreamImpl();
    stream1.write(Buffer.buffer("foo"));
    stream1.end();
    Observable<Buffer> obs1 = toObservable(stream1);
    BufferReadStreamImpl stream2 = new BufferReadStreamImpl();
    stream2.write(Buffer.buffer("bar"));
    stream2.end();
    Observable<Buffer> obs2 = toObservable(stream1);
    obs1.flatMap(s -> obs2).subscribe(new Subscriber<Buffer>() {
      final LinkedList<Buffer> events = new LinkedList<>();
      @Override
      public void onNext(Buffer buffer) {
        events.add(buffer);
      }
      @Override
      public void onError(Throwable e) {
        fail();
      }
      @Override
      public void onCompleted() {
        assertEquals(1, events.size());
        testComplete();
      }
    });
    await();
  }
*/
}
