package io.vertx.codegen.rxjava3;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface MethodWithAsync {

  @GenIgnore(GenIgnore.PERMITTED_TYPE)
  static Future<String> singleMethod(Consumer<Handler<AsyncResult<String>>> control) {
    return Future.future(control::accept);
  }

  @GenIgnore(GenIgnore.PERMITTED_TYPE)
  static Future<Void> completableMethod(Consumer<Handler<AsyncResult<Void>>> control) {
    return Future.future(control::accept);
  }

  @GenIgnore(GenIgnore.PERMITTED_TYPE)
  static Future<@Nullable String> maybeMethod(Consumer<Handler<AsyncResult<String>>> control) {
    return Future.future(control::accept);
  }
}
