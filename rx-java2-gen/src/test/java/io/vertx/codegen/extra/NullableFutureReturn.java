package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;

@VertxGen
public interface NullableFutureReturn {

  // Generates a maybe
  Future<@Nullable String> method(String arg);

}
