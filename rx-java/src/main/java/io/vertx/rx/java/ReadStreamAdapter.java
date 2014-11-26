package io.vertx.rx.java;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class ReadStreamAdapter<T> extends SingleOnSubscribeAdapter<T> implements Handler<T> {

  private final ReadStream<T> stream;

  ReadStreamAdapter(ReadStream<T> stream) {
    this.stream = stream;
  }

  /** Handle response */
  public void handle(T msg) {
    // Assume stream
    fireNext(msg);
  }

  @Override
  public void execute() {
    stream.exceptionHandler(this::fireError);
    stream.endHandler(v -> fireComplete());
    stream.handler(this);
  }

  @Override
  public void onUnsubscribed() {
    try {
      stream.exceptionHandler(null);
      stream.endHandler(null);
      stream.handler(null);
    }
    catch(Exception e) {
      // Clearing handlers after stream closed causes issues for some (eg AsyncFile) so silently drop errors
    }
  }
}
