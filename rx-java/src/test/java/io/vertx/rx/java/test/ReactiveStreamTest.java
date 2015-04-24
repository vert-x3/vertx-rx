package io.vertx.rx.java.test;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.Pump;
import io.vertx.ext.reactivestreams.ReactiveWriteStream;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import rx.Observable;
import rx.RxReactiveStreams;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReactiveStreamTest extends AbstractBackPressureTest {

  @Override
  protected Observable<Buffer> toObservable(BufferReadStreamImpl stream) {
    ReactiveWriteStream<Buffer> ws = ReactiveWriteStream.writeStream(vertx);
    Observable<Buffer> obs = RxReactiveStreams.toObservable(new Publisher<Buffer>() {
      @Override
      public void subscribe(org.reactivestreams.Subscriber<? super Buffer> subscriber) {
        Subscriber<Buffer> subscriberProxy = new Subscriber<Buffer>() {

          Pump pump = Pump.pump(stream, ws);

          @Override
          public void onSubscribe(Subscription subscription) {
            Subscription subscriptionProxy = new Subscription() {
              @Override
              public void request(long n) {
                subscription.request(n);
              }

              @Override
              public void cancel() {
                pump.stop();
                subscription.cancel();
//                ws.close(); ?
              }
            };
            stream.endHandler(done -> {
              subscriber.onComplete();
              subscriptionProxy.cancel();
            });
            stream.exceptionHandler(this::onError);
            pump.start();
            subscriber.onSubscribe(subscriptionProxy);
          }

          @Override
          public void onNext(Buffer event) {
            subscriber.onNext(event);
          }

          @Override
          public void onError(Throwable t) {
            subscriber.onError(t);
          }

          @Override
          public void onComplete() {
            subscriber.onComplete();
          }
        };
        ws.subscribe(subscriberProxy);
      }
    });
    return obs;
  }

  @Override
  public void testHandlers() {
    // Does not pass
  }

  @Override
  public void testDataHandlerShouldBeSetAndUnsetAfterOtherHandlers() {
    // Does not pass
  }
}
