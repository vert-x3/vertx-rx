package io.vertx.reactivex.core.json;

import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.FlowableOperator;
import io.reactivex.annotations.NonNull;
import io.vertx.core.buffer.Buffer;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import static io.vertx.core.json.Json.mapper;
import static java.util.Objects.nonNull;

/**
 * An operator to unmarshall json to pojos.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FlowableUnmarshaller<T, B> implements FlowableOperator<T, B> {

  public static <T> FlowableOperator<T, Buffer> of(Class<T> mappedType) {
    return new FlowableUnmarshaller<>(java.util.function.Function.identity(), mappedType);
  }

  public static <T> FlowableOperator<T, Buffer> of(TypeReference<T> mappedTypeRef) {
    return new FlowableUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef);
  }

  private final java.util.function.Function<B, Buffer> unwrap;
  private final Class<T> mappedType;
  private final TypeReference<T> mappedTypeRef;

  protected FlowableUnmarshaller(java.util.function.Function<B, Buffer> unwrap, Class<T> mappedType) {
    this.unwrap = unwrap;
    this.mappedType = mappedType;
    this.mappedTypeRef = null;
  }

  protected FlowableUnmarshaller(java.util.function.Function<B, Buffer> unwrap, TypeReference<T> mappedTypeRef) {
    this.unwrap = unwrap;
    this.mappedType = null;
    this.mappedTypeRef = mappedTypeRef;
  }

  @Override
  public Subscriber<? super B> apply(@NonNull Subscriber<? super T> subscriber) throws Exception {
    final Buffer buffer = Buffer.buffer();
    return new Subscriber<B>() {
      @Override
      public void onSubscribe(Subscription s) {
        subscriber.onSubscribe(new Subscription() {
          @Override
          public void request(long n) {
            s.request(Long.MAX_VALUE);
          }
          @Override
          public void cancel() {
            s.cancel();
          }
        });
      }

      @Override
      public void onNext(B item) {
        buffer.appendBuffer(unwrap.apply(item));
      }

      @Override
      public void onError(Throwable t) {
        subscriber.onError(t);
      }

      @Override
      public void onComplete() {
        try {
          T obj = null;
          if (buffer.length() > 0) {
            obj = nonNull(mappedType) ? mapper.readValue(buffer.getBytes(), mappedType) :
              mapper.readValue(buffer.getBytes(), mappedTypeRef);
          }
          subscriber.onNext(obj);
          subscriber.onComplete();
        } catch (IOException e) {
          onError(e);
        }
      }
    };
  }
}
