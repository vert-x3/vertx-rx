# Rx extension for Vert.x

Vert.x module adding support for Reactive Extensions (Rx) using the Rx libraries.
This allows Vert.x developers to use the Rx type-safe composable API to build Vert.x verticles.
This module provides helpers for adapting Vert.x stream and future constructs to Rx observables.

## RxJava

There are two ways for using the RxJava api:

- using the original Vert.x api with the `io.vertx.rx.java.RxHelper` helper class that provides static methods for
converting objects between Vert.x core api and RxJava api
- via the _Rxified_ Vert.x api enhancing the core Vert.x api

### Read stream support

Vert.x for Java provides `io.vertx.core.streams.ReadStream` objects, the `RxHelper` class provides a static util method
for convering such stream to a `rx.Observable`.

```
ReadStream<T> stream = ...;
Observable<T> observable = RxHelper.toObservable(stream);
```

The _Rxified_ Vert.x api provides a `toObservable()` method on `io.vertx.rxjava.core.streams.ReadStream`:

```
ReadStream<T> stream = ...;
Observable<T> observable = stream.toObservable();
```

### Handler support

The `io.vertx.ext.rx.java.RxHelper` can create an `io.vertx.ext.rx.java.ObservableHandler`: an `Observable` with a
`asHandler` method returning a `Handler<T>` implementation:

```
ObservableHandler<Long> observable = RxHelper.observableHandler();
observable.subscribe(id -> {
  // Fired
});
vertx.setTimer(1000, observable.asHandler());
```

The _Rxified_ Vert.x api does not provide a specific api for handler (todo).

### Future support

In Vert.x future objects are modelled as async result handlers and occur as last parameter of asynchronous methods.

The `io.vertx.ext.rx.java.RxHelper` can create an `io.vertx.ext.rx.java.ObservableFuture`: an `Observable` with a
`asHandler` method returning a `Handler<AsyncResult<T>>` implementation:

```
HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(1234).setHost("localhost"));
ObservableFuture<HttpServer> observable = RxHelper.observableFuture();
observable.subscribe(
  server -> {
    // Server is listening
  },
  failure -> {
    // Server could not start
  }
);
server.listen(observable.asHandler());
```

The `ObservableFuture<Server>` will get a single `HttpServer` object, if the `listen` operation fails,
the subscriber will be notified with the failure.

The helper can also turn an existing `Observer` into an handler:

```
Observer<Server> observer = ...;
Handler<AsyncResult<Server>> o = RxHelper.toFuture(observer);
```

It also works with just actions:

```
Handler<AsyncResult<Server>> o = RxHelper.toFuture(
  server -> {}, // onNext
  cause -> {},  // onError
  () -> {}      // onCompleted
);
```

The _Rxified_ Vert.x api duplicates each such method with the `Observable` suffix that returns an observable:

```
HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(1234).setHost("localhost"));
Observable<HttpServer> observable = server.listen(observable.asHandler()).toObservable();
observable.subscribe(
  server -> {
    // Server is listening
  },
  failure -> {
    // Server could not start
  }
);
```

### Scheduler support

The reactive extension sometimes needs to schedule actions, for instance `Observable#timer` create and returns
a timer that emit periodic events. By default, scheduled actions are managed by RxJava, it means that the
timer thread are not Vert.x threads and therefore not executing in a Vert.x event loop.

When an RxJava method deals with a scheduler, it accepts an overloaded method accepting an extra `Rx.Scheduler`,
the `RxHelper#scheduler(vertx)` method will return a scheduler that can be used in such places.

```
Observable<Long> timer = Observable.timer(100, 100, TimeUnit.MILLISECONDS, RxHelper.scheduler(vertx));
```

It is also possible to configure RxJava to use a scheduler by default, it can be used in your application:

```
rx.plugins.RxJavaPlugins.getInstance().registerSchedulersHook(RxHelper.schedulerHook(vertx))
```

The _Rxified_ Vert.x api does not provide a specific api for handler (todo?).

### Rxified api usage

#### Embedded

Wrap `io.core.vertx.Vertx`:

~~~~
Vertx vertx = new io.vertx.rxjava.core.Vertx(io.core.vertx.Vertx.vertx());
~~~~

#### As a Verticle

Let `io.vertx.lang.rxjava.AbstractVerticle` wrap it for you:

~~~~
public class MyVerticle extends io.vertx.lang.rxjava.AbstractVerticle {
  public void start() {
    // Use wrapped Vertx here
  }
}
~~~~

### Examples

#### Buffering + map/reduce with the event bus

```
Observable<Double> observable = vertx.eventBus().<Double>consumer("heat-sensor").bodyStream().toObservable();
observable.
     buffer(1, TimeUnit.SECONDS).
     map(samples -> samples.stream().
     collect(Collectors.averagingDouble(d -> d))).
     subscribe(heat -> {
  System.out.println("Current heat is " + heat);
  // vertx.eventBus().send("news-feed", "Current heat is " + heat);
});
```

#### HttpServer provides a ReadStream<WebSocket> for incoming connections

```
Observable<ServerWebSocket> socketObs = server.websocketStream().toObservable();
socketObs.subscribe(
  socket -> System.out.println("Web socket connect"),
  failure -> System.out.println("Should never be called"),
  () -> { System.out.println("Subscription ended or server closed"); }
```

#### WebSocket buffer stream:

```
ServerWebSocket ws = ...;
Observable<Buffer> dataObs = o.toObservable();
```

#### EventBus message stream:

```
EventBus eb = vertx.eventBus();
MessageConsumer<String> consumer = eb.<String>consumer("the-address");
Observable<Message<String>> obs = consumer.toObservable();
Subscription sub = obs.subscriber( msg -> { // Got message });
```

When the subscriber, unsubcribes, the message consumer will be unregistered automatically:

```
sub.unsucribe(); // Unregisters the stream
```

#### EventBus body stream:

```
EventBus eb = vertx.eventBus();
MessageConsumer<String> consumer = eb.<String>consumer("the-address");
Observable<String> obs = consumer.bodyStream().toObservable();
```

## RxJS

### Read stream support

Vert.x provides an `rx.vertx` module for RxJS. An read stream can be adapted to an observable with the `Rx.Observable.fromReadStream` function:

```
var stream = ...;
var Rx = require("rx.vertx");
var observable = Rx.Observable.fromReadStream(stream);
```

### Handler support

The `rx.vertx` module provides an `observableHandler` function:

```
var Rx = require("rx.vertx");
var observable = Rx.observableHandler();
observable.subscribe(
  function(evt) {
    // Got event
  }
);
vertx.setTimer(1000, observable.asHandler());
```

Rx can also turn an existing Observer into an handler:

```
var Rx = require("rx.vertx");
var observer = Rx.Observer.create(
  function(evt) {
    // Got event
  }
);
var handler = observer.toHandler();
vertx.setTimer(1000, handler);
```

### Future support

In Vert.x future objects are modelled as async result handlers and occur as last parameter of asynchronous methods.

The `rx.vertx` module provides an `observableFuture` function:

```
var server = vertx.createHttpServer({ "port":1234, "host":"localhost" });
var Rx = require("rx.vertx");
var observable = Rx.observableFuture();
observable.subscribe(
  function(server) {
    // Server is listening
  },
  function(err) {
    // Server could not start
  }
);
server.listen(observable.asHandler());
```

Rx can also turn an existing Observer into an future:

```
var observer = Rx.Observer.create(
  function(item) { ... }, // onNext
  function(err) { ... },  // onError
  function() { ... }      // onCompleted
);
var future = observer.toFuture();
```

### Scheduler support

RxJS relies on the default context method _timeout_ and _interval_ functions to schedule operations. The
vertx-js integration implements such functions providing an out of the box scheduler support.

### Examples

#### Buffering + map/reduce with the event bus

```
Rx = require("rx.time");
Rx = require("rx.vertx");
var consumer = vertx.eventBus().consumer("heat-sensor").bodyStream();
var observable = Rx.Observable.fromReadStream(consumer);
observable.
  bufferWithTime(1000).
  filter(function (arr) { return arr.length > 0; }).
  map(function (arr) { return arr.reduce(function (acc, x) { return acc + x; }, 0) / arr.length; }).
  subscribe(function (heat) {
          console.log('Current heat is: ' + heat);
});
console.log("listening");
```

## RxGroovy

### Read stream support

Vert.x API for Groovy provides `io.vertx.groovy.core.stream.ReadStream` objects, the RxGroovy provides a
Groovy extension module that adds the `toObservable` method to the read stream class.

```
ReadStream<T> stream = ...;
Observable<T> observable = stream.toObservable();
```

### Handler support

The RxJava `io.vertx.ext.rx.java.RxHelper` should be used to:
- create an `io.vertx.ext.rx.java.ObservableHandler`,
- transform actions to an handler

The RxGroovy extension module adds the `toHandler` method on the `rx.Observer` class:

```
Observer<Long> observer = ...;
Handler<Long> handler = observer.toHandler();
vertx.setTimer(1000, observable.asHandler());
```

### Future support

In Vert.x future objects are modelled as async result handlers and occur as last parameter of asynchronous methods.

The RxJava `io.vertx.ext.rx.java.RxHelper` should be used to:
- create an `io.vertx.ext.rx.java.ObservableFuture`,
- transform actions to an async result handler

The RxGroovy extension module adds the `toFuture` method on the `rx.Observer` class:

```
Observer<Server> observer = ...;
Handler<AsyncResult<Server>> o = observer.toFuture();
```

### Scheduler support

The reactive extension sometimes needs to schedule actions, for instance `Observable#timer` create and returns
a timer that emit periodic events. By default, scheduled actions are managed by RxJava, it means that the
timer thread are not Vert.x threads and therefore not executing in a Vert.x event loop.

When an RxJava method deals with a scheduler, it accepts an overloaded method accepting an extra `Rx.Scheduler`,
the RxGroovy extension module adds to the `Vertx` class the `scheduler()` method will return a scheduler that can be used in such places.

```
Observable<Long> timer = Observable.timer(100, 100, TimeUnit.MILLISECONDS, vertx.scheduler());
```

It is also possible to configure RxJava to use a scheduler by default, it can be used in your application:

```
rx.plugins.RxJavaPlugins.getInstance().registerSchedulersHook(RxHelper.schedulerHook(vertx))
```

### Examples

#### Buffering + map/reduce with the event bus

```
import java.util.concurrent.TimeUnit;

def observable = vertx.eventBus().consumer("heat-sensor").bodyStream().toObservable();
observable.
   buffer(1, TimeUnit.SECONDS).
   filter({ values -> !values.empty }).
   map({ values -> values.sum() / values.size() }).
   subscribe({ heat ->
   System.out.println("Current heat is " + heat);
});
```