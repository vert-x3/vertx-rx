package io.vertx.rx.java.test;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.lang.rx.test.ReadStreamAdapterTestBase;
import io.vertx.lang.rx.test.TestSubscriber;
import io.vertx.rx.java.test.stream.BufferReadStream;
import io.vertx.rxjava.core.buffer.Buffer;
import rx.Observable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadStreamAdapterConvertingTest extends ReadStreamAdapterTestBase<Buffer, Observable<Buffer>> {

  @Override
  protected Observable<Buffer> toObservable(ReadStream<io.vertx.core.buffer.Buffer> stream) {
    return new io.vertx.rxjava.rx.java.test.stream.BufferReadStream(new BufferReadStream() {
      @Override
      public BufferReadStream exceptionHandler(Handler<Throwable> handler) {
        stream.exceptionHandler(handler);
        return this;
      }
      @Override
      public BufferReadStream handler(Handler<io.vertx.core.buffer.Buffer> handler) {
        stream.handler(handler);
        return this;
      }
      @Override
      public BufferReadStream fetch(long amount) {
        stream.fetch(amount);
        return this;
      }
      @Override
      public BufferReadStream pause() {
        stream.pause();
        return this;
      }
      @Override
      public BufferReadStream resume() {
        stream.resume();
        return this;
      }
      @Override
      public BufferReadStream endHandler(Handler<Void> endHandler) {
        stream.endHandler(endHandler);
        return this;
      }
    }).toObservable();
  }

  @Override
  protected void subscribe(Observable<Buffer> obs, TestSubscriber<Buffer> sub) {
    TestUtils.subscribe(obs, sub);
  }

  @Override
  protected Observable<Buffer> concat(Observable<Buffer> obs1, Observable<Buffer> obs2) {
    return Observable.concat(obs1, obs2);
  }

  @Override
  protected Buffer buffer(String s) {
    return Buffer.buffer(s);
  }

  @Override
  protected String string(Buffer buffer) {
    return buffer.toString("UTF-8");
  }
}
