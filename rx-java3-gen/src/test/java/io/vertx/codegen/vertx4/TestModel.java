package io.vertx.codegen.vertx4;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.streams.ReadStream;

import java.util.function.Function;

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

  static Future<TestModel> futureMethod3() {
    return Future.succeededFuture(new TestModel() {
    });
  }

  static void function1(Function<ReadStream<TestModel>, ReadStream<TestModel>> f) {
  }
}
