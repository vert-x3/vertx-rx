package io.vertx.reactivex.test;

import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

public class VerticleTest extends VertxTestBase {

  private static final RuntimeException err = new RuntimeException();

  private Vertx vertx;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    vertx = new Vertx(super.vertx);
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

  public static class FaultyVerticle extends AbstractVerticle {
    @Override
    public Completable rxStart() {
      return null;
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
  public void testRxStartFaulty() {
    vertx.deployVerticle(FaultyVerticle.class.getName(), onFailure(t -> {
      assertTrue(t instanceof NullPointerException);
      testComplete();
    }));
    await();
  }
}
