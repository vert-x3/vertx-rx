package io.vertx.reactivex.core;

import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.ObservableOperator;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.core.json.ObservableUnmarshaller;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableHelper {

  public static <T> ObservableOperator<T, Buffer> unmarshaller(Class<T> mappedType) {
    return new ObservableUnmarshaller<>(java.util.function.Function.identity(), mappedType);
  }

  public static <T> ObservableOperator<T, Buffer> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new ObservableUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef);
  }

}
