package io.vertx.codegen.vertx4;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestModel {

  static Future<Boolean> futureMethod1() {
    return Future.succeededFuture(true);
  }

  static Future<Integer> futureMethod2(String s) {
    return Future.succeededFuture(s.length());
  }
}
