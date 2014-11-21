vertx-rxjava
========

Support for a Vert.x API based on RxJava.

# Handler<AsyncResult<T>> support

Methods with an `Handler<AsyncResult<T>>` have
an `Observable<T>` counter part, for instance:

~~~~
createServer().listen(server -> {});
~~~~

can be rewritten:

~~~~
Observable<HttpServer> futureServer = createServer().listenObservable();
futureServer.thenAccept(server -> {});
~~~~

# Usage

## Wrap `io.core.vertx.Vertx`

~~~~
Vertx vertx = new io.vertx.rxjava.core.Vertx(io.core.vertx.Vertx.verts());
~~~~

## Let `io.vertx.lang.rxjava.AbstractVerticle` wrap it for you

~~~~
public class MyVerticle extends io.vertx.lang.rxjava.AbstractVerticle {
  public void start() {
    // Use wrapped Vertx here
  }
}
~~~~

# Todo

- ReadStream<T> support
- more things...