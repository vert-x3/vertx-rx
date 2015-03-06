var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");

// Create an observable that is also an handler of async result
var observable = Rx.observableFuture();
observable.subscribe(
    function(server) {
        // Server is listening
    },
    function(err) {
        // Server could not start
    }
);

var server = vertx.createHttpServer({ "port":1234, "host":"localhost" });
server.listen(observable.toHandler());
// end::example[]
