package io.vertx.rxjava3.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.jackson.JacksonFactory;

import static java.util.Objects.nonNull;

/**
 * An operator to unmarshall json to pojos.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableUnmarshaller<T, B> implements ObservableTransformer<B, T> {

  private final java.util.function.Function<B, Buffer> unwrap;
  private final Class<T> mappedType;
  private final TypeReference<T> mappedTypeRef;
  private ObjectCodec mapper;

  public ObservableUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType) {
    this(unwrap, mappedType, null, null);

  }

  public ObservableUnmarshaller(java.util.function.Function<B, Buffer> unwrap, TypeReference<T> mappedTypeRef) {
    this(unwrap, null, mappedTypeRef, null);
  }

  public ObservableUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType, ObjectCodec mapper) {
    this(unwrap, mappedType, null, mapper);
  }

  public ObservableUnmarshaller(java.util.function.Function<B, Buffer> unwrap, TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    this(unwrap, null, mappedTypeRef, mapper);
  }

  private ObservableUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType, TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    this.unwrap = unwrap;
    this.mappedType = mappedType;
    this.mappedTypeRef = mappedTypeRef;
    this.mapper = mapper;
  }

  @Override
  public ObservableSource<T> apply(@NonNull Observable<B> upstream) {
    Observable<Buffer> unwrapped = upstream.map(unwrap::apply);
    Single<Buffer> aggregated = unwrapped.collect(Buffer::buffer, Buffer::appendBuffer);
    Maybe<T> unmarshalled = aggregated.toMaybe().concatMap(buffer -> {
      if (buffer.length() > 0) {
        try {
          T obj;
          if (mapper != null) {
            JsonParser parser = mapper.getFactory().createParser(buffer.getBytes());
            obj = nonNull(mappedType) ? mapper.readValue(parser, mappedType) :
              mapper.readValue(parser, mappedTypeRef);
          } else {
            obj = getT(buffer, mappedType, mappedTypeRef);
          }
          return Maybe.just(obj);
        } catch (Exception e) {
          return Maybe.error(e);
        }
      } else {
        return Maybe.empty();
      }
    });
    return unmarshalled.toObservable();
  }

  static <T> T getT(Buffer buffer, Class<T> mappedType, TypeReference<T> mappedTypeRef) {
    T obj;
    obj = nonNull(mappedType) ? Json.CODEC.fromBuffer(buffer, mappedType) :
      JacksonFactory.CODEC.fromBuffer(buffer, mappedTypeRef);
    return obj;
  }
}
