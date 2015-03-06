var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");
var client = vertx.createHttpClient();
var stream = client.websocketStream(8080, "localhost", "/the_uri");
Rx.Observable.fromReadStream(stream).
    subscribe(function (ws) {
        var observable = Rx.Observable.fromReadStream(ws);
        observable.forEach(function(buffer) {
            // Process message
        });

    });
// end::example[]
