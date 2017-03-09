package io.vertx.rx.java.test;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.rx.java.RxHelper;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import rx.Emitter;
import rx.Observable;
import rx.Single;
import rx.SingleEmitter;
import rx.Subscriber;
import rx.internal.operators.OnSubscribeCreate;
import rx.internal.operators.SingleFromEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SubscriberTest extends VertxTestBase {

  private static SingleEmitter<String> subscribeToSingle(Subscriber<String> subscriber) {
    AtomicReference<SingleEmitter<String>> emitter = new AtomicReference<>();
    Single<String> single = Single.create(new SingleFromEmitter<>(emitter::set));
    single.subscribe(subscriber);
    return emitter.get();
  }

  private static Emitter<String> subscribe(Subscriber<String> subscriber) {
    AtomicReference<Emitter<String>> emitter = new AtomicReference<>();
    Observable<String> observable = Observable.create(new OnSubscribeCreate<>(emitter::set, Emitter.BackpressureMode.BUFFER));
    observable.subscribe(subscriber);
    return emitter.get();
  }

  @Test
  public void testSingleFutureSubscriptionSuccess() {
    List<Object> results = new ArrayList<>();
    Subscriber<String> sub = RxHelper.toSubscriber(handler(results));
    SingleEmitter<String> emitter = subscribeToSingle(sub);
    assertEquals(Collections.emptyList(), results);
    emitter.onSuccess("the-value-1");
    assertEquals(Collections.singletonList("the-value-1"), results);
    emitter.onSuccess("the-value-2");
    assertEquals(Collections.singletonList("the-value-1"), results);
  }

  @Test
  public void testSingleFutureSubscriptionError() {
    List<Object> results = new ArrayList<>();
    Subscriber<String> sub = RxHelper.toSubscriber(handler(results));
    SingleEmitter<String> emitter = subscribeToSingle(sub);
    assertEquals(Collections.emptyList(), results);
    Throwable cause = new Throwable();
    emitter.onError(cause);
    assertEquals(Collections.singletonList(cause), results);
    emitter.onSuccess("the-value");
    assertEquals(Collections.singletonList(cause), results);
  }

  @Test
  public void testObservableFutureSubscriptionCompletedWithNoItems() {
    List<Object> results = new ArrayList<>();
    Subscriber<String> sub = RxHelper.toSubscriber(handler(results));
    Emitter<String> emitter = subscribe(sub);
    assertEquals(Collections.emptyList(), results);
    emitter.onCompleted();
    assertEquals(Collections.singletonList(null), results);
    emitter.onNext("the-value-1");
    assertEquals(Collections.singletonList(null), results);
  }

  @Test
  public void testObservableFutureSubscriptionCompletedWithItem() {
    List<Object> results = new ArrayList<>();
    Subscriber<String> sub = RxHelper.toSubscriber(handler(results));
    Emitter<String> emitter = subscribe(sub);
    assertEquals(Collections.emptyList(), results);
    emitter.onNext("the-value-1");
    assertEquals(Collections.singletonList("the-value-1"), results);
    emitter.onCompleted();
    emitter.onNext("the-value-1");
    assertEquals(Collections.singletonList("the-value-1"), results);
  }

  @Test
  public void testObservableFutureSubscriptionCompletedWithItems() {
    List<Object> results = new ArrayList<>();
    Subscriber<String> sub = RxHelper.toSubscriber(handler(results));
    Emitter<String> emitter = subscribe(sub);
    assertEquals(Collections.emptyList(), results);
    emitter.onNext("the-value-1");
    assertEquals(Collections.singletonList("the-value-1"), results);
    emitter.onNext("the-value-2");
    assertEquals(Collections.singletonList("the-value-1"), results);
  }

  @Test
  public void testObservableFutureSubscriptionError() {
    List<Object> results = new ArrayList<>();
    Subscriber<String> sub = RxHelper.toSubscriber(handler(results));
    Emitter<String> emitter = subscribe(sub);
    assertEquals(Collections.emptyList(), results);
    Throwable cause = new Throwable();
    emitter.onError(cause);
    assertEquals(Collections.singletonList(cause), results);
    emitter.onNext("the-value");
    assertEquals(Collections.singletonList(cause), results);
  }

  private static <T> Handler<AsyncResult<T>> handler(List<Object> list) {
    return ar -> {
      if (ar.succeeded()) {
        list.add(ar.result());
      } else {
        list.add(ar.cause());
      }
    };
  }




}
