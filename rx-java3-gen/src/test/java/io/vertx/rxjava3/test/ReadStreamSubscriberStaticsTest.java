package io.vertx.rxjava3.test;

import io.reactivex.rxjava3.core.Flowable;
import io.vertx.core.streams.ReadStream;
import io.vertx.lang.rx.test.ReadStreamSubscriberStaticsTestBase;
import io.vertx.rxjava3.impl.ReadStreamSubscriber;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReadStreamSubscriberStaticsTest extends ReadStreamSubscriberStaticsTestBase<Integer, Flowable<Integer>> {

  @Override
  public Flowable<Integer> emptyFlowable() {
    return Flowable.empty();
  }

  @Override
  public List<Integer> generateData(int count) {
    return IntStream.range(0, count).boxed().collect(Collectors.toList());
  }

  @Override
  public Flowable<Integer> flowable(Iterable<Integer> source) {
    return Flowable.fromIterable(source);
  }

  @Override
  public ReadStream<Integer> asReadStream(Flowable<Integer> flowable) {
    return ReadStreamSubscriber.asReadStream(flowable, Function.identity());
  }

  @Override
  public Flowable<Integer> emptyExceptionFlowable(String errorMessage) {
    return Flowable.error(new RuntimeException(errorMessage));
  }

  @Override
  public Flowable<Integer> exceptionAfterDataFlowable(String errorMessage, Iterable<Integer> source) {
    return flowable(source).concatWith(emptyExceptionFlowable(errorMessage));
  }
}
