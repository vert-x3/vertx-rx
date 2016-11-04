import io.vertx.rx.java.RxHelper
import io.vertx.rx.java.ObservableFuture
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpServer
import io.vertx.core.http.ServerWebSocket
import rx.Subscriber
import rx.Observer
import rx.Observable

ObservableFuture<HttpServer> onListen = RxHelper.observableFuture();
onListen.subscribe({
  server ->
    vertx.createHttpClient().websocket(1234, "localhost", "/some/path", { ws ->
      ws.write(Buffer.buffer("foo"));
      ws.close();
    })
}, { error -> test.fail(error.getMessage()) }
);
HttpServer server = vertx.createHttpServer(port: 1234, host: "localhost");
Observable<ServerWebSocket> socketObs = server.websocketStream().toObservable();
socketObs.subscribe(new Subscriber<ServerWebSocket>() {
  @Override
  public void onNext(ServerWebSocket o) {
    Observable<Buffer> dataObs = o.toObservable();
    dataObs.subscribe(new Observer<Buffer>() {

      LinkedList<Buffer> buffers = new LinkedList<>();

      @Override
      public void onNext(Buffer buffer) {
        buffers.add(buffer);
      }

      @Override
      public void onError(Throwable e) {
        test.fail(e.getMessage());
      }

      @Override
      public void onCompleted() {
        test.assertEquals(1, buffers.size());
        test.assertEquals("foo", buffers.get(0).toString("UTF-8"));
        server.close();
      }
    });
  }

  @Override
  public void onError(Throwable e) {
    test.fail(e.getMessage());
  }

  @Override
  public void onCompleted() {
    test.testComplete();
  }
});
server.listen(onListen.toHandler());
test.await();
