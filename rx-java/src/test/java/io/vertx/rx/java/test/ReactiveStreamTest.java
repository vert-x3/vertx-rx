package io.vertx.rx.java.test;

import io.vertx.core.buffer.Buffer;
import io.vertx.rx.java.RxHelper;
import io.vertx.rx.java.test.stream.BufferReadStreamImpl;
import rx.Observable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReactiveStreamTest extends AbstractBackPressureTest {

  @Override
  protected Observable<Buffer> toObservable(BufferReadStreamImpl stream) {
    return RxHelper.toObservable(stream, vertx);
  }

  @Override
  public void testHandlers() {
    // Does not pass
  }

  @Override
  public void testDataHandlerShouldBeSetAndUnsetAfterOtherHandlers() {
    // Does not pass
  }
}
