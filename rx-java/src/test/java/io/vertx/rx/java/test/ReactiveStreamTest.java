package io.vertx.rx.java.test;

import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.RxHelper;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import rx.Observable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReactiveStreamTest extends AbstractReadStreamTest<Buffer> {

  @Override
  protected Buffer buffer(String s) {
    return Buffer.buffer(s);
  }

  @Override
  protected String string(Buffer buffer) {
    return buffer.toString("UTF-8");
  }

  @Override
  protected Observable<Buffer> toObservable(BufferReadStreamImpl stream) {
    return RxHelper.toObservable(stream, vertx);
  }
}
