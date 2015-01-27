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
  client.websocket(8080, "localhost", "/the_uri", function(ws) {
    var content = Buffer.buffer();
    var observable = Rx.Observable.fromReadStream(ws);
    observable.forEach(
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
});
