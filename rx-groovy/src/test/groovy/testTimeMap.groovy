import io.vertx.groovy.core.Vertx

import rx.Observer

import java.util.concurrent.TimeUnit

vertx.runOnContext({ v ->
  def initCtx = Vertx.currentContext();
  def eb = vertx.eventBus();
  def consumer = eb.<String>localConsumer("the-address").bodyStream();
  Observer<String> observer = new Observer<String>() {
    @Override
    public void onNext(String s) {
      test.assertEquals(initCtx.delegate, Vertx.currentContext().delegate);
      test.assertEquals("msg1msg2msg3", s);
      test.testComplete();
    }
    @Override
    public void onError(Throwable e) {
      test.fail(e.getMessage());
    }
    @Override
    public void onCompleted() {
      test.fail();
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
