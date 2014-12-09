package io.vertx.rx.java.test.stream;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.test.SimpleReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BufferReadStreamImpl extends SimpleReadStream<Buffer> implements BufferReadStream {

  @Override
  public BufferReadStreamImpl exceptionHandler(Handler<Throwable> handler) {
    return (BufferReadStreamImpl) super.exceptionHandler(handler);
  }

  @Override
  public BufferReadStreamImpl handler(Handler<Buffer> handler) {
    return (BufferReadStreamImpl) super.handler(handler);
  }

  @Override
  public BufferReadStreamImpl pause() {
    return (BufferReadStreamImpl) super.pause();
  }

  @Override
  public BufferReadStreamImpl resume() {
    return (BufferReadStreamImpl) super.resume();
  }

  @Override
  public BufferReadStreamImpl endHandler(Handler<Void> endHandler) {
    return (BufferReadStreamImpl) super.endHandler(endHandler);
  }
}
