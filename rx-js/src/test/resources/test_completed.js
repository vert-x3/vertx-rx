var test = require("test");
var Rx = require("rx.vertx");
var eb = vertx.eventBus();
var consumer = eb.localConsumer("the-address");
var observer = Rx.Observer.create(
  function (evt) {
    test.fail(err);
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
consumer.unregister();