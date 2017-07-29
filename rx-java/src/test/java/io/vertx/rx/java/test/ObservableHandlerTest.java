package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.rx.java.ObservableHandler;
import io.vertx.rx.java.RxHelper;
import io.vertx.rx.java.test.support.SimpleSubscriber;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableHandlerTest {

  @Test
  public void testMultiNotifyBeforeSubscribe() {
    ObservableHandler<String> o = RxHelper.observableHandler(true);
    o.toHandler().handle("abc");
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    SimpleSubscriber.subscribe(o, subscriber);
    subscriber.assertEmpty();
  }

  @Test
  public void testSingleNotifyBeforeSubscribe() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    o.toHandler().handle("abc");
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    SimpleSubscriber.subscribe(o, subscriber);
    subscriber.assertCompleted().assertEmpty();
  }

  @Test
  public void testMultiNotifyAfterSubscribe() {
    ObservableHandler<String> o = RxHelper.observableHandler(true);
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    SimpleSubscriber.subscribe(o, subscriber);
    subscriber.assertEmpty();
    o.toHandler().handle("abc");
    subscriber.assertItem("abc").assertEmpty();
  }

  @Test
  public void testSingleNotifyAfterSubscribe() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    SimpleSubscriber.subscribe(o, subscriber);
    subscriber.assertEmpty();
    o.toHandler().handle("abc");
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testMultiUnsubscribeBeforeNotify() {
    ObservableHandler<String> o = RxHelper.observableHandler(true);
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    SimpleSubscriber.subscribe(o, subscriber);
    subscriber.unsubscribe();
    assertTrue(subscriber.isUnsubscribed());
    subscriber.assertEmpty();
    o.toHandler().handle("abc");
    subscriber.assertEmpty();
  }

  @Test
  public void testSingleUnsubscribeBeforeNotify() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    SimpleSubscriber.subscribe(o, subscriber);
    subscriber.unsubscribe();
    assertTrue(subscriber.isUnsubscribed());
    subscriber.assertEmpty();
    o.toHandler().handle("abc");
    subscriber.assertEmpty();
  }

  @Test
  public void testMultiNotifyTwice() {
    ObservableHandler<String> o = RxHelper.observableHandler(true);
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    SimpleSubscriber.subscribe(o, subscriber);
    o.toHandler().handle("abc");
    o.toHandler().handle("def");
    subscriber.assertItem("abc").assertItem("def").assertEmpty();
  }

  @Test
  public void testSingleNotifyTwice() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    SimpleSubscriber.subscribe(o, subscriber);
    o.toHandler().handle("abc");
    o.toHandler().handle("def");
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testMultiFulfillAdaptedSubscriber() {
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    Handler<String> o = RxHelper.toHandler(subscriber.toObserver(), true);
    o.handle("abc");
    subscriber.assertItem("abc").assertEmpty();
  }

  @Test
  public void testSingleFulfillAdaptedSubscriber() {
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    Handler<String> o = RxHelper.toHandler(subscriber.toObserver());
    o.handle("abc");
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testFulfillAdaptedFunctions1() {
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    Handler<String> o = RxHelper.toHandler(subscriber::onNext);
    o.handle("abc");
    subscriber.assertItem("abc").assertEmpty();
  }
}
