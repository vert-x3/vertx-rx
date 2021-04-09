package io.vertx.rxjava3.impl;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
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
public class AsyncResultCompletable extends Completable {

  private final Consumer<Handler<AsyncResult<Void>>> subscriptionConsumer;

  public static Completable toCompletable(Consumer<Handler<AsyncResult<Void>>> subscriptionConsumer) {
    return RxJavaPlugins.onAssembly(new AsyncResultCompletable(subscriptionConsumer));
  }

  public AsyncResultCompletable(Consumer<Handler<AsyncResult<Void>>> subscriptionConsumer) {
    this.subscriptionConsumer = subscriptionConsumer;
  }

  @Override
  protected void subscribeActual(CompletableObserver observer) {
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
                observer.onComplete();
              } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                RxJavaPlugins.onError(t);
              }
            } else {
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
