package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.rx.java.RxHelper;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamTest extends VertxTestBase {

  @Test
  public void testReact() {
    SimpleReadStream<String> rs = new SimpleReadStream<>();
    Observable<String> o = RxHelper.toObservable(rs);
    MySubscriber<String> subscriber = new MySubscriber<>();
    Subscription sub = o.subscribe(subscriber);
    assertNotNull(rs.endHandler);
    assertNotNull(rs.endHandler);
    assertNotNull(rs.handler);
    rs.handler.handle("foo");
    subscriber.assertItem("foo").assertEmpty();
    rs.handler.handle("bar");
    subscriber.assertItem("bar").assertEmpty();
    rs.endHandler.handle(null);
    subscriber.assertCompleted().assertEmpty();
    assertTrue(sub.isUnsubscribed());
    testComplete();
  }

  @Test
  public void testConcat() {
    SimpleReadStream<String> s1 = new SimpleReadStream<>();
    SimpleReadStream<String> s2 = new SimpleReadStream<>();
    Observable<String> o1 = RxHelper.toObservable(s1);
    Observable<String> o2 = RxHelper.toObservable(s2);
    Observable<String> o = Observable.concat(o1, o2);
    Observer<String> observer = new Subscriber<String>() {
      @Override
      public void onNext(String s) {
        switch (s) {
          case "item1":
            assertNotNull(s1.handler);
            assertNull(s2.handler);
            s1.endHandler.handle(null);
            s2.handler.handle("item2");
            break;
          case "item2":
            assertNotNull(s1.handler);
            assertNotNull(s2.handler);
            s2.endHandler.handle(null);
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
    o.subscribe(observer);
    s1.handler.handle("item1");
    await();
  }

  @Test
  public void testDataHandlerShouldBeSetAndUnsetAfterOtherHandlers() {
    SimpleReadStream<String> s1 = new SimpleReadStream<String>() {
      @Override
      public ReadStream<String> handler(Handler<String> handler) {
        if (handler == null) {
          assertNull(exceptionHandler);
          assertNull(endHandler);
        } else {
          assertNotNull(exceptionHandler);
          assertNotNull(endHandler);
        }
        return super.handler(handler);
      }
    };
    Observable<String> o1 = RxHelper.toObservable(s1);
    Subscription subscription = o1.subscribe(item -> {});
    subscription.unsubscribe();
  }
}
