package io.vertx.rx.java;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import rx.Observable;
import rx.Subscriber;

import java.io.IOException;

/**
 * An operator
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class UnmarshallerOperator<T, B> implements Observable.Operator<T, B> {

  private final Class<T> mappedType;

  public UnmarshallerOperator(Class<T> mappedType) {
    this.mappedType = mappedType;
  }

  public abstract Buffer unwrap(B buffer);

  @Override
  public Subscriber<? super B> call(Subscriber<? super T> subscriber) {
    Buffer buffer = Buffer.buffer();
    return new Subscriber<B>(subscriber) {

      @Override
      public void onCompleted() {
        try {
          T obj = Json.mapper.readValue(buffer.getBytes(), mappedType);
          subscriber.onNext(obj);
          subscriber.onCompleted();
        } catch (IOException e) {
          onError(e);
        }
      }

      @Override
      public void onError(Throwable e) {
        subscriber.onError(e);
      }

      @Override
      public void onNext(B item) {
        buffer.appendBuffer(unwrap(item));
      }
    };
  }
}
