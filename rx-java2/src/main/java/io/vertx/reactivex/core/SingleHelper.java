package io.vertx.reactivex.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.SingleTransformer;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.impl.SingleUnmarshaller;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SingleHelper {

  public static <T> SingleTransformer<Buffer, T> unmarshaller(Class<T> mappedType) {
    return new SingleUnmarshaller<>(Buffer::getDelegate, mappedType);
  }

  public static <T> SingleTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new SingleUnmarshaller<>(Buffer::getDelegate, mappedTypeRef);
  }

  public static <T> SingleTransformer<Buffer, T> unmarshaller(Class<T> mappedType, ObjectMapper mapper) {
    return new SingleUnmarshaller<>(Buffer::getDelegate, mappedType, mapper);
  }

  public static <T> SingleTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef, ObjectMapper mapper) {
    return new SingleUnmarshaller<>(Buffer::getDelegate, mappedTypeRef, mapper);
  }
}
