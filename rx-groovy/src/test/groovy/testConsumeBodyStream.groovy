import rx.Observable
import rx.Subscription

import java.util.concurrent.atomic.AtomicReference;

def eb = vertx.eventBus();
def consumer = eb.<String>consumer("the-address");
Observable<String> obs = consumer.bodyStream().toObservable();
def items = [];
def sub = new AtomicReference<Subscription>();
def s = obs.subscribe(
    { arg ->
      items << arg;
      if (items.size() == 3) {
        sub.get().unsubscribe();
      }
    },
    { err ->
      test.fail(err.message)
    },
    {
      test.assertEquals(Arrays.asList("msg1", "msg2", "msg3"), items);
      test.assertFalse(consumer.isRegistered());
      test.testComplete();
    }
);
sub.set(s);
eb.send("the-address", "msg1");
eb.send("the-address", "msg2");
eb.send("the-address", "msg3");
test.await();
