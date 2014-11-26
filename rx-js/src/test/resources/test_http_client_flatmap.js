var Buffer = require('vertx-js/buffer');
var test = require("test");
var Rx = require("rx.vertx");

var server = vertx.createHttpServer({port: 8080});
server.requestStream().handler(function(req) {
  req.response().setChunked(true).end("some_content");
});

server.listen(function(server, cause) {
  var client = vertx.createHttpClient({});
  var observable = Rx.observableHandler();
  client.request("GET", 8080, "localhost", "/the_uri", observable.asHandler()).end();
  var content = Buffer.buffer();
  observable.
    take(1).
    flatMap(function(resp) {
      return Rx.Observable.fromReadStream(resp);
    }).
    forEach(
      function(buffer) {
        content.appendBuffer(buffer);
      },
      function(err) {
        test.fail();
      },
      function() {
        server.close();
        test.assertEquals("some_content", content.toString("UTF-8"));
        test.testComplete();
      });
});
