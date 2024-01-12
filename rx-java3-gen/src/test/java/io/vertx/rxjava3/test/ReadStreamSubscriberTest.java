package io.vertx.rxjava3.test;

import io.vertx.rxjava3.impl.ReadStreamSubscriber;
import io.vertx.lang.rx.test.ReadStreamSubscriberTestBase;
import org.junit.Test;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamSubscriberTest extends ReadStreamSubscriberTestBase {

  @Override
  public long bufferSize() {
    return ReadStreamSubscriber.BUFFER_SIZE;
  }

  @Override
  protected Sender sender() {
    return new Sender() {

      private boolean cancelled;

      public ReadStreamSubscriber<String, String> subscriber = new ReadStreamSubscriber<>(Function.identity(), subscriber -> {
        subscriber.onSubscribe(new Subscription() {
          @Override
          public void request(long n) {
            requested += n;
          }
          @Override
          public void cancel() {
            cancelled = true;
          }
        });
      });

      {
        stream = subscriber;
      }

      protected void emit() {
        subscriber.onNext("" + seq++);
      }

      protected void complete() {
        subscriber.onComplete();
      }

      protected void fail(Throwable cause) {
        subscriber.onError(cause);
      }

      @Override
      protected boolean isUnsubscribed() {
        return cancelled;
      }
    };
  }

  @Test
  public void testFoo() {

    ReadStreamSubscriber<String, String> subscriber = new ReadStreamSubscriber<>(Function.identity(), s -> {
      s.onSubscribe(new Subscription() {
        @Override
        public void request(long n) {
        }
        @Override
        public void cancel() {
        }
      });
    });

    List<String> elements = Collections.synchronizedList(new ArrayList<>());
    AtomicBoolean ok = new AtomicBoolean(true);
    AtomicReference<Thread> current = new AtomicReference<>();
    subscriber.handler(item -> {
      if (current.get() != null) {
        ok.set(current.get() == Thread.currentThread());
      } else {
        current.set(Thread.currentThread());
        Thread t = new Thread(() -> {
          subscriber.onNext("elt2");
        });
        t.start();
        try {
          t.join();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      elements.add(item);
    });
    subscriber.pause();
    subscriber.onNext("elt1");
    subscriber.resume();
    assertEquals(Arrays.asList("elt1", "elt2"), elements);
    assertTrue(ok.get());
  }
}
