var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");

var eb = vertx.eventBus();
var consumer = eb.consumer("heat-sensor").bodyStream();
var observable = Rx.Observable.fromReadStream(consumer);

observable.
    bufferWithTime(500).
    map(function (arr) { return arr.reduce(function (acc, x) { return acc + x; }, "") }).
    subscribe(
    function(heat) {
        console.log("Current heat is " + heat);
    });
// end::example[]
