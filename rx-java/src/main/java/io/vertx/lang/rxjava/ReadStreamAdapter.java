package io.vertx.lang.rxjava;

import io.vertx.core.Handler;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.rx.java.SingleOnSubscribeAdapter;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamAdapter<J, R> extends SingleOnSubscribeAdapter<R> implements Handler<J> {

  private final io.vertx.core.streams.ReadStream<J> stream;
  private final Function<J, R> adapter;

  public ReadStreamAdapter(ReadStream<R> stream, Function<J, R> adapter) {
    this.adapter = adapter;
    this.stream = (io.vertx.core.streams.ReadStream<J>) stream.getDelegate();
  }

  @Override
  public void handle(J event) {
    fireNext(adapter.apply(event));
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
