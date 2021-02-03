package io.vertx.it.discovery;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.eventbus.EventBus;
import io.vertx.rxjava3.core.eventbus.MessageConsumer;
import io.vertx.rxjava3.core.http.HttpClient;
import io.vertx.rxjava3.ext.mongo.MongoClient;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.it.discovery.service.HelloService;
import io.vertx.rxjava3.redis.client.Redis;
import io.vertx.rxjava3.servicediscovery.ServiceDiscovery;
import io.vertx.rxjava3.servicediscovery.ServiceReference;
import io.vertx.rxjava3.servicediscovery.types.*;

import static io.vertx.it.discovery.ServiceDiscoveryTest.DISCOVERY_OPTIONS;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MyRX2Verticle extends AbstractVerticle {


  @Override
  public void start() {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, DISCOVERY_OPTIONS);
    EventBus eb = vertx.eventBus();

    eb.consumer("http-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-http-service"))
        .subscribe(res -> {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(res);
          if (reference == null) {
            message.fail(0, "FAIL - reference is null");
          } else {
            HttpClient client = reference.getAs(HttpClient.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }, err -> message.fail(0, "FAIL - No service"))
      );

    eb.consumer("http-sugar", message -> {
      JsonObject result = new JsonObject();
      HttpEndpoint.getClient(discovery, record -> record.getName().equalsIgnoreCase("my-http-service"))
        .subscribe(client -> {
          result.put("client", client.getClass().toString());
          ServiceDiscovery.releaseServiceObject(discovery, client);
          result.put("bindings", getBindings(discovery));
          message.reply(result);
        }, err -> message.fail(0, "FAIL - no service"));
    });

    eb.consumer("web-ref", message ->
      discovery
        .getRecord(rec -> rec.getName().equalsIgnoreCase("my-http-service"))
        .subscribe(res -> {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(res);
          if (reference == null) {
            message.fail(0, "FAIL - reference is null");
          } else {
            WebClient client = reference.getAs(WebClient.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }, err -> message.fail(0, "FAIL - No service")));

    eb.consumer("web-sugar", message -> {
      JsonObject result = new JsonObject();
      HttpEndpoint
        .getWebClient(discovery, record -> record.getName().equalsIgnoreCase("my-http-service"))
        .subscribe(client -> {
          result.put("client", client.getClass().toString());
          ServiceDiscovery.releaseServiceObject(discovery, client);
          result.put("bindings", getBindings(discovery));
          message.reply(result);
        }, err -> message.fail(0, "FAIL - no service"));
    });

    eb.consumer("service-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-service")).subscribe(
        res -> {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(res);
          if (reference == null) {
            message.fail(0, "FAIL - reference is null");
          } else {
            HelloService client = reference.getAs(HelloService.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }, err -> message.fail(0, "FAIL - No service")
      )
    );

    eb.consumer("service-sugar", message -> {
      JsonObject result = new JsonObject();
      EventBusService.getServiceProxy(discovery,
        record -> record.getName().equalsIgnoreCase("my-service"),
        HelloService.class,
        ar -> {
          if (ar.failed()) {
            message.fail(0, "FAIL - no service");
          } else {
            HelloService client = ar.result();
            result.put("client", client.getClass().toString());
            ServiceDiscovery.releaseServiceObject(discovery, client);
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        });
    });

    eb.consumer("redis-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-redis-data-source"))
        .subscribe(res -> {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(res);
          if (reference == null) {
            message.fail(0, "FAIL - reference is null");
          } else {
            Redis client = reference.getAs(Redis.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }, err -> message.fail(0, "FAIL - No service")));

    eb.consumer("redis-sugar", message -> {
      JsonObject result = new JsonObject();
      RedisDataSource.getRedisClient(discovery, record -> record.getName().equalsIgnoreCase("my-redis-data-source"))
        .subscribe(client -> {
          result.put("client", client.getClass().toString());
          ServiceDiscovery.releaseServiceObject(discovery, client);
          result.put("bindings", getBindings(discovery));
          message.reply(result);
        }, err -> message.fail(0, "FAIL - no service"));
    });

    eb.consumer("mongo-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-mongo-data-source"))
        .subscribe(res -> {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(res);
          if (reference == null) {
            message.fail(0, "FAIL - reference is null");
          } else {
            MongoClient client = reference.getAs(MongoClient.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }, err -> message.fail(0, "FAIL - No service")));

    eb.consumer("mongo-sugar", message -> {
      JsonObject result = new JsonObject();
      MongoDataSource.getMongoClient(discovery,
        record -> record.getName().equalsIgnoreCase("my-mongo-data-source"))
        .subscribe(client -> {
          result.put("client", client.getClass().toString());
          ServiceDiscovery.releaseServiceObject(discovery, client);
          result.put("bindings", getBindings(discovery));
          message.reply(result);
        }, err -> message.fail(0, "FAIL - no service"));
    });

    eb.consumer("source1-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-message-source-1"))
        .subscribe(res -> {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(res);
          if (reference == null) {
            message.fail(0, "FAIL - reference is null");
          } else {
            MessageConsumer<String> client = reference.getAs(MessageConsumer.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }, err -> message.fail(0, "FAIL - No service")
      )
    );

    eb.consumer("source1-sugar", message -> {
      JsonObject result = new JsonObject();
      MessageSource.getConsumer(discovery, record -> record.getName().equalsIgnoreCase("my-message-source-1"))
        .subscribe(client -> {
          result.put("client", client.getClass().toString());
          ServiceDiscovery.releaseServiceObject(discovery, client);
          result.put("bindings", getBindings(discovery));
          message.reply(result);
        }, err -> message.fail(0, "FAIL - no service"));
    });
  }

  private synchronized JsonArray getBindings(ServiceDiscovery discovery) {
    JsonArray array = new JsonArray();
    for (ServiceReference ref : discovery.bindings()) {
      array.add(ref.toString());
    }
    return array;
  }
}
