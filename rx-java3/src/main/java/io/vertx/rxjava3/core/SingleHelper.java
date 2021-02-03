package io.vertx.rxjava3.core;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.impl.SingleUnmarshaller;

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

  public static <T> SingleTransformer<Buffer, T> unmarshaller(Class<T> mappedType, ObjectCodec mapper) {
    return new SingleUnmarshaller<>(Buffer::getDelegate, mappedType, mapper);
  }

  public static <T> SingleTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    return new SingleUnmarshaller<>(Buffer::getDelegate, mappedTypeRef, mapper);
  }
}
