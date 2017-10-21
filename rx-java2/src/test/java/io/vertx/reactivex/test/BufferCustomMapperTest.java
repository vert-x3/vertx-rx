package io.vertx.reactivex.test;

import com.fasterxml.jackson.core.JsonFactory;
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
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BufferCustomMapperTest {



  ObjectMapper mapper;

  @Before
  public void setUp() throws Exception {
    mapper = new ObjectMapper(new JsonFactory());
  }

  @Test
  public void testFlowableMapPojoFromBuffer() throws Exception {
    Flowable<Buffer> stream = Flowable.just(Buffer.buffer("{\"foo\""), Buffer.buffer(":\"bar\"}"));
    Flowable<SimplePojo> mapped = stream.compose(FlowableHelper.unmarshaller(SimplePojo.class, mapper));
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
    Observable<SimplePojo> mapped = stream.compose(ObservableHelper.unmarshaller(SimplePojo.class, mapper));
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
    Single<SimplePojo> mapped = stream.compose(SingleHelper.unmarshaller(SimplePojo.class, mapper));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new SimplePojo("bar"))
      .assertEmpty();
  }

  @Test
  public void testMaybeMapPojoFromBuffer() throws Exception {
    Maybe<Buffer> stream = Maybe.just(Buffer.buffer("{\"foo\":\"bar\"}"));
    Maybe<SimplePojo> mapped = stream.compose(MaybeHelper.unmarshaller(SimplePojo.class, mapper));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new SimplePojo("bar"))
      .assertEmpty();
  }

  @Test
  public void testFlowableMapPojoFromBufferFailure() throws Exception {
    Flowable<Buffer> stream = Flowable.just(Buffer.buffer("{\"foo\""));
    Flowable<SimplePojo> mapped = stream.compose(FlowableHelper.unmarshaller(SimplePojo.class, mapper));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<SimplePojo>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .request(1)
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testObservableMapPojoFromBufferFailure() throws Exception {
    Observable<Buffer> stream = Observable.just(Buffer.buffer("{\"foo\""));
    Observable<SimplePojo> mapped = stream.compose(ObservableHelper.unmarshaller(SimplePojo.class, mapper));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<SimplePojo>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testSingleMapPojoFromBufferFailure() throws Exception {
    Single<Buffer> stream = Single.just(Buffer.buffer("{\"foo\""));
    Single<SimplePojo> mapped = stream.compose(SingleHelper.unmarshaller(SimplePojo.class, mapper));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testMaybeMapPojoFromBufferFailure() throws Exception {
    Maybe<Buffer> stream = Maybe.just(Buffer.buffer("{\"foo\""));
    Maybe<SimplePojo> mapped = stream.compose(MaybeHelper.unmarshaller(SimplePojo.class, mapper));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertError(err -> assertTrue(err instanceof JsonParseException))
      .assertEmpty();
  }

  @Test
  public void testFlowableMapObjectNodeFromBuffer() throws Exception {
    Flowable<Buffer> stream = Flowable.just(Buffer.buffer("{\"foo\""), Buffer.buffer(":\"bar\"}"));
    Flowable<JsonNode> mapped = stream.compose(FlowableHelper.unmarshaller(JsonNode.class, mapper));
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
    Observable<JsonNode> mapped = stream.compose(ObservableHelper.unmarshaller(JsonNode.class, mapper));
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
    Single<JsonNode> mapped = stream.compose(SingleHelper.unmarshaller(JsonNode.class, mapper));
    SimpleSubscriber<JsonNode> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new ObjectMapper().createObjectNode().put("foo", "bar"))
      .assertEmpty();
  }

  @Test
  public void testMaybeMapObjectNodeFromBuffer() throws Exception {
    Maybe<Buffer> stream = Maybe.just(Buffer.buffer("{\"foo\":\"bar\"}"));
    Maybe<JsonNode> mapped = stream.compose(MaybeHelper.unmarshaller(JsonNode.class, mapper));
    SimpleSubscriber<JsonNode> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(new ObjectMapper().createObjectNode().put("foo", "bar"))
      .assertEmpty();
  }

  @Test
  public void testFlowableMapPojoListFromBuffer() throws Exception {
    Flowable<Buffer> stream = Flowable.just(Buffer.buffer("[{\"foo\":\"bar\"}]"));
    Flowable<List<SimplePojo>> mapped = stream.compose(FlowableHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}, mapper));
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
    Observable<List<SimplePojo>> mapped = stream.compose(ObservableHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}, mapper));
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
    Single<List<SimplePojo>> mapped = stream.compose(SingleHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}, mapper));
    SimpleSubscriber<List<SimplePojo>> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(Arrays.asList(new SimplePojo("bar")))
      .assertEmpty();
  }

  @Test
  public void testMaybeMapPojoListFromBuffer() throws Exception {
    Maybe<Buffer> stream = Maybe.just(Buffer.buffer("[{\"foo\":\"bar\"}]"));
    Maybe<List<SimplePojo>> mapped = stream.compose(MaybeHelper.unmarshaller(new TypeReference<List<SimplePojo>>(){}, mapper));
    SimpleSubscriber<List<SimplePojo>> subscriber = new SimpleSubscriber<>();
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertItem(Arrays.asList(new SimplePojo("bar")))
      .assertEmpty();
  }

  @Test
  public void testFlowableMapFromEmptyBuffer() throws Exception {
    Flowable<Buffer> stream = Flowable.empty();
    Flowable<SimplePojo> mapped = stream.compose(FlowableHelper.unmarshaller(SimplePojo.class, mapper));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<SimplePojo>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testObservableMapFromEmptyBuffer() throws Exception {
    Observable<Buffer> stream = Observable.empty();
    Observable<SimplePojo> mapped = stream.compose(ObservableHelper.unmarshaller(SimplePojo.class, mapper));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<SimplePojo>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertCompleted()
      .assertEmpty();
  }

  @Test
  public void testMaybeMapFromEmptyBuffer() throws Exception {
    Maybe<Buffer> stream = Maybe.empty();
    Maybe<SimplePojo> mapped = stream.compose(MaybeHelper.unmarshaller(SimplePojo.class, mapper));
    SimpleSubscriber<SimplePojo> subscriber = new SimpleSubscriber<SimplePojo>().prefetch(0);
    TestUtils.subscribe(mapped, subscriber);
    subscriber
      .assertCompleted()
      .assertEmpty();
  }

}
