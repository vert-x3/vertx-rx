package io.vertx.reactivex;

import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.FlowableOperator;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.core.json.FlowableUnmarshaller;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FlowableHelper {

  public static <T> FlowableOperator<T, Buffer> unmarshaller(Class<T> mappedType) {
    return new FlowableUnmarshaller<>(java.util.function.Function.identity(), mappedType);
  }

  public static <T> FlowableOperator<T, Buffer> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new FlowableUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef);
  }

}
