import io.vertx.core.http.HttpMethod
import io.vertx.rx.java.RxHelper
import io.vertx.rx.java.ObservableFuture
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.http.HttpClientRequest
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.core.http.HttpServerRequest
import rx.Subscriber
import rx.Observer
import rx.Observable

ObservableFuture<HttpServer> onListen = RxHelper.observableFuture();
onListen.subscribe({ server ->
  HttpClientRequest req = vertx.createHttpClient().request(HttpMethod.PUT, 1234, "localhost", "/some/path", { resp -> });
  req.putHeader("Content-Length", "3");
  req.write("foo");
},  { error -> test.fail(error.getMessage())
});
def server = vertx.createHttpServer(port: 1234, host: "localhost");
Observable<HttpServerRequest> socketObs = server.requestStream().toObservable();
socketObs.subscribe(new Subscriber<HttpServerRequest>() {
  @Override
  public void onNext(HttpServerRequest o) {
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
