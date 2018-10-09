package io.vertx.rx.java.test;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.lang.rx.test.TestSubscriber;
import io.vertx.rx.java.ObservableFuture;
import io.vertx.rx.java.RxHelper;
import org.junit.Test;
import rx.exceptions.OnErrorNotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.vertx.rx.java.test.TestUtils.subscribe;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
  public class AsyncResultHandlerTest {

  @Test
  public void testCompleteWithSuccessBeforeSubscribe() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    o.toHandler().handle(Future.succeededFuture("abc"));
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    subscribe(o, subscriber);
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testCompleteWithSuccessAfterSubscribe() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    subscribe(o, subscriber);
    subscriber.assertEmpty();
    o.toHandler().handle(Future.succeededFuture("abc"));
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testCompleteWithFailureBeforeSubscribe() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    Throwable failure = new Throwable();
    o.toHandler().handle(Future.failedFuture(failure));
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    subscribe(o, subscriber);
    subscriber.assertError(failure).assertEmpty();
  }

  @Test
  public void testCompleteWithFailureAfterSubscribe() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    subscribe(o, subscriber);
    subscriber.assertEmpty();
    Throwable failure = new Throwable();
    o.toHandler().handle(Future.failedFuture(failure));
    subscriber.assertError(failure).assertEmpty();
  }

  @Test
  public void testUnsubscribeBeforeResolve() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    subscribe(o, subscriber);
    subscriber.unsubscribe();
    assertTrue(subscriber.isUnsubscribed());
    subscriber.assertEmpty();
  }

  @Test
  public void testCompleteTwice() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    subscribe(o, subscriber);
    o.toHandler().handle(Future.succeededFuture("abc"));
    o.toHandler().handle(Future.succeededFuture("def"));
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testFailTwice() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    subscribe(o, subscriber);
    Throwable failure = new Throwable();
    o.toHandler().handle(Future.failedFuture(failure));
    o.toHandler().handle(Future.failedFuture(new Throwable()));
    subscriber.assertError(failure).assertEmpty();
  }

  @Test
  public void testFulfillAdaptedSubscriber() {
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(TestUtils.toObserver(subscriber));
    o.handle(Future.succeededFuture("abc"));
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testRejectAdaptedSubscriber() {
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(TestUtils.toObserver(subscriber));
    Exception e = new Exception();
    o.handle(Future.failedFuture(e));
    subscriber.assertError(e).assertEmpty();
  }

  @Test
  public void testFulfillAdaptedFunctions1() {
    List<String> items = new ArrayList<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(items::add);
    o.handle(Future.succeededFuture("abc"));
    assertEquals(Collections.singletonList("abc"), items);
  }

  @Test
  public void testFulfillAdaptedFunctions2() {
    List<String> items = new ArrayList<>();
    List<Throwable> errors = new ArrayList<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(items::add, errors::add);
    o.handle(Future.succeededFuture("abc"));
    assertEquals(Collections.singletonList("abc"), items);
    assertEquals(Collections.emptyList(), errors);
  }

  @Test
  public void testFulfillAdaptedFunctions3() {
    List<String> items = new ArrayList<>();
    List<Throwable> errors = new ArrayList<>();
    AtomicInteger completions = new AtomicInteger();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(items::add, errors::add, completions::incrementAndGet);
    o.handle(Future.succeededFuture("abc"));
    assertEquals(Collections.singletonList("abc"), items);
    assertEquals(Collections.emptyList(), errors);
    assertEquals(1, completions.get());
  }

  @Test
  public void testRejectAdaptedFunctions1() {
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext);
    Exception cause = new Exception();
    try {
      o.handle(Future.failedFuture(cause));
    } catch (OnErrorNotImplementedException e) {
      assertSame(cause, e.getCause());
    }
    subscriber.assertEmpty();
  }

  @Test
  public void testRejectAdaptedFunctions2() {
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext, subscriber::onError);
    Exception cause = new Exception();
    o.handle(Future.failedFuture(cause));
    subscriber.assertError(cause).assertEmpty();
  }

  @Test
  public void testRejectAdaptedFunctions3() {
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext, subscriber::onError, subscriber::onCompleted);
    Exception cause = new Exception();
    o.handle(Future.failedFuture(cause));
    subscriber.assertError(cause).assertEmpty();
  }
}
