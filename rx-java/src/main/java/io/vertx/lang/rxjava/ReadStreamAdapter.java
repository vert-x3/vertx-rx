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
    if (producer.want == 0) {
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
    private long want;
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
        if (want > 0) {
          want--;
          subscriber.onNext(adapter.apply(event));
        } else {
          pending.add(event);
          if (status != Status.PAUSED) {
            status = Status.PAUSED;
            stream.pause();
          }
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
      while (status == Status.ACTIVE && want > 0 && (event = pending.poll()) != null) {
        want--;
        subscriber.onNext(adapter.apply(event));
      }
    }

    @Override
    public void request(long n) {
      if (status != Status.COMPLETED) {
        want += n;
        if (want > 0) {
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
              if (want > 0) {
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