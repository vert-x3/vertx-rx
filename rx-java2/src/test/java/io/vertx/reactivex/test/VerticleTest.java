package io.vertx.reactivex.test;

import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

public class VerticleTest extends VertxTestBase {

  private static final RuntimeException err = new RuntimeException();
  private static volatile int startedCount;

  private Vertx vertx;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    vertx = new Vertx(super.vertx);
    startedCount = 0;
  }

  public static class CompleteVerticle extends AbstractVerticle {
    @Override
    public Completable rxStart() {
      return Completable.complete();
    }
  }

  public static class FailureVerticle extends AbstractVerticle {
    @Override
    public Completable rxStart() {
      throw err;
    }
  }

  public static class ErrorVerticle extends AbstractVerticle {
    @Override
    public Completable rxStart() {
      return Completable.error(err);
    }
  }

  public static class SynchronousVerticle extends AbstractVerticle {
    @Override
    public void start() {
      startedCount++;
    }
  }

  @Test
  public void testRxStartComplete() {
    vertx.deployVerticle(CompleteVerticle.class.getName(), onSuccess(v -> testComplete()));
    await();
  }

  @Test
  public void testRxStartFailure() {
    vertx.deployVerticle(FailureVerticle.class.getName(), onFailure(t -> {
      assertEquals(t, err);
      testComplete();
    }));
    await();
  }

  @Test
  public void testRxStartError() {
    vertx.deployVerticle(ErrorVerticle.class.getName(), onFailure(t -> {
      assertEquals(t, err);
      testComplete();
    }));
    await();
  }

  @Test
  public void testSynchronousStart() {
    vertx.deployVerticle(SynchronousVerticle.class.getName(), onSuccess(t -> {
      assertEquals(1, startedCount);
      testComplete();
    }));
    await();
  }
}
