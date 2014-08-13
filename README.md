vertx-java
========

Support for a Vert.x API based on Java 8 CompletableFuture. Methods with an `Handler<AsyncResult<T>>` have
a `CompletableFuture` counter part, for instance:

~~~~
createServer().listen(server -> {});
~~~~

can be rewritten:

~~~~
CompletableFuture<HttpServer> futureServer = createServer().listenFuture();
futureServer.thenAccept(server -> {});
~~~~

# Usage

## Wrap `io.core.vertx.Vertx`

~~~~
Vertx vertx = new io.vertx.java.core.Vertx(io.core.vertx.Vertx.verts());
~~~~

## Let `io.vertx.lang.java.AbstractVerticle` wrap it for you

~~~~
public class MyVerticle extends io.vertx.lang.java.AbstractVerticle {
  public void start() {
    // Use wrapped Vertx here
  }
}
~~~~
