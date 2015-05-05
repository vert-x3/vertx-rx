package io.vertx.rx.java.test;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.rx.java.ObservableFuture;
import io.vertx.rx.java.RxHelper;
import org.junit.Test;
import rx.Subscription;
import rx.exceptions.OnErrorNotImplementedException;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
  public class AsyncResultHandlerTest {

  @Test
  public void testCompleteWithSuccessBeforeSubscribe() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    o.toHandler().handle(Future.succeededFuture("abc"));
    MySubscriber<String> subscriber = new MySubscriber<>();
    o.subscribe(subscriber);
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testCompleteWithSuccessAfterSubscribe() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    MySubscriber<String> subscriber = new MySubscriber<>();
    o.subscribe(subscriber);
    subscriber.assertEmpty();
    o.toHandler().handle(Future.succeededFuture("abc"));
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testCompleteWithFailureBeforeSubscribe() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    Throwable failure = new Throwable();
    o.toHandler().handle(Future.failedFuture(failure));
    MySubscriber<String> subscriber = new MySubscriber<>();
    o.subscribe(subscriber);
    subscriber.assertError(failure).assertEmpty();
  }

  @Test
  public void testCompleteWithFailureAfterSubscribe() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    MySubscriber<String> subscriber = new MySubscriber<>();
    o.subscribe(subscriber);
    subscriber.assertEmpty();
    Throwable failure = new Throwable();
    o.toHandler().handle(Future.failedFuture(failure));
    subscriber.assertError(failure).assertEmpty();
  }

  @Test
  public void testUnsubscribeBeforeResolve() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    MySubscriber<String> subscriber = new MySubscriber<>();
    Subscription sub = o.subscribe(subscriber);
    sub.unsubscribe();
    assertTrue(sub.isUnsubscribed());
    subscriber.assertEmpty();
  }

  @Test
  public void testCompleteTwice() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    MySubscriber<String> subscriber = new MySubscriber<>();
    o.subscribe(subscriber);
    o.toHandler().handle(Future.succeededFuture("abc"));
    o.toHandler().handle(Future.succeededFuture("def"));
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testFailTwice() {
    ObservableFuture<String> o = RxHelper.observableFuture();
    MySubscriber<String> subscriber = new MySubscriber<>();
    o.subscribe(subscriber);
    Throwable failure = new Throwable();
    o.toHandler().handle(Future.failedFuture(failure));
    o.toHandler().handle(Future.failedFuture(new Throwable()));
    subscriber.assertError(failure).assertEmpty();
  }

  @Test
  public void testFulfillAdaptedSubscriber() {
    MySubscriber<String> subscriber = new MySubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber);
    o.handle(Future.succeededFuture("abc"));
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testRejectAdaptedSubscriber() {
    MySubscriber<String> subscriber = new MySubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber);
    Exception e = new Exception();
    o.handle(Future.failedFuture(e));
    subscriber.assertError(e).assertEmpty();
  }

  @Test
  public void testFulfillAdaptedFunctions1() {
    MySubscriber<String> subscriber = new MySubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext);
    o.handle(Future.succeededFuture("abc"));
    subscriber.assertItem("abc").assertEmpty();
  }

  @Test
  public void testFulfillAdaptedFunctions2() {
    MySubscriber<String> subscriber = new MySubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext, subscriber::onError);
    o.handle(Future.succeededFuture("abc"));
    subscriber.assertItem("abc").assertEmpty();
  }

  @Test
  public void testFulfillAdaptedFunctions3() {
    MySubscriber<String> subscriber = new MySubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext, subscriber::onError, subscriber::onCompleted);
    o.handle(Future.succeededFuture("abc"));
    subscriber.assertItem("abc").assertCompleted().assertEmpty();
  }

  @Test
  public void testRejectAdaptedFunctions1() {
    MySubscriber<String> subscriber = new MySubscriber<>();
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
    MySubscriber<String> subscriber = new MySubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext, subscriber::onError);
    Exception cause = new Exception();
    o.handle(Future.failedFuture(cause));
    subscriber.assertError(cause).assertEmpty();
  }

  @Test
  public void testRejectAdaptedFunctions3() {
    MySubscriber<String> subscriber = new MySubscriber<>();
    Handler<AsyncResult<String>> o = RxHelper.toFuture(subscriber::onNext, subscriber::onError, subscriber::onCompleted);
    Exception cause = new Exception();
    o.handle(Future.failedFuture(cause));
    subscriber.assertError(cause).assertEmpty();
  }
}
