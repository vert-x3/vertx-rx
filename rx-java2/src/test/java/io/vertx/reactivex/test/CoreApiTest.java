package io.vertx.reactivex.test;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.RxHelper;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.WebSocket;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
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
    }).rxListen(8080, "localhost").blockingGet();
    HttpClient client = vertx.createHttpClient();
    AtomicLong clientReceived = new AtomicLong();
    client.rxWebSocket(8080, "localhost", "/")
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

}
