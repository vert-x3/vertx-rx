package examples;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.streams.ReadStream;
import io.vertx.docgen.Source;
import io.vertx.rxjava3.CompletableHelper;
import io.vertx.rxjava3.FlowableHelper;
import io.vertx.rxjava3.MaybeHelper;
import io.vertx.rxjava3.RxHelper;
import io.vertx.rxjava3.SingleHelper;

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
    readStream.pipeTo(response);
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
