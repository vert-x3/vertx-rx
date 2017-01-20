package io.vertx.rx.java.test;

import io.vertx.rx.java.ObservableReadStream;
import io.vertx.rx.java.RxHelper;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableReadStreamTest {

  @Test
  public void testFoo() {

    TestReadStream<Integer> rs = new TestReadStream<>();

    Observable<Integer> obs = RxHelper.toObservable(rs);

    Subscription sub = obs.subscribe(new Subscriber<Integer>() {

      @Override
      public void onStart() {
        request(2);
      }

      @Override
      public void onNext(Integer integer) {
        System.out.println("got something " + integer);
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onCompleted() {

      }
    });

    rs.emit(0);
    rs.emit(1);
    rs.emit(2);

    sub.unsubscribe();

  }
}
