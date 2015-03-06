var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");
var client = vertx.createHttpClient();
var req = client.request("GET", 8080, "localhost", "/the_uri");
Rx.Observable.fromReadStream(req).subscribe(function(resp) {
    var observable = Rx.Observable.fromReadStream(resp);
    observable.forEach(function(buffer) {
        // Process buffer
    });
});
req.end();
// end::example[]
