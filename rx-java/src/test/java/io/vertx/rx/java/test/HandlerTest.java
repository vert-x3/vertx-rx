package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.rx.java.ObservableHandler;
import io.vertx.rx.java.RxHelper;
import org.junit.Test;
import rx.Subscription;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HandlerTest {

  @Test
  public void testNotifyBeforeSubscribe() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    o.toHandler().handle("abc");
    MySubscriber<String> subscriber = new MySubscriber<>();
    o.subscribe(subscriber);
    subscriber.assertEmpty();
  }

  @Test
  public void testNotifyAfterSubscribe() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    MySubscriber<String> subscriber = new MySubscriber<>();
    o.subscribe(subscriber);
    subscriber.assertEmpty();
    o.toHandler().handle("abc");
    subscriber.assertItem("abc").assertEmpty();
  }

  @Test
  public void testUnsubscribeBeforeNotify() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    MySubscriber<String> subscriber = new MySubscriber<>();
    Subscription sub = o.subscribe(subscriber);
    sub.unsubscribe();
    assertTrue(sub.isUnsubscribed());
    subscriber.assertCompleted().assertEmpty();
    o.toHandler().handle("abc");
    subscriber.assertEmpty();
  }

  @Test
  public void testNotifyTwice() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    MySubscriber<String> subscriber = new MySubscriber<>();
    o.subscribe(subscriber);
    o.toHandler().handle("abc");
    o.toHandler().handle("def");
    subscriber.assertItem("abc").assertItem("def").assertEmpty();
  }

  @Test
  public void testFulfillAdaptedSubscriber() {
    MySubscriber<String> subscriber = new MySubscriber<>();
    Handler<String> o = RxHelper.toHandler(subscriber);
    o.handle("abc");
    subscriber.assertItem("abc").assertEmpty();
  }

  @Test
  public void testFulfillAdaptedFunctions1() {
    MySubscriber<String> subscriber = new MySubscriber<>();
    Handler<String> o = RxHelper.toHandler(subscriber::onNext);
    o.handle("abc");
    subscriber.assertItem("abc").assertEmpty();
  }
}
