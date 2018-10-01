package io.vertx.reactivex.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.FlowableTransformer;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.impl.FlowableUnmarshaller;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FlowableHelper {

  public static <T> FlowableTransformer<Buffer, T> unmarshaller(Class<T> mappedType) {
    return new FlowableUnmarshaller<>(Buffer::getDelegate, mappedType);
  }

  public static <T> FlowableTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new FlowableUnmarshaller<>(Buffer::getDelegate, mappedTypeRef);
  }

  public static <T> FlowableTransformer<Buffer, T> unmarshaller(Class<T> mappedType, ObjectMapper mapper) {
    return new FlowableUnmarshaller<>(Buffer::getDelegate, mappedType, mapper);
  }

  public static <T> FlowableTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef, ObjectMapper mapper) {
    return new FlowableUnmarshaller<>(Buffer::getDelegate, mappedTypeRef, mapper);
  }
}
