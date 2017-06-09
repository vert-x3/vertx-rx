package io.vertx.rx.groovy.test;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import io.vertx.core.Vertx;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GroovyIntegrationTest extends VertxTestBase {

  @Test
  public void testConsumeBodyStream() throws Exception {
    runScript("src/test/groovy/testConsumeBodyStream.groovy");
  }

  @Test
  public void testRegisterAgain() throws Exception {
    runScript("src/test/groovy/testRegisterAgain.groovy");
  }

  @Test
  public void testObservableUnsubscribeDuringObservation() throws Exception {
    runScript("src/test/groovy/testObservableUnsubscribeDuringObservation.groovy");
  }

  @Test
  public void testObservableNetSocket() throws Exception {
    runScript("src/test/groovy/testObservableNetSocket.groovy");
  }

  @Test
  public void testObservableWebSocket() throws Exception {
    runScript("src/test/groovy/testObservableWebSocket.groovy");
  }

  @Test
  public void testObservableHttpRequest() throws Exception {
    runScript("src/test/groovy/testObservableHttpRequest.groovy");
  }

  @Test
  public void testObserverToFuture() throws Exception {
    runScript("src/test/groovy/testObserverToFuture.groovy");
  }

  @Test
  public void testObserverToHandler() throws Exception {
    runScript("src/test/groovy/testObserverToHandler.groovy");
  }

  @Test
  public void testScheduledTimer() throws Exception {
    runScript("src/test/groovy/testScheduledTimer.groovy");
  }

  @Test
  public void testScheduledBuffer() throws Exception {
    runScript("src/test/groovy/testScheduledBuffer.groovy");
  }

  @Test
  public void testTimeMap() throws Exception {
    runScript("src/test/groovy/testTimeMap.groovy");
  }

  @Test
  public void testConcat() throws Exception {
    runScript("src/test/groovy/testConcat.groovy");
  }

  @Test
  public void testHttpClient() throws Exception {
    runScript("src/test/groovy/testHttpClient.groovy");
  }

  @Test
  public void testHttpClientFlatMap() throws Exception {
    runScript("src/test/groovy/testHttpClientFlatMap.groovy");
  }

  @Test
  public void testHttpClientFlatMapUnmarshall() throws Exception {
    runScript("src/test/groovy/testHttpClientFlatMapUnmarshall.groovy");
  }

  @Test
  public void testHttpClientFlatMapUnmarshallTypeRef() throws Exception {
    runScript("src/test/groovy/testHttpClientFlatMapUnmarshallTypeRef.groovy");
  }

  @Test
  public void testWebsocketClient() throws Exception {
    runScript("src/test/groovy/testWebsocketClient.groovy");
  }

  @Test
  public void testWebsocketClientFlatMap() throws Exception {
    runScript("src/test/groovy/testWebsocketClientFlatMap.groovy");
  }

  private void runScript(String script) throws Exception {
    Vertx vertx = Vertx.vertx();
    try {
      GroovyShell gcl = new GroovyShell();
      Script s = gcl.parse(new File(script));
      Binding binding = new Binding();
      binding.setProperty("test", this);
      binding.setProperty("vertx", vertx);
      s.setBinding(binding);
      s.run();
    } finally {
      CountDownLatch latch = new CountDownLatch(1);
      vertx.close(v -> {
        latch.countDown();
      });
      latch.await();
    }
  }

}
