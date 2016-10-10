package io.vertx.test.reactivex;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.reactivex.core.Context;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.core.eventbus.MessageConsumer;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpClientRequest;
import io.vertx.reactivex.core.http.HttpClientResponse;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.core.http.ServerWebSocket;
import io.vertx.reactivex.core.http.WebSocket;
import io.vertx.reactivex.core.net.NetServer;
import io.vertx.reactivex.core.net.NetSocket;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JavaIntegrationTest extends VertxTestBase {

  private Vertx vertx;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    vertx = new Vertx(super.vertx);
  }

  private <T> T inContext(Supplier<T> handler) throws Exception {
    Context ctx = vertx.getOrCreateContext();
    CompletableFuture<T> fut = new CompletableFuture<T>();
    ctx.runOnContext(v -> {
      try {
        T t = handler.get();
        fut.complete(t);
      } catch (Exception e) {
        fut.completeExceptionally(e);
      }
    });
    return fut.get(10, TimeUnit.SECONDS);
  }

  @Test
  public void testConsumeBodyStream() throws Exception {
    EventBus eb = vertx.eventBus();
    MessageConsumer<String> consumer = eb.<String>consumer("the-address");
    Flowable<String> obs = inContext(() -> consumer.bodyStream().toFlowable());
    List<String> items = new ArrayList<>();
    obs.subscribe(new Subscriber<String>() {
      Subscription subscription;
      @Override
      public void onSubscribe(Subscription s) {
        subscription = s;
        s.request(Long.MAX_VALUE);
      }
      @Override
      public void onNext(String s) {
        items.add(s);
        if (items.size() == 3) {
          subscription.cancel();
          assertEquals(Arrays.asList("msg1", "msg2", "msg3"), items);
          waitUntil(() -> !consumer.isRegistered());
          testComplete();
        }
      }
      @Override
      public void onError(Throwable throwable) {
        fail(throwable.getMessage());
      }
      @Override
      public void onComplete() {
        fail();
      }
    });
    waitUntil(consumer::isRegistered);
    eb.send("the-address", "msg1");
    eb.send("the-address", "msg2");
    eb.send("the-address", "msg3");
    await();
  }

  @Test
  public void testRegisterAgain() {
    EventBus eb = vertx.eventBus();
    MessageConsumer<String> consumer = eb.<String>consumer("the-address");
    Flowable<String> obs = consumer.bodyStream().toFlowable();
    obs.subscribe(new Subscriber<String>() {
      Subscription sub;
      public void onSubscribe(Subscription s) {
        sub = s;
        sub.request(Long.MAX_VALUE);
      }
      public void onNext(String s) {
        assertEquals("msg1", s);
        sub.cancel();
        assertFalse(consumer.isRegistered());
        obs.subscribe(new Subscriber<String>() {
          Subscription sub;
          public void onSubscribe(Subscription s) {
            sub = s;
            sub.request(Long.MAX_VALUE);
          }
          public void onNext(String s) {
            assertEquals("msg2", s);
            sub.cancel();
            assertFalse(consumer.isRegistered());
            testComplete();
          }
          public void onError(Throwable throwable) {
            fail("Was not esxpecting error " + throwable.getMessage());
          }
          public void onComplete() {
            fail();
          }
        });
        eb.send("the-address", "msg2");
      }
      public void onError(Throwable throwable) {
        fail("Was not expecting error " + throwable.getMessage());
      }
      public void onComplete() {
        fail();
      }
    });
    eb.send("the-address", "msg1");
    await();
  }

  @Test
  public void testFlowableUnsubscribeDuringObservation() {
    EventBus eb = vertx.eventBus();
    MessageConsumer<String> consumer = eb.<String>consumer("the-address");
    Flowable<String> obs = consumer.bodyStream().toFlowable();
    Flowable<String> a = obs.take(4);
    List<String> obtained = new ArrayList<>();
    a.subscribe(new Subscriber<String>() {
      public void onComplete() {
        assertEquals(Arrays.asList("msg0", "msg1", "msg2", "msg3"), obtained);
        assertFalse(consumer.isRegistered());
        testComplete();
      }
      public void onError(Throwable e) {
        fail(e.getMessage());
      }
      public void onNext(String str) {
        obtained.add(str);
      }
      public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
      }
    });
    for (int i = 0; i < 7; i++) {
      eb.send("the-address", "msg" + i);
    }
    await();
  }

  @Test
  public void testConcatReplies() {
    EventBus eb = vertx.eventBus();
    eb.<String>consumer("the-address", msg -> {
      msg.reply(msg.body());
    });
    Single<Message<String>> obs1 = eb.sendSingle("the-address", "msg1");
    Single<Message<String>> obs2 = eb.sendSingle("the-address", "msg2");
    eb.send("the-address", "done", reply -> {
      Flowable<Message<String>> all = Single.concat(obs1, obs2);
      LinkedList<String> values = new LinkedList<String>();
      all.subscribe(next -> {
        values.add(next.body());
      }, err -> {
        fail();
      }, () -> {
        assertEquals(Arrays.asList("msg1", "msg2"), values);
        testComplete();
      });
    });
    await();
  }

  @Test
  public void testObservableNetSocket() throws Exception {
    NetServer server = vertx.createNetServer(new NetServerOptions().setPort(1234).setHost("localhost"));
    Flowable<NetSocket> socketObs = inContext(() -> server.connectStream().toFlowable());
    socketObs.subscribe(socket -> {
      Flowable<Buffer> dataObs = socket.toFlowable();
      LinkedList<Buffer> buffers = new LinkedList<>();
      dataObs.subscribe(buffers::add, err -> fail(err.getMessage()), () -> {
        assertEquals(1, buffers.size());
        assertEquals("foo", buffers.get(0).toString("UTF-8"));
        server.close();
      });
    }, err -> fail(err.getMessage()), this::testComplete);
    Single<NetServer> onListen = server.listenSingle();
    onListen.subscribe(
        s -> vertx.createNetClient(new NetClientOptions()).connect(1234, "localhost", ar -> {
          assertTrue(ar.succeeded());
          NetSocket so = ar.result();
          so.write("foo");
          so.close();
        }),
        error -> fail(error.getMessage())
    );
    await();
  }

  @Test
  public void testObservableWebSocket() {
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"));
    Flowable<ServerWebSocket> socketObs = server.websocketStream().toFlowable();
    socketObs.subscribe(ws -> {
      Flowable<Buffer> dataObs = ws.toFlowable();
      LinkedList<Buffer> buffers = new LinkedList<>();
      dataObs.subscribe(buffers::add, err -> fail(err.getMessage()), () -> {
        assertEquals(1, buffers.size());
        assertEquals("foo", buffers.get(0).toString("UTF-8"));
        server.close();
      });
    }, err -> fail(err.getMessage()), this::testComplete);
    server.listenSingle().subscribe(
        s -> vertx.createHttpClient(new HttpClientOptions()).websocket(8080, "localhost", "/some/path", ws -> {
          ws.write(Buffer.buffer("foo"));
          ws.close();
        }),
        error -> fail(error.getMessage())
    );
    await();
  }

  @Test
  public void testObservableHttpRequest() throws Exception {
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"));
    Flowable<HttpServerRequest> socketObs = inContext(() -> server.requestStream().toFlowable());
    socketObs.subscribe(new Subscriber<HttpServerRequest>() {
      public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
      }
      public void onNext(HttpServerRequest o) {
        Flowable<Buffer> dataObs = o.toFlowable();
        dataObs.subscribe(new Subscriber<Buffer>() {
          LinkedList<Buffer> buffers = new LinkedList<>();
          public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
          }
          public void onNext(Buffer buffer) {
            buffers.add(buffer);
          }
          public void onError(Throwable e) {
            fail(e.getMessage());
          }
          public void onComplete() {
            assertEquals(1, buffers.size());
            assertEquals("foo", buffers.get(0).toString("UTF-8"));
            server.close();
          }
        });
      }
      public void onError(Throwable e) {
        fail(e.getMessage());
      }
      public void onComplete() {
        testComplete();
      }
    });
    Single<HttpServer> onListen = server.listenSingle();
    onListen.subscribe(
        s -> {
          HttpClientRequest req = vertx.createHttpClient(new HttpClientOptions()).request(HttpMethod.PUT, 8080, "localhost", "/some/path", resp -> {
          });
          req.putHeader("Content-Length", "3");
          req.write("foo");
        },
        error -> fail(error.getMessage())
    );
    await();
  }
  @Test
  public void testConcatOperator() {
    Flowable<Long> o1 = vertx.timerStream(100).toFlowable();
    Flowable<Long> o2 = vertx.timerStream(100).toFlowable();
    Flowable<Long> obs = Flowable.concat(o1, o2);
    AtomicInteger count = new AtomicInteger();
    obs.subscribe(msg -> count.incrementAndGet(),
        err -> fail(),
        () -> {
          assertEquals(2, count.get());
          testComplete();
        });
    await();
  }
/*

  @Test
  public void testScheduledTimer() {
    vertx.runOnContext(v -> {
      long startTime = System.currentTimeMillis();
      Context initCtx = Vertx.currentContext();
      Observable.timer(100, 100, TimeUnit.MILLISECONDS, io.vertx.rxjava.core.RxHelper.scheduler(vertx)).take(10).subscribe(new Observer<Long>() {
        public void onNext(Long value) {
          assertEquals(initCtx.getDelegate(), Vertx.currentContext().getDelegate());
        }

        public void onError(Throwable e) {
          fail("unexpected failure");
        }

        public void onCompleted() {
          long timeTaken = System.currentTimeMillis() - startTime;
          assertTrue("Was expecting to have time taken | " + timeTaken + " -  1000 | < 200", Math.abs(timeTaken - 1000) < 1000);
          testComplete();
        }
      });
    });
    await();
  }

  @Test
  public void testScheduledBuffer() {
    vertx.runOnContext(v -> {
      long startTime = System.currentTimeMillis();
      Context initCtx = Vertx.currentContext();
      Observable
          .timer(10, 10, TimeUnit.MILLISECONDS, io.vertx.rxjava.core.RxHelper.scheduler(vertx))
          .buffer(100, TimeUnit.MILLISECONDS, io.vertx.rxjava.core.RxHelper.scheduler(vertx))
          .take(10)
          .subscribe(new Observer<List<Long>>() {
            private int eventCount = 0;

            public void onNext(List<Long> value) {
              eventCount++;
              assertEquals(initCtx.getDelegate(), Vertx.currentContext().getDelegate());
            }

            public void onError(Throwable e) {
              fail("unexpected failure");
            }

            public void onCompleted() {
              long timeTaken = System.currentTimeMillis() - startTime;
              assertEquals(10, eventCount);
              assertTrue("Was expecting to have time taken | " + timeTaken + " -  1000 | < 200", Math.abs(timeTaken - 1000) < 1000);
              testComplete();
            }
          });
    });
    await();
  }

  @Test
  public void testTimeMap() {
    vertx.runOnContext(v -> {
      Context initCtx = Vertx.currentContext();
      EventBus eb = vertx.eventBus();
      ReadStream<String> consumer = eb.<String>localConsumer("the-address").bodyStream();
      Observer<String> observer = new Subscriber<String>() {
        boolean first = true;
        @Override
        public void onNext(String s) {
          if (first) {
            first = false;
            assertEquals(initCtx.getDelegate(), Vertx.currentContext().getDelegate());
            assertEquals("msg1msg2msg3", s);
            testComplete();
          }
        }
        @Override
        public void onError(Throwable e) {
          fail(e.getMessage());
        }
        @Override
        public void onCompleted() {
        }
      };
      Observable<String> observable = consumer.toObservable();
      observable.
          buffer(500, TimeUnit.MILLISECONDS, io.vertx.rxjava.core.RxHelper.scheduler(vertx)).
          map(samples -> samples.stream().reduce("", (a, b) -> a + b)).
          subscribe(observer);
      eb.send("the-address", "msg1");
      eb.send("the-address", "msg2");
      eb.send("the-address", "msg3");
    });
    await();
  }

  @Test
  public void testObserverToFuture() {
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080)).requestHandler(req -> {});
    AtomicInteger count = new AtomicInteger();
    Observer<HttpServer> observer = new Observer<HttpServer>() {
      @Override
      public void onCompleted() {
        server.close();
        assertEquals(1, count.get());
        testComplete();
      }

      @Override
      public void onError(Throwable e) {
        fail(e.getMessage());
      }

      @Override
      public void onNext(HttpServer httpServer) {
        count.incrementAndGet();
      }
    };
    Observable<HttpServer> onListen = server.listenObservable();
    onListen.subscribe(observer);
    await();
  }

  @Test
  public void testObserverToHandler() throws Exception {
    AtomicInteger count = new AtomicInteger();
    Observer<Long> observer = new Observer<Long>() {
      @Override
      public void onCompleted() {
        assertEquals(1, count.get());
        testComplete();
      }

      @Override
      public void onError(Throwable e) {
        fail(e.getMessage());
      }

      @Override
      public void onNext(Long l) {
        count.incrementAndGet();
      }
    };
    vertx.setTimer(1, RxHelper.toHandler(observer));
    await();
  }
*/
  @Test
  public void testHttpClient() {
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
    server.requestStream().handler(req -> {
      req.response().setChunked(true).end("some_content");
    });
    server.listen(ar -> {
      HttpClient client = vertx.createHttpClient(new HttpClientOptions());
      client.request(HttpMethod.GET, 8080, "localhost", "/the_uri", resp -> {
        Buffer content = Buffer.buffer();
        Flowable<Buffer> observable = resp.toFlowable();
        observable.subscribe(content::appendBuffer, err -> fail(), () -> {
          server.close();
          assertEquals("some_content", content.toString("UTF-8"));
          testComplete();
        });
      }).end();
    });
    await();
  }

  @Test
  public void testHttpClientFlatMap() {
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
    server.requestStream().handler(req -> {
      req.response().setChunked(true).end("some_content");
    });
    server.listen(ar -> {
      HttpClient client = vertx.createHttpClient(new HttpClientOptions());
      HttpClientRequest req = client.request(HttpMethod.GET, 8080, "localhost", "/the_uri");
      Flowable<HttpClientResponse> obs =  req.toFlowable();
      Buffer content = Buffer.buffer();
      obs.flatMap(HttpClientResponse::toFlowable).subscribe(
          content::appendBuffer,
          err -> fail(), () -> {
        server.close();
        assertEquals("some_content", content.toString("UTF-8"));
        testComplete();
      });
      req.end();
    });
    await();
  }

/*
  @Test
  public void testHttpClientFlatMapUnmarshallPojo() {
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
    server.requestStream().handler(req -> {
      req.response().setChunked(true).end("{\"foo\":\"bar\"}");
    });
    server.listen(ar -> {
      HttpClient client = vertx.createHttpClient(new HttpClientOptions());
      HttpClientRequest req = client.request(HttpMethod.GET, 8080, "localhost", "/the_uri");
      Observable<HttpClientResponse> obs =  req.toObservable();
      ArrayList<MyPojo> objects = new ArrayList<>();
      obs.flatMap(HttpClientResponse::toObservable).
          lift(io.vertx.rxjava.core.RxHelper.unmarshaller(MyPojo.class)).
          forEach(
              objects::add,
              err -> fail(), () -> {
                server.close();
                assertEquals(Arrays.asList(new MyPojo("bar")), objects);
                testComplete();
              });;
      req.end();
    });
    await();
  }
*/
  @Test
  public void testHttpClientConnectionFailure() {
    HttpClient client = vertx.createHttpClient(new HttpClientOptions());
    HttpClientRequest req = client.request(HttpMethod.GET, 9998, "255.255.255.255", "/the_uri");
    Flowable<HttpClientResponse> obs = req.toFlowable(); // Should be single
    obs.subscribe(
        buffer -> fail(),
        err -> testComplete(),
        this::fail);
    req.end();
    await();
  }

  @Test
  public void testHttpClientConnectionFailureFlatMap() {
    HttpClient client = vertx.createHttpClient(new HttpClientOptions());
    HttpClientRequest req = client.request(HttpMethod.GET, 9998, "255.255.255.255", "/the_uri");
    Flowable<HttpClientResponse> obs = req.toFlowable();
    obs.flatMap(HttpClientResponse::toFlowable).subscribe(
        buffer -> fail(),
        err -> testComplete(),
        this::fail);
    req.end();
    await();
  }

  @Test
  public void testWebsocketClient() {
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
    server.websocketStream().handler(ws -> {
      ws.write(Buffer.buffer("some_content"));
      ws.close();
    });
    server.listen(ar -> {
      HttpClient client = vertx.createHttpClient(new HttpClientOptions());
      client.websocket(8080, "localhost", "/the_uri", ws -> {
        Buffer content = Buffer.buffer();
        Flowable<Buffer> observable = ws.toFlowable();
        observable.subscribe(content::appendBuffer, err -> fail(), () -> {
          server.close();
          assertEquals("some_content", content.toString("UTF-8"));
          testComplete();
        });
      });
    });
    await();
  }

  @Test
  public void testWebsocketClientFlatMap() {
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
    server.websocketStream().handler(ws -> {
      ws.write(Buffer.buffer("some_content"));
      ws.close();
    });
    server.listen(ar -> {
      HttpClient client = vertx.createHttpClient(new HttpClientOptions());
      Buffer content = Buffer.buffer();
      client.
          websocketStream(8080, "localhost", "/the_uri").
          toFlowable().
          flatMap(WebSocket::toFlowable).
          subscribe(content::appendBuffer, err -> fail(), () -> {
            server.close();
            assertEquals("some_content", content.toString("UTF-8"));
            testComplete();
          });
    });
    await();
  }
/*

  @Test
  public void testGetHelper() throws Exception {
    CountDownLatch listenLatch = new CountDownLatch(1);
    HttpServer server = vertx.createHttpServer();
    AtomicInteger count = new AtomicInteger();
    server.requestHandler(req -> {
      req.response().end(Buffer.buffer("request=" + count.getAndIncrement()));
    }).listen(8080, onSuccess(s -> {
      listenLatch.countDown();
    }));
    awaitLatch(listenLatch);
    HttpClient client = vertx.createHttpClient();
    Observable<HttpClientResponse> obs = io.vertx.rxjava.core.RxHelper.get(client, 8080, "localhost", "/foo");
    List<Buffer> bodies = Collections.synchronizedList(new ArrayList<>());
    CountDownLatch reqLatch = new CountDownLatch(1);
    obs.subscribe(resp -> {
      resp.toObservable().subscribe(bodies::add, this::fail, reqLatch::countDown);
    }, this::fail);
    awaitLatch(reqLatch);
    obs.subscribe(resp -> {
      resp.toObservable().subscribe(bodies::add, this::fail, () -> {
        assertEquals(Arrays.asList("request=0", "request=1"), bodies.stream().map(Buffer::toString).collect(Collectors.toList()));
        testComplete();
      });
    }, this::fail);
    await();
  }

  @Test
  public void testDeployVerticle() throws Exception {
    CountDownLatch deployLatch = new CountDownLatch(2);
    io.vertx.rxjava.core.RxHelper.deployVerticle(vertx, new AbstractVerticle() {
      @Override
      public void start() {
        deployLatch.countDown();
      }
    }).subscribe(resp -> {
      deployLatch.countDown();
    });
    awaitLatch(deployLatch);
  }
*/
}
