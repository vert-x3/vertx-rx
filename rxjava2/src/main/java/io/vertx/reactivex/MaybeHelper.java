package io.vertx.reactivex;

import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.MaybeOperator;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.core.json.MaybeUnmarshaller;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MaybeHelper {

  public static <T> MaybeOperator<T, Buffer> unmarshaller(Class<T> mappedType) {
    return new MaybeUnmarshaller<>(java.util.function.Function.identity(), mappedType);
  }

  public static <T> MaybeOperator<T, Buffer> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new MaybeUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef);
  }

}
