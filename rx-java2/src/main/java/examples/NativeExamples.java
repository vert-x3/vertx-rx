package examples;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.plugins.RxJavaPlugins;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.streams.Pump;
import io.vertx.core.streams.ReadStream;
import io.vertx.docgen.Source;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.FlowableHelper;
import io.vertx.reactivex.MaybeHelper;
import io.vertx.reactivex.RxHelper;
import io.vertx.reactivex.SingleHelper;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Source
public class NativeExamples {

  public void toFlowable(Vertx vertx) {
    FileSystem fileSystem = vertx.fileSystem();
    fileSystem.open("/data.txt", new OpenOptions(), result -> {
      AsyncFile file = result.result();
      Flowable<Buffer> observable = FlowableHelper.toFlowable(file);
      observable.forEach(data -> System.out.println("Read data: " + data.toString("UTF-8")));
    });
  }

  private Flowable<Buffer> getFlowable() {
    throw new UnsupportedOperationException();
  }

  public void toReadStream(HttpServerResponse response) {
    Flowable<Buffer> observable = getFlowable();
    ReadStream<Buffer> readStream = FlowableHelper.toReadStream(observable);
    Pump pump = Pump.pump(readStream, response);
    pump.start();
  }

  public <T> Handler<AsyncResult<T>> getHandler() {
    throw new UnsupportedOperationException();
  }

  public void handlerToSingleObserver() {

    Handler<AsyncResult<String>> handler = getHandler();

    // Subscribe to a Single
    Single.just("hello").subscribe(SingleHelper.toObserver(handler));
  }

  public void handlerToMaybeObserver() {

    Handler<AsyncResult<String>> handler = getHandler();

    // Subscribe to a Single
    Maybe.just("hello").subscribe(MaybeHelper.toObserver(handler));
  }

  public void handlerToCompletableObserver() {

    Handler<AsyncResult<Void>> handler = getHandler();

    // Subscribe to a Single
    Completable.complete().subscribe(CompletableHelper.toObserver(handler));
  }

  public void scheduler(Vertx vertx) {
    Scheduler scheduler = RxHelper.scheduler(vertx);
    Observable<Long> timer = Observable.interval(100, 100, TimeUnit.MILLISECONDS, scheduler);
  }

  public void blockingScheduler(Vertx vertx) {
    Scheduler scheduler = RxHelper.blockingScheduler(vertx);
    Observable<Long> timer = Observable.interval(100, 100, TimeUnit.MILLISECONDS, scheduler);
  }

  public void schedulerHook(Vertx vertx) {
    RxJavaPlugins.setComputationSchedulerHandler(s -> RxHelper.scheduler(vertx));
    RxJavaPlugins.setIoSchedulerHandler(s -> RxHelper.blockingScheduler(vertx));
    RxJavaPlugins.setNewThreadSchedulerHandler(s -> RxHelper.scheduler(vertx));
  }

  private class MyPojo {
  }

  public void unmarshaller(FileSystem fileSystem) {
    fileSystem.open("/data.txt", new OpenOptions(), result -> {
      AsyncFile file = result.result();
      Flowable<Buffer> observable = FlowableHelper.toFlowable(file);
      observable.compose(FlowableHelper.unmarshaller(MyPojo.class)).subscribe(
          mypojo -> {
            // Process the object
          }
      );
    });
  }
}
