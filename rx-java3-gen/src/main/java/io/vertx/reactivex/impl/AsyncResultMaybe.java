package io.vertx.reactivex.impl;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
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
public class AsyncResultMaybe<T> extends Maybe<T> {

  private final Consumer<Handler<AsyncResult<T>>> subscriptionConsumer;

  public static <T> Maybe<T> toMaybe(Consumer<Handler<AsyncResult<T>>> subscriptionConsumer) {
    return RxJavaPlugins.onAssembly(new AsyncResultMaybe<T>(subscriptionConsumer));
  }

  public AsyncResultMaybe(Consumer<Handler<AsyncResult<T>>> subscriptionConsumer) {
    this.subscriptionConsumer = subscriptionConsumer;
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
                T val = ar.result();
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
