package io.vertx.reactivex;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.vertx.core.Context;
import io.vertx.core.streams.ReadStream;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxHelper {

  /**
   * Adapts a Vert.x {@link io.vertx.core.streams.ReadStream<T>} to an RxJava {@link Observable<T>}. After
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
   * Adapts a Vert.x {@link io.vertx.core.streams.ReadStream<T>} to an RxJava {@link Flowable<T>}. After
   * the stream is adapted to a flowable, the original stream handlers should not be used anymore
   * as they will be used by the flowable adapter.<p>
   *
   * @param stream the stream to adapt
   * @return the adapted observable
   */
  public static <T, U> Flowable<U> toFlowable(ReadStream<T> stream, Function<T, U> f, Context context) {
    return new FlowableReadStream<>(stream, f);
  }
}
