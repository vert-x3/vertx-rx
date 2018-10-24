package io.vertx.rx.java;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Adapt a {@link ReadStream} to be an {@link Observable.OnSubscribe} that can be used to
 * build {@link Observable}.
 * <p>
 * The adapter supports <a href="https://github.com/ReactiveX/RxJava/wiki/Backpressure">reactive pull</a>
 * back-pressure.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableReadStream<T, R> implements Observable.OnSubscribe<R> {

  private static final Throwable COMPLETED_SENTINEL = new Throwable();
  public static final long DEFAULT_MAX_BUFFER_SIZE = 256;

  private final ReadStream<T> stream;
  private final Function<T, R> adapter;
  private final AtomicReference<Sub> subscription = new AtomicReference<>();
  private boolean subscribed;

  public ObservableReadStream(ReadStream<T> stream, Function<T, R> adapter) {
    this(stream, adapter, DEFAULT_MAX_BUFFER_SIZE);
  }

  public ObservableReadStream(ReadStream<T> stream, Function<T, R> adapter, long maxBufferSize) {

    stream.pause();

    this.stream = stream;
    this.adapter = adapter;
  }

  public long getRequested() {
    Sub sub = subscription.get();
    return sub != null ? sub.adapter.requested() : 0;
  }

  private class Sub implements Subscription, Producer {

    private Adapter adapter;

    Sub(Adapter queue) {
      this.adapter = queue;
    }

    @Override
    public void request(long n) {
      if (n < 0) {
        throw new IllegalArgumentException("Cannot request negative items:" + n);
      }
      adapter.request(n);
    }

    @Override
    public void unsubscribe() {
      if (subscription.compareAndSet(this, null)) {
        boolean resume;
        synchronized (ObservableReadStream.this) {
          resume = adapter.dispose();
        }
        RxHelper.setNullHandlers(stream);
        if (resume) {
          stream.resume();
        }
        subscribed = false;
      }
    }

    @Override
    public boolean isUnsubscribed() {
      return subscription.get() != this;
    }
  }

  @Override
  public void call(Subscriber<? super R> subscriber) {

    QueueAdapter adapter = new QueueAdapter(subscriber);
    Sub sub = new Sub(adapter);
    if (!subscription.compareAndSet(null, sub)) {
      throw new IllegalStateException();
    }

    subscriber.setProducer(sub);
    subscriber.add(sub);

    // At this moment reactive back-pressure should have been established (or not)
    // so we can pass set the handlers as they won't change
    stream.exceptionHandler(sub.adapter::end);
    stream.endHandler(v -> sub.adapter.end(COMPLETED_SENTINEL));
    stream.handler(sub.adapter);
    subscribed = true;

    // Ask what we want
    long requested = adapter.requested();
    stream.pause();
    if (requested > 0L) {
      stream.fetch(requested);
    }
  }

  /*
   * Adapt the callbacks from the read stream to the subscriber with a strategy.
   */
  private abstract class Adapter implements Handler<T> {

    protected final Subscriber<? super R> subscriber;
    long requested;

    Adapter(Subscriber<? super R> subscriber) {
      this.subscriber = subscriber;
    }

    synchronized long requested() {
      synchronized (ObservableReadStream.this) {
        return requested;
      }
    }

    /**
     * Request n items
     *
     * @param n the number of items
     */
    void request(long n) {
      synchronized (ObservableReadStream.this) {
        if (n == Long.MAX_VALUE || (n >= Long.MAX_VALUE - requested)) {
          requested = Long.MAX_VALUE;
        } else {
          requested += n;
        }
      }
    }

    /**
     * Dispose the state - should not do any callback as it is called from an unsubscribe
     * @return true if the stream should be resumed according to the internal state
     */
    abstract boolean dispose();

    /**
     * Signal read stream end or error.
     *
     * @param t null if end otherwise the error
     */
    abstract void end(Throwable t);

  }

  private class QueueAdapter extends Adapter {

    QueueAdapter(Subscriber<? super R> subscriber) {
      super(subscriber);
    }

    @Override
    boolean dispose() {
      return true;
    }

    @Override
    void end(Throwable t) {
      if (t == COMPLETED_SENTINEL) {
        subscriber.onCompleted();
      } else {
        subscriber.onError(t);
      }
    }

    @Override
    public void handle(T item) {
      subscriber.onNext(adapter.apply(item));
    }

    @Override
    void request(long n) {
      super.request(n);
      if (subscribed && n > 0L) {
        stream.fetch(n);
      }
    }
  }
}
