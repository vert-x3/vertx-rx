var test = require("test");
var Rx = require("rx.vertx");
var eb = vertx.eventBus();
eb.consumer("the_address").handler(function(msg) {
  msg.reply("pong");
});
var events = [];
var observer = Rx.Observer.create(
  function(evt) {
    events.push(evt.body());
  }, function(err) {
    test.fail();
  }, function() {
    test.assertEquals(1, events.length);
    test.assertEquals("pong", events[0]);
    test.testComplete();
  }
);
var future = observer.toFuture();
eb.send("the_address", {}, {}, future);
