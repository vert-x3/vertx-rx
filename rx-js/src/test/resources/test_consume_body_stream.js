var test = require("test");
var Rx = require("rx.vertx");
var eb = vertx.eventBus();
var consumer = eb.localConsumer("the-address");
var events = [];
var observer = Rx.Observer.create(
  function (evt) {
    var body = evt.body();
    events.push(body);
    if (body == "msg3") {
      test.assertEquals(3, events.length);
      test.assertEquals("msg1", events[0]);
      test.assertEquals("msg2", events[1]);
      test.assertEquals("msg3", events[2]);
      test.testComplete();
    }
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
eb.send("the-address", "msg2");
eb.send("the-address", "msg3");
