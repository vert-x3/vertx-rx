var test = require("test");
var Rx = require("rx.vertx");
var Rx = require("rx.time");
var Vertx = require("vertx-js/vertx");
var initContext = Vertx.currentContext();
var eb = vertx.eventBus();
var consumer = eb.localConsumer("the-address").bodyStream();
var observer = Rx.Observer.create(
  function (evt) {
    test.assertEquals(initContext._jdel, Vertx.currentContext()._jdel);
    test.assertEquals("msg1msg2msg3", evt);
    this.dispose();
    test.testComplete();
  },
  function (err) {
    test.fail(err);
  },
  function () {
    test.fail(err);
  }
);
var observable = Rx.Observable.fromReadStream(consumer);
observable.
  bufferWithTime(500).
  map(function (arr) { return arr.reduce(function (acc, x) { return acc + x; }, "") }).
  subscribe(observer);
eb.send("the-address", "msg1");
eb.send("the-address", "msg2");
eb.send("the-address", "msg3");
