package io.vertx.lang.rxjava;

import io.vertx.core.Handler;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.rx.java.SingleOnSubscribeAdapter;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HandlerAdapter<J, R> extends SingleOnSubscribeAdapter<R> implements Handler<J> {

  private final ReadStream<R> stream;
  private final io.vertx.core.streams.ReadStream<J> stream2;
  private final Function<J, R> adapter;

  public HandlerAdapter(ReadStream<R> stream, Function<J, R> adapter) {
    this.stream = stream;
    this.adapter = adapter;
    stream2 = (io.vertx.core.streams.ReadStream<J>) stream.getDelegate();
  }

  @Override
  public void handle(J event) {
    fireNext(adapter.apply(event));
  }

  @Override
  public void execute() {
    stream2.handler(this);
    stream2.exceptionHandler(this::fireError);
    stream2.endHandler(v -> fireComplete());
  }

  @Override
  public void onUnsubscribed() {
    try {
      stream2.handler(null);
      stream2.exceptionHandler(null);
      stream2.endHandler(null);
    }
    catch(Exception e) {
      // Clearing handlers after stream closed causes issues for some (eg AsyncFile) so silently drop errors
    }
  }
}
