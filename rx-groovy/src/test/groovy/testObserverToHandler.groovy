import rx.Observer

import java.util.concurrent.atomic.AtomicInteger;

def count = new AtomicInteger()
Observer<Long> observer = new Observer<Long>() {
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
  void onNext(Long l) {
    count.incrementAndGet();
  }
}
vertx.setTimer(1, observer.toHandler());
test.await();