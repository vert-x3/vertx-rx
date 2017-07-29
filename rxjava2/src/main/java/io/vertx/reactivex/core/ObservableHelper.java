package io.vertx.reactivex.core;

import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.ObservableOperator;
import io.reactivex.ObservableTransformer;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.json.ObservableUnmarshaller;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableHelper {

  public static <T> ObservableTransformer<Buffer, T> unmarshaller(Class<T> mappedType) {
    return new ObservableUnmarshaller<>(Buffer::getDelegate, mappedType);
  }

  public static <T> ObservableTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new ObservableUnmarshaller<>(Buffer::getDelegate, mappedTypeRef);
  }

}
