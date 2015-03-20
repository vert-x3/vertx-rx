package io.vertx.rx.groovy;

import io.vertx.groovy.core.Vertx;
import io.vertx.groovy.core.buffer.Buffer;
import io.vertx.rx.java.UnmarshallerOperator;
import rx.Observable;
import rx.plugins.RxJavaSchedulersHook;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxHelper {

  /**
   * Create a scheduler hook for a {@link io.vertx.groovy.core.Vertx} object

   * @param vertx the vertx object
   * @return the scheduler hook
   */
  public static RxJavaSchedulersHook schedulerHook(Vertx vertx) {
    return io.vertx.rx.java.RxHelper.schedulerHook((io.vertx.core.Vertx) vertx.getDelegate());
  }

  /**
   * Returns a json unmarshaller for the specified java type as a {@link rx.Observable.Operator} instance.<p/>
   *
   * The marshaller can be used with the {@link rx.Observable#lift(rx.Observable.Operator)} method to transform
   * a {@literal Observable<Buffer>} into a {@literal Observable<T>}.<p/>
   *
   * The unmarshaller buffers the content until <i>onComplete</i> is called, then unmarshalling happens.<p/>
   *
   * Note that the returned observable will emit at most a single object.
   *
   * @param mappedType the type to unmarshall
   * @return the unmarshaller operator
   */
  public static <T> Observable.Operator<T, Buffer> unmarshaller(Class<T> mappedType) {
    return new UnmarshallerOperator<T, Buffer>(mappedType) {
      @Override
      public io.vertx.core.buffer.Buffer unwrap(Buffer buffer) {
        return ((io.vertx.core.buffer.Buffer) buffer.getDelegate());
      }
    };
  }
}
