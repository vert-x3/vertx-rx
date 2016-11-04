import io.vertx.core.Vertx

import rx.Subscriber

import java.util.concurrent.TimeUnit

vertx.runOnContext({ v ->
  def initCtx = Vertx.currentContext();
  def eb = vertx.eventBus();
  def consumer = eb.<String>localConsumer("the-address").bodyStream();
  Subscriber<String> observer = new Subscriber<String>() {
    boolean first = true;
    @Override
    public void onNext(String s) {
      if (first) {
        first = false;
        test.assertEquals(initCtx.delegate, Vertx.currentContext().delegate);
        test.assertEquals("msg1msg2msg3", s);
        unsubscribe()
        test.testComplete();
      }
    }
    @Override
    public void onError(Throwable e) {
      test.fail(e.getMessage());
    }
    @Override
    public void onCompleted() {
    }
  };
  def observable = consumer.toObservable();
  observable.
      buffer(500, TimeUnit.MILLISECONDS, vertx.scheduler()).
      map({ samples -> samples.sum() }).
      subscribe(observer);
  eb.send("the-address", "msg1");
  eb.send("the-address", "msg2");
  eb.send("the-address", "msg3");
});
test.await();
