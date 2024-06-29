package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface FutureTCK {

  Future<Void> asyncMethod();

}
