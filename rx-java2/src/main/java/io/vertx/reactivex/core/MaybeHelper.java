package io.vertx.reactivex.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.MaybeTransformer;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.impl.MaybeUnmarshaller;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MaybeHelper {

  public static <T> MaybeTransformer<Buffer, T> unmarshaller(Class<T> mappedType) {
    return new MaybeUnmarshaller<>(Buffer::getDelegate, mappedType);
  }

  public static <T> MaybeTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new MaybeUnmarshaller<>(Buffer::getDelegate, mappedTypeRef);
  }

  public static <T> MaybeTransformer<Buffer, T> unmarshaller(Class<T> mappedType, ObjectMapper mapper) {
    return new MaybeUnmarshaller<>(Buffer::getDelegate, mappedType, mapper);
  }

  public static <T> MaybeTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef, ObjectMapper mapper) {
    return new MaybeUnmarshaller<>(Buffer::getDelegate, mappedTypeRef, mapper);
  }
}
