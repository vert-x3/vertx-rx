package io.vertx.reactivex.core;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.vertx.core.Expectation;

import java.util.function.Function;

public final class ExpectationTransformer<T, U> implements SingleTransformer<T, T> {

  private final Expectation<U> expectation;
  private final Function<T, U> unwrapper;

  public ExpectationTransformer(Expectation<U> expectation, Function<T, U> unwrapper) {
    this.expectation = expectation;
    this.unwrapper = unwrapper;
  }

  @Override
  public SingleSource<T> apply(Single<T> upstream) {
    return upstream.flatMap(t -> {
      U u = unwrapper.apply(t);
      if (expectation.test(u)) {
        return Single.just(t);
      } else {
        Throwable desc = expectation.describe(u);
        return Single.error(desc);
      }
    });
  }
}
