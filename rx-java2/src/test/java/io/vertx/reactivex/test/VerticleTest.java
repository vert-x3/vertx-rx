package io.vertx.reactivex.test;

import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

public class VerticleTest extends VertxTestBase {

  private static final RuntimeException err = new RuntimeException();
  private static volatile int startedCount;
  private static volatile int stoppedCount;

  private Vertx vertx;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    vertx = new Vertx(super.vertx);
    startedCount = 0;
    stoppedCount = 0;
  }

  public static class StartVerticle extends AbstractVerticle {
    @Override
    public Completable rxStart() {
      return Completable.complete();
    }
  }

  public static class StartVerticleWithFailure extends AbstractVerticle {
    @Override
    public Completable rxStart() {
      throw err;
    }
  }

  public static class StartVerticleWithError extends AbstractVerticle {
    @Override
    public Completable rxStart() {
      return Completable.error(err);
    }
  }

  public static class StartVerticleSynchronously extends AbstractVerticle {
    @Override
    public void start() {
      startedCount++;
    }
  }

  @Test
  public void testStart() {
    vertx.deployVerticle(StartVerticle.class.getName()).onComplete(onSuccess(v -> testComplete()));
    await();
  }

  @Test
  public void testStartWithFailure() {
    vertx.deployVerticle(StartVerticleWithFailure.class.getName()).onComplete(onFailure(t -> {
      assertEquals(t, err);
      testComplete();
    }));
    await();
  }

  @Test
  public void testStartWithError() {
    vertx.deployVerticle(StartVerticleWithError.class.getName()).onComplete(onFailure(t -> {
      assertEquals(t, err);
      testComplete();
    }));
    await();
  }

  @Test
  public void testStartSynchronously() {
    vertx.deployVerticle(StartVerticleSynchronously.class.getName()).onComplete(onSuccess(t -> {
      assertEquals(1, startedCount);
      testComplete();
    }));
    await();
  }

  public static class StopVerticle extends AbstractVerticle {
    @Override
    public Completable rxStop() {
      return Completable.complete();
    }
  }

  public static class StopVerticleWithFailure extends AbstractVerticle {
    @Override
    public Completable rxStop() {
      throw err;
    }
  }

  public static class StopVerticleWithError extends AbstractVerticle {
    @Override
    public Completable rxStop() {
      return Completable.error(err);
    }
  }

  public static class StopVerticleSynchronously extends AbstractVerticle {
    @Override
    public void stop() {
      stoppedCount++;
    }
  }

  @Test
  public void testStop() {
    vertx.deployVerticle(StopVerticle.class.getName()).onComplete(onSuccess(id -> {
      vertx.undeploy(id).onComplete(onSuccess(v -> testComplete()));
    }));
    await();
  }

  @Test
  public void testStopWithFailure() {
    vertx.deployVerticle(StopVerticleWithFailure.class.getName()).onComplete(onSuccess(id -> {
      vertx.undeploy(id).onComplete(onFailure(t -> {
        assertEquals(t, err);
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void testStopWithError() {
    vertx.deployVerticle(StopVerticleWithError.class.getName()).onComplete(onSuccess(id -> {
      vertx.undeploy(id).onComplete(onFailure(t -> {
        assertEquals(t, err);
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void testStopSynchronously() {
    vertx.deployVerticle(StopVerticleSynchronously.class.getName()).onComplete(onSuccess(id -> {
      vertx.undeploy(id).onComplete(onSuccess(v -> {
        assertEquals(1, stoppedCount);
        testComplete();
      }));
    }));
    await();
  }
}
