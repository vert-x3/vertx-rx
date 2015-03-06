var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");
var client = vertx.createHttpClient();
var req = client.request("GET", 8080, "localhost", "/the_uri");
var observable = Rx.Observable.fromReadStream(req);
observable.subscribe(function(resp) {
    // Process the response
}, function(err) {
    // Could not connect
});
req.end();
// end::example[]
