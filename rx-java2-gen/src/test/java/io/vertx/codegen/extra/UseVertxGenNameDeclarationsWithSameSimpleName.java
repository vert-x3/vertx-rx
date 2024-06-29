package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

import java.util.function.Function;

@VertxGen
public interface UseVertxGenNameDeclarationsWithSameSimpleName extends
  io.vertx.codegen.extra.sub.UseVertxGenNameDeclarationsWithSameSimpleName,
  Handler<io.vertx.codegen.extra.sub.UseVertxGenNameDeclarationsWithSameSimpleName>,
  ReadStream<io.vertx.codegen.extra.sub.UseVertxGenNameDeclarationsWithSameSimpleName> {
  @CacheReturn io.vertx.codegen.extra.sub.UseVertxGenNameDeclarationsWithSameSimpleName foo(io.vertx.codegen.extra.sub.UseVertxGenNameDeclarationsWithSameSimpleName arg);
  void function(Function<io.vertx.codegen.extra.sub.UseVertxGenNameDeclarationsWithSameSimpleName, io.vertx.codegen.extra.sub.UseVertxGenNameDeclarationsWithSameSimpleName> function);
  Handler<io.vertx.codegen.extra.sub.UseVertxGenNameDeclarationsWithSameSimpleName> returnHandler();
}
