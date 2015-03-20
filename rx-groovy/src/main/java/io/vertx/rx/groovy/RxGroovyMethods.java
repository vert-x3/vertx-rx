package io.vertx.rx.groovy;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.groovy.core.Vertx;
import io.vertx.groovy.core.streams.ReadStream;
import io.vertx.rx.java.RxHelper;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;

/**
 * A set of Groovy extensions for Rxifying the Groovy API.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxGroovyMethods {

  public static <T> Observable<T> toObservable(ReadStream<T> stream) {
    return Observable.create(new HandlerAdapter<>(stream));
  }

  public static <T>Handler<AsyncResult<T>> toFuture(Observer<T> observer) {
    return RxHelper.toFuture(observer);
  }

  public static <T>Handler<T> toHandler(Observer<T> observer) {
    return RxHelper.toHandler(observer);
  }

  public static Scheduler scheduler(Vertx vertx) {
    return RxHelper.scheduler((io.vertx.core.Vertx) vertx.getDelegate());
  }

  public static Scheduler blockingScheduler(Vertx vertx) {
    return RxHelper.blockingScheduler((io.vertx.core.Vertx) vertx.getDelegate());
  }
}
