package io.vertx.rx.java;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.jackson.JacksonCodec;
import io.vertx.core.spi.json.JsonCodec;
import rx.Observable;
import rx.Subscriber;

import static java.util.Objects.nonNull;

/**
 * An operator
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class UnmarshallerOperator<T, B> implements Observable.Operator<T, B> {

  private final Class<T> mappedType;
  private final TypeReference<T> mappedTypeRef;
  private ObjectCodec mapper;

  public UnmarshallerOperator(Class<T> mappedType) {
    this.mappedType = mappedType;
    this.mapper = null;
    this.mappedTypeRef = null;
  }

  public UnmarshallerOperator(Class<T> mappedType, ObjectCodec mapper) {
    this.mappedType = mappedType;
    this.mapper = mapper;
    this.mappedTypeRef = null;
  }

  public UnmarshallerOperator(TypeReference<T> mappedTypeRef) {
    this.mappedType = null;
    this.mapper = null;
    this.mappedTypeRef = mappedTypeRef;
  }

  public UnmarshallerOperator(TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    this.mappedType = null;
    this.mapper = mapper;
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
            if (mapper != null) {
              JsonParser parser = mapper.getFactory().createParser(buffer.getBytes());
              obj = nonNull(mappedType) ? mapper.readValue(parser, mappedType) :
                mapper.readValue(parser, mappedTypeRef);
            } else {
              obj = nonNull(mappedType) ? Json.CODEC.fromBuffer(buffer, mappedType) :
                JacksonCodec.INSTANCE.fromBuffer(buffer, mappedTypeRef);
            }
          }
          subscriber.onNext(obj);
          subscriber.onCompleted();
        } catch (Exception e) {
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
