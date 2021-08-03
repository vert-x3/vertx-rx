package io.vertx.rx.java.test;

import io.vertx.core.streams.ReadStream;
import io.vertx.lang.rx.test.ReadStreamSubscriberStaticsTestBase;
import io.vertx.rx.java.ReadStreamSubscriber;
import rx.Observable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReadStreamSubscriberStaticsTest extends ReadStreamSubscriberStaticsTestBase<Integer, Observable<Integer>> {

  @Override
  public Observable<Integer> emptyFlowable() {
    return Observable.empty();
  }

  @Override
  public List<Integer> generateData(int count) {
    return IntStream.range(0, count).boxed().collect(Collectors.toList());
  }

  @Override
  public Observable<Integer> flowable(Iterable<Integer> source) {
    return Observable.from(source);
  }

  @Override
  public ReadStream<Integer> asReadStream(Observable<Integer> flowable) {
    return ReadStreamSubscriber.asReadStream(flowable, Function.identity());
  }

  @Override
  public Observable<Integer> emptyExceptionFlowable(String errorMessage) {
    return Observable.error(new RuntimeException(errorMessage));
  }

  @Override
  public Observable<Integer> exceptionAfterDataFlowable(String errorMessage, Iterable<Integer> source) {
    return flowable(source).concatWith(emptyExceptionFlowable(errorMessage));
  }
}
