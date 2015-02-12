var test = require("test");
var Rx = require("rx.vertx");
var observable = Rx.observableHandler();
observable.subscribe(
  function(evt) {
    test.testComplete();
  }, function(err) {
    test.fail();
  }, function() {
    test.fail();
  }
);
vertx.setTimer(1, observable.toHandler());
