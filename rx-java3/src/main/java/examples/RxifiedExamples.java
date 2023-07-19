package examples;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.impl.NoStackTraceThrowable;
import io.vertx.docgen.Source;
import io.vertx.rxjava3.MaybeHelper;
import io.vertx.rxjava3.WriteStreamSubscriber;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.FlowableHelper;
import io.vertx.rxjava3.core.ObservableHelper;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.WorkerExecutor;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.core.dns.DnsClient;
import io.vertx.rxjava3.core.eventbus.EventBus;
import io.vertx.rxjava3.core.eventbus.Message;
import io.vertx.rxjava3.core.eventbus.MessageConsumer;
import io.vertx.rxjava3.core.file.AsyncFile;
import io.vertx.rxjava3.core.file.FileSystem;
import io.vertx.rxjava3.core.http.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Source
public class RxifiedExamples {

  public void toFlowable(Vertx vertx) {
    FileSystem fs = vertx.fileSystem();
    fs.rxOpen("/data.txt", new OpenOptions())
      .flatMapPublisher(file -> file.toFlowable())
      .subscribe(data -> System.out.println("Read data: " + data.toString("UTF-8")));
  }

  private static void checkAuth(Handler<AsyncResult<Void>> handler) {
    throw new UnsupportedOperationException();
  }

  public void delayFlowable(HttpServer server) {
    server.requestHandler(request -> {
      if (request.method() == HttpMethod.POST) {

        // Stop receiving buffers
        request.pause();

        checkAuth(res -> {

          // Now we can receive buffers again
          request.resume();

          if (res.succeeded()) {
            Flowable<Buffer> flowable = request.toFlowable();
            flowable.subscribe(buff -> {
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

  public void maybe(Vertx vertx, int dnsPort, String dnsHost, String ipAddress) {

    DnsClient client = vertx.createDnsClient(dnsPort, dnsHost);

    // Obtain a maybe that performs the actual reverse lookup on subscribe
    Maybe<String> maybe = client.rxReverseLookup(ipAddress);

    // Subscribe to perform the lookup
    maybe.
      subscribe(
        name -> {
          // Lookup produced a result
        },
        failure -> {
          // Lookup failed
        },
        () -> {
          // Lookup produced no result
        }
      );
  }

  public void completable(HttpServer server) {

    // Obtain a completable that performs the actual close on subscribe
    Completable single = server.rxClose();

    // Subscribe to close the server
    single.
      subscribe(
        () -> {
          // Server is closed
        },
        failure -> {
          // Server closed but encountered issue
        }
      );
  }

  public void executeBlockingAdapter(io.vertx.core.Vertx vertx) {
    Maybe<String> maybe = MaybeHelper.toMaybe(handler -> {
      vertx.executeBlocking(() -> invokeBlocking()).onComplete(handler);
    });
  }

  private String invokeBlocking() {
    return null;
  }

  public void scheduler(Vertx vertx) {
    Scheduler scheduler = RxHelper.scheduler(vertx);
    Observable<Long> timer = Observable.interval(100, 100, TimeUnit.MILLISECONDS, scheduler);
  }

  public void scheduler(WorkerExecutor workerExecutor) {
    Scheduler scheduler = RxHelper.blockingScheduler(workerExecutor);
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
    fileSystem
      .rxOpen("/data.txt", new OpenOptions())
      .flatMapObservable(file -> file.toObservable())
      .compose(ObservableHelper.unmarshaller((MyPojo.class)))
      .subscribe(mypojo -> {
        // Process the object
      });
  }

  public void deployVerticle(Vertx vertx, Verticle verticle) {
    Single<String> deployment = RxHelper.deployVerticle(vertx, verticle);

    deployment.subscribe(id -> {
      // Deployed
    }, err -> {
      // Could not deploy
    });
  }

  public void embedded() {
    Vertx vertx = io.vertx.rxjava3.core.Vertx.vertx();
  }

  public void verticle() {
    class MyVerticle extends AbstractVerticle {
      public void start() {
        // Use Rxified Vertx here
      }
    }
  }

  public void rxStart() {
    class MyVerticle extends AbstractVerticle {
      public Completable rxStart() {
        return vertx.createHttpServer()
          .requestHandler(req -> req.response().end("Hello World"))
          .rxListen()
          .ignoreElement();
      }
    }
  }

  public void eventBusMessages(Vertx vertx) {
    EventBus eb = vertx.eventBus();
    MessageConsumer<String> consumer = eb.<String>consumer("the-address");
    Flowable<Message<String>> flowable = consumer.toFlowable();
    Disposable sub = flowable.subscribe(msg -> {
      // Got message
    });

    // Unregisters the stream after 10 seconds
    vertx.setTimer(10000, id -> {
      sub.dispose();
    });
  }

  public void eventBusBodies(Vertx vertx) {
    EventBus eb = vertx.eventBus();
    MessageConsumer<String> consumer = eb.<String>consumer("the-address");
    Flowable<String> flowable = consumer.bodyStream().toFlowable();
  }

  public void eventBusMapReduce(Vertx vertx) {
    Flowable<Double> flowable = vertx.eventBus().
        <Double>consumer("heat-sensor").
        bodyStream().
        toFlowable();

    flowable.
        buffer(1, TimeUnit.SECONDS).
        map(samples -> samples.
            stream().
            collect(Collectors.averagingDouble(d -> d))).
        subscribe(heat -> {
          vertx.eventBus().send("news-feed", "Current heat is " + heat);
        });
  }

  public void webSocketServerBuffer(Flowable<ServerWebSocket> socketObservable) {
    socketObservable.subscribe(
        socket -> {
          Flowable<Buffer> dataObs = socket.toFlowable();
          dataObs.subscribe(buffer -> {
            System.out.println("Got message " + buffer.toString("UTF-8"));
          });
        }
    );
  }

  public void httpClient(Vertx vertx) {
    HttpClient client = vertx.createHttpClient();
    client.rxRequest(HttpMethod.GET, 8080, "localhost", "/")
      .flatMap(request -> request
        .rxSend()
        .flatMap(response -> {
          if (response.statusCode() == 200) {
            return response.body();
          } else {
            return Single.error(new NoStackTraceThrowable("Invalid response"));
          }
        }))
      .subscribe(body -> {
        // Process the body
      });
  }

  public void httpClientResponseStream(Vertx vertx) {
    HttpClient client = vertx.createHttpClient();
    client.rxRequest(HttpMethod.GET, 8080, "localhost", "/")
      .flatMapPublisher(request -> request
        .rxSend()
        .flatMapPublisher(response -> {
          if (response.statusCode() == 200) {
            return response.toFlowable();
          } else {
            return Flowable.error(new NoStackTraceThrowable("Invalid response"));
          }
        }))
      .subscribe(chunk -> {
        // Process the response chunks
      });
  }

  public void webSocketClient(Vertx vertx) {
    HttpClient client = vertx.createHttpClient(new HttpClientOptions());
    client.rxWebSocket(8080, "localhost", "/the_uri").subscribe(
        ws -> {
          // Use the websocket
        },
        error -> {
          // Could not connect
        }
    );
  }

  public void webSocketClientBuffer(Flowable<WebSocket> socketObservable) {
    socketObservable.subscribe(
        socket -> {
          Flowable<Buffer> dataObs = socket.toFlowable();
          dataObs.subscribe(buffer -> {
            System.out.println("Got message " + buffer.toString("UTF-8"));
          });
        }
    );
  }

  public void httpClientRequest(Vertx vertx) {
    HttpClient client = vertx.createHttpClient(new HttpClientOptions());
    Single<HttpClientResponse> request = client
      .rxRequest( HttpMethod.GET, 8080, "localhost", "/the_uri")
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
    request.subscribe(
      response -> {
        Flowable<Buffer> flowable = response.toFlowable();
        flowable.forEach(
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
    request.
      flatMapPublisher(HttpClientResponse::toFlowable).
      forEach(
        buffer -> {
          // Process buffer
        }
      );
  }

  public void httpServerRequestObservable(HttpServerRequest request) {
    Observable<Buffer> observable = request.toObservable();
  }

  public void httpServerRequestObservableUnmarshall(HttpServerRequest request) {
    Flowable<MyPojo> flowable = request.
      toFlowable().
      compose(FlowableHelper.unmarshaller(MyPojo.class));
  }

  public void writeStreamSubscriberAdapter(Flowable<io.vertx.core.buffer.Buffer> flowable, io.vertx.core.http.HttpServerResponse response) {
    response.setChunked(true);
    WriteStreamSubscriber<io.vertx.core.buffer.Buffer> subscriber = io.vertx.rxjava3.RxHelper.toSubscriber(response);
    flowable.subscribe(subscriber);
  }

  public void rxWriteStreamSubscriberAdapter(Flowable<Buffer> flowable, HttpServerResponse response) {
    response.setChunked(true);
    flowable.subscribe(response.toSubscriber());
  }

  public void writeStreamSubscriberAdapterCallbacks(Flowable<Buffer> flowable, HttpServerResponse response) {
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

    flowable.subscribe(subscriber);
  }
}
