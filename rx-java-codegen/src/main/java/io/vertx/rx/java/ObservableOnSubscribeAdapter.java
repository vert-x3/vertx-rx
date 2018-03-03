package io.vertx.rx.java;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableOnSubscribeAdapter<T> implements Observable.OnSubscribe<T> {

  protected final AtomicReference<SingleSubscription> subRef = new AtomicReference<>();

  // OnSubscribe

  /**
   * Subscription
   */
  public void call(Subscriber<? super T> sub) {
    SingleSubscription singleSub = new SingleSubscription(sub);
    if (!this.subRef.compareAndSet(null, singleSub)) {
      throw new IllegalStateException("Cannot have multiple subscriptions");
    }
    sub.add(singleSub);
    try {
      onSubscribed();
    }
    // If the execution fails then assume then handle() will never be called and
    // emit an error
    catch (Throwable t) {
      fireError(t);
    }
  }

  // Hooks

  protected void onSubscribed() {
  }

  protected void onUnsubscribed() {
  }

  // Implementation

  /**
   * Fire next to active observer
   */
  protected final void fireNext(T next) {
    Subscriber<? super T> s = getSubscriber();
    if (s != null) {
      s.onNext(next);
    }
  }

  /**
   * Fire result to active observer
   */
  protected final void fireResult(T res) {
    Subscriber<? super T> s = getSubscriber();
    if (s != null) {
      s.onNext(res);
      subRef.set(null);
      s.onCompleted();
    }
  }

  /**
   * Fire completed to active observer
   */
  protected void fireComplete() {
    Subscriber<? super T> s = getSubscriber();
    if (s != null) {
      this.subRef.set(null);
      s.onCompleted();
    }
  }

  /**
   * Fire error to active observer
   */
  protected void fireError(Throwable t) {
    Subscriber<? super T> s = getSubscriber();
    if (s != null) {
      this.subRef.set(null);
      s.onError(t);
    }
  }

  /**
   * Get subscriber
   */
  protected Subscriber<? super T> getSubscriber() {
    SingleSubscription singleSub = this.subRef.get();
    return (singleSub != null) ? singleSub.subscriber : null;
  }

  class SingleSubscription implements Subscription {

    final Subscriber<? super T> subscriber;

    SingleSubscription(Subscriber<? super T> subscriber) {
      this.subscriber = subscriber;
    }

    public void unsubscribe() {
      if (isUnsubscribed()) {
        return;
      }
      if (subRef.compareAndSet(this, null)) {
        onUnsubscribed();
      }
    }

    public boolean isUnsubscribed() {
      return subRef.get() != this;
    }
  }
}
