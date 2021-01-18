package io.vertx.reactivex;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;
import io.vertx.reactivex.impl.ObservableReadStream;
import io.vertx.reactivex.impl.ReadStreamSubscriber;
import io.vertx.reactivex.impl.ObservableUnmarshaller;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableHelper {

  /**
   * Adapts an RxJava {@link Observable<T>} to a Vert.x {@link io.vertx.core.streams.ReadStream<T>}. The returned
   * readstream will be subscribed to the {@link Observable<T>}.<p>
   *
   * @param observable the observable to adapt
   * @return the adapted stream
   */
  public static <T> ReadStream<T> toReadStream(Observable<T> observable) {
    return ReadStreamSubscriber.asReadStream(observable, Function.identity());
  }

  /**
   * Adapts a Vert.x {@link ReadStream <T>} to an RxJava {@link Observable <T>}. After
   * the stream is adapted to an observable, the original stream handlers should not be used anymore
   * as they will be used by the observable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T> Observable<T> toObservable(ReadStream<T> stream) {
    return RxJavaPlugins.onAssembly(new ObservableReadStream<T, T>(stream, Function.identity()));
  }

  /**
   * Like {@link #toObservable(ReadStream)} but with a {@code mapping} function
   */
  public static <T, U> Observable<U> toObservable(ReadStream<T> stream, Function<T, U> mapping) {
    return RxJavaPlugins.onAssembly(new ObservableReadStream<>(stream, mapping));
  }

  public static <T> ObservableTransformer<Buffer, T> unmarshaller(Class<T> mappedType) {
    return new ObservableUnmarshaller<>(java.util.function.Function.identity(), mappedType);
  }

  public static <T> ObservableTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new ObservableUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef);
  }

  public static <T> ObservableTransformer<Buffer, T> unmarshaller(Class<T> mappedType, ObjectCodec mapper) {
    return new ObservableUnmarshaller<>(java.util.function.Function.identity(), mappedType, mapper);
  }

  public static <T> ObservableTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    return new ObservableUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef, mapper);
  }
}
