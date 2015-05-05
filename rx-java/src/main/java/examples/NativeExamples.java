package examples;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.rx.java.ObservableFuture;
import io.vertx.rx.java.ObservableHandler;
import io.vertx.rx.java.RxHelper;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.plugins.RxJavaSchedulersHook;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class NativeExamples {

  public void readStream(Vertx vertx) {
    FileSystem fileSystem = vertx.fileSystem();
    fileSystem.open("/data.txt", new OpenOptions(), result -> {
      AsyncFile file = result.result();
      Observable<Buffer> observable = RxHelper.toObservable(file, vertx);
      observable.forEach(data -> System.out.println("Read data: " + data.toString("UTF-8")));
    });
  }

  public void observableHandler(Vertx vertx) {
    ObservableHandler<Long> observable = RxHelper.observableHandler();
    observable.subscribe(id -> {
      // Fired
    });
    vertx.setTimer(1000, observable.toHandler());
  }

  public void observableFuture(Vertx vertx) {
    ObservableFuture<HttpServer> observable = RxHelper.observableFuture();
    observable.subscribe(
        server -> {
          // Server is listening
        },
        failure -> {
          // Server could not start
        }
    );
    vertx.createHttpServer(new HttpServerOptions().
        setPort(1234).
        setHost("localhost")
    ).listen(observable.toHandler());
  }

  public void observableToHandler() {
    Observer<HttpServer> observer = new Observer<HttpServer>() {
      @Override
      public void onNext(HttpServer o) {
      }
      @Override
      public void onError(Throwable e) {
      }
      @Override
      public void onCompleted() {
      }
    };
    Handler<AsyncResult<HttpServer>> handler = RxHelper.toFuture(observer);
  }

  public void actionsToHandler() {
    Action1<HttpServer> onNext = httpServer -> {};
    Action1<Throwable> onError = httpServer -> {};
    Action0 onComplete = () -> {};

    Handler<AsyncResult<HttpServer>> handler1 = RxHelper.toFuture(onNext);
    Handler<AsyncResult<HttpServer>> handler2 = RxHelper.toFuture(onNext, onError);
    Handler<AsyncResult<HttpServer>> handler3 = RxHelper.toFuture(onNext, onError, onComplete);
  }

  public void scheduler(Vertx vertx) {
    Scheduler scheduler = RxHelper.scheduler(vertx);
    Observable<Long> timer = Observable.timer(100, 100, TimeUnit.MILLISECONDS, scheduler);
  }

  public void blockingScheduler(Vertx vertx, Observable<Integer> blockingObservable) {
    Scheduler scheduler = RxHelper.blockingScheduler(vertx);
    Observable<Integer> obs = blockingObservable.observeOn(scheduler);
  }

  public void schedulerHook(Vertx vertx) {
    RxJavaSchedulersHook hook = RxHelper.schedulerHook(vertx);
    rx.plugins.RxJavaPlugins.getInstance().registerSchedulersHook(hook);
  }

  private class MyPojo {
  }

  public void unmarshaller(Vertx vertx, FileSystem fileSystem) {
    fileSystem.open("/data.txt", new OpenOptions(), result -> {
      AsyncFile file = result.result();
      Observable<Buffer> observable = RxHelper.toObservable(file, vertx);
      observable.lift(RxHelper.unmarshaller(MyPojo.class)).subscribe(
          mypojo -> {
            // Process the object
          }
      );
    });
  }
}
