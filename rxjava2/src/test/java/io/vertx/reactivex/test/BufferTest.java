package io.vertx.reactivex.test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.buffer.Buffer;
import io.vertx.reactivex.FlowableHelper;
import io.vertx.reactivex.MaybeHelper;
import io.vertx.reactivex.ObservableHelper;
import io.vertx.reactivex.SingleHelper;
import io.vertx.rx.java.test.support.SimplePojo;
import io.vertx.rx.java.test.support.SimpleSubscriber;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BufferTest {

  @Test
  public void testFlowableMapPojoFromBuffer() throws Exception {
    Flowable<Buffer> stream = Flowable.just(Buffer.buffer("{\"foo\""), Buffer.buffer(":\"bar\"}"));
    Flowable<SimplePojo> mapped = stream.lift(FlowableHelper.unmarshaller(SimplePojo.class));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<SimplePojo>().prefetch(0);
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
    Observable<Buffer> stream = Observable.just(Buffer.buffer("{\"foo\""), Buffer.buffer(":\"bar\"}"));
    Observable<SimplePojo> mapped = stream.lift(ObservableHelper.unmarshaller(SimplePojo.class));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<SimplePojo>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new SimplePojo("bar"))
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testSingleMapPojoFromBuffer() throws Exception {
    Single<Buffer> stream = Single.just(Buffer.buffer("{\"foo\":\"bar\"}"));
    Single<SimplePojo> mapped = stream.lift(SingleHelper.unmarshaller(SimplePojo.class));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new SimplePojo("bar"))
      .assertEmpty();
  }

  @Test
  public void testMaybeMapPojoFromBuffer() throws Exception {
    Maybe<Buffer> stream = Maybe.just(Buffer.buffer("{\"foo\":\"bar\"}"));
    Maybe<SimplePojo> mapped = stream.lift(MaybeHelper.unmarshaller(SimplePojo.class));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new SimplePojo("bar"))
      .assertEmpty();
  }

  @Test
  public void testFlowableMapPojoFromBufferFailure() throws Exception {
    Flowable<Buffer> stream = Flowable.just(Buffer.buffer("{\"foo\""));
    Flowable<SimplePojo> mapped = stream.lift(FlowableHelper.unmarshaller(SimplePojo.class));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<SimplePojo>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertEmpty()
      .request(1)
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testObservableMapPojoFromBufferFailure() throws Exception {
    Observable<Buffer> stream = Observable.just(Buffer.buffer("{\"foo\""));
    Observable<SimplePojo> mapped = stream.lift(ObservableHelper.unmarshaller(SimplePojo.class));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<SimplePojo>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testSingleMapPojoFromBufferFailure() throws Exception {
    Single<Buffer> stream = Single.just(Buffer.buffer("{\"foo\""));
    Single<SimplePojo> mapped = stream.lift(SingleHelper.unmarshaller(SimplePojo.class));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testMaybeMapPojoFromBufferFailure() throws Exception {
    Maybe<Buffer> stream = Maybe.just(Buffer.buffer("{\"foo\""));
    Maybe<SimplePojo> mapped = stream.lift(MaybeHelper.unmarshaller(SimplePojo.class));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testFlowableMapObjectNodeFromBuffer() throws Exception {
    Flowable<Buffer> stream = Flowable.just(Buffer.buffer("{\"foo\""), Buffer.buffer(":\"bar\"}"));
    Flowable<JsonNode> mapped = stream.lift(FlowableHelper.unmarshaller(JsonNode.class));
    SimpleSubscriber<JsonNode> subscriber = new SimpleSubscriber<JsonNode>().prefetch(0);
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
    Observable<Buffer> stream = Observable.just(Buffer.buffer("{\"foo\""), Buffer.buffer(":\"bar\"}"));
    Observable<JsonNode> mapped = stream.lift(ObservableHelper.unmarshaller(JsonNode.class));
    SimpleSubscriber<JsonNode> subscriber = new SimpleSubscriber<JsonNode>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new ObjectMapper().createObjectNode().put("foo", "bar"))
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testSingleMapObjectNodeFromBuffer() throws Exception {
    Single<Buffer> stream = Single.just(Buffer.buffer("{\"foo\":\"bar\"}"));
    Single<JsonNode> mapped = stream.lift(SingleHelper.unmarshaller(JsonNode.class));
    SimpleSubscriber<JsonNode> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new ObjectMapper().createObjectNode().put("foo", "bar"))
      .assertEmpty();
  }

  @Test
  public void testMaybeMapObjectNodeFromBuffer() throws Exception {
    Maybe<Buffer> stream = Maybe.just(Buffer.buffer("{\"foo\":\"bar\"}"));
    Maybe<JsonNode> mapped = stream.lift(MaybeHelper.unmarshaller(JsonNode.class));
    SimpleSubscriber<JsonNode> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new ObjectMapper().createObjectNode().put("foo", "bar"))
      .assertEmpty();
  }

  @Test
  public void testFlowableMapPojoListFromBuffer() throws Exception {
    Flowable<Buffer> stream = Flowable.just(Buffer.buffer("[{\"foo\":\"bar\"}]"));
    Flowable<List<SimplePojo>> mapped = stream.lift(FlowableHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}));
    SimpleSubscriber<List<SimplePojo>> subscriber = new SimpleSubscriber<List<SimplePojo>>().prefetch(0);
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
    Observable<Buffer> stream = Observable.just(Buffer.buffer("[{\"foo\":\"bar\"}]"));
    Observable<List<SimplePojo>> mapped = stream.lift(ObservableHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}));
    SimpleSubscriber<List<SimplePojo>> subscriber = new SimpleSubscriber<List<SimplePojo>>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(Arrays.asList(new SimplePojo("bar")))
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testSingleMapPojoListFromBuffer() throws Exception {
    Single<Buffer> stream = Single.just(Buffer.buffer("[{\"foo\":\"bar\"}]"));
    Single<List<SimplePojo>> mapped = stream.lift(SingleHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}));
    SimpleSubscriber<List<SimplePojo>> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(Arrays.asList(new SimplePojo("bar")))
      .assertEmpty();
  }

  @Test
  public void testMaybeMapPojoListFromBuffer() throws Exception {
    Maybe<Buffer> stream = Maybe.just(Buffer.buffer("[{\"foo\":\"bar\"}]"));
    Maybe<List<SimplePojo>> mapped = stream.lift(MaybeHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}));
    SimpleSubscriber<List<SimplePojo>> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(Arrays.asList(new SimplePojo("bar")))
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
