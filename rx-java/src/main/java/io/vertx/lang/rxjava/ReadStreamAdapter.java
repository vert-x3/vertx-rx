package io.vertx.lang.rxjava;

import io.vertx.core.streams.ReadStream;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * todo:
 * - provide a max size for the pending buffer
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamAdapter<J, R> implements Observable.OnSubscribe<R> {

  private final ReadStream<J> stream;
  private final Function<J, R> adapter;
  private final AtomicReference<ProducerImpl> subRef = new AtomicReference<>();

  public ReadStreamAdapter(ReadStream<J> stream, Function<J, R> adapter) {
    this.stream = stream;
    this.adapter = adapter;
  }

  /**
   * @return the number of expected events for the current subscriber or -1 is there is no subscriber
   */
  public long getExpected() {
    // Use for testing
    ProducerImpl producer = subRef.get();
    if (producer != null) {
      synchronized (producer) {
        return producer.expected;
      }
    }
    return -1;
  }

  public void call(Subscriber<? super R> subscriber) {
    ProducerImpl producer = new ProducerImpl(subscriber);
    if (!this.subRef.compareAndSet(null, producer)) {
      throw new IllegalStateException("Cannot have multiple subscriptions");
    }
    stream.exceptionHandler(producer::handleException);
    stream.endHandler(producer::handleEnd);
    stream.handler(producer::handleData);
    subscriber.setProducer(producer);
    subscriber.add(producer);
    synchronized (producer) {
      if (producer.expected == 0 && producer.status == Status.ACTIVE) {
        producer.status = Status.PAUSED;
        stream.pause();
      }
    }
  }

  enum Status {
    ACTIVE, PAUSED, ENDED
  }

  class ProducerImpl implements Subscription, Producer {

    private final Subscriber<? super R> subscriber;
    private Status status = Status.ACTIVE;
    private boolean unregistered; // unregistered => (expected == 0 && buffer.isEmpty())
    private final Deque<J> buffer = new ArrayDeque<>();
    private long expected;

    ProducerImpl(Subscriber<? super R> subscriber) {
      this.subscriber = subscriber;
    }

    public synchronized void unsubscribe() {
      if (!unregistered) {
        unregistered = true;
        subRef.set(null);
        expected = 0;
        buffer.clear();
        try {
          stream.exceptionHandler(null);
          stream.endHandler(null);
          stream.handler(null);
        } catch (Exception ignore) {
        }
      }
    }

    public synchronized boolean isUnsubscribed() {
      return unregistered;
    }

    public synchronized void handleData(J event) {
      checkPending();
      if (!unregistered) {
        if (expected > 0) {
          // (expected > 0) => buffer.isEmpty()
          if (expected < Long.MAX_VALUE) {
            expected--;
          }
          subscriber.onNext(adapter.apply(event));
          if (expected == 0 && status == Status.ACTIVE && !isUnsubscribed()) {
            status = Status.PAUSED;
            stream.pause();
          }
        } else {
          buffer.add(event);
        }
      }
    }

    public synchronized void handleException(Throwable exception) {
      if (status != Status.ENDED) {
        status = Status.ENDED;
        unsubscribe();
        subscriber.onError(exception);
      }
    }

    public synchronized void handleEnd(Void end) {
      switch (status) {
        case ACTIVE:
          if (buffer.isEmpty()) {
            unsubscribe();
            subscriber.onCompleted();
          }
          break;
        case PAUSED:
          if (buffer.isEmpty() || expected == 0) {
            unsubscribe();
            subscriber.onCompleted();
          }
          break;
      }
    }

    private void checkPending() {
      J event;
      while (expected > 0 && (event = buffer.poll()) != null) {
        if (expected < Long.MAX_VALUE) {
          expected--;
        }
        subscriber.onNext(adapter.apply(event));
      }
    }

    @Override
    public synchronized void request(long n) {
      if (n < 0) {
        throw new IllegalArgumentException("No negative request accepted: " + n);
      }
      if (!unregistered) {
        if (expected < Long.MAX_VALUE) {
          if (n == Long.MAX_VALUE) {
            expected = Long.MAX_VALUE;
          } else {
            try {
              expected = Math.addExact(expected, n);
            } catch (ArithmeticException e) {
              expected = Long.MAX_VALUE;
            }
          }
        }
        checkPending();
        if (status == Status.ENDED) {
          if (buffer.isEmpty()) {
            unsubscribe();
            subscriber.onCompleted();
          }
        } else if (status == Status.PAUSED) {
          if (expected > 0) {
            status = Status.ACTIVE;
            stream.resume();
          }
        } else if (status == Status.ACTIVE) {
          if (expected == 0) {
            status = Status.PAUSED;
            stream.pause();
          }
        }
      }
    }
  }
}