var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");
var timer = Rx.Observable.fromReadStream(vertx.periodicStream(1000));
timer.subscribe(function(id) {
    console.log("Callback every second");
});
// end::example[]
