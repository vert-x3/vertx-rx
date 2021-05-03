package io.vertx.rxjava3.impl;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultSingle<T, U> extends Single<T> {

  public static <T> Single<T> toSingle(Consumer<Handler<AsyncResult<T>>> subscriptionConsumer) {
    return RxJavaPlugins.onAssembly(new AsyncResultSingle<>(subscriptionConsumer, Function.identity()));
  }

  public static <T, U> Single<T> toSingle(Future<U> future, Function<U, T> mapping) {
    if (future == null) {
      return Single.error(new NullPointerException());
    }
    return RxJavaPlugins.onAssembly(new AsyncResultSingle<>(future::onComplete, mapping));
  }

  private final Consumer<Handler<AsyncResult<U>>> subscriptionConsumer;
  private final Function<U, T> mapping;

  private AsyncResultSingle(Consumer<Handler<AsyncResult<U>>> subscriptionConsumer, Function<U, T> mapping) {
    this.subscriptionConsumer = subscriptionConsumer;
    this.mapping = mapping;
  }

  @Override
  protected void subscribeActual(@NonNull SingleObserver<? super T> observer) {
    AtomicBoolean disposed = new AtomicBoolean();
    observer.onSubscribe(new Disposable() {
      @Override
      public void dispose() {
        disposed.set(true);
      }
      @Override
      public boolean isDisposed() {
        return disposed.get();
      }
    });
    if (!disposed.get()) {
      try {
        subscriptionConsumer.accept(ar -> {
          if (!disposed.getAndSet(true)) {
            if (ar.succeeded()) {
              try {
                observer.onSuccess(mapping.apply(ar.result()));
              } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                RxJavaPlugins.onError(t);
              }
            } else if (ar.failed()) {
              try {
                observer.onError(ar.cause());
              } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                RxJavaPlugins.onError(t);
              }
            }
          }
        });
      } catch (Exception e) {
        if (!disposed.getAndSet(true)) {
          try {
            observer.onError(e);
          } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            RxJavaPlugins.onError(t);
          }
        }
      }
    }
  }
}
