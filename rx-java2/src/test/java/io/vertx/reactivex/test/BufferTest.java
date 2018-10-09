package io.vertx.reactivex.test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.reactivex.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.FlowableHelper;
import io.vertx.reactivex.MaybeHelper;
import io.vertx.reactivex.ObservableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.lang.rx.test.SimplePojo;
import io.vertx.lang.rx.test.TestSubscriber;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BufferTest {

  ObjectMapper mapper;

  @Before
  public void setUp() throws Exception {
    mapper = new ObjectMapper(new YAMLFactory());
  }

  @Test
  public void testFlowableMapPojoFromBuffer() throws Exception {
    testFlowableMapPojoFromBuffer(Flowable.just(Buffer.buffer("{\"foo\""), Buffer.buffer(":\"bar\"}")), FlowableHelper.unmarshaller(SimplePojo.class));
  }

  @Test
  public void testFlowableMapPojoFromBufferCustom() throws Exception {
    testFlowableMapPojoFromBuffer(Flowable.just(Buffer.buffer("foo"), Buffer.buffer(": bar")), FlowableHelper.unmarshaller(SimplePojo.class, mapper));
  }

  private void testFlowableMapPojoFromBuffer(Flowable<Buffer> stream, FlowableTransformer<Buffer, SimplePojo> composer) throws Exception {
    Flowable<SimplePojo> mapped = stream.compose(composer);
    TestSubscriber<SimplePojo> subscriber = new TestSubscriber<SimplePojo>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertEmpty()
      .request(1)
      .assertItem(new SimplePojo("bar"))
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testObservableMapPojoFromBuffer() throws Exception {
    testObservableMapPojoFromBuffer(Observable.just(Buffer.buffer("{\"foo\""), Buffer.buffer(":\"bar\"}")), ObservableHelper.unmarshaller(SimplePojo.class));
  }

  @Test
  public void testObservableMapPojoFromBufferCustom() throws Exception {
    testObservableMapPojoFromBuffer(Observable.just(Buffer.buffer("foo"), Buffer.buffer(": bar")), ObservableHelper.unmarshaller(SimplePojo.class, mapper));
  }

  private void testObservableMapPojoFromBuffer(Observable<Buffer> stream, ObservableTransformer<Buffer, SimplePojo> composer) throws Exception {
    Observable<SimplePojo> mapped = stream.compose(composer);
    TestSubscriber<SimplePojo> subscriber = new TestSubscriber<SimplePojo>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new SimplePojo("bar"))
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testSingleMapPojoFromBuffer() throws Exception {
    testSingleMapFromBuffer("{\"foo\":\"bar\"}", SingleHelper.unmarshaller(SimplePojo.class), new SimplePojo("bar"));
  }

  @Test
  public void testSingleMapPojoFromBufferCustom() throws Exception {
    testSingleMapFromBuffer("foo: bar", SingleHelper.unmarshaller(SimplePojo.class, mapper), new SimplePojo("bar"));
  }

  @Test
  public void testMaybeMapPojoFromBuffer() throws Exception {
    testMaybeMapFromBuffer("{\"foo\":\"bar\"}", MaybeHelper.unmarshaller(SimplePojo.class), new SimplePojo("bar"));
  }

  @Test
  public void testMaybeMapPojoFromBufferCustom() throws Exception {
    testMaybeMapFromBuffer("foo: bar", MaybeHelper.unmarshaller(SimplePojo.class, mapper), new SimplePojo("bar"));
  }

  @Test
  public void testFlowableMapPojoFromBufferFailure() throws Exception {
    testFlowableMapPojoFromBufferFailure(Flowable.just(Buffer.buffer("{\"foo\"")), FlowableHelper.unmarshaller(SimplePojo.class));
  }

  @Test
  public void testFlowableMapPojoFromBufferFailureCustom() throws Exception {
    testFlowableMapPojoFromBufferFailure(Flowable.just(Buffer.buffer("{\"foo\"")), FlowableHelper.unmarshaller(SimplePojo.class, mapper));
  }

  private void testFlowableMapPojoFromBufferFailure(Flowable<Buffer> stream, FlowableTransformer<Buffer, SimplePojo> composer) throws Exception {
    Flowable<SimplePojo> mapped = stream.compose(composer);
    TestSubscriber<SimplePojo> subscriber = new TestSubscriber<SimplePojo>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .request(1)
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testObservableMapPojoFromBufferFailure() throws Exception {
    testObservableMapPojoFromBufferFailure(Observable.just(Buffer.buffer("{\"foo\"")), ObservableHelper.unmarshaller(SimplePojo.class));
  }

  @Test
  public void testObservableMapPojoFromBufferFailureCustom() throws Exception {
    testObservableMapPojoFromBufferFailure(Observable.just(Buffer.buffer("{\"foo\"")), ObservableHelper.unmarshaller(SimplePojo.class, mapper));
  }

  private void testObservableMapPojoFromBufferFailure(Observable<Buffer> stream, ObservableTransformer<Buffer, SimplePojo> composer) throws Exception {
    Observable<SimplePojo> mapped = stream.compose(composer);
    TestSubscriber<SimplePojo> subscriber = new TestSubscriber<SimplePojo>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testSingleMapPojoFromBufferFailure() throws Exception {
    testSingleMapPojoFromBufferFailure(Single.just(Buffer.buffer("{\"foo\"")), SingleHelper.unmarshaller(SimplePojo.class));
  }

  @Test
  public void testSingleMapPojoFromBufferFailureCustom() throws Exception {
    testSingleMapPojoFromBufferFailure(Single.just(Buffer.buffer("{\"foo\"")), SingleHelper.unmarshaller(SimplePojo.class, mapper));
  }

  private void testSingleMapPojoFromBufferFailure(Single<Buffer> stream, SingleTransformer<Buffer, SimplePojo> composer) throws Exception {
    Single<SimplePojo> mapped = stream.compose(composer);
    TestSubscriber<SimplePojo> subscriber = new TestSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testMaybeMapPojoFromBufferFailure() throws Exception {
    testMaybeMapPojoFromBufferFailure(Maybe.just(Buffer.buffer("{\"foo\"")), MaybeHelper.unmarshaller(SimplePojo.class));
  }

  @Test
  public void testMaybeMapPojoFromBufferFailureCustom() throws Exception {
    testMaybeMapPojoFromBufferFailure(Maybe.just(Buffer.buffer("{\"foo\"")), MaybeHelper.unmarshaller(SimplePojo.class, mapper));
  }

  private void testMaybeMapPojoFromBufferFailure(Maybe<Buffer> stream, MaybeTransformer<Buffer, SimplePojo> transformer) throws Exception {
    Maybe<SimplePojo> mapped = stream.compose(transformer);
    TestSubscriber<SimplePojo> subscriber = new TestSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testFlowableMapObjectNodeFromBuffer() throws Exception {
    testFlowableMapObjectNodeFromBuffer(Flowable.just(Buffer.buffer("{\"foo\""), Buffer.buffer(":\"bar\"}")), FlowableHelper.unmarshaller(JsonNode.class));
  }

  @Test
  public void testFlowableMapObjectNodeFromBufferCustom() throws Exception {
    testFlowableMapObjectNodeFromBuffer(Flowable.just(Buffer.buffer("foo"), Buffer.buffer(": bar ")), FlowableHelper.unmarshaller(JsonNode.class, mapper));
  }

  private void testFlowableMapObjectNodeFromBuffer(Flowable<Buffer> stream, FlowableTransformer<Buffer, JsonNode> composer) throws Exception {
    Flowable<JsonNode> mapped = stream.compose(composer);
    TestSubscriber<JsonNode> subscriber = new TestSubscriber<JsonNode>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertEmpty()
      .request(1)
      .assertItem(new ObjectMapper().createObjectNode().put("foo", "bar"))
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testObservableMapObjectNodeFromBuffer() throws Exception {
    testObservableMapObjectNodeFromBuffer(Observable.just(Buffer.buffer("{\"foo\""), Buffer.buffer(":\"bar\"}")), ObservableHelper.unmarshaller(JsonNode.class));
  }

  @Test
  public void testObservableMapObjectNodeFromBufferCustom() throws Exception {
    testObservableMapObjectNodeFromBuffer(Observable.just(Buffer.buffer("foo"), Buffer.buffer(": bar")), ObservableHelper.unmarshaller(JsonNode.class, mapper));
  }

  private void testObservableMapObjectNodeFromBuffer(Observable<Buffer> stream, ObservableTransformer<Buffer, JsonNode> composer) throws Exception {
    Observable<JsonNode> mapped = stream.compose(composer);
    TestSubscriber<JsonNode> subscriber = new TestSubscriber<JsonNode>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new ObjectMapper().createObjectNode().put("foo", "bar"))
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testSingleMapObjectNodeFromBuffer() throws Exception {
    testSingleMapFromBuffer("{\"foo\":\"bar\"}", SingleHelper.unmarshaller(JsonNode.class), new ObjectMapper().createObjectNode().put("foo", "bar"));
  }

  @Test
  public void testSingleMapObjectNodeFromBufferCustom() throws Exception {
    testSingleMapFromBuffer("foo: bar", SingleHelper.unmarshaller(JsonNode.class, mapper), new ObjectMapper().createObjectNode().put("foo", "bar"));
  }

  private <T> void testSingleMapFromBuffer(String json, SingleTransformer<Buffer, ? extends T> transformer, T expected) throws Exception {
    Single<Buffer> stream = Single.just(Buffer.buffer(json));
    Single<T> mapped = stream.compose(transformer);
    List<T> items = new ArrayList<>();
    List<Throwable> errors = new ArrayList<>();
    AtomicInteger completions = new AtomicInteger();
    mapped.subscribe(items::add, errors::add);
    Assert.assertEquals(Collections.singletonList(expected), items);
    Assert.assertEquals(Collections.emptyList(), errors);
  }

  @Test
  public void testMaybeMapObjectNodeFromBuffer() throws Exception {
    testMaybeMapFromBuffer("{\"foo\":\"bar\"}", MaybeHelper.unmarshaller(JsonNode.class), new ObjectMapper().createObjectNode().put("foo", "bar"));
  }

  @Test
  public void testMaybeMapObjectNodeFromBufferCustom() throws Exception {
    testMaybeMapFromBuffer("foo: bar", MaybeHelper.unmarshaller(JsonNode.class, mapper), new ObjectMapper().createObjectNode().put("foo", "bar"));
  }

  static <T> void testMaybeMapFromBuffer(String json, MaybeTransformer<Buffer, ? extends T> transformer, T expected) throws Exception {
    Maybe<Buffer> stream = Maybe.just(Buffer.buffer(json));
    Maybe<T> mapped = stream.compose(transformer);
    List<T> items = new ArrayList<>();
    List<Throwable> errors = new ArrayList<>();
    AtomicInteger completions = new AtomicInteger();
    mapped.subscribe(items::add, errors::add, completions::incrementAndGet);
    Assert.assertEquals(Collections.singletonList(expected), items);
    Assert.assertEquals(Collections.emptyList(), errors);
    Assert.assertEquals(0, completions.get());
  }

  @Test
  public void testFlowableMapPojoListFromBuffer() throws Exception {
    testFlowableMapPojoListFromBuffer(Flowable.just(Buffer.buffer("[{\"foo\":\"bar\"}]")), FlowableHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}));
  }

  @Test
  public void testFlowableMapPojoListFromBufferCustom() throws Exception {
    testFlowableMapPojoListFromBuffer(Flowable.just(Buffer.buffer("- foo: bar")), FlowableHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}, mapper));
  }

  private void testFlowableMapPojoListFromBuffer(Flowable<Buffer> stream, FlowableTransformer<Buffer, List<SimplePojo>> composer) throws Exception {
    Flowable<List<SimplePojo>> mapped = stream.compose(composer);
    TestSubscriber<List<SimplePojo>> subscriber = new TestSubscriber<List<SimplePojo>>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertEmpty()
      .request(1)
      .assertItem(Arrays.asList(new SimplePojo("bar")))
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testObservableMapPojoListFromBuffer() throws Exception {
    testObservableMapPojoListFromBuffer(Observable.just(Buffer.buffer("[{\"foo\":\"bar\"}]")), ObservableHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}));
  }

  @Test
  public void testObservableMapPojoListFromBufferCustom() throws Exception {
    testObservableMapPojoListFromBuffer(Observable.just(Buffer.buffer("- foo: bar")), ObservableHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}, mapper));
  }

  private void testObservableMapPojoListFromBuffer(Observable<Buffer> stream, ObservableTransformer<Buffer, List<SimplePojo>> composer) throws Exception {
    Observable<List<SimplePojo>> mapped = stream.compose(composer);
    TestSubscriber<List<SimplePojo>> subscriber = new TestSubscriber<List<SimplePojo>>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(Arrays.asList(new SimplePojo("bar")))
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testSingleMapPojoListFromBuffer() throws Exception {
    testSingleMapFromBuffer("[{\"foo\":\"bar\"}]", SingleHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}), Collections.singletonList(new SimplePojo("bar")));
  }

  @Test
  public void testSingleMapPojoListFromBufferCustom() throws Exception {
    testSingleMapFromBuffer("- foo: bar", SingleHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}, mapper), Collections.singletonList(new SimplePojo("bar")));
  }

  @Test
  public void testMaybeMapPojoListFromBuffer() throws Exception {
    testMaybeMapFromBuffer("[{\"foo\":\"bar\"}]", MaybeHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}), Collections.singletonList(new SimplePojo("bar")));
  }

  @Test
  public void testMaybeMapPojoListFromBufferCustom() throws Exception {
    testMaybeMapFromBuffer("- foo: bar", MaybeHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}, mapper), Collections.singletonList(new SimplePojo("bar")));
  }

  @Test
  public void testFlowableMapFromEmptyBuffer() throws Exception {
    testFlowableMapFromEmptyBuffer(FlowableHelper.unmarshaller(SimplePojo.class, mapper));
  }

  @Test
  public void testFlowableMapFromEmptyBufferCustom() throws Exception {
    testFlowableMapFromEmptyBuffer(FlowableHelper.unmarshaller(SimplePojo.class));
  }

  private void testFlowableMapFromEmptyBuffer(FlowableTransformer<Buffer, SimplePojo> composer) throws Exception {
    Flowable<Buffer> stream = Flowable.empty();
    Flowable<SimplePojo> mapped = stream.compose(composer);
    TestSubscriber<SimplePojo> subscriber = new TestSubscriber<SimplePojo>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testObservableMapFromEmptyBuffer() throws Exception {
    testObservableMapFromEmptyBuffer(ObservableHelper.unmarshaller(SimplePojo.class, mapper));
  }

  @Test
  public void testObservableMapFromEmptyBufferCustom() throws Exception {
    testObservableMapFromEmptyBuffer(ObservableHelper.unmarshaller(SimplePojo.class));
  }

  private void testObservableMapFromEmptyBuffer(ObservableTransformer<Buffer, SimplePojo> composer) throws Exception {
    Observable<Buffer> stream = Observable.empty();
    Observable<SimplePojo> mapped = stream.compose(composer);
    TestSubscriber<SimplePojo> subscriber = new TestSubscriber<SimplePojo>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testMaybeMapFromEmptyBuffer() throws Exception {
    testMaybeMapFromEmptyBuffer(MaybeHelper.unmarshaller(SimplePojo.class));
  }

  @Test
  public void testMaybeMapFromEmptyBufferCustom() throws Exception {
    testMaybeMapFromEmptyBuffer(MaybeHelper.unmarshaller(SimplePojo.class, mapper));
  }

  private void testMaybeMapFromEmptyBuffer(MaybeTransformer<Buffer, SimplePojo> transformer) throws Exception {
    Maybe<Buffer> stream = Maybe.empty();
    Maybe<SimplePojo> mapped = stream.compose(transformer);
    TestSubscriber<SimplePojo> subscriber = new TestSubscriber<SimplePojo>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testClusterSerializable() throws Exception {
    io.vertx.reactivex.core.buffer.Buffer buff = io.vertx.reactivex.core.buffer.Buffer.buffer("hello-world");
    Buffer actual = Buffer.buffer();
    buff.writeToBuffer(actual);
    Buffer expected = Buffer.buffer();
    Buffer.buffer("hello-world").writeToBuffer(expected);
    assertEquals(expected, actual);
    buff = io.vertx.reactivex.core.buffer.Buffer.buffer("hello-world");
    assertEquals(expected.length(), buff.readFromBuffer(0, expected));
    assertEquals("hello-world", buff.toString());
  }
}
