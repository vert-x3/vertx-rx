package io.vertx.reactivex.test;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.core.http.HttpClient;
import io.vertx.rxjava3.core.http.HttpClientResponse;
import io.vertx.rxjava3.core.http.HttpServerResponse;
import io.vertx.rxjava3.core.http.WebSocket;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
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
    HttpClient client = vertx.createHttpClient();
    AtomicLong clientReceived = new AtomicLong();
    client.webSocket(8080, "localhost", "/")
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
    Buffer result = client.request(HttpMethod.GET, 8080, "localhost", "/")
      .flatMap(request -> request
        .send()
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
    String result = client.request(HttpMethod.GET, 8080, "localhost", "/")
      .flatMapPublisher(request -> request
        .send()
        .flatMapPublisher(HttpClientResponse::toFlowable))
      .reduce("", (s, b) -> s + b)
      .blockingGet();
    assertEquals("0123456789", result);
  }
}
