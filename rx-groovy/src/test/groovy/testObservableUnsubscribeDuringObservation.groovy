import rx.Observable

def eb = vertx.eventBus();
def consumer = eb.<String>consumer("the-address");
Observable<String> obs = consumer.bodyStream().toObservable().take(4);
def obtained = [];
obs.subscribe(
    { arg -> obtained.add(arg) },
    { err -> test.fail(err.message) },
    {
      test.assertEquals(Arrays.asList("msg0", "msg1", "msg2", "msg3"), obtained);
      consumer.endHandler({
        test.testComplete();
      })
    }
);
(0..7).each { eb.send("the-address", "msg" + it) }
test.await();
