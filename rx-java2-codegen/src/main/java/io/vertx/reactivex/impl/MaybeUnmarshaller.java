package io.vertx.reactivex.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.annotations.NonNull;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;

import java.io.IOException;

import static java.util.Objects.nonNull;

/**
 * An operator to unmarshall json to pojos.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MaybeUnmarshaller<T, B> implements MaybeTransformer<B, T> {

  private final java.util.function.Function<B, Buffer> unwrap;
  private final Class<T> mappedType;
  private final TypeReference<T> mappedTypeRef;
  private final ObjectMapper mapper;

  public MaybeUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType) {
    this(unwrap, mappedType, null, Json.mapper);
  }

  public MaybeUnmarshaller(java.util.function.Function<B, Buffer> unwrap, TypeReference<T> mappedTypeRef) {
    this(unwrap, null, mappedTypeRef, Json.mapper);
  }

  public MaybeUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType, ObjectMapper mapper) {
    this(unwrap, mappedType, null, mapper);
  }

  public MaybeUnmarshaller(java.util.function.Function<B, Buffer> unwrap, TypeReference<T> mappedTypeRef, ObjectMapper mapper) {
    this(unwrap, null, mappedTypeRef, mapper);
  }

  private MaybeUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType, TypeReference<T> mappedTypeRef, ObjectMapper mapper) {
    this.unwrap = unwrap;
    this.mappedType = mappedType;
    this.mappedTypeRef = mappedTypeRef;
    this.mapper = mapper;
  }

  @Override
  public MaybeSource<T> apply(@NonNull Maybe<B> upstream) {
    Maybe<Buffer> unwrapped = upstream.map(unwrap::apply);
    Maybe<T> unmarshalled = unwrapped.concatMap(buffer -> {
      if (buffer.length() > 0) {
        try {
          T obj = nonNull(mappedType) ? mapper.readValue(buffer.getBytes(), mappedType) :
            mapper.readValue(buffer.getBytes(), mappedTypeRef);
          return Maybe.just(obj);
        } catch (IOException e) {
          return Maybe.error(e);
        }
      } else {
        return Maybe.empty();
      }
    });
    return unmarshalled;
  }
}
