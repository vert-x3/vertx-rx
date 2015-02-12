var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");
var observable = Rx.observableHandler();
observable.subscribe(
    function(evt) {
        // Got event
    }
);
vertx.setTimer(1000, observable.toHandler());
// end::example[]
