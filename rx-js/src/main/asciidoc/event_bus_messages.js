var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");

var eb = vertx.eventBus();
var messageConsumer = eb.consumer("the-address");

// Create an observable from the message consumer
var observable = Rx.Observable.fromReadStream(messageConsumer);

// Subscribe to the observable
var subscription = observable.subscribe(
    function(msg) {
        // Got message
    });

// Unregisters the stream after 10 seconds
vertx.setTimer(10000, function() {
    subscription.dispose();
});
// end::example[]
