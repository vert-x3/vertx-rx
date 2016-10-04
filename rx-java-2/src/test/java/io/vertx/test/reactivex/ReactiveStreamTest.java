package io.vertx.test.reactivex;

import io.reactivex.Flowable;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.RxHelper;
import io.vertx.test.reactivex.stream.BufferReadStreamImpl;

import java.util.function.Function;

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
  protected Flowable<Buffer> toObservable(BufferReadStreamImpl stream) {
    return RxHelper.toFlowable(stream, Function.identity(), vertx.getOrCreateContext());
  }
}
