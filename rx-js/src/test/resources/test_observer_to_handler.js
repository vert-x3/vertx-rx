var test = require("test");
var Rx = require("rx.vertx");
var observer = Rx.Observer.create(
  function(evt) {
    test.testComplete();
  }, function(err) {
    test.fail();
  }, function() {
    test.fail();
  }
);
var handler = observer.toHandler();
vertx.setTimer(1, handler);
