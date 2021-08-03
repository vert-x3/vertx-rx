package io.vertx.lang.rx.test;

import io.vertx.core.streams.ReadStream;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class ReadStreamSubscriberStaticsTestBase<T, F> extends VertxTestBase {

  public abstract F emptyFlowable();

  public abstract F emptyExceptionFlowable(String errorMessage);

  public abstract F exceptionAfterDataFlowable(String errorMessage, Iterable<T> source);

  public abstract F flowable(Iterable<T> source);

  public abstract ReadStream<T> asReadStream(F flowable);

  public abstract List<T> generateData(int count);

  @Test
  public void testEmptyFlowable() {
    waitFor(1);
    F flowable = emptyFlowable();
    ReadStream<T> readStream = asReadStream(flowable);
    readStream.exceptionHandler(this::fail);
    readStream.endHandler(v -> complete());
    readStream.handler(i -> fail("empty stream"));
    await();
  }

  @Test
  public void testEmtpyListFlowable() {
    waitFor(1);
    F flowable = flowable(Collections.emptyList());
    ReadStream<T> readStream = asReadStream(flowable);
    readStream.exceptionHandler(this::fail);
    readStream.endHandler(v -> complete());
    readStream.handler(i -> fail("empty stream"));
    await();
  }

  @Test
  public void testWithElements() {
    List<T> data = generateData(5);
    waitFor(data.size() + 1);
    LinkedList<T> copy = new LinkedList<>(data);
    F flowable = flowable(data);
    ReadStream<T> readStream = asReadStream(flowable);
    readStream.exceptionHandler(this::fail);
    readStream.endHandler(v -> complete());
    readStream.handler(i -> {
      assertEquals(copy.poll(), i);
      complete();
    });
    await();
  }

  @Test
  public void testWithException() {
    waitFor(2);
    String errorMessage = "error msg";
    F flowable = emptyExceptionFlowable(errorMessage);
    ReadStream<T> readStream = asReadStream(flowable);
    readStream.exceptionHandler(e -> {
      assertEquals(errorMessage, e.getMessage());
      complete();
    });
    readStream.endHandler(v -> complete());
    readStream.handler(i -> fail("only error in stream"));
    await();
  }

  @Test
  public void testWithDataAndException() {
    List<T> data = generateData(5);
    waitFor(data.size() + 2);
    LinkedList<T> copy = new LinkedList<>(data);
    String errorMessage = "error msg";
    F flowable = exceptionAfterDataFlowable(errorMessage, data);
    ReadStream<T> readStream = asReadStream(flowable);
    readStream.exceptionHandler(e -> {
      assertEquals(errorMessage, e.getMessage());
      assertTrue("all data elements should be received ahead of the exception", copy.isEmpty());
      complete();
    });
    readStream.endHandler(v -> complete());
    readStream.handler(i -> {
      assertEquals(copy.poll(), i);
      complete();
    });
    await();
  }

}
