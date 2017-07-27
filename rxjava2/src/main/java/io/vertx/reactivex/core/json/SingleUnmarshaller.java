package io.vertx.reactivex.core.json;

import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

import static io.vertx.core.json.Json.mapper;
import static java.util.Objects.nonNull;

/**
 * An operator to unmarshall json to pojos.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SingleUnmarshaller<T, B> implements SingleTransformer<B, T> {

  private final java.util.function.Function<B, Buffer> unwrap;
  private final Class<T> mappedType;
  private final TypeReference<T> mappedTypeRef;

  public SingleUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType) {
    this.unwrap = unwrap;
    this.mappedType = mappedType;
    this.mappedTypeRef = null;
  }

  public SingleUnmarshaller(java.util.function.Function<B, Buffer> unwrap, TypeReference<T> mappedTypeRef) {
    this.unwrap = unwrap;
    this.mappedType = null;
    this.mappedTypeRef = mappedTypeRef;
  }

  @Override
  public SingleSource<T> apply(@NonNull Single<B> upstream) {
    Single<Buffer> unwrapped = upstream.map(unwrap::apply);
    Single<T> unmarshalled = unwrapped.flatMap(buffer -> {
      try {
        T obj = nonNull(mappedType) ? mapper.readValue(buffer.getBytes(), mappedType) :
          mapper.readValue(buffer.getBytes(), mappedTypeRef);
        return Single.just(obj);
      } catch (IOException e) {
        return Single.error(e);
      }
    });
    return unmarshalled;
  }
}
