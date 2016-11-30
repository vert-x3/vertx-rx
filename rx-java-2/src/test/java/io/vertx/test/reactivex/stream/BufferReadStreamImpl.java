package io.vertx.test.reactivex.stream;

import io.vertx.core.Context;
import io.vertx.core.buffer.Buffer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BufferReadStreamImpl extends ReadStreamBase<Buffer, BufferReadStreamImpl> {
  public BufferReadStreamImpl(Context context) {
    super(context);
  }
  public BufferReadStreamImpl() {
  }
}