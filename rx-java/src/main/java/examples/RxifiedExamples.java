package examples;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.WebSocketClientOptions;
import io.vertx.docgen.Source;
import io.vertx.rx.java.WriteStreamSubscriber;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.WorkerExecutor;
import io.vertx.core.buffer.Buffer;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.rxjava.core.eventbus.MessageConsumer;
import io.vertx.rxjava.core.file.AsyncFile;
import io.vertx.rxjava.core.file.FileSystem;
import io.vertx.rxjava.core.http.*;
import rx.*;
import rx.plugins.RxJavaHooks;
import rx.plugins.RxJavaSchedulersHook;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Source
public class RxifiedExamples {

  public void toObservable(Vertx vertx) {
    FileSystem fs = vertx.fileSystem();
    fs.open("/data.txt", new OpenOptions()).onComplete(result -> {
      AsyncFile file = result.result();
      Observable<Buffer> observable = file.toObservable();
      observable.forEach(data -> System.out.println("Read data: " + data.toString("UTF-8")));
    });
  }

  private static void checkAuth(Handler<AsyncResult<Void>> handler) {
    throw new UnsupportedOperationException();
  }

  public void delayToObservable(HttpServer server) {
    server.requestHandler(request -> {
      if (request.method() == HttpMethod.POST) {

        // Stop receiving buffers
        request.pause();

        checkAuth(res -> {

          // Now we can receive buffers again
          request.resume();

          if (res.succeeded()) {
            Observable<Buffer> observable = request.toObservable();
            observable.subscribe(buff -> {
              // Get buffers
            });
          }
        });
      }
    });
  }

  public void single(Vertx vertx) {

    // Obtain a single that performs the actual listen on subscribe
    Single<HttpServer> single = vertx
      .createHttpServer()
      .rxListen(1234, "localhost");

    // Subscribe to bind the server
    single.
        subscribe(
            server -> {
              // Server is listening
            },
            failure -> {
              // Server could not start
            }
        );
  }

  public void scheduler(Vertx vertx) {
    Scheduler scheduler = io.vertx.rxjava.core.RxHelper.scheduler(vertx);
    Observable<Long> timer = Observable.interval(100, 100, TimeUnit.MILLISECONDS, scheduler);
  }

  public void scheduler(WorkerExecutor workerExecutor) {
    Scheduler scheduler = io.vertx.rxjava.core.RxHelper.scheduler(workerExecutor);
    Observable<Long> timer = Observable.interval(100, 100, TimeUnit.MILLISECONDS, scheduler);
  }

  public void schedulerHook(Vertx vertx) {
    RxJavaSchedulersHook hook = io.vertx.rxjava.core.RxHelper.schedulerHook(vertx);
      RxJavaHooks.setOnIOScheduler(f -> hook.getIOScheduler());
      RxJavaHooks.setOnNewThreadScheduler(f -> hook.getNewThreadScheduler());
      RxJavaHooks.setOnComputationScheduler(f -> hook.getComputationScheduler());
  }

  public void unmarshaller(FileSystem fileSystem) {
    fileSystem.open("/data.txt", new OpenOptions()).onComplete(result -> {
      AsyncFile file = result.result();
      Observable<Buffer> observable = file.toObservable();
      observable.lift(io.vertx.rxjava.core.RxHelper.unmarshaller(MyPojo.class)).subscribe(
          mypojo -> {
            // Process the object
          }
      );
    });
  }

  public void deployVerticle(Vertx vertx, Verticle verticle) {
    Observable<String> deployment = RxHelper.deployVerticle(vertx, verticle);

    deployment.subscribe(id -> {
      // Deployed
    }, err -> {
      // Could not deploy
    });
  }

  public void get(HttpClient client) {
    Single<HttpClientResponse> get = client
      .rxRequest( HttpMethod.GET, "http://the-server")
      .flatMap(HttpClientRequest::rxSend);

    // Do the request
    get.subscribe(resp -> {
      // Got response
    }, err -> {
      // Something went wrong
    });
  }

  public void embedded() {
    Vertx vertx = io.vertx.rxjava.core.Vertx.vertx();
  }

  public void verticle() {
    class MyVerticle extends io.vertx.rxjava.core.AbstractVerticle {
      public void start() {
        // Use Rxified Vertx here
      }
    }
  }

  public void rxStart() {
    class MyVerticle extends io.vertx.rxjava.core.AbstractVerticle {
      public Completable rxStart() {
        return vertx.createHttpServer()
          .requestHandler(req -> req.response().end("Hello World"))
          .rxListen()
          .toCompletable();
      }
    }
  }

  public void eventBusMessages(Vertx vertx) {
    EventBus eb = vertx.eventBus();
    MessageConsumer<String> consumer = eb.<String>consumer("the-address");
    Observable<Message<String>> observable = consumer.toObservable();
    Subscription sub = observable.subscribe(msg -> {
      // Got message
    });

    // Unregisters the stream after 10 seconds
    vertx.setTimer(10000, id -> {
      sub.unsubscribe();
    });
  }

  public void eventBusBodies(Vertx vertx) {
    EventBus eb = vertx.eventBus();
    MessageConsumer<String> consumer = eb.<String>consumer("the-address");
    Observable<String> observable = consumer.bodyStream().toObservable();
  }

  public void eventBusMapReduce(Vertx vertx) {
    Observable<Double> observable = vertx.eventBus().
        <Double>consumer("heat-sensor").
        bodyStream().
        toObservable();

    observable.
        buffer(1, TimeUnit.SECONDS).
        map(samples -> samples.
            stream().
            collect(Collectors.averagingDouble(d -> d))).
        subscribe(heat -> {
          vertx.eventBus().send("news-feed", "Current heat is " + heat);
        });
  }

  public void websocketServerBuffer(Observable<ServerWebSocket> socketObservable) {
    socketObservable.subscribe(
        socket -> {
          Observable<Buffer> dataObs = socket.toObservable();
          dataObs.subscribe(buffer -> {
            System.out.println("Got message " + buffer.toString("UTF-8"));
          });
        }
    );
  }

  public void websocketClient(Vertx vertx) {
    WebSocketClient client = vertx.createWebSocketClient(new WebSocketClientOptions());
    client.rxConnect(8080, "localhost", "/the_uri").subscribe(
        ws -> {
          // Use the websocket
        },
        error -> {
          // Could not connect
        }
    );
  }

  public void websocketClientBuffer(Observable<WebSocket> socketObservable) {
    socketObservable.subscribe(
        socket -> {
          Observable<Buffer> dataObs = socket.toObservable();
          dataObs.subscribe(buffer -> {
            System.out.println("Got message " + buffer.toString("UTF-8"));
          });
        }
    );
  }

  public void httpClientRequest(Vertx vertx) {
    HttpClient client = vertx.createHttpClient(new HttpClientOptions());
    Single<HttpClientResponse> request = client
      .rxRequest(HttpMethod.GET, 8080, "localhost", "/the_uri")
      .flatMap(HttpClientRequest::rxSend);
    request.subscribe(
        response -> {
          // Process the response
        },
        error -> {
          // Could not connect
        }
    );
  }

  public void httpClientResponse(HttpClient client) {
    Single<HttpClientResponse> request = client
      .rxRequest(HttpMethod.GET, 8080, "localhost", "/the_uri")
      .flatMap(HttpClientRequest::rxSend);
    request.toObservable().
        subscribe(
            response -> {
              Observable<Buffer> observable = response.toObservable();
              observable.forEach(
                  buffer -> {
                    // Process buffer
                  }
              );
            }
        );
  }

  public void httpClientResponseFlatMap(HttpClient client) {
    Single<HttpClientResponse> request = client
      .rxRequest(HttpMethod.GET, 8080, "localhost", "/the_uri")
      .flatMap(HttpClientRequest::rxSend);
    request.toObservable().
        flatMap(HttpClientResponse::toObservable).
        forEach(
            buffer -> {
              // Process buffer
            }
        );
  }

  private class MyPojo {
  }

  public void httpClientResponseFlatMapUnmarshall(HttpClient client) {
    Single<HttpClientResponse> request = client
      .rxRequest(HttpMethod.GET, 8080, "localhost", "/the_uri")
      .flatMap(HttpClientRequest::rxSend);
    request.toObservable().
        flatMap(HttpClientResponse::toObservable).
        lift(io.vertx.rxjava.core.RxHelper.unmarshaller(MyPojo.class)).
        forEach(
            pojo -> {
              // Process pojo
            }
        );
  }

  public void httpServerRequestObservable(HttpServerRequest request) {
    Observable<Buffer> observable = request.toObservable();
  }

  public void httpServerRequestObservableUnmarshall(HttpServerRequest request) {
    Observable<MyPojo> observable = request.
      toObservable().
      lift(io.vertx.rxjava.core.RxHelper.unmarshaller(MyPojo.class));
  }

  public void writeStreamSubscriberAdapter(Observable<io.vertx.core.buffer.Buffer> observable, io.vertx.core.http.HttpServerResponse response) {
    response.setChunked(true);
    WriteStreamSubscriber<io.vertx.core.buffer.Buffer> subscriber = io.vertx.rx.java.RxHelper.toSubscriber(response);
    observable.subscribe(subscriber);
  }

  public void rxWriteStreamSubscriberAdapter(Observable<Buffer> observable, HttpServerResponse response) {
    response.setChunked(true);
    observable.subscribe(response.toSubscriber());
  }

  public void writeStreamSubscriberAdapterCallbacks(Observable<Buffer> observable, HttpServerResponse response) {
    response.setChunked(true);

    WriteStreamSubscriber<Buffer> subscriber = response.toSubscriber();

    subscriber.onError(throwable -> {
      if (!response.headWritten() && response.closed()) {
        response.setStatusCode(500).end("oops");
      } else {
        // log error
      }
    });

    subscriber.onWriteStreamError(throwable -> {
      // log error
    });

    subscriber.onWriteStreamEnd(() -> {
      // log end of transaction to audit system...
    });

    observable.subscribe(subscriber);
  }
}
