package io.vertx.rx.java.test;

import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import io.vertx.rxjava.core.buffer.Buffer;
import rx.Observable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ConvertingReactiveStreamTest extends AbstractReadStreamTest<Buffer> {

  @Override
  protected Observable<Buffer> toObservable(BufferReadStreamImpl stream) {
    return new io.vertx.rxjava.rx.java.test.stream.BufferReadStream(stream, vertx).toObservable();
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
