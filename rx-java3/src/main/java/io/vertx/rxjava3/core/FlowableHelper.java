package io.vertx.rxjava3.core;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.vertx.core.buffer.Buffer;
import io.vertx.rxjava3.impl.FlowableUnmarshaller;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FlowableHelper {

  public static <T> FlowableTransformer<Buffer, T> unmarshaller(Class<T> mappedType) {
    return new FlowableUnmarshaller<>(Function.identity(), mappedType);
  }

  public static <T> FlowableTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new FlowableUnmarshaller<>(Function.identity(), mappedTypeRef);
  }

  public static <T> FlowableTransformer<Buffer, T> unmarshaller(Class<T> mappedType, ObjectCodec mapper) {
    return new FlowableUnmarshaller<>(Function.identity(), mappedType, mapper);
  }

  public static <T> FlowableTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    return new FlowableUnmarshaller<>(Function.identity(), mappedTypeRef, mapper);
  }
}
