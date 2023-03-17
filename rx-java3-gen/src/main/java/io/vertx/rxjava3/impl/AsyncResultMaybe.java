package io.vertx.rxjava3.impl;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultMaybe<T, U> extends Maybe<T> {

  public static <T> Maybe<T> toMaybe(Consumer<Handler<AsyncResult<T>>> subscriptionConsumer) {
    return RxJavaPlugins.onAssembly(new AsyncResultMaybe<>(subscriptionConsumer, Function.identity()));
  }

  public static <T, U> Maybe<T> toMaybe(Future<U> future, Function<U, T> mapping) {
    if (future == null) {
      return Maybe.error(new NullPointerException());
    }
    return RxJavaPlugins.onAssembly(new AsyncResultMaybe<>(future::onComplete, mapping));
  }

  public static <T, U> Maybe<T> toMaybe(Supplier<Future<U>> future, Function<U, T> mapping) {
    if (future == null) {
      return Maybe.error(new NullPointerException());
    }
    return RxJavaPlugins.onAssembly(new AsyncResultMaybe<>(h -> future.get().onComplete(h), mapping));
  }

  private final Consumer<Handler<AsyncResult<U>>> subscriptionConsumer;
  private final Function<U, T> mapping;

  private AsyncResultMaybe(Consumer<Handler<AsyncResult<U>>> subscriptionConsumer, Function<U, T> mapping) {
    this.subscriptionConsumer = subscriptionConsumer;
    this.mapping = mapping;
  }

  @Override
  protected void subscribeActual(MaybeObserver<? super T> observer) {
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
                T val = mapping.apply(ar.result());
                if (val != null) {
                  observer.onSuccess(val);
                } else {
                  observer.onComplete();
                }
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
