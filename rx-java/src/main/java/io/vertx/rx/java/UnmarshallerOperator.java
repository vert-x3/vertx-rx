package io.vertx.rx.java;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.buffer.Buffer;
import rx.Observable;
import rx.Subscriber;

import static io.vertx.core.json.Json.mapper;
import static java.util.Objects.nonNull;

/**
 * An operator
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class UnmarshallerOperator<T, B> implements Observable.Operator<T, B> {

  private final Class<T> mappedType;
  private final TypeReference<T> mappedTypeRef;

  public UnmarshallerOperator(Class<T> mappedType) {
    this.mappedType = mappedType;
    this.mappedTypeRef = null;
  }

  public UnmarshallerOperator(TypeReference<T> mappedTypeRef) {
    this.mappedType = null;
    this.mappedTypeRef = mappedTypeRef;
  }

  public abstract Buffer unwrap(B buffer);

  @Override
  public Subscriber<? super B> call(Subscriber<? super T> subscriber) {
    final Buffer buffer = Buffer.buffer();

    return new Subscriber<B>(subscriber) {

      @Override
      public void onCompleted() {
        try {
          T obj = null;
          if (buffer.length() > 0) {
            obj = nonNull(mappedType) ? mapper.readValue(buffer.getBytes(), mappedType) :
              mapper.readValue(buffer.getBytes(), mappedTypeRef);
          }
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
