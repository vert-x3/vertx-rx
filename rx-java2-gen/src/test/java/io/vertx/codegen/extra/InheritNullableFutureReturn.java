package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;

@VertxGen
public interface InheritNullableFutureReturn extends NullableFutureReturn {
  static InheritNullableFutureReturn create() {
    return Future::succeededFuture;
  }
  @Override
  Future<@Nullable String> method(String arg);
}
