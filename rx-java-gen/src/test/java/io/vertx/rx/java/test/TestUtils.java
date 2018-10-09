package io.vertx.rx.java.test;

import io.vertx.lang.rx.test.TestSubscriber;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class TestUtils {


  public static <B> void subscribe(Observable<B> observable, TestSubscriber<B> subscriber) {
    observable.subscribe(toObserver(subscriber));
  }

  public static <T> Observer<T> toObserver(final TestSubscriber<T> subscriber) {
    class SubscriberImpl extends Subscriber<T> implements TestSubscriber.Subscription {
      @Override
      public void onCompleted() {
        subscriber.onCompleted();
      }
      @Override
      public void onError(Throwable throwable) {
        subscriber.onError(throwable);
      }
      @Override
      public void onNext(T t) {
        subscriber.onNext(t);
      }
      @Override
      public void onStart() {
        subscriber.onSubscribe(this);
      }
      @Override
      public void fetch(long val) {
        request(val);
      }
    }
    return new SubscriberImpl();
  }
}
