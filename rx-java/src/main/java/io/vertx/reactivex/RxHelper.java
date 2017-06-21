package io.vertx.reactivex;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.vertx.core.Context;
import io.vertx.core.streams.ReadStream;
import io.vertx.rx.java.ObservableReadStream;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxHelper {

  /**
   * Adapts a Vert.x {@link ReadStream<T>} to an RxJava {@link Observable<T>}. After
   * the stream is adapted to an observable, the original stream handlers should not be used anymore
   * as they will be used by the observable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T> Observable<T> toObservable(ReadStream<T> stream) {
    throw new UnsupportedOperationException();
  }

  /**
   * Adapts a Vert.x {@link ReadStream<T>} to an RxJava {@link Flowable<U>}. After
   * the stream is adapted to a flowable, the original stream handlers should not be used anymore
   * as they will be used by the flowable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T, U> Flowable<U> toFlowable(ReadStream<T> stream, Function<T, U> f) {
    return new FlowableReadStream<>(stream, 0, f);
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
    return new FlowableReadStream<>(stream, ObservableReadStream.DEFAULT_MAX_BUFFER_SIZE, Function.identity());
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
    return new FlowableReadStream<>(stream, maxBufferSize, Function.identity());
  }
}
