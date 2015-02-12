var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");
var server = vertx.createHttpServer();
var sockets = Rx.Observable.fromReadStream(server.websocketStream());
sockets.subscribe(function(ws) {
    // Process the web socket
});
// end::example[]
