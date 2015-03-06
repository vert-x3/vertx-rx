var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");
var timer = Rx.Observable.fromReadStream(vertx.timerStream(1000));
timer.subscribe(function(id) {
    console.log("Callback after 1 second");
});
// end::example[]
