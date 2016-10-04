package io.vertx.test.reactivex;

import io.reactivex.processors.UnicastProcessor;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SomeTest {

  @Test
  public void testFoo() {

    UnicastProcessor<String> proc = UnicastProcessor.create();
    proc.subscribe(new Subscriber<String>() {
      @Override
      public void onSubscribe(Subscription s) {
        System.out.println(s.getClass());
      }

      @Override
      public void onNext(String s) {

      }

      @Override
      public void onError(Throwable t) {

      }

      @Override
      public void onComplete() {

      }
    });


  }
}
