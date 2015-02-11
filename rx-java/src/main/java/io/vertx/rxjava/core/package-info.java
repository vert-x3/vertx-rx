/**
 * = Vert.x RxJava
 * :toc: left
 *
 * = Vert.x API for RxJava
 *
 * RxJava is a popular library for composing asynchronous and event based programs using
 * observable sequences for the Java VM. Vert.x integrates naturally with RxJava, allowing to use
 * observable wherever you can use streams or asynchronous results.
 *
 * There are two ways for using the RxJava API with Vert.x:
 *
 * - via the original Vert.x API with the {@link io.vertx.rx.java.RxHelper} helper class that provides static
 * methods for converting objects between Vert.x core API and RxJava API.
 * - via the _Rxified_ Vert.x API enhancing the core Vert.x API.
 *
 * == Read stream support
 *
 * RxJava observable is a perfect match for Vert.x `ReadStream` class : both provides provides a flow of items.
 *
 * The {@link io.vertx.rx.java.RxHelper#toObservable(io.vertx.core.streams.ReadStream)} static methods converts
 * a Vert.x read stream to an `rx.Observable`:
 *
 * [source,java]
 * ----
 * {@link examples.NativeExamples#readStream(io.vertx.core.Vertx)}
 * ----
 *
 * The _Rxified_ Vert.x API provides a {@link io.vertx.rxjava.core.streams.ReadStream#toObservable()}  method on
 * {@link io.vertx.rxjava.core.streams.ReadStream}:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#readStream(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * == Handler support
 *
 * The {@link io.vertx.rx.java.RxHelper} can create an {@link io.vertx.rx.java.ObservableHandler}: an `Observable` with a
 * {@link io.vertx.rx.java.ObservableHandler#asHandler()} method returning an `Handler<T>` implementation:
 *
 * [source,java]
 * ----
 * {@link examples.NativeExamples#observableHandler(io.vertx.core.Vertx)}
 * ----
 *
 * The _Rxified_ Vert.x API does not provide a specific API for handler.
 *
 * == Async result support
 *
 * The Vert.x `Handler<AsyncResult<T>>` construct occuring as last parameter of an asynchronous methods can
 * be mapped to an observable of a single element:
 *
 * - when the callback is a success, the observer `onNext` method is called with the item
 * and the `onComplete` method is immediatly invoked after
 * - when the callback is a failure, the observer `onError` method is called
 *
 * The {@link io.vertx.rx.java.RxHelper#observableFuture()} method creates an {@link io.vertx.rx.java.ObservableFuture}:
 * an `Observable` with a {@link io.vertx.rx.java.ObservableFuture#asHandler()} method returning a `Handler<AsyncResult<T>>`
 * implementation:
 *
 * [source,java]
 * ----
 * {@link examples.NativeExamples#observableFuture(io.vertx.core.Vertx)}
 * ----
 *
 * The `ObservableFuture<Server>` will get a single `HttpServer` object, if the `listen` operation fails,
 * the subscriber will be notified with the failure.
 *
 * The {@link io.vertx.rx.java.RxHelper#toHandler(rx.Observer)} method adapts an existing `Observer` into an handler:
 *
 * [source,java]
 * ----
 * {@link examples.NativeExamples#observableToHandler()}
 * ----
 *
 * It also works with just actions:
 *
 * [source,java]
 * ----
 * {@link examples.NativeExamples#actionsToHandler()}
 * ----
 *
 * The _Rxified_ Vert.x API duplicates each such method with the `Observable` suffix that returns an observable:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#observableFuture(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * == Scheduler support
 *
 * The reactive extension sometimes needs to schedule actions, for instance `Observable#timer` creates and returns
 * a timer that emit periodic events. By default, scheduled actions are managed by RxJava, it means that the
 * timer thread are not Vert.x threads and therefore not executing in a Vert.x event loop.
 *
 * When an RxJava method deals with a scheduler, it accepts an overloaded method accepting an extra `rx.Scheduler`,
 * the {@link io.vertx.rx.java.RxHelper#scheduler(io.vertx.core.Vertx)}  method will return a scheduler that can be used
 * in such places.
 *
 * [source,java]
 * ----
 * {@link examples.NativeExamples#scheduler(io.vertx.core.Vertx)}
 * ----
 *
 * RxJava can also be reconfigured to use the Vert.x scheduler, thanks to the scheduler hook created with
 * {@link io.vertx.rx.java.RxHelper#schedulerHook(io.vertx.core.Vertx)}:
 *
 * [source,java]
 * ----
 * {@link examples.NativeExamples#schedulerHook(io.vertx.core.Vertx)}
 * ----
 *
 * The _Rxified_ Vert.x API provides also similar method on the {@link io.vertx.rxjava.core.RxHelper} class:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#scheduler(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#schedulerHook(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * == Json unmashalling
 *
 * The {@link io.vertx.rxjava.core.RxHelper#unmarshaller(java.lang.Class)} creates an `rx.Observable.Operator` that
 * transforms an `Observable<Buffer>` in json format into an object observable:
 *
 * [source,java]
 * ----
 * {@link examples.NativeExamples#unmarshaller(io.vertx.core.file.FileSystem)}
 * ----
 *
 * The same can be done with the _Rxified_ helper:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#unmarshaller(io.vertx.rxjava.core.file.FileSystem)}
 * ----
 *
 * = Rxified API
 *
 * The _Rxified_ API is a code generated version of the Vert.x API, just like the _JavaScript_ or _Groovy_
 * language. The API uses the `io.vertx.rxjava` prefix, for instance the `io.vertx.core.Vertx` class is
 * translated to the {@link io.vertx.rxjava.core.Vertx} class.
 *
 * == Embedding Rxfified Vert.x
 *
 * Just use the {@link io.vertx.rxjava.core.Vertx#vertx()} methods:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#embedded()}
 * ----
 *
 * == As a Verticle
 *
 * Extend the {@link io.vertx.rxjava.core.AbstractVerticle} class, it will wrap it for you:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#verticle()}
 * ----
 *
 * Deploying an RxJava verticle is still performed by the Java deployer and does not need a specified
 * deployer.
 *
 * = Rxified API example
 *
 * Let's study now a few examples of using Vert.x with RxJava.
 *
 * == EventBus message stream
 *
 * The event bus {@link io.vertx.rxjava.core.eventbus.MessageConsumer} provides naturally an `Observable<Message<T>>`:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#eventBusMessages(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * The {@link io.vertx.rxjava.core.eventbus.MessageConsumer} provides a stream of {@link io.vertx.rxjava.core.eventbus.Message}.
 * The {@link io.vertx.rxjava.core.eventbus.Message#body()} gives access to a new stream of message bodies if needed:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#eventBusBodies(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * RxJava map/reduce composition style can be then be used:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#eventBusMapReduce(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * == Timers
 *
 * Timer task can be created with {@link io.vertx.rxjava.core.Vertx#timerStream(long)}:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#timer(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * Periodic task can be created with {@link io.vertx.rxjava.core.Vertx#periodicStream(long)}:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#periodic(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * The observable can be cancelled with an unsubscription:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#periodicUnsubscribe(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * == Http client requests
 *
 * {@link io.vertx.rxjava.core.http.HttpClientRequest#toObservable()} provides a one shot callback with the
 * {@link io.vertx.core.http.HttpClientResponse} object. The observable reports a request failure.
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#httpClientRequest(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * The response can be processed as an `Observable<Buffer>` with the
 * {@link io.vertx.rxjava.core.http.HttpClientResponse#toObservable()} method:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#httpClientResponse(io.vertx.rxjava.core.http.HttpClientRequest)}
 * ----
 *
 * The same flow can be achieved with the `flatMap` operation:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#httpClientResponseFlatMap(io.vertx.rxjava.core.http.HttpClientRequest)}
 * ----
 *
 * We can also unmarshall the `Observable<Buffer>` into an object using the {@link io.vertx.rxjava.core.RxHelper#unmarshaller(java.lang.Class)}
 * static method. This method creates an `Rx.Observable.Operator` unmarshalling buffers to an object:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#httpClientResponseFlatMapPojo(io.vertx.rxjava.core.http.HttpClientRequest)}
 * ----
 *
 * == Http server requests
 *
 * The {@link io.vertx.rxjava.core.http.HttpServer#requestStream()} ()} provides a callback for each incoming
 * request:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#httpServerRequest}
 * ----
 *
 * The {@link io.vertx.core.http.HttpServerRequest} can then be adapted to an `Observable<Buffer>`:
 *
 * ----
 * {@link examples.RxifiedExamples#httpServerRequestObservable(io.vertx.rxjava.core.http.HttpServer)}
 * ----
 *
 * The {@link io.vertx.rxjava.core.RxHelper#unmarshaller(java.lang.Class)} can be used to parse and map
 * a json request to an object:
 *
 * ----
 * {@link examples.RxifiedExamples#httpServerRequestObservablePojo(io.vertx.rxjava.core.http.HttpServer)}
 * ----
 *
 * == Websocket client
 *
 * The {@link io.vertx.rxjava.core.http.HttpClient#websocket} provides a single callback when the websocket
 * connects, otherwise a failure:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#websocketClient(io.vertx.rxjava.core.Vertx)}
 * ----
 *
 * The {@link io.vertx.rxjava.core.http.WebSocket} can then be turned into an `Observable<Buffer>` easily
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#websocketClientBuffer(rx.Observable)}
 * ----
 *
 * == Websocket server
 *
 * The {@link io.vertx.rxjava.core.http.HttpServer#websocketStream()} provides a callback for each incoming
 * connection:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#websocketServer(io.vertx.rxjava.core.http.HttpServer)}
 * ----
 *
 * The {@link io.vertx.core.http.ServerWebSocket} can be turned into an `Observable<Buffer>` easily:
 *
 * [source,java]
 * ----
 * {@link examples.RxifiedExamples#websocketServerBuffer(rx.Observable)}
 * ----
 */
@Document(fileName = "index.adoc")
package io.vertx.rxjava.core;

import io.vertx.docgen.Document;