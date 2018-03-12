package io.vertx.reactivex.test;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.vertx.core.Future;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.MaybeHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HelperTest extends VertxTestBase {

  @Test
  public void testToSingleObserverSuccess() {
    Future<String> fut = Future.future();
    SingleObserver<String> observer = SingleHelper.toObserver(fut);
    Single<String> s = Single.just("foobar");
    s.subscribe(observer);
    assertTrue(fut.succeeded());
    assertSame("foobar", fut.result());
  }

  @Test
  public void testToSingleObserverFailure() {
    Future<String> fut = Future.future();
    SingleObserver<String> observer = SingleHelper.toObserver(fut);
    RuntimeException cause = new RuntimeException();
    Single<String> s = Single.error(cause);
    s.subscribe(observer);
    assertTrue(fut.failed());
    assertSame(cause, fut.cause());
  }

  @Test
  public void testToMaybeObserverSuccess() {
    Future<String> fut = Future.future();
    MaybeObserver<String> observer = MaybeHelper.toObserver(fut);
    Maybe<String> s = Maybe.just("foobar");
    s.subscribe(observer);
    assertTrue(fut.succeeded());
    assertSame("foobar", fut.result());
  }

  @Test
  public void testToMaybeObserverEmpty() {
    Future<String> fut = Future.future();
    MaybeObserver<String> observer = MaybeHelper.toObserver(fut);
    Maybe<String> s = Maybe.empty();
    s.subscribe(observer);
    assertTrue(fut.succeeded());
    assertNull(fut.result());
  }

  @Test
  public void testToMaybeObserverFailure() {
    Future<String> fut = Future.future();
    MaybeObserver<String> observer = MaybeHelper.toObserver(fut);
    RuntimeException cause = new RuntimeException();
    Maybe<String> s = Maybe.error(cause);
    s.subscribe(observer);
    assertTrue(fut.failed());
    assertSame(cause, fut.cause());
  }

  @Test
  public void testToCompletableObserverSuccess() {
    Future<String> fut = Future.future();
    CompletableObserver observer = CompletableHelper.toObserver(fut);
    Completable s = Completable.complete();
    s.subscribe(observer);
    assertTrue(fut.succeeded());
    assertNull(fut.result());
  }

  @Test
  public void testToCompletableObserverFailure() {
    Future<String> fut = Future.future();
    CompletableObserver observer = CompletableHelper.toObserver(fut);
    RuntimeException cause = new RuntimeException();
    Completable s = Completable.error(cause);
    s.subscribe(observer);
    assertTrue(fut.failed());
    assertSame(cause, fut.cause());
  }
}
