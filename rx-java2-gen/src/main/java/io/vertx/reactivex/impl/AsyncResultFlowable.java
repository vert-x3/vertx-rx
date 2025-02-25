package io.vertx.reactivex.impl;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.reactivex.FlowableHelper;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncResultFlowable {
  /**
   * Adapts a Vert.x {@link ReadStream<T>} to an RxJava {@link Flowable<T>}. After
   * the stream is adapted to a flowable, the original stream handlers should not be used anymore
   * as they will be used by the flowable adapter.<p>
   *
   * @param callback the supplier of future of stream
   * @return the adapted observable
   */
  public static <T> Flowable<T> toFlowable(Handler<Handler<AsyncResult<ReadStream<T>>>> callback) {
    Single<ReadStream<T>> single = AsyncResultSingle.toSingle(callback::handle);
    return single.flatMapPublisher(FlowableHelper::toFlowable);
  }
}
