var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");
var client = vertx.createHttpClient();
var stream = client.websocketStream(8080, "localhost", "/the_uri");
var observable = Rx.Observable.fromReadStream(stream);
observable.subscribe(function(ws) {
    // Use the websocket
}, function(err) {
    // Could not connect
});
// end::example[]
