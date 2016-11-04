import io.vertx.rx.java.RxHelper
import io.vertx.rx.java.ObservableFuture
import io.vertx.core.buffer.Buffer
import io.vertx.core.net.NetServer
import io.vertx.core.net.NetSocket
import io.vertx.core.streams.ReadStream
import rx.Observable

ObservableFuture<NetServer> onListen = RxHelper.observableFuture();
onListen.subscribe({
  server ->
    vertx.createNetClient().connect(1234, "localhost", { ar ->
      test.assertTrue(ar.succeeded());
      NetSocket so = ar.result();
      so.write("foo");
      so.close();
    })} , {
  err -> test.fail(err.message)
});
NetServer server = vertx.createNetServer(port: 1234, host: "localhost");
ReadStream<NetSocket> socketStream = server.connectStream();
Observable<NetSocket> socketObs = socketStream.toObservable();
socketObs.subscribe({ socket ->
  ReadStream<Buffer> bufStream = socket;
  Observable<Buffer> dataObs = bufStream.toObservable();
  LinkedList<Buffer> buffers = new LinkedList<>();
  dataObs.subscribe(
      buffers.&add,
      { err -> test.fail(err.message) },
      {
        test.assertEquals(1, buffers.size());
        test.assertEquals("foo", buffers[0].toString("UTF-8"));
        server.close();
      }
  )
}, { err -> err.printStackTrace(); test.fail(err.message) }, { test.testComplete() });
server.listen(onListen.toHandler());
test.await();
