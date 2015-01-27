var Buffer = require('vertx-js/buffer');
var test = require("test");
var Rx = require("rx.vertx");

var server = vertx.createHttpServer({port: 8080});
server.websocketStream().handler(function(ws) {
    ws.write(Buffer.buffer("some_content"));
    ws.close();
});

server.listen(function(server, cause) {
  var client = vertx.createHttpClient({});
  var content = Buffer.buffer();
  var stream = client.websocketStream(8080, "localhost", "/the_uri");
  var observable = Rx.Observable.fromReadStream(stream);
  observable.flatMap(function(ws) {
      return Rx.Observable.fromReadStream(ws);
    }).
    forEach(
      function(buffer) {
        content.appendBuffer(buffer);
      }, function(err) {
        test.fail();
      } , function() {
        server.close();
        test.assertEquals("some_content", content.toString("UTF-8"));
        test.testComplete();
      });
});
