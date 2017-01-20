package io.vertx.rx.java;

import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
interface BackPressure<T> extends Handler<T> {

  /**
   * A sentinel signaling a completion.
   */
  Throwable COMPLETED_SENTINEL = new Throwable();

  /**
   * Drain.
   */
  default void drain() {}

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
