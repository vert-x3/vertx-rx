import rx.Observable
import rx.Subscription

import java.util.concurrent.atomic.AtomicReference;

def eb = vertx.eventBus();
def consumer = eb.<String>consumer("the-address");
Observable<String> obs = consumer.bodyStream().toObservable();
def sub = new AtomicReference<Subscription>();
def s = obs.subscribe(
    { arg -> test.fail("Was not expecting item $arg") },
    { err ->  test.fail("Was not expecting error ${err.message}") },
    {
      sub.get().unsubscribe();
      def t = obs.subscribe(
          { arg ->
            test.assertEquals("msg1", arg);
          },
          { err -> test.fail("Was not expecting error ${err.message}") },
          {
            test.assertFalse(consumer.registered);
            test.testComplete();
          }
      );
      sub.set(t);
    }
);
sub.set(s);
