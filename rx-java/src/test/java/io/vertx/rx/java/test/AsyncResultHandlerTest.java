package io.vertx.rx.java.test;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.rx.java.ObservableFuture;
import io.vertx.rx.java.RxHelper;
import io.vertx.rx.java.test.support.SimpleSubscriber;
import org.junit.Test;
import rx.exceptions.OnErrorNotImplementedException;

import static io.vertx.rx.java.test.support.SimpleSubscriber.subscribe;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
  public class AsyncResultHandlerTest {

  @Test
  public void testCompleteWithSuccessBeforeSubscribe() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    o.toHandler().handle(Future.succeededFuture("abc"));
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    subscribe(o, subscriber);
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testCompleteWithSuccessAfterSubscribe() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
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
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    subscribe(o, subscriber);
    subscriber.assertError(failure).assertEmpty();
  }

  @Test
  public void testCompleteWithFailureAfterSubscribe() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    subscribe(o, subscriber);
    subscriber.assertEmpty();
    Throwable failure = new Throwable();
    o.toHandler().handle(Future.failedFuture(failure));
    subscriber.assertError(failure).assertEmpty();
  }

  @Test
  public void testUnsubscribeBeforeResolve() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    subscribe(o, subscriber);
    subscriber.unsubscribe();
    assertTrue(subscriber.isUnsubscribed());
    subscriber.assertEmpty();
  }

  @Test
  public void testCompleteTwice() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    subscribe(o, subscriber);
    o.toHandler().handle(Future.succeededFuture("abc"));
    o.toHandler().handle(Future.succeededFuture("def"));
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testFailTwice() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    subscribe(o, subscriber);
    Throwable failure = new Throwable();
    o.toHandler().handle(Future.failedFuture(failure));
    o.toHandler().handle(Future.failedFuture(new Throwable()));
    subscriber.assertError(failure).assertEmpty();
  }

  @Test
  public void testFulfillAdaptedSubscriber() {
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber.toObserver());
    o.handle(Future.succeededFuture("abc"));
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testRejectAdaptedSubscriber() {
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber.toObserver());
    Exception e = new Exception();
    o.handle(Future.failedFuture(e));
    subscriber.assertError(e).assertEmpty();
  }

  @Test
  public void testFulfillAdaptedFunctions1() {
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext);
    o.handle(Future.succeededFuture("abc"));
    subscriber.assertItem("abc").assertEmpty();
  }

  @Test
  public void testFulfillAdaptedFunctions2() {
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext, subscriber::onError);
    o.handle(Future.succeededFuture("abc"));
    subscriber.assertItem("abc").assertEmpty();
  }

  @Test
  public void testFulfillAdaptedFunctions3() {
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext, subscriber::onError, subscriber::onCompleted);
    o.handle(Future.succeededFuture("abc"));
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testRejectAdaptedFunctions1() {
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
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
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext, subscriber::onError);
    Exception cause = new Exception();
    o.handle(Future.failedFuture(cause));
    subscriber.assertError(cause).assertEmpty();
  }

  @Test
  public void testRejectAdaptedFunctions3() {
    SimpleSubscriber<String> subscriber = new SimpleSubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext, subscriber::onError, subscriber::onCompleted);
    Exception cause = new Exception();
    o.handle(Future.failedFuture(cause));
    subscriber.assertError(cause).assertEmpty();
  }
}
