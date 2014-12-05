var test = require("test");
var Rx = require("rx.vertx");
var Rx = require("rx.time");
var Vertx = require("vertx-js/vertx");
var initContext = Vertx.currentContext();
Rx.Observable.timer(100, 100).take(10).subscribe(
  function(event) {
    test.assertEquals(initContext._jdel, Vertx.currentContext()._jdel);
  },
  function(err) {
    test.fail(err.message);
  }, function() {
    test.testComplete();
  }
);
