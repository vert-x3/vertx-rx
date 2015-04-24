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

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class AbstractReadStreamAdapterTest<B> extends VertxTestBase {

  
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
//      assertNotNull(stream.endHandler);
//      assertNotNull(stream.endHandler);
//      assertNotNull(stream.handler);
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
              assertNotNull(stream1.getDataHandler());
              assertNull(stream2.getDataHandler());
              stream1.end();
              stream2.write(Buffer.buffer("item2"));
              break;
            case "item2":
              assertNull(stream1.getDataHandler());
              assertNotNull(stream2.getDataHandler());
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
          assertNull(stream1.getDataHandler());
          assertNull(stream2.getDataHandler());
          testComplete();
        }
      };
      observable.subscribe(observer);
      stream1.write(Buffer.buffer("item1"));
    });
    await();
  }

  @Test
  public void testDataHandlerShouldBeSetAndUnsetAfterOtherHandlers() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl handler(Handler<Buffer> handler) {
        if (handler == null) {
          assertNull(getExceptionHandler());
          assertNull(getEndHandler());
        } else {
          assertNotNull(getExceptionHandler());
          assertNotNull(getEndHandler());
        }
        return super.handler(handler);
      }
    };
    Observable<B> observable = toObservable(stream);
    Subscription subscription = observable.subscribe(item -> {});
    subscription.unsubscribe();
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
  public void testHandlers() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl();
    Observable<B> observable = toObservable(stream);
    Subscription subscription = observable.subscribe(s -> {}, err -> {}, () -> {});
    assertNotNull(stream.getDataHandler());
    assertNotNull(stream.getExceptionHandler());
    assertNotNull(stream.getEndHandler());
    subscription.unsubscribe();
    assertNull(stream.getDataHandler());
    assertNull(stream.getExceptionHandler());
    assertNull(stream.getEndHandler());
  }
}
