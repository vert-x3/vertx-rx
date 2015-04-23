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
 * - implement correctly the requested == Long_MAX_VALUE semantic of Producer#request
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
    subscriber.setProducer(producer);
    subscriber.add(producer);
    stream.exceptionHandler(producer::handleException);
    stream.endHandler(producer::handleEnd);
    stream.handler(producer::handleData);
    if (producer.expected == 0) {
      producer.status = Status.PAUSED;
      stream.pause();
    }
  }

  enum Status {
    ACTIVE, PAUSED, COMPLETED
  }

  class ProducerImpl implements Subscription, Producer {

    private final Subscriber<? super R> subscriber;
    private final Deque<J> pending = new ArrayDeque<>();
    private long expected;
    private Status status = Status.ACTIVE;
    private boolean ended;

    ProducerImpl(Subscriber<? super R> subscriber) {
      this.subscriber = subscriber;
    }

    public void unsubscribe() {
      if (status != Status.COMPLETED) {
        status = Status.COMPLETED;
        subRef.set(null);
        try {
          stream.exceptionHandler(null);
          stream.endHandler(null);
          stream.handler(null);
        } catch (Exception ignore) {
        }
        subscriber.onCompleted();
      }
    }

    public boolean isUnsubscribed() {
      return status == Status.COMPLETED;
    }

    public void handleData(J event) {
      checkPending();
      if (status != Status.COMPLETED) {
        if (expected > 0) {
          if (expected < Long.MAX_VALUE) {
            expected--;
          }
          subscriber.onNext(adapter.apply(event));
          if (expected == 0 && status != Status.PAUSED) {
            status = Status.PAUSED;
            stream.pause();
          }
        } else {
          pending.add(event);
        }
      }
    }

    public void handleException(Throwable exception) {
//      if (!ended) {
//        ended = true;
//        pending.clear();
//      }
      if (status != Status.COMPLETED) {
        status = Status.COMPLETED;
        subRef.set(null);
        try {
          stream.exceptionHandler(null);
          stream.endHandler(null);
          stream.handler(null);
        } catch (Exception ignore) {
        }
        subscriber.onError(exception);
      }
    }

    public void handleEnd(Void end) {
      if (!ended) {
        ended = true;
        if (pending.size() == 0 && status != Status.COMPLETED) {
          unsubscribe();
        }
      }
    }

    private void checkPending() {
      J event;
      while (status == Status.ACTIVE && expected > 0 && (event = pending.poll()) != null) {
        expected--;
        subscriber.onNext(adapter.apply(event));
      }
    }

    @Override
    public void request(long n) {
      if (n < 0) {
        throw new IllegalArgumentException("No negative request accepted: " + n);
      }
      if (status != Status.COMPLETED) {
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
        if (expected > 0) {
          boolean paused = status == Status.PAUSED;
          status = Status.ACTIVE;
          checkPending();
          if (status != Status.COMPLETED) {
            if (ended) {
              if (pending.size() == 0) {
                status = Status.COMPLETED;
                subscriber.onCompleted();
              }
            } else {
              if (expected > 0) {
                if (paused) {
                  stream.resume();
                }
              } else {
                if (!paused) {
                  status = Status.PAUSED;
                }
              }
            }
          }
        }
      }
    }
  }
}