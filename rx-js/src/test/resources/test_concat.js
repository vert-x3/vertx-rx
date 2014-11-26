var test = require("test");
var Rx = require("rx.vertx");
var o1 = Rx.Observable.fromReadStream(vertx.timerStream(100));
var o2 = Rx.Observable.fromReadStream(vertx.timerStream(100));
var obs = Rx.Observable.concat(o1, o2);
var count = 0;
obs.subscribe(
  function(evt) { count++; },
  function(err) { test.fail(err); },
  function() {
    test.assertEquals(2, count);
    test.testComplete();
  }
);
