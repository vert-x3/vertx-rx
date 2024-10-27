package io.vertx.reactivex.test;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Handler;
import io.vertx.core.VertxException;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.rxjava3.core.eventbus.DeliveryContext;
import io.vertx.rxjava3.core.eventbus.EventBus;
import io.vertx.rxjava3.core.file.AsyncFile;
import io.vertx.rxjava3.core.http.*;
import io.vertx.rxjava3.core.parsetools.RecordParser;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CoreApiTest extends VertxTestBase {

  private Vertx vertx;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    vertx = new Vertx(super.vertx);
  }

  @Test
  public void testDeployVerticle() throws Exception {
    CountDownLatch deployLatch = new CountDownLatch(2);
    RxHelper.deployVerticle(vertx, new AbstractVerticle() {
      @Override
      public void start() {
        deployLatch.countDown();
      }
    }).subscribe(resp -> {
      deployLatch.countDown();
    });
    awaitLatch(deployLatch);
  }

  @Test
  public void testWebSocket() {
    waitFor(2);
    AtomicLong serverReceived = new AtomicLong();
    // Set a 2 seconds timeout to force a TCP connection close
    vertx.createHttpServer(new HttpServerOptions().setIdleTimeout(2)).webSocketHandler(ws -> {
      ws.toFlowable()
        .subscribe(msg -> {
          serverReceived.incrementAndGet();
          ws.writeTextMessage("pong");
        }, err -> {
          assertEquals(1, serverReceived.get());
          complete();
        }, this::fail);
    }).listen(8080, "localhost").blockingGet();
    WebSocketClient client = vertx.createWebSocketClient();
    AtomicLong clientReceived = new AtomicLong();
    client.rxConnect(8080, "localhost", "/")
      .doAfterSuccess(ws -> {
        ws.writeTextMessage("ping");
      })
      .flatMapPublisher(WebSocket::toFlowable).subscribe(
      msg -> {
        clientReceived.incrementAndGet();
      }, err -> {
        assertEquals(1, clientReceived.get());
        complete();
      }, this::fail
    );
    await();
  }

  @Test
  public void testHttpClient() {
    vertx.createHttpServer().requestHandler(req -> {
      req.response().end("Hello World");
    }).listen(8080, "localhost").blockingGet();
    HttpClient client = vertx.createHttpClient();
    Buffer result = client.rxRequest(HttpMethod.GET, 8080, "localhost", "/")
      .flatMap(request -> request
        .rxSend()
        .flatMap(HttpClientResponse::body))
      .blockingGet();
    assertEquals("Hello World", result.toString());
  }

  @Test
  public void testHttpClientResponseStream() {
    vertx.createHttpServer().requestHandler(req -> {
      AtomicInteger counter = new AtomicInteger();
      HttpServerResponse response = req
        .response()
        .setChunked(true);
      vertx.setPeriodic(10, id -> {
        int cnt = counter.getAndIncrement();
        if (cnt < 10) {
          response.write("" + cnt);
        } else {
          vertx.cancelTimer(id);
          response.end();
        }
      });
    }).listen(8080, "localhost").blockingGet();
    HttpClient client = vertx.createHttpClient();
    String result = client.rxRequest(HttpMethod.GET, 8080, "localhost", "/")
      .flatMapPublisher(request -> request
        .rxSend()
        .flatMapPublisher(HttpClientResponse::toFlowable))
      .reduce("", (s, b) -> s + b)
      .blockingGet();
    assertEquals("0123456789", result);
  }


  @Test
  public void shouldRemoveInterceptor() {
    String headerName = UUID.randomUUID().toString();
    String headerValue = UUID.randomUUID().toString();
    Handler<DeliveryContext<Object>> interceptor = dc -> {
      dc.message().headers().add(headerName, headerValue);
      dc.next();
    };
    EventBus eventBus = vertx.eventBus();
    eventBus.addInboundInterceptor(interceptor);
    eventBus.consumer("foo", msg -> msg.reply(msg.headers().get(headerName))).completion()
      .andThen(eventBus.rxRequest("foo", "bar").flatMapCompletable(reply -> {
        if (reply.body().equals(headerValue)) {
          return Completable.complete();
        } else {
          return Completable.error(VertxException.noStackTrace("Expected msg to be intercepted"));
        }
      }))
      .andThen(Completable.fromAction(() -> {
        eventBus.removeInboundInterceptor(interceptor);
      }))
      .andThen(eventBus.rxRequest("foo", "bar").flatMapCompletable(reply -> {
        if (reply.body() == null) {
          return Completable.complete();
        } else {
          return Completable.error(VertxException.noStackTrace("Expected msg not to be intercepted"));
        }
      })).subscribe(() -> testComplete(), throwable -> fail(throwable));
    await();
  }

  @Test
  public void testPipeFailureShouldUnsubscribe() throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
        Flowable<Buffer> f = Flowable
          .<Buffer, Long>generate(
            () -> 0L,
            (state, emitter) -> {
              emitter.onNext(Buffer.buffer("Chunk " + state + "\n"));
              return state + 1;
            }
          )
          .delay(100, TimeUnit.MILLISECONDS)
          .rebatchRequests(1)
          .doOnCancel(this::testComplete);
        req.response().send(f);

      }).rxListen(8080, "localhost")
      .blockingGet();
    HttpClient client = vertx.createHttpClient();
    client.rxRequest(HttpMethod.GET, 8080, "localhost", "/")
      .flatMap(HttpClientRequest::rxSend)
      .subscribe(resp -> {
        resp
          .toFlowable()
          .take(5)
          .subscribe(buff -> {
          }, this::fail, () -> {
            resp.request().reset();
          });
      }, this::fail);
    await();
  }

  @Test
  public void testRecordParser() {
    Single<AsyncFile> source = vertx.fileSystem().rxOpen("src/test/resources/test.txt", new OpenOptions());

    waitFor(5);

    source.map(file -> RecordParser.newDelimited("\n", file.toFlowable()))
      .flatMapObservable(RecordParser::toObservable)
      .doOnNext(v -> complete())
      .doOnComplete(() -> complete())
      .ignoreElements()
      .subscribe(() -> complete(), this::fail);

    await();
  }

  @Test
  public void testHttpResponseExpectation() {
    waitFor(2);
    HttpServer server = vertx.createHttpServer().requestHandler(req -> {
        req.response().end("Hello World");
      }).listen(8080, "localhost").blockingGet();
    HttpClient client = vertx.createHttpClient();
    client.rxRequest(HttpMethod.GET, 8080, "localhost", "/")
      .flatMap(req -> req.rxSend())
      .lift(HttpResponseExpectation.status(200))
      .flatMap(resp -> resp.rxBody())
      .subscribe((body, err) -> {
        assertNull(err);
        assertEquals("Hello World", body.toString());
        complete();
      });
    client.rxRequest(HttpMethod.GET, 8080, "localhost", "/")
      .flatMap(req -> req.rxSend())
      .lift(HttpResponseExpectation.status(201))
      .flatMap(resp -> resp.rxBody())
      .subscribe((body, err) -> {
        assertNull(body);
        assertNotNull(err);
        assertEquals("Response status code 200 is not equal to 201", err.getMessage());
        complete();
      });
    await();
  }
}
