var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");

var eb = vertx.eventBus();
var messageConsumer = eb.consumer("the-address");
var bodyConsumer = messageConsumer.bodyStream();

// Create an observable from the body consumer
var observable = Rx.Observable.fromReadStream(bodyConsumer);
// end::example[]
