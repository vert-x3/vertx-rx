var test = require("test");
var Rx = require("rx.vertx");
var consumer = vertx.timerStream(100);
var observer = Rx.Observer.create(
  function (evt) {
  },
  function (err) {
    test.fail(err);
  },
  function () {
    test.testComplete();
  }
);
var observable = Rx.Observable.fromReadStream(consumer);
observable.subscribe(observer);
