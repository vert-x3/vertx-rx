vertx-rxjava
========

Support for a Vert.x API based on RxJava.

# Handler<AsyncResult<T>> support

Methods with an `Handler<AsyncResult<T>>` have
an `Observable<T>` counter part,:

~~~~
createServer().listen(server -> {});
~~~~

can be rewritten:

~~~~
Observable<HttpServer> futureServer = createServer().listenObservable();
futureServer.thenAccept(server -> {});
~~~~

# ReadStream<T> support

The `ReadStream` interface has a `toObservable()` method that converts the object to an `Observable` to use instead
of the stream:

```
Observable<Long> stream = vertx.periodicStream(1).toObservable();
stream.subscribe(l -> {
  System.out.println("Tick");
});
```

# Usage

## Embedded

Wrap `io.core.vertx.Vertx`:

~~~~
Vertx vertx = new io.vertx.rxjava.core.Vertx(io.core.vertx.Vertx.verts());
~~~~

## As a Verticle

Let `io.vertx.lang.rxjava.AbstractVerticle` wrap it for you:

~~~~
public class MyVerticle extends io.vertx.lang.rxjava.AbstractVerticle {
  public void start() {
    // Use wrapped Vertx here
  }
}
~~~~

# Todo

- more things...