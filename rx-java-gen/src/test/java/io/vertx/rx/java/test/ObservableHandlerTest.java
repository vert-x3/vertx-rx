package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.lang.rx.test.TestSubscriber;
import io.vertx.rx.java.ObservableHandler;
import io.vertx.rx.java.RxHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableHandlerTest {

  @Test
  public void testMultiNotifyBeforeSubscribe() {
    ObservableHandler<String> o = RxHelper.observableHandler(true);
    o.toHandler().handle("abc");
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    TestUtils.subscribe(o, subscriber);
    subscriber.assertEmpty();
  }

  @Test
  public void testSingleNotifyBeforeSubscribe() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    o.toHandler().handle("abc");
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    TestUtils.subscribe(o, subscriber);
    subscriber.assertCompleted().assertEmpty();
  }

  @Test
  public void testMultiNotifyAfterSubscribe() {
    ObservableHandler<String> o = RxHelper.observableHandler(true);
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    TestUtils.subscribe(o, subscriber);
    subscriber.assertEmpty();
    o.toHandler().handle("abc");
    subscriber.assertItem("abc").assertEmpty();
  }

  @Test
  public void testSingleNotifyAfterSubscribe() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    TestUtils.subscribe(o, subscriber);
    subscriber.assertEmpty();
    o.toHandler().handle("abc");
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testMultiUnsubscribeBeforeNotify() {
    ObservableHandler<String> o = RxHelper.observableHandler(true);
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    TestUtils.subscribe(o, subscriber);
    subscriber.unsubscribe();
    assertTrue(subscriber.isUnsubscribed());
    subscriber.assertEmpty();
    o.toHandler().handle("abc");
    subscriber.assertEmpty();
  }

  @Test
  public void testSingleUnsubscribeBeforeNotify() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    TestUtils.subscribe(o, subscriber);
    subscriber.unsubscribe();
    assertTrue(subscriber.isUnsubscribed());
    subscriber.assertEmpty();
    o.toHandler().handle("abc");
    subscriber.assertEmpty();
  }

  @Test
  public void testMultiNotifyTwice() {
    ObservableHandler<String> o = RxHelper.observableHandler(true);
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    TestUtils.subscribe(o, subscriber);
    o.toHandler().handle("abc");
    o.toHandler().handle("def");
    subscriber.assertItem("abc").assertItem("def").assertEmpty();
  }

  @Test
  public void testSingleNotifyTwice() {
    ObservableHandler<String> o = RxHelper.observableHandler();
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    TestUtils.subscribe(o, subscriber);
    o.toHandler().handle("abc");
    o.toHandler().handle("def");
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testMultiFulfillAdaptedSubscriber() {
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Handler<String> o = RxHelper.toHandler(TestUtils.toObserver(subscriber), true);
    o.handle("abc");
    subscriber.assertItem("abc").assertEmpty();
  }

  @Test
  public void testSingleFulfillAdaptedSubscriber() {
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Handler<String> o = RxHelper.toHandler(TestUtils.toObserver(subscriber));
    o.handle("abc");
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testFulfillAdaptedFunctions1() {
    List<String> items = new ArrayList<>();
    Handler<String> o = RxHelper.toHandler(items::add);
    o.handle("abc");
    assertEquals(Collections.singletonList("abc"), items);
  }
}
