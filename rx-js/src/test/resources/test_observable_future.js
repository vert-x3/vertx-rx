var test = require("test");
var Rx = require("rx.vertx");
var eb = vertx.eventBus();
eb.consumer("the_address").handler(function(msg) {
  msg.reply("pong");
});
var observable = Rx.observableFuture();
var events = [];
observable.subscribe(
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
eb.send("the_address", {}, {}, observable.toHandler());
