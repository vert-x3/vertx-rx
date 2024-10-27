package io.vertx.reactivex.core;

import io.reactivex.SingleObserver;
import io.reactivex.SingleOperator;
import io.reactivex.disposables.Disposable;
import io.vertx.core.Expectation;

import java.util.function.Function;

public final class ExpectationOperator<T, U> implements SingleOperator<T, T> {

  private final Expectation<U> expectation;
  private final Function<T, U> unwrapper;

  public ExpectationOperator(Expectation<U> expectation, Function<T, U> unwrapper) {
    this.expectation = expectation;
    this.unwrapper = unwrapper;
  }

  @Override
  public SingleObserver<? super T> apply(SingleObserver<? super T> observer) throws Exception {
    return new SingleObserver<T>() {
      @Override
      public void onSubscribe(Disposable d) {
        observer.onSubscribe(d);
      }

      @Override
      public void onSuccess(T t) {
        U u = unwrapper.apply(t);
        if (expectation.test(u)) {
          observer.onSuccess(t);
        } else {
          Throwable desc = expectation.describe(u);
          observer.onError(desc);
        }
      }

      @Override
      public void onError(Throwable e) {
        observer.onError(e);
      }
    };
  }
}
