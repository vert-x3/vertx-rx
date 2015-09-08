package io.vertx.rx.groovy;

import io.vertx.core.Handler;
import io.vertx.rx.java.SingleOnSubscribeAdapter;
import io.vertx.groovy.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class HandlerAdapter<T> extends SingleOnSubscribeAdapter<T> implements Handler<T> {

  private final ReadStream<T> stream;

  HandlerAdapter(ReadStream<T> stream) {
    this.stream = stream;
  }

  /** Handle response */
  public void handle(T msg) {
    // Assume stream
    fireNext(msg);
  }

  @Override
  public void onSubscribed() {
    stream.exceptionHandler(this::fireError);
    stream.endHandler(v -> fireComplete());
    stream.handler(this);
  }

  @Override
  public void onUnsubscribed() {
    io.vertx.core.streams.ReadStream<?> delegate = (io.vertx.core.streams.ReadStream<?>) stream.getDelegate();
    try {
      delegate.handler(null);
      delegate.exceptionHandler(null);
      delegate.endHandler(null);
    }
    catch(Exception e) {
      // Clearing handlers after stream closed causes issues for some (eg AsyncFile) so silently drop errors
    }
  }
}
