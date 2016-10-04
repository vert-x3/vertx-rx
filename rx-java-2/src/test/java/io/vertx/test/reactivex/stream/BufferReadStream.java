package io.vertx.test.reactivex.stream;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface BufferReadStream extends ReadStream<Buffer> {

  @Override
  BufferReadStream exceptionHandler(Handler<Throwable> handler);

  @Override
  BufferReadStream handler(Handler<Buffer> handler);

  @Override
  BufferReadStream pause();

  @Override
  BufferReadStream resume();

  @Override
  BufferReadStream endHandler(Handler<Void> endHandler);
}