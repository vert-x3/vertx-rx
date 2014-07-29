package io.vertx.lang.groovy;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.test.core.AsyncTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CompileTest extends AsyncTestBase {

  public static final CyclicBarrier started = new CyclicBarrier(2);
  public static final CyclicBarrier stopped = new CyclicBarrier(2);

  @Before
  public void before() {
    started.reset();
  }

  @Test
  public void testCompileVerticle() throws Exception {
    Vertx vertx = Vertx.vertx();
    try {
      BlockingQueue<String> deployed = new ArrayBlockingQueue<>(1);
      vertx.deployVerticle("groovy:verticles/compile/VerticleClass.groovy", new DeploymentOptions(), id -> deployed.offer(id.result()));
      started.await(10, TimeUnit.SECONDS);
      String id = deployed.poll(10, TimeUnit.SECONDS);
      vertx.undeployVerticle(id, null);
      stopped.await(10, TimeUnit.SECONDS);
    } finally {
      vertx.close();
    }
  }

  @Test
  public void testCompileScript() throws Exception {
    Vertx vertx = Vertx.vertx();
    try {
      vertx.deployVerticle("groovy:verticles/compile/VerticleScript.groovy");
      started.await(10, TimeUnit.SECONDS);
    } finally {
      vertx.close();
    }
  }
}
