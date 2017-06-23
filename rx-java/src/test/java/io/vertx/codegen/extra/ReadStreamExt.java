package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

/**
 * We just want to check it compiles fine.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ReadStreamExt<I, U> extends ReadStream<U> {

  @Override
  ReadStreamExt<I, U> exceptionHandler(Handler<Throwable> handler);

  @Override
  ReadStreamExt<I, U> handler(Handler<U> handler);

  @Override
  ReadStreamExt<I, U> pause();

  @Override
  ReadStreamExt<I, U> resume();

  @Override
  ReadStreamExt<I, U> endHandler(Handler<Void> endHandler);

}
