package io.vertx.codegen.rxjava2;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface MethodWithSingleString {

  Future<String> doSomethingWithResult();

}
