package io.vertx.rx.groovy;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.ReadStream;
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
    return RxHelper.scheduler(vertx);
  }

  public static Scheduler blockingScheduler(Vertx vertx) {
    return RxHelper.blockingScheduler(vertx);
  }
}
