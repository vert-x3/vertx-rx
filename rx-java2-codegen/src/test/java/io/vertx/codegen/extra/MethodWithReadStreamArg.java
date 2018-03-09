package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface MethodWithReadStreamArg<T> {
  void doSomething(ReadStream<T> theStream);
}
