package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.codegen.extra.duplicates.SomeRandomType;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@VertxGen
public interface DuplicatesInterface {

  static DuplicatesInterface create() {
    return new DuplicatesInterfaceImpl();
  }

  @GenIgnore(GenIgnore.PERMITTED_TYPE)
  void abc(SomeRandomType arg, Handler<AsyncResult<SomeRandomType>> handler);

  @GenIgnore(GenIgnore.PERMITTED_TYPE)
  void def(io.vertx.codegen.extra.duplicates.nested.SomeRandomType arg, Handler<AsyncResult<io.vertx.codegen.extra.duplicates.nested.SomeRandomType>> handler);
}
