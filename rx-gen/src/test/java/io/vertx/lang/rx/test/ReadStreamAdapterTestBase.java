package io.vertx.lang.rx.test;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class ReadStreamAdapterTestBase<B, O> extends VertxTestBase {

  protected abstract O toObservable(ReadStream<Buffer> stream);
  protected abstract B buffer(String s);
  protected abstract String string(B buffer);
  protected abstract void subscribe(O obs, TestSubscriber<B> sub);
  protected abstract O concat(O obs1, O obs2);

  @Test
  public void testReact() {
    TestReadStream<Buffer> stream = new TestReadStream<>();
    O observable = toObservable(stream);
    TestSubscriber<B> subscriber = new TestSubscriber<B>() {
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
    TestReadStream<Buffer> stream1 = new TestReadStream<>();
    TestReadStream<Buffer> stream2 = new TestReadStream<>();
    O observable1 = toObservable(stream1);
    O observable2 = toObservable(stream2);
    O observable = concat(observable1, observable2);
    TestSubscriber<B> observer = new TestSubscriber<B>() {
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
    TestReadStream<Buffer> stream = new TestReadStream<Buffer>() {
      @Override
      public TestReadStream<Buffer> handler(Handler<Buffer> handler) {
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
    TestSubscriber<B> subscriber = new TestSubscriber<>();
    subscribe(observable, subscriber);
    subscriber.unsubscribe();
  }

  @Test
  public void testOnSubscribeHandlerIsSetLast() {
    TestReadStream<Buffer> stream = new TestReadStream<Buffer>() {
      @Override
      public TestReadStream<Buffer> handler(Handler<Buffer> handler) {
        assertHasExceptionHandler();
        assertHasEndHandler();
        return super.handler(handler);
      }
    };
    O observable = toObservable(stream);
    TestSubscriber<B> subscriber = new TestSubscriber<>();
    subscribe(observable, subscriber);
  }

  @Test
  public void testHandlers() {
    TestReadStream<Buffer> stream = new TestReadStream<>();
    O observable = toObservable(stream);
    TestSubscriber<B> subscriber = new TestSubscriber<>();
    subscribe(observable, subscriber);
    stream.assertHasHandlers();
    subscriber.unsubscribe();
    stream.assertHasNoHandlers();
  }
}
