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
    test.fail(err);
  }
);

var observable = Rx.Observable.fromReadStream(consumer);
var subscription = observable.subscribe(observer);
test.assertTrue(consumer.isRegistered());
subscription.dispose();
test.assertFalse(consumer.isRegistered());
test.testComplete();