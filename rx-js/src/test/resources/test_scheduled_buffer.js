var test = require("test");
var Rx = require("rx.vertx");
var Rx = require("rx.time");
var Vertx = require("vertx-js/vertx");
var initContext = Vertx.currentContext();
var eventCount = 0;
Rx.Observable.timer(10, 10).bufferWithTime(100).take(10).subscribe(
  function(event) {
    eventCount++;
    test.assertEquals(initContext._jdel, Vertx.currentContext()._jdel);
  },
  function(err) {
    test.fail(err.message);
  }, function() {
    test.assertEquals(10, eventCount);
    test.testComplete();
  }
);
