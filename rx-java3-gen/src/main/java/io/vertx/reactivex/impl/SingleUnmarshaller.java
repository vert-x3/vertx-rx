package io.vertx.reactivex.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.vertx.core.buffer.Buffer;

import static io.vertx.reactivex.impl.ObservableUnmarshaller.getT;
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
  private final ObjectCodec mapper;


  public SingleUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType) {
    this(unwrap, mappedType,null,  null);

  }

  public SingleUnmarshaller(java.util.function.Function<B, Buffer> unwrap, TypeReference<T> mappedTypeRef) {
    this(unwrap, null, mappedTypeRef, null);
  }

  public SingleUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType, ObjectCodec mapper) {
    this(unwrap, mappedType,null,  mapper);
  }

  public SingleUnmarshaller(java.util.function.Function<B, Buffer> unwrap, TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    this(unwrap, null, mappedTypeRef, mapper);
  }

  private SingleUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType, TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    this.unwrap = unwrap;
    this.mappedType = mappedType;
    this.mappedTypeRef = mappedTypeRef;
    this.mapper = mapper;
  }

  @Override
  public SingleSource<T> apply(@NonNull Single<B> upstream) {
    Single<Buffer> unwrapped = upstream.map(unwrap::apply);
    Single<T> unmarshalled = unwrapped.flatMap(buffer -> {
      try {
        T obj;
        if (mapper != null) {
          JsonParser parser = mapper.getFactory().createParser(buffer.getBytes());
          obj = nonNull(mappedType) ? mapper.readValue(parser, mappedType) :
            mapper.readValue(parser, mappedTypeRef);
        } else {
          obj = getT(buffer, mappedType, mappedTypeRef);
        }
        return Single.just(obj);
      } catch (Exception e) {
        return Single.error(e);
      }
    });
    return unmarshalled;
  }
}
