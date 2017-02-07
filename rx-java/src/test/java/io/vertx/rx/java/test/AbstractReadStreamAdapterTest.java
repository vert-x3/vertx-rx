package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import io.vertx.rx.java.test.support.SimpleSubscriber;
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
    SimpleSubscriber<B> subscriber = new SimpleSubscriber<B>() {
      @Override
      protected void assertEquals(Object expected, Object actual) {
        super.assertEquals(string((B) expected), string((B) actual));
      }
    };
    Subscription subscription = observable.subscribe(subscriber);
    stream.assertHasHandlers();
    stream.emit(Buffer.buffer("foo"));
    subscriber.assertItem(buffer("foo")).assertEmpty();
    stream.emit(Buffer.buffer("bar"));
    subscriber.assertItem(buffer("bar")).assertEmpty();
    stream.end();
    subscriber.assertCompleted().assertEmpty();
    assertTrue(subscription.isUnsubscribed());
    testComplete();
  }

  @Test
  public void testConcat() {
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
            stream1.assertHasItemHandler();
            stream2.assertHasNoItemHandler();
            stream1.end();
            break;
          case "item2":
            stream1.assertHasNoItemHandler();
            stream2.assertHasItemHandler();
            stream2.end();
            break;
          default:
            fail();
        }
      }
      @Override
      public void onError(Throwable e) {
        fail();
      }
      @Override
      public void onCompleted() {
        testComplete();
      }
    };
    observable.subscribe(observer);
    stream1.emit(Buffer.buffer("item1"));
    stream1.assertHasNoItemHandler();
    stream2.emit(Buffer.buffer("item2"));
    stream2.assertHasNoItemHandler();
    await();
  }

  @Test
  public void testDataHandlerShouldBeSetAndUnsetAfterOtherHandlers() {
    BufferReadStreamImpl stream = new BufferReadStreamImpl() {
      @Override
      public BufferReadStreamImpl handler(Handler<Buffer> handler) {
        if (handler == null) {
          assertHasNoExceptionHandler();
          assertHasNoEndHandler();
        } else {
          assertHasExceptionHandler();
          assertHasEndHandler();
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
        assertHasExceptionHandler();
        assertHasEndHandler();
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
    stream.assertHasHandlers();
    subscription.unsubscribe();
    stream.assertHasNoHandlers();
  }
}
