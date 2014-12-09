package io.vertx.rx.js.test;

import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JsIntegrationTest extends VertxTestBase {

  public static VertxAssert current;

  @Override
  public void before() throws Exception {
    super.before();
    current = new VertxAssert() {
      @Override
      public void testComplete() {
        JsIntegrationTest.this.testComplete();
      }
      @Override
      public void assertEquals(Object expected, Object actual) {
        JsIntegrationTest.this.assertEquals(expected, actual);
      }
      @Override
      public void assertEquals(long expected, long actual) {
        JsIntegrationTest.this.assertEquals(expected, actual);
      }
      @Override
      public void assertTrue(boolean b) {
        JsIntegrationTest.this.assertTrue(b);
      }
      @Override
      public void assertFalse(boolean b) {
        JsIntegrationTest.this.assertFalse(b);
      }
      @Override
      public void fail(String msg) {
        JsIntegrationTest.this.fail(msg);
      }
    };
  }

  @Test
  public void testConsumeBodyStream() throws Throwable {
    deployTest("test_consume_body_stream.js");
  }

  @Test
  public void testAdaptItem() throws Throwable {
    deployTest("test_adapt_item.js");
  }

  @Test
  public void testConsumerRegistration() throws Throwable {
    deployTest("test_consumer_registration.js");
  }

  @Test
  public void testCompleted() throws Throwable {
    deployTest("test_completed.js");
  }

  @Test
  public void testSubscribeTwice() throws Throwable {
    deployTest("test_subscribe_twice.js");
  }

  @Test
  public void testObservableFuture() throws Throwable {
    deployTest("test_observable_future.js");
  }

  @Test
  public void testObserverToFuture() throws Throwable {
    deployTest("test_observer_to_future.js");
  }

  @Test
  public void testObservableHandler() throws Throwable {
    deployTest("test_observable_handler.js");
  }

  @Test
  public void testObserverToHandler() throws Throwable {
    deployTest("test_observer_to_handler.js");
  }

  @Test
  public void testScheduledTimer() throws Throwable {
    deployTest("test_scheduled_timer.js");
  }

  @Test
  public void testScheduledBuffer() throws Throwable {
    deployTest("test_scheduled_buffer.js");
  }

  @Test
  public void testConcat() throws Throwable {
    deployTest("test_concat.js");
  }

  @Test
  public void testTimeMap() throws Throwable {
    deployTest("test_time_map.js");
  }

  @Test
  public void testHttpClient() throws Throwable {
    deployTest("test_http_client.js");
  }

  @Test
  public void testHttpClientFlatMap() throws Throwable {
    deployTest("test_http_client_flatmap.js");
  }

  @Test
  public void testWebsocketClient() throws Throwable {
    deployTest("test_websocket_client.js");
  }

  @Test
  public void testWebsocketClientFlatMap() throws Throwable {
    deployTest("test_websocket_client_flatmap.js");
  }

  private void deployTest(String test) {
    vertx.deployVerticle(test, ar -> {
      if (!ar.succeeded()) {
        Throwable cause = ar.cause();
        cause.printStackTrace();
        fail(cause.getMessage());
      }
    });
    await();
  }
}
