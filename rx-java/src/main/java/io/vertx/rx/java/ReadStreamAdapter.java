package io.vertx.rx.java;

import io.vertx.core.streams.ReadStream;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamAdapter<T> extends io.vertx.lang.rxjava.ReadStreamAdapter<T, T> {

  public ReadStreamAdapter(ReadStream<T> stream) {
    super(stream, Function.<T>identity());
  }
}