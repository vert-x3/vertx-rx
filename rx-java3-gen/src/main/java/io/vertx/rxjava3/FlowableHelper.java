package io.vertx.rxjava3;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;
import io.vertx.rxjava3.impl.AsyncResultSingle;
import io.vertx.rxjava3.impl.FlowableReadStream;
import io.vertx.rxjava3.impl.ReadStreamSubscriber;
import io.vertx.rxjava3.impl.FlowableUnmarshaller;
import org.reactivestreams.Subscription;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FlowableHelper {

  private static final FlowableSubscriber NULL_OBSERVER = new FlowableSubscriber() {
    @Override
    public void onSubscribe(@NonNull Subscription s) {
    }
    @Override
    public void onNext(Object o) {
    }
    @Override
    public void onComplete() {
    }
    @Override
    public void onError(@NonNull Throwable e) {
    }
  };

  /**
   * @return a {@code FlowableSubscriber} that does nothing
   */
  @SuppressWarnings("unchecked")
  public static <T>  FlowableSubscriber<T> nullObserver() {
    return NULL_OBSERVER;
  }

  /**
   * Adapts an RxJava {@link Flowable<T>} to a Vert.x {@link io.vertx.core.streams.ReadStream<T>}. The returned
   * readstream will be subscribed to the {@link Flowable<T>}.<p>
   *
   * @param observable the observable to adapt
   * @return the adapted stream
   */
  public static <T> ReadStream<T> toReadStream(Flowable<T> observable) {
    return ReadStreamSubscriber.asReadStream(observable, Function.identity());
  }

  /**
   * Like {@link #toFlowable(ReadStream)} but with a {@code mapping} function
   */
  public static <T, U> Flowable<U> toFlowable(ReadStream<T> stream, Function<T, U> mapping) {
    return RxJavaPlugins.onAssembly(new FlowableReadStream<>(stream, FlowableReadStream.DEFAULT_MAX_BUFFER_SIZE, mapping));
  }

  /**
   * Adapts a Vert.x {@link ReadStream<T>} to an RxJava {@link Flowable<T>}. After
   * the stream is adapted to a flowable, the original stream handlers should not be used anymore
   * as they will be used by the flowable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T> Flowable<T> toFlowable(ReadStream<T> stream) {
    return RxJavaPlugins.onAssembly(new FlowableReadStream<>(stream, FlowableReadStream.DEFAULT_MAX_BUFFER_SIZE, Function.identity()));
  }

  /**
   * Adapts a Vert.x {@link ReadStream<T>} to an RxJava {@link Flowable<T>}. After
   * the stream is adapted to a flowable, the original stream handlers should not be used anymore
   * as they will be used by the flowable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T> Flowable<T> toFlowable(ReadStream<T> stream, long maxBufferSize) {
    return RxJavaPlugins.onAssembly(new FlowableReadStream<>(stream, maxBufferSize, Function.identity()));
  }

  /**
   * Adapts a Vert.x {@link ReadStream<T>} to an RxJava {@link Flowable<T>}. After
   * the stream is adapted to a flowable, the original stream handlers should not be used anymore
   * as they will be used by the flowable adapter.<p>
   *
   * @param supplier the supplier of future of stream
   * @return the adapted observable
   */
  public static <T> Flowable<T> toFlowable(Supplier<Future<ReadStream<T>>> supplier) {
    Single<Flowable<T>> single = AsyncResultSingle.toSingle(supplier, FlowableHelper::toFlowable);
    return single.flatMapPublisher(f -> f);
  }

  public static <T> FlowableTransformer<Buffer, T> unmarshaller(Class<T> mappedType) {
    return new FlowableUnmarshaller<>(java.util.function.Function.identity(), mappedType);
  }

  public static <T> FlowableTransformer<Buffer, T> unmarshaller(TypeReference<T> mappedTypeRef) {
    return new FlowableUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef);
  }

  public static <T> FlowableTransformer<Buffer, T> unmarshaller(Class<T> mappedType, ObjectCodec mapper) {
    return new FlowableUnmarshaller<>(java.util.function.Function.identity(), mappedType, mapper);
  }

  public static <T> FlowableTransformer<Buffer, T>unmarshaller(TypeReference<T> mappedTypeRef, ObjectCodec mapper) {
    return new FlowableUnmarshaller<>(java.util.function.Function.identity(), mappedTypeRef, mapper);
  }
}
