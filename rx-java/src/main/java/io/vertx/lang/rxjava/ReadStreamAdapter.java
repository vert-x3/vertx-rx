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
    ProducerImpl producer = subRef.get();
    return producer != null ? producer.expected : -1;
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
    if (producer.expected == 0 && producer.status == Status.ACTIVE) {
      producer.status = Status.PAUSED;
      stream.pause();
    }
  }

  enum Status {
    ACTIVE, PAUSED, ENDED
  }

  class ProducerImpl implements Subscription, Producer {

    private final Subscriber<? super R> subscriber;
    private Status status = Status.ACTIVE;
    private boolean completed; // completed => (expected == 0 && buffer.isEmpty())
    private final Deque<J> buffer = new ArrayDeque<>();
    private long expected;

    ProducerImpl(Subscriber<? super R> subscriber) {
      this.subscriber = subscriber;
    }

    public void unsubscribe() {
      if (!completed) {
        completed = true;
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

    public boolean isUnsubscribed() {
      return completed;
    }

    public void handleData(J event) {
      checkPending();
      if (!completed) {
        if (expected > 0) {
          // (expected > 0) => buffer.isEmpty()
          if (expected < Long.MAX_VALUE) {
            expected--;
          }
          subscriber.onNext(adapter.apply(event));
          if (expected == 0 && status == Status.ACTIVE) {
            status = Status.PAUSED;
            stream.pause();
          }
        } else {
          buffer.add(event);
        }
      }
    }

    public void handleException(Throwable exception) {
      if (status != Status.ENDED) {
        status = Status.ENDED;
        unsubscribe();
        subscriber.onError(exception);
      }
    }

    public void handleEnd(Void end) {
      if (status != Status.ENDED) {
        status = Status.ENDED;
        if (buffer.isEmpty()) {
          unsubscribe();
          subscriber.onCompleted();
        }
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
    public void request(long n) {
      if (n < 0) {
        throw new IllegalArgumentException("No negative request accepted: " + n);
      }
      if (!completed) {
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