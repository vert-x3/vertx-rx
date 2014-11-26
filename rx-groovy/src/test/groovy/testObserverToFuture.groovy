import io.vertx.groovy.core.http.HttpServer
import rx.Observer

import java.util.concurrent.atomic.AtomicInteger;

AtomicInteger count = new AtomicInteger()
Observer<HttpServer> observer = new Observer<HttpServer>() {
  @Override
  void onCompleted() {
    test.assertEquals(1, count.get());
    test.testComplete();
  }

  @Override
  void onError(Throwable e) {
    test.fail(e.message);
  }

  @Override
  void onNext(HttpServer httpServer) {
    count.incrementAndGet();
  }
}
vertx.createHttpServer(port: 8080).requestHandler({ req -> }).listen(observer.toFuture());
test.await();