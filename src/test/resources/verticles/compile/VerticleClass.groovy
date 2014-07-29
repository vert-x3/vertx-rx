package verticles.compile;

import io.vertx.groovy.core.GroovyVerticle
import io.vertx.lang.groovy.CompileTest;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MyVerticle extends GroovyVerticle {

    @Override
    void start() throws Exception {
        CompileTest.started.await()
    }

    @Override
    void stop() throws Exception {
        CompileTest.stopped.await()
    }
}
