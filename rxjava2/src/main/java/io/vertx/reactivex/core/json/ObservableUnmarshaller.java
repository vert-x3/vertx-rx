package io.vertx.reactivex.core.json;

import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
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
public class ObservableUnmarshaller<T, B> implements ObservableOperator<T, B> {

  private final java.util.function.Function<B, Buffer> unwrap;
  private final Class<T> mappedType;
  private final TypeReference<T> mappedTypeRef;

  public ObservableUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType) {
    this.unwrap = unwrap;
    this.mappedType = mappedType;
    this.mappedTypeRef = null;
  }

  public ObservableUnmarshaller(java.util.function.Function<B, Buffer> unwrap, TypeReference<T> mappedTypeRef) {
    this.unwrap = unwrap;
    this.mappedType = null;
    this.mappedTypeRef = mappedTypeRef;
  }

  @Override
  public Observer<? super B> apply(@NonNull Observer<? super T> observer) throws Exception {
    final Buffer buffer = Buffer.buffer();
    return new Observer<B>() {
      @Override
      public void onSubscribe(@NonNull Disposable d) {
        observer.onSubscribe(d);
      }
      @Override
      public void onNext(B item) {
        buffer.appendBuffer(unwrap.apply(item));
      }
      @Override
      public void onError(Throwable t) {
        observer.onError(t);
      }
      @Override
      public void onComplete() {
        try {
          T obj = null;
          if (buffer.length() > 0) {
            obj = nonNull(mappedType) ? mapper.readValue(buffer.getBytes(), mappedType) :
              mapper.readValue(buffer.getBytes(), mappedTypeRef);
          }
          observer.onNext(obj);
          observer.onComplete();
        } catch (IOException e) {
          onError(e);
        }
      }
    };
  }
}
