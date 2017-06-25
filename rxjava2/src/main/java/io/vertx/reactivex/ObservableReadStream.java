package io.vertx.reactivex;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.vertx.core.streams.ReadStream;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableReadStream<T, U> extends Observable<U> {

  private final ReadStream<T> stream;
  private final Function<T, U> f;
  private final AtomicReference<Observer<? super U>> observer = new AtomicReference<>();

  public ObservableReadStream(ReadStream<T> stream, Function<T, U> f) {
    this.stream = stream;
    this.f = f;
  }

  class Subscription implements Disposable {
    private static final int SUBSCRIBED = 0x01;
    private static final int DISPOSED = 0x02;
    private AtomicInteger status = new AtomicInteger();
    private final Observer<? super U> observer;
    public Subscription(Observer<? super U> observer) {
      this.observer = observer;
    }
    private boolean cancel() {
      while (true) {
        int current = status.get();
        if ((current & DISPOSED) != 0) {
          return false;
        }
        int next = current | DISPOSED;
        if (status.compareAndSet(current, next)) {
          if ((next & SUBSCRIBED) != 0) {
            unset();
          }
          return true;
        }
      }
    }
    private void set() {
      stream.endHandler(v -> {
        if (cancel()) {
          observer.onComplete();
        }
      });
      stream.exceptionHandler(err -> {
        if (cancel()) {
          observer.onError(err);
        }
      });
      stream.handler(item -> {
        observer.onNext(f.apply(item));
      });
      while (true) {
        int current = status.get();
        int next = current | SUBSCRIBED;
        if (status.compareAndSet(current, next)) {
          if ((current & DISPOSED) != 0) {
            unset();
          }
          break;
        }
      }
    }
    private void unset() {
      stream.exceptionHandler(null);
      stream.endHandler(null);
      stream.handler(null);
      ObservableReadStream.this.observer.set(null);
    }
    @Override
    public void dispose() {
      cancel();
    }
    @Override
    public boolean isDisposed() {
      return (status.get() & DISPOSED) != 0;
    }
  }

  @Override
  protected void subscribeActual(Observer<? super U> o) {
    if (observer.compareAndSet(o, null)) {
      o.onError(new RuntimeException("Already subscribed"));
      return;
    }
    Subscription sub = new Subscription(o);
    o.onSubscribe(sub);
    sub.set();
  }
}
