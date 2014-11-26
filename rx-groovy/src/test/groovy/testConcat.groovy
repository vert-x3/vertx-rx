import rx.Observable

import java.util.concurrent.atomic.AtomicInteger

def o1 = vertx.timerStream(100).toObservable();
def o2 = vertx.timerStream(100).toObservable();
def obs = Observable.concat(o1, o2);
AtomicInteger count = new AtomicInteger();
obs.subscribe(
    { msg -> count.incrementAndGet() },
    { err -> test.fail();},
    { ->
      test.assertEquals(2, count.get());
      test.testComplete();
    });
test.await();
