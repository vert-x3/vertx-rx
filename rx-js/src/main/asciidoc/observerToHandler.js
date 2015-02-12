var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");

// Create an observer via the Rx api
var observer = Rx.Observer.create(
    function(evt) {
        // Got event
    }
);

// The rx.vertx extension augmented the observer with the toHandler method
var handler = observer.toHandler();
vertx.setTimer(1000, handler);
// end::example[]
