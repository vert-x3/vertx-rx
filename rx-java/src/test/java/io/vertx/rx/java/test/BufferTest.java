package io.vertx.rx.java.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.rx.java.RxHelper;
import org.junit.Test;
import rx.Observable;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BufferTest {

  @Test
  public void testBufferReduce() {
    Observable<Buffer> stream = Observable.from(Arrays.asList(Buffer.buffer("lorem"), Buffer.buffer("ipsum")));
    Observable<Buffer> reduced = stream.reduce(Buffer::appendBuffer);
    MySubscriber<Buffer> sub = new MySubscriber<>();
    reduced.subscribe(sub);
    sub.assertItem(Buffer.buffer("loremipsum"));
    sub.assertCompleted();
    sub.assertEmpty();
  }

  @Test
  public void testUnmarshallJsonObjectFromBuffer() {
    Observable<Buffer> stream = Observable.from(Arrays.asList(Buffer.buffer("{\"foo\":\"bar\"}")));
    Observable<JsonObject> mapped = stream.map(buffer -> new JsonObject(buffer.toString("UTF-8")));
    MySubscriber<JsonObject> sub = new MySubscriber<>();
    mapped.subscribe(sub);
    sub.assertItem(new JsonObject().put("foo", "bar"));
    sub.assertCompleted();
    sub.assertEmpty();
  }

  @Test
  public void testMapPojoFromBuffer() throws Exception {
    Observable<Buffer> stream = Observable.from(Arrays.asList(Buffer.buffer("{\"foo\":\"bar\"}")));
    Observable<MyPojo> mapped = stream.lift(RxHelper.unmarshaller(MyPojo.class));
    MySubscriber<MyPojo> sub = new MySubscriber<>();
    mapped.subscribe(sub);
    sub.assertItem(new MyPojo("bar"));
    sub.assertCompleted();
    sub.assertEmpty();
  }

  @Test
  public void testMapObjectNodeFromBuffer() throws Exception {
    Observable<Buffer> stream = Observable.from(Arrays.asList(Buffer.buffer("{\"foo\":\"bar\"}")));
    Observable<JsonNode> mapped = stream.lift(RxHelper.unmarshaller(JsonNode.class));
    MySubscriber<JsonNode> sub = new MySubscriber<>();
    mapped.subscribe(sub);
    sub.assertItem(new ObjectMapper().createObjectNode().put("foo", "bar"));
    sub.assertCompleted();
    sub.assertEmpty();
  }

  @Test
  public void testClusterSerializable() throws Exception {
    io.vertx.rxjava.core.buffer.Buffer buff = io.vertx.rxjava.core.buffer.Buffer.buffer("hello-world");
    Buffer actual = Buffer.buffer();
    buff.writeToBuffer(actual);
    Buffer expected = Buffer.buffer();
    Buffer.buffer("hello-world").writeToBuffer(expected);
    assertEquals(expected, actual);
    buff = io.vertx.rxjava.core.buffer.Buffer.buffer("hello-world");
    assertEquals(expected.length(), buff.readFromBuffer(0, expected));
    assertEquals("hello-world", buff.toString());
  }
}
