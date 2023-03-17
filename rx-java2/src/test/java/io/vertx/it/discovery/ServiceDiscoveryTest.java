package io.vertx.it.discovery;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.it.discovery.service.HelloService;
import io.vertx.it.discovery.service.HelloServiceImpl;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.impl.DiscoveryImpl;
import io.vertx.servicediscovery.types.*;
import io.vertx.serviceproxy.ProxyHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@RunWith(VertxUnitRunner.class)
public class ServiceDiscoveryTest {

  public static final ServiceDiscoveryOptions DISCOVERY_OPTIONS = new ServiceDiscoveryOptions()
    .setBackendConfiguration(new JsonObject().put("backend-name", "io.vertx.servicediscovery.impl.DefaultServiceDiscoveryBackend"));

  protected Vertx vertx;
  protected ServiceDiscovery discovery;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    discovery = new DiscoveryImpl(vertx, DISCOVERY_OPTIONS);
  }

  @After
  public void tearDown() {
    discovery.close();
    AtomicBoolean completed = new AtomicBoolean();
    vertx.close().onComplete((v) -> completed.set(true));
    await().untilAtomic(completed, is(true));

    assertThat(discovery.bindings()).isEmpty();
  }

  @Test
  public void testRX2(TestContext tc) {
    HelloService svc = new HelloServiceImpl();
    ProxyHelper.registerService(HelloService.class, vertx, svc, "my-service");

    AtomicBoolean httpEndpointPublished = new AtomicBoolean();
    AtomicBoolean serviceProxyPublished = new AtomicBoolean();
    AtomicBoolean jdbcDataSourcePublished = new AtomicBoolean();
    AtomicBoolean messageSource1Published = new AtomicBoolean();
    AtomicBoolean messageSource2Published = new AtomicBoolean();
    AtomicBoolean redisDataSourcePublished = new AtomicBoolean();
    AtomicBoolean mongoDataSourcePublished = new AtomicBoolean();

    discovery.publish(
      HttpEndpoint.createRecord("my-http-service", "localhost", 8080, "/")).onComplete(
      ar -> httpEndpointPublished.set(ar.succeeded()));

    discovery.publish(
      EventBusService.createRecord("my-service", "my-service", HelloService.class.getName())).onComplete(
      ar -> serviceProxyPublished.set(ar.succeeded()));

    discovery.publish(
      JDBCDataSource.createRecord("my-data-source",
        new JsonObject().put("url", "jdbc:hsqldb:file:target/dumb-db;shutdown=true"),
        new JsonObject().put("database", "some-raw-data"))).onComplete(ar -> jdbcDataSourcePublished.set(ar.succeeded())
    );

    discovery.publish(
      MessageSource.createRecord("my-message-source-1", "source1")).onComplete(ar -> messageSource1Published.set(ar.succeeded())
    );

    discovery.publish(
      MessageSource.createRecord("my-message-source-2", "source2", JsonObject.class.getName())).onComplete(ar -> messageSource2Published.set(ar.succeeded())
    );

    discovery.publish(
      RedisDataSource.createRecord("my-redis-data-source",
        new JsonObject().put("url", "localhost"),
        new JsonObject().put("database", "some-raw-data"))).onComplete(ar -> redisDataSourcePublished.set(ar.succeeded())
    );

    discovery.publish(
      MongoDataSource.createRecord("my-mongo-data-source",
        new JsonObject().put("connection_string", "mongodb://localhost:12345"),
        new JsonObject().put("database", "some-raw-data"))).onComplete(ar -> mongoDataSourcePublished.set(ar.succeeded())
    );

    await().untilAtomic(httpEndpointPublished, is(true));
    await().untilAtomic(serviceProxyPublished, is(true));
    await().untilAtomic(jdbcDataSourcePublished, is(true));
    await().untilAtomic(messageSource1Published, is(true));
    await().untilAtomic(messageSource2Published, is(true));
    await().untilAtomic(redisDataSourcePublished, is(true));
    await().untilAtomic(mongoDataSourcePublished, is(true));

    Async deployment = tc.async();
    Async http_ref = tc.async();
    Async http_sugar = tc.async();
    Async web_ref = tc.async();
    Async web_sugar = tc.async();
    Async svc_ref = tc.async();
    Async svc_sugar = tc.async();
    Async ds_ref = tc.async();
    Async ds_sugar = tc.async();
    Async ms_ref = tc.async();
    Async ms_sugar = tc.async();
    Async redis_ref = tc.async();
    Async redis_sugar = tc.async();
    Async mongo_ref = tc.async();
    Async mongo_sugar = tc.async();

    vertx.deployVerticle(MyRX2Verticle.class.getName()).onComplete(deployed -> {

      vertx.eventBus().<JsonObject>request("http-ref", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("HttpClient"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        http_ref.complete();
      }));

      vertx.eventBus().<JsonObject>request("http-sugar", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("HttpClient"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        http_sugar.complete();
      }));

      vertx.eventBus().<JsonObject>request("web-ref", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("WebClient"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        web_ref.complete();
      }));

      vertx.eventBus().<JsonObject>request("web-sugar", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("WebClient"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        web_sugar.complete();
      }));

      vertx.eventBus().<JsonObject>request("service-sugar", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("HelloService"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        svc_sugar.complete();
      }));

      vertx.eventBus().<JsonObject>request("service-ref", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("HelloService"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        svc_ref.complete();
      }));

      vertx.eventBus().<JsonObject>request("ds-sugar", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("JDBCClient"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        ds_sugar.complete();
      }));

      vertx.eventBus().<JsonObject>request("ds-ref", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("JDBCClient"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        ds_ref.complete();
      }));

      vertx.eventBus().<JsonObject>request("redis-sugar", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("Redis"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        redis_sugar.complete();
      }));

      vertx.eventBus().<JsonObject>request("redis-ref", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("Redis"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        redis_ref.complete();
      }));

      vertx.eventBus().<JsonObject>request("mongo-sugar", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("MongoClient"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        mongo_sugar.complete();
      }));

      vertx.eventBus().<JsonObject>request("mongo-ref", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("MongoClient"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        mongo_ref.complete();
      }));

      vertx.eventBus().<JsonObject>request("source1-sugar", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("MessageConsumer"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        ms_sugar.complete();
      }));

      vertx.eventBus().<JsonObject>request("source1-ref", "").onComplete(tc.asyncAssertSuccess(reply -> {
        tc.assertTrue(reply.body().getString("client").contains("MessageConsumer"));
        tc.assertTrue(reply.body().getString("client").contains("reactivex"));
        tc.assertTrue(reply.body().getJsonArray("bindings").isEmpty());
        ms_ref.complete();
      }));

      deployment.complete();
    });
  }
}
