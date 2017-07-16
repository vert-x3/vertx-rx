package io.vertx.reactivex.core.json;

import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOperator;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

import static io.vertx.core.json.Json.mapper;
import static java.util.Objects.nonNull;

/**
 * An operator to unmarshall json to pojos.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MaybeUnmarshaller<T, B> implements MaybeOperator<T, B> {

  private final java.util.function.Function<B, Buffer> unwrap;
  private final Class<T> mappedType;
  private final TypeReference<T> mappedTypeRef;

  public MaybeUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType) {
    this.unwrap = unwrap;
    this.mappedType = mappedType;
    this.mappedTypeRef = null;
  }

  public MaybeUnmarshaller(java.util.function.Function<B, Buffer> unwrap, TypeReference<T> mappedTypeRef) {
    this.unwrap = unwrap;
    this.mappedType = null;
    this.mappedTypeRef = mappedTypeRef;
  }

  @Override
  public MaybeObserver<? super B> apply(@NonNull MaybeObserver<? super T> observer) throws Exception {
    return new MaybeObserver<B>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        observer.onSubscribe(d);
      }
      @Override
      public void onSuccess(@NonNull B b) {
        try {
          Buffer buffer = unwrap.apply(b);
          T obj = null;
          if (buffer.length() > 0) {
            obj = nonNull(mappedType) ? mapper.readValue(buffer.getBytes(), mappedType) :
              mapper.readValue(buffer.getBytes(), mappedTypeRef);
          }
          observer.onSuccess(obj);
        } catch (IOException e) {
          onError(e);
        }
      }
      @Override
      public void onError(Throwable t) {
        observer.onError(t);
      }
      @Override
      public void onComplete() {
        observer.onComplete();
      }
    };
  }
}
