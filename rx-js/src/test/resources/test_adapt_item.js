var test = require("test");
var Rx = require("rx.vertx");
var eb = vertx.eventBus();
var consumer = eb.localConsumer("the-address");
var observer = Rx.Observer.create(
  function (evt) {
    test.assertEquals("function", typeof evt._jdel);
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
observable.subscribe(observer);
eb.send("the-address", "msg1");
