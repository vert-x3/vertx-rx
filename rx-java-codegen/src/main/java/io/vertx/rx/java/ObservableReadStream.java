package io.vertx.rx.java;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;

import java.util.ArrayDeque;
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
  private static final Object NO_ITEM = new Object();
  public static final long DEFAULT_MAX_BUFFER_SIZE = 256;

  private final long highWaterMark;
  private final ReadStream<T> stream;
  private final Function<T, R> adapter;
  private final AtomicReference<Sub> subscription = new AtomicReference<>();
  private Throwable completed;

  public ObservableReadStream(ReadStream<T> stream, Function<T, R> adapter) {
    this(stream, adapter, DEFAULT_MAX_BUFFER_SIZE);
  }

  public ObservableReadStream(ReadStream<T> stream, Function<T, R> adapter, long maxBufferSize) {
    this.stream = stream;
    this.adapter = adapter;
    this.highWaterMark = maxBufferSize;
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
          resume = adapter.dispose() && completed == null;
        }
        if (resume) {
          stream.resume();
        }
        RxHelper.setNullHandlers(stream);
      }
    }

    @Override
    public boolean isUnsubscribed() {
      return subscription.get() != this;
    }
  }

  @Override
  public void call(Subscriber<? super R> subscriber) {

    SimpleAdapter adapter = new SimpleAdapter(subscriber);
    Sub sub = new Sub(adapter);
    if (!subscription.compareAndSet(null, sub)) {
      throw new IllegalStateException();
    }

    subscriber.setProducer(sub);
    subscriber.add(sub);
    long requested = adapter.requested();
    if (requested != Long.MAX_VALUE) {
      sub.adapter = new QueueAdapter(requested, subscriber);
    }

    // At this moment reactive back-pressure should have been established (or not)
    // so we can pass set the handlers as they won't change
    stream.exceptionHandler(sub.adapter::end);
    stream.endHandler(v -> sub.adapter.end(COMPLETED_SENTINEL));
    stream.handler(sub.adapter);
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
     * @param n the number o fitems
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

  private class SimpleAdapter extends Adapter {


    SimpleAdapter(Subscriber<? super R> subscriber) {
      super(subscriber);
    }

    @Override
    boolean dispose() {
      return false;
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
  }

  private class QueueAdapter extends Adapter {

    private final long lowWaterMark;
    private ArrayDeque<R> pending = new ArrayDeque<>();
    private boolean draining;
    private boolean paused;
    private boolean subscribed = true;

    private QueueAdapter(long requested, Subscriber<? super R> subscriber) {
      super(subscriber);
      this.requested = requested;
      this.lowWaterMark = highWaterMark / 2;
    }

    @Override
    boolean dispose() {
      if (!subscribed) {
        throw new AssertionError();
      }
      boolean resume = paused;
      paused = false;
      subscribed = false;
      return resume;
    }

    @Override
    public void request(long n) {
      super.request(n);
      drain();
    }

    private void drain() {
      synchronized (ObservableReadStream.this) {
        if (draining) {
          return;
        }
        draining = true;
      }
      while (true) {
        @SuppressWarnings("unchecked")
        R next = (R) NO_ITEM;
        synchronized (ObservableReadStream.this) {
          if (!subscribed) {
            draining = false;
            return;
          } else if (pending.size() > 0) {
            if (requested > 0) {
              next = pending.poll();
              if (requested != Long.MAX_VALUE) {
                requested--;
              }
            }
          } else {
            if (completed != null) {
              break;
            }
          }
        }
        if (next != NO_ITEM) {
          subscriber.onNext(next);
        } else {
          break;
        }
      }
      boolean pause = false;
      boolean resume = false;
      Object completion = null;
      synchronized (ObservableReadStream.this) {
        if (completed != null) {
          if (pending.size() == 0) {
            completion = completed;
            if (paused && pending.size() < lowWaterMark) {
              pause = false;
              resume = true;
            }
          }
        } else {
          if (paused && pending.size() < lowWaterMark) {
            paused = false;
            resume = true;
            pause = false;
          } else
          if (!paused && pending.size() >= highWaterMark) {
            paused = true;
            resume = false;
            pause = true;
          }
        }
        draining = false;
      }
      if (completion != null) {
        if (completion == COMPLETED_SENTINEL) {
          subscriber.onCompleted();
        } else {
          subscriber.onError((Throwable) completion);
        }
      }
      if (pause) {
        stream.pause();
      } else if (resume) {
        stream.resume();
      }
    }

    @Override
    public void handle(T item) {
      synchronized (ObservableReadStream.this) {
        pending.add(adapter.apply(item));
      }
      drain();
    }

    void end(Throwable t) {
      synchronized (ObservableReadStream.this) {
        if (completed != null) {
          return;
        }
        completed = t;
      }
      drain();
    }
  }
}
