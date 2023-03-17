package io.vertx.it;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.lang.rx.it.WineAndCheese;
import io.vertx.rxjava3.core.FlowableHelper;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.ext.web.client.HttpResponse;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.codec.BodyCodec;
import io.vertx.test.core.VertxTestBase;
import org.junit.Ignore;
import org.junit.Test;

public class WebClientTest extends VertxTestBase {

  private Vertx vertx;
  private WebClient client;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    vertx = new Vertx(super.vertx);
  }

  @Test
  public void testGet() {
    int times = 5;
    waitFor(times);
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
    server.requestStream().handler(req -> req.response().setChunked(true).end("some_content").subscribe());
    try {
      server.listen().blockingGet();
      client = WebClient.wrap(vertx.createHttpClient(new HttpClientOptions()));
      Single<HttpResponse<Buffer>> single = client
        .get(8080, "localhost", "/the_uri")
        .as(BodyCodec.buffer())
        .send();
      for (int i = 0; i < times; i++) {
        single.subscribe(resp -> {
          Buffer body = resp.body();
          assertEquals("some_content", body.toString("UTF-8"));
          complete();
        }, this::fail);
      }
      await();
    } finally {
      server.close();
    }
  }

  @Test
  public void testPost() {
    int times = 5;
    waitFor(times);
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
    server.requestStream().handler(req -> req.bodyHandler(buff -> {
      assertEquals("onetwothree", buff.toString());
      req.response().end("").subscribe();
    }));
    try {
      server.listen().blockingGet();
      client = WebClient.wrap(vertx.createHttpClient(new HttpClientOptions()));
      Observable<Buffer> stream = Observable.just(Buffer.buffer("one"), Buffer.buffer("two"), Buffer.buffer("three"));
      Single<HttpResponse<Buffer>> single = client
        .post(8080, "localhost", "/the_uri")
        .sendStream(stream.toFlowable(BackpressureStrategy.BUFFER));
      for (int i = 0; i < times; i++) {
        single.subscribe(resp -> complete(), this::fail);
      }
      await();
    } finally {
      server.close();
    }
  }

  @Test
  public void testResponseMissingBody() throws Exception {
    int times = 5;
    waitFor(times);
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
    server.requestStream().handler(req -> {
      req.response().setStatusCode(403).end("");
    });
    try {
      server.listen().blockingGet();
      client = WebClient.wrap(vertx.createHttpClient(new HttpClientOptions()));
      Single<HttpResponse<Buffer>> single = client
        .get(8080, "localhost", "/the_uri")
        .send();
      for (int i = 0; i < times; i++) {
        single.subscribe(resp -> {
          assertEquals(403, resp.statusCode());
          assertNull(resp.body());
          complete();
        }, this::fail);
      }
      await();
    } finally {
      server.close();
    }
  }

  @Test
  public void testResponseBodyAsAsJsonMapped() throws Exception {
    JsonObject expected = new JsonObject().put("cheese", "Goat Cheese").put("wine", "Condrieu");
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
    server.requestStream().handler(req -> req.response().end(expected.encode()).subscribe());
    try {
      server.listen().blockingGet();
      client = WebClient.wrap(vertx.createHttpClient(new HttpClientOptions()));
      Single<HttpResponse<WineAndCheese>> single = client
        .get(8080, "localhost", "/the_uri")
        .as(BodyCodec.json(WineAndCheese.class))
        .send();
      single.subscribe(resp -> {
        assertEquals(200, resp.statusCode());
        assertEquals(new WineAndCheese().setCheese("Goat Cheese").setWine("Condrieu"), resp.body());
        testComplete();
      }, this::fail);
      await();
    } finally {
      server.close();
    }
  }

  @Test
  public void testErrorHandling() throws Exception {
    try {
      client = WebClient.wrap(vertx.createHttpClient(new HttpClientOptions()));
      Single<HttpResponse<WineAndCheese>> single = client
              .get(-1, "localhost", "/the_uri")
              .as(BodyCodec.json(WineAndCheese.class))
              .send();
      single.subscribe(resp -> fail(), error -> {
        assertEquals(IllegalArgumentException.class, error.getClass());
        testComplete();
      });
      await();
    } catch (Throwable t) {
      fail(t);
    }
  }
}
