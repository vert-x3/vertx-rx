package io.vertx.test.reactivex.stream;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface RxInterface {

  void methodWithHandlerAsyncResultString(Future<String> fut, Handler<AsyncResult<String>> handler);
  void methodWithHandlerAsyncResultVoid(Future<Void> fut, Handler<AsyncResult<Void>> handler);

}
