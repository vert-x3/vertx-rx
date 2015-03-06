var Vertx = require("vertx-js/vertx");

// tag::example[]
var Rx = require("rx.vertx");

// Create an observer via the Rx api
var observer = Rx.Observer.create(
    function(evt) {
        // Got event
    }
);

// The rx.vertx extension augmented the observer with the toHandler method
var future = observer.toFuture();
// end::example[]
