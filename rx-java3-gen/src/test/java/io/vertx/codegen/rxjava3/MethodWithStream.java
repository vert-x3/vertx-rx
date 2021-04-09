package io.vertx.codegen.rxjava3;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface MethodWithStream {

  static void stringStream(ReadStream<String> stream) {
  }

  static void apiStream(ReadStream<MethodWithStream> stream) {
  }
}
