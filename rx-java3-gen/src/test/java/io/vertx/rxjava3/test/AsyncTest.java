package io.vertx.rxjava3.test;

import io.reactivex.rxjava3.core.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.rxjava3.codegen.rxjava3.MethodWithAsync;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncTest {

  @Test
  public void testSingle() {
    List<Handler<AsyncResult<String>>> handlers = new ArrayList<>();
    Single<String> single = MethodWithAsync.singleMethod(handlers::add);
    assertEquals(1, handlers.size());
    AtomicReference<String> val1 = new AtomicReference<>();
    single.subscribe(val1::set);
    assertEquals(1, handlers.size());
    assertNull(val1.get());
    handlers.get(0).handle(Future.succeededFuture("expected"));
    assertEquals("expected", val1.get());
    AtomicReference<String> val2 = new AtomicReference<>();
    single.subscribe(val2::set);
    assertEquals(1, handlers.size());
    assertEquals("expected", val2.get());
  }

  @Test
  public void testLazySingle() {
    List<Handler<AsyncResult<String>>> handlers = new ArrayList<>();
    Single<String> single = MethodWithAsync.rxSingleMethod(handlers::add);
    assertEquals(0, handlers.size());
    AtomicReference<String> val1 = new AtomicReference<>();
    single.subscribe(val1::set);
    assertEquals(1, handlers.size());
    assertNull(val1.get());
    handlers.get(0).handle(Future.succeededFuture("expected1"));
    assertEquals("expected1", val1.get());
    AtomicReference<String> val2 = new AtomicReference<>();
    single.subscribe(val2::set);
    assertEquals(2, handlers.size());
    assertNull(val2.get());
    handlers.get(1).handle(Future.succeededFuture("expected2"));
    assertEquals("expected2", val2.get());
  }
}
