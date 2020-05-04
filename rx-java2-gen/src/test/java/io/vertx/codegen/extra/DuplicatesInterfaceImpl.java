package io.vertx.codegen.extra;

import io.vertx.codegen.extra.duplicates.SomeRandomType;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class DuplicatesInterfaceImpl implements DuplicatesInterface {
  @Override
  public void abc(SomeRandomType arg, Handler<AsyncResult<SomeRandomType>> handler) {
    handler.handle(Future.succeededFuture(arg));
  }

  @Override
  public void def(io.vertx.codegen.extra.duplicates.nested.SomeRandomType arg, Handler<AsyncResult<io.vertx.codegen.extra.duplicates.nested.SomeRandomType>> handler) {
    handler.handle(Future.succeededFuture(arg));
  }
}
