var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");
var server = vertx.createHttpServer();
Rx.Observable.fromReadStream(server.websocketStream()).subscribe(function(ws) {
    var observable = Rx.Observable.fromReadStream(ws);
    observable.forEach(function(buffer) {
       // Process message
    });
});
// end::example[]
