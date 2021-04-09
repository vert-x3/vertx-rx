package io.vertx.rxjava3.test;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.vertx.lang.rx.test.TestSubscriber;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestUtils {

  public static <T>  void subscribe(Flowable<T> obs, TestSubscriber<T> sub) {
    obs.subscribe(new Subscriber<T>() {
      boolean unsubscribed;
      @Override
      public void onSubscribe(Subscription s) {
        sub.onSubscribe(new TestSubscriber.Subscription() {
          @Override
          public void fetch(long val) {
            if (val > 0) {
              s.request(val);
            }
          }
          @Override
          public void unsubscribe() {
            unsubscribed = true;
            s.cancel();
          }
          @Override
          public boolean isUnsubscribed() {
            return unsubscribed;
          }
        });

      }
      @Override
      public void onNext(T buffer) {
        sub.onNext(buffer);
      }
      @Override
      public void onError(Throwable t) {
        unsubscribed = true;
        sub.onError(t);
      }
      @Override
      public void onComplete() {
        unsubscribed = true;
        sub.onCompleted();
      }
    });
  }

  public static <T> void subscribe(Observable<T> obs, TestSubscriber<T> sub) {
    obs.subscribe(new Observer<T>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        sub.onSubscribe(new TestSubscriber.Subscription() {
          @Override
          public void fetch(long val) {}
          @Override
          public void unsubscribe() {
            d.dispose();
          }
          @Override
          public boolean isUnsubscribed() {
            return d.isDisposed();
          }
        });
      }

      @Override
      public void onNext(@NonNull T t) {
        sub.onNext(t);
      }

      @Override
      public void onError(@NonNull Throwable e) {
        sub.onError(e);
      }

      @Override
      public void onComplete() {
        sub.onCompleted();
      }
    });
  }

  public static <T> void subscribe(Single<T> obs, TestSubscriber<T> sub) {
    obs.subscribe(sub::onNext, sub::onError);
  }

  public static <T> void subscribe(Maybe<T> obs, TestSubscriber<T> sub) {
    obs.subscribe(sub::onNext, sub::onError, sub::onCompleted);
  }
}
