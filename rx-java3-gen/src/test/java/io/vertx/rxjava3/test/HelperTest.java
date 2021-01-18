package io.vertx.rxjava3.test;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.rxjava3.CompletableHelper;
import io.vertx.rxjava3.FlowableHelper;
import io.vertx.rxjava3.MaybeHelper;
import io.vertx.rxjava3.ObservableHelper;
import io.vertx.rxjava3.SingleHelper;
import io.vertx.test.core.VertxTestBase;
import io.vertx.test.fakestream.FakeStream;
import org.junit.Test;

import static java.util.function.Function.identity;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HelperTest extends VertxTestBase {

  @Test
  public void testToFutureSuccess() {
    Single<String> promise = Single.just("foobar");
    Future<String> future = SingleHelper.toFuture(promise);
    assertTrue(future.succeeded());
    assertEquals("foobar", future.result());
  }

  @Test
  public void testToFutureFailure() {
    Exception err = new Exception();
    Single<String> promise = Single.error(err);
    Future<String> future = SingleHelper.toFuture(promise);
    assertTrue(future.failed());
    assertEquals(err, future.cause());
  }

  @Test
  public void testToSingleObserverSuccess() {
    Promise<String> promise = Promise.promise();
    SingleObserver<String> observer = SingleHelper.toObserver(promise);
    Single<String> s = Single.just("foobar");
    s.subscribe(observer);
    assertTrue(promise.future().succeeded());
    assertSame("foobar", promise.future().result());
  }

  @Test
  public void testToSingleObserverFailure() {
    Promise<String> promise = Promise.promise();
    SingleObserver<String> observer = SingleHelper.toObserver(promise);
    RuntimeException cause = new RuntimeException();
    Single<String> s = Single.error(cause);
    s.subscribe(observer);
    assertTrue(promise.future().failed());
    assertSame(cause, promise.future().cause());
  }

  @Test
  public void testToMaybeObserverSuccess() {
    Promise<String> promise = Promise.promise();
    MaybeObserver<String> observer = MaybeHelper.toObserver(promise);
    Maybe<String> s = Maybe.just("foobar");
    s.subscribe(observer);
    assertTrue(promise.future().succeeded());
    assertSame("foobar", promise.future().result());
  }

  @Test
  public void testToMaybeObserverEmpty() {
    Promise<String> promise = Promise.promise();
    MaybeObserver<String> observer = MaybeHelper.toObserver(promise);
    Maybe<String> s = Maybe.empty();
    s.subscribe(observer);
    assertTrue(promise.future().succeeded());
    assertNull(promise.future().result());
  }

  @Test
  public void testToMaybeObserverFailure() {
    Promise<String> promise = Promise.promise();
    MaybeObserver<String> observer = MaybeHelper.toObserver(promise);
    RuntimeException cause = new RuntimeException();
    Maybe<String> s = Maybe.error(cause);
    s.subscribe(observer);
    assertTrue(promise.future().failed());
    assertSame(cause, promise.future().cause());
  }

  @Test
  public void testToCompletableObserverSuccess() {
    Promise<String> promise = Promise.promise();
    CompletableObserver observer = CompletableHelper.toObserver(promise);
    Completable s = Completable.complete();
    s.subscribe(observer);
    assertTrue(promise.future().succeeded());
    assertNull(promise.future().result());
  }

  @Test
  public void testToCompletableObserverFailure() {
    Promise<String> promise = Promise.promise();
    CompletableObserver observer = CompletableHelper.toObserver(promise);
    RuntimeException cause = new RuntimeException();
    Completable s = Completable.error(cause);
    s.subscribe(observer);
    assertTrue(promise.future().failed());
    assertSame(cause, promise.future().cause());
  }

  @Test
  public void testToObservableAssemblyHook() {
    FakeStream<String> stream = new FakeStream<>();
    try {
      final Observable<String> justMe = Observable.just("me");
      RxJavaPlugins.setOnObservableAssembly(new Function<Observable, Observable>() {
        @Override public Observable apply(Observable f) {
          return justMe;
        }
      });
      Observable<String> observable = ObservableHelper.toObservable(stream);
      assertSame(observable, justMe);
      Observable<String> observableFn = ObservableHelper.toObservable(stream, identity());
      assertSame(observableFn, justMe);
    } finally {
      RxJavaPlugins.reset();
    }
  }

  @Test
  public void testToFlowableAssemblyHook() {
    FakeStream<String> stream = new FakeStream<>();
    try {
      final Flowable<String> justMe = Flowable.just("me");
      RxJavaPlugins.setOnFlowableAssembly(new Function<Flowable, Flowable>() {
        @Override public Flowable apply(Flowable f) {
          return justMe;
        }
      });
      Flowable<String> flowable = FlowableHelper.toFlowable(stream);
      assertSame(flowable, justMe);
      Flowable<String> flowableFn = FlowableHelper.toFlowable(stream, identity());
      assertSame(flowableFn, justMe);
      Flowable<String> flowableSize = FlowableHelper.toFlowable(stream, 1);
      assertSame(flowableSize, justMe);
    } finally {
      RxJavaPlugins.reset();
    }
  }
}
