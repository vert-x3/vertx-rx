package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;
import io.vertx.rx.java.test.support.SimpleReadStream;
import io.vertx.rx.java.test.support.SimpleSubscriber;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class ReadStreamAdapterTestBase<B, O> extends VertxTestBase {

  protected abstract O toObservable(ReadStream<Buffer> stream);
  protected abstract B buffer(String s);
  protected abstract String string(B buffer);
  protected abstract void subscribe(O obs, SimpleSubscriber<B> sub);
  protected abstract O concat(O obs1, O obs2);

  @Test
  public void testReact() {
    SimpleReadStream<Buffer> stream = new SimpleReadStream<>();
    O observable = toObservable(stream);
    SimpleSubscriber<B> subscriber = new SimpleSubscriber<B>() {
      @Override
      protected void assertEquals(Object expected, Object actual) {
        super.assertEquals(string((B) expected), string((B) actual));
      }
    };
    subscribe(observable, subscriber);
    stream.assertHasHandlers();
    stream.emit(Buffer.buffer("foo"));
    subscriber.assertItem(buffer("foo")).assertEmpty();
    stream.emit(Buffer.buffer("bar"));
    subscriber.assertItem(buffer("bar")).assertEmpty();
    stream.end();
    subscriber.assertCompleted().assertEmpty();
    assertTrue(subscriber.isUnsubscribed());
    testComplete();
  }

  @Test
  public void testConcat() {
    SimpleReadStream<Buffer> stream1 = new SimpleReadStream<>();
    SimpleReadStream<Buffer> stream2 = new SimpleReadStream<>();
    O observable1 = toObservable(stream1);
    O observable2 = toObservable(stream2);
    O observable = concat(observable1, observable2);
    SimpleSubscriber<B> observer = new SimpleSubscriber<B>() {
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
        super.onError(e);
        fail();
      }
      @Override
      public void onCompleted() {
        super.onCompleted();
        testComplete();
      }
    };
    subscribe(observable, observer);
    stream1.emit(Buffer.buffer("item1"));
    stream1.assertHasNoItemHandler();
    stream2.emit(Buffer.buffer("item2"));
    assertTrue(observer.isUnsubscribed());
    stream2.assertHasNoItemHandler();
    await();
  }

  @Test
  public void testDataHandlerShouldBeSetAndUnsetAfterOtherHandlers() {
    SimpleReadStream<Buffer> stream = new SimpleReadStream<Buffer>() {
      @Override
      public SimpleReadStream<Buffer> handler(Handler<Buffer> handler) {
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
    O observable = toObservable(stream);
    SimpleSubscriber<B> subscriber = new SimpleSubscriber<>();
    subscribe(observable, subscriber);
    subscriber.unsubscribe();
  }

  @Test
  public void testOnSubscribeHandlerIsSetLast() {
    SimpleReadStream<Buffer> stream = new SimpleReadStream<Buffer>() {
      @Override
      public SimpleReadStream<Buffer> handler(Handler<Buffer> handler) {
        assertHasExceptionHandler();
        assertHasEndHandler();
        return super.handler(handler);
      }
    };
    O observable = toObservable(stream);
    SimpleSubscriber<B> subscriber = new SimpleSubscriber<>();
    subscribe(observable, subscriber);
  }

  @Test
  public void testHandlers() {
    SimpleReadStream<Buffer> stream = new SimpleReadStream<>();
    O observable = toObservable(stream);
    SimpleSubscriber<B> subscriber = new SimpleSubscriber<>();
    subscribe(observable, subscriber);
    stream.assertHasHandlers();
    subscriber.unsubscribe();
    stream.assertHasNoHandlers();
  }
}
