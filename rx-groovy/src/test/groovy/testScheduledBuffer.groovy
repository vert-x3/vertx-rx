import rx.Observable
import rx.Observer

import java.util.concurrent.TimeUnit

vertx.runOnContext({
  def startTime = System.currentTimeMillis();
  def initCtx = vertx.currentContext();
  Observable
      .timer(10, 10, TimeUnit.MILLISECONDS, vertx.scheduler())
      .buffer(100, TimeUnit.MILLISECONDS, vertx.scheduler())
      .take(10)
      .subscribe(new Observer<List<Long>>() {
    private int eventCount = 0;
    public void onNext(List<Long> value) {
      eventCount++;
      test.assertEquals(initCtx.delegate, vertx.currentContext().delegate);
    }
    public void onError(Throwable e) {
      test.fail("unexpected failure");
    }
    public void onCompleted() {
      def timeTaken = System.currentTimeMillis() - startTime;
      test.assertEquals(10, eventCount);
      test.assertTrue("Was expecting to have time taken | $timeTaken -  1000 | < 200", Math.abs(timeTaken - 1000) < 200);
      test.testComplete();
    }
  });
});
test.await();
