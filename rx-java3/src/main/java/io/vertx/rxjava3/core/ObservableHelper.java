package io.vertx.rxjava3.core;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.vertx.core.buffer.Buffer;
import io.vertx.rxjava3.impl.ObservableUnmarshaller;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableHelper {

  public static <T> ObservableTransformer<Buffer, T> unmarshaller(Class<T> mappedType) {
    return new ObservableUnmarshaller<>(Function.identity(), mappedType);
  }

  public static <T> ObservableTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new ObservableUnmarshaller<>(Function.identity(), mappedTypeRef);
  }

  public static <T> ObservableTransformer<Buffer, T> unmarshaller(Class<T> mappedType, ObjectCodec mapper) {
    return new ObservableUnmarshaller<>(Function.identity(), mappedType, mapper);
  }

  public static <T> ObservableTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    return new ObservableUnmarshaller<>(Function.identity(), mappedTypeRef, mapper);
  }

}
