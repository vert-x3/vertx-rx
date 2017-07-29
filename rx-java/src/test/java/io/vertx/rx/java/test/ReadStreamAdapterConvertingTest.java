package io.vertx.rx.java.test;

import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import io.vertx.rx.java.test.support.SimpleSubscriber;
import io.vertx.rxjava.core.buffer.Buffer;
import rx.Observable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamAdapterConvertingTest extends ReadStreamAdapterTestBase<Buffer, Observable<Buffer>> {

  @Override
  protected Observable<Buffer> toObservable(BufferReadStreamImpl stream) {
    return new io.vertx.rxjava.rx.java.test.stream.BufferReadStream(stream).toObservable();
  }

  @Override
  protected void subscribe(Observable<Buffer> obs, SimpleSubscriber<Buffer> sub) {
    SimpleSubscriber.subscribe(obs, sub);
  }

  @Override
  protected Observable<Buffer> concat(Observable<Buffer> obs1, Observable<Buffer> obs2) {
    return Observable.concat(obs1, obs2);
  }

  @Override
  protected Buffer buffer(String s) {
    return Buffer.buffer(s);
  }

  @Override
  protected String string(Buffer buffer) {
    return buffer.toString("UTF-8");
  }
}
