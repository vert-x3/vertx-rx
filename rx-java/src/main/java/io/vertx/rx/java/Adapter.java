package io.vertx.rx.java;

import io.vertx.core.Handler;
import rx.Producer;

/**
 * Adapt
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
interface Adapter<T> extends Handler<T>, Producer {


  /**
   * A sentinel signaling a completion.
   */
  Throwable COMPLETED_SENTINEL = new Throwable();

  long requested();

  /**
   * Dispose.
   */
  default void dispose() {}

  /**
   * Signals an emission.
   *
   * @param item the emitted item
   */
  @Override
  void handle(T item);

  /**
   * Signals end:
   * <ul>
   *   <li>with {@link #COMPLETED_SENTINEL} for a completion</li>
   *   <li>with any {@link Throwable} for an error</li>
   * </ul>
   */
  void end(Throwable t);

}
