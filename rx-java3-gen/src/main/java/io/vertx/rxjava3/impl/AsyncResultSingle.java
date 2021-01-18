package io.vertx.rxjava3.impl;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.Exceptions;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultSingle<T> extends Single<T> {

  public static <T> Single<T> toSingle(Consumer<Handler<AsyncResult<T>>> subscriptionConsumer) {
    return RxJavaPlugins.onAssembly(new AsyncResultSingle<T>(subscriptionConsumer));
  }

  private final Consumer<Handler<AsyncResult<T>>> subscriptionConsumer;

  public AsyncResultSingle(Consumer<Handler<AsyncResult<T>>> subscriptionConsumer) {
    this.subscriptionConsumer = subscriptionConsumer;
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
                observer.onSuccess(ar.result());
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
