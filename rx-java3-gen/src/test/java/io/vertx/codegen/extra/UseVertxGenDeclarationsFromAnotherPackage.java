package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.codegen.extra.sub.SomeType;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

import java.util.function.Function;

@VertxGen
public interface UseVertxGenDeclarationsFromAnotherPackage extends
  SomeType,
  Handler<SomeType>,
  ReadStream<SomeType> {
  @CacheReturn SomeType foo(SomeType arg);
  void function(Function<SomeType, SomeType> function);
  Handler<SomeType> returnHandler();
}
