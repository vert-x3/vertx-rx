var Vertx = require("vertx-js/vertx");
var vertx = Vertx.vertx();

// tag::example[]
var Rx = require("rx.vertx");
var fs = vertx.fileSystem();
fs.open("/data.txt", {}, function(result, err) {
  var file = result.result();
  var observable = Rx.Observable.fromReadStream(file);
  observable.forEach(function(data) {
    console.log("Read data: " + data.toString("UTF-8"));
  });
});
// end::example[]
