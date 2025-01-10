package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.VertxGen;

@VertxGen
public interface DeprecatedMethod {

  @Deprecated
  void foo();

}
