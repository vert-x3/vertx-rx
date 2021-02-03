package io.vertx.rxjava3.core;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.rxjava3.core.MaybeTransformer;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.impl.MaybeUnmarshaller;

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

  public static <T> MaybeTransformer<Buffer, T> unmarshaller(Class<T> mappedType, ObjectCodec mapper) {
    return new MaybeUnmarshaller<>(Buffer::getDelegate, mappedType, mapper);
  }

  public static <T> MaybeTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    return new MaybeUnmarshaller<>(Buffer::getDelegate, mappedTypeRef, mapper);
  }
}
