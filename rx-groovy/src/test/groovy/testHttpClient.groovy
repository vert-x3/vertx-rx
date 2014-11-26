import io.vertx.core.http.HttpMethod
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.http.HttpClient
import io.vertx.groovy.core.http.HttpServer

HttpServer server = vertx.createHttpServer(port: 8080);
server.requestStream().handler({ req ->
  req.response().setChunked(true).end("some_content");
});
server.listen({ ar ->
  HttpClient client = vertx.createHttpClient();
  client.request(HttpMethod.GET, 8080, "localhost", "/the_uri", { resp ->
    def content = Buffer.buffer();
    def observable = resp.toObservable();
    observable.forEach(content.&appendBuffer, { err -> test.fail() } , { ->
      server.close();
      test.assertEquals("some_content", content.toString("UTF-8"));
      test.testComplete();
    });
  }).end();
});
test.await();
