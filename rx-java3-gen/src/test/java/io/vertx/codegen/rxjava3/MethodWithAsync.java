package io.vertx.codegen.rxjava3;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface MethodWithAsync {

  @GenIgnore(GenIgnore.PERMITTED_TYPE)
  static void singleMethod(Consumer<Handler<AsyncResult<String>>> control, Handler<AsyncResult<String>> handler) {
    control.accept(handler);
  }

  @GenIgnore(GenIgnore.PERMITTED_TYPE)
  static void completableMethod(Consumer<Handler<AsyncResult<Void>>> control, Handler<AsyncResult<Void>> handler) {
    control.accept(handler);
  }

  @GenIgnore(GenIgnore.PERMITTED_TYPE)
  static void maybeMethod(Consumer<Handler<AsyncResult<String>>> control, Handler<AsyncResult<@Nullable  String>> handler) {
    control.accept(handler);
  }
}
