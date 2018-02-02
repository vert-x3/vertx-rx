package io.vertx.reactivex.test;

import io.reactivex.observers.DisposableObserver;
import io.vertx.core.Future;
import io.vertx.reactivex.core.impl.AsyncResultSingle;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Locale;

public class AsyncResultSingleAutoCloseTest extends VertxTestBase {

  @Test
  public void testSingleWithoutCloseable() {
    AsyncResultSingle<String> single = new AsyncResultSingle<>(h -> Future.succeededFuture("test").setHandler(h));
    single.subscribe(it -> testComplete(), err -> fail());
    await();
  }

  @Test
  public void testSingleWithCloseable() {
    StringBuilder sb = new StringBuilder();
    Formatter formatter = new Formatter(sb, Locale.US);
    AsyncResultSingle<Formatter> single = new AsyncResultSingle<>(h2 -> Future.succeededFuture(formatter).setHandler(h2));

    DisposableObserver<Formatter> disObs = new DisposableObserver<Formatter>() {
      @Override
      public void onNext(Formatter f) {
        formatter.format("%4$2s %3$2s %2$2s %1$2s", "a", "b", "c", "d");
      }
      @Override
      public void onError(Throwable throwable) {
      }
      @Override
      public void onComplete() {
      }
    };

    single.toObservable().doOnDispose(() -> {
      try {
        formatter.format("%4$2s %3$2s %2$2s %1$2s", "a", "b", "c", "d");
        fail();
      } catch (FormatterClosedException e) {
        testComplete();
      }
    }).subscribe(disObs);
    disObs.dispose();
    await();
  }
}
