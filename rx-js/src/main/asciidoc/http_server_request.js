var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");
var server = vertx.createHttpServer();
var requests = Rx.Observable.fromReadStream(server.requestStream());
requests.subscribe(function(request) {
    // Process the request
});
// end::example[]
