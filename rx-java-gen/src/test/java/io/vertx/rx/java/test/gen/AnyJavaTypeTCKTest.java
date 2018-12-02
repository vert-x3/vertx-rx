package io.vertx.rx.java.test.gen;

import io.vertx.codegen.testmodel.AnyJavaTypeTCKImpl;
import io.vertx.rxjava.codegen.testmodel.AnyJavaTypeTCK;
import org.junit.Test;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.vertx.rx.java.test.gen.ApiTCKTest.get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AnyJavaTypeTCKTest {

  final AnyJavaTypeTCK obj = new AnyJavaTypeTCK(new AnyJavaTypeTCKImpl());

  @Test
  public void testHandlersWithAsyncResult() throws Exception {
    List<Socket> socketsRxList = obj.rxMethodWithHandlerAsyncResultListOfJavaTypeParam().toBlocking().value();

    Set<Socket> socketSetRx = obj.rxMethodWithHandlerAsyncResultSetOfJavaTypeParam().toBlocking().value();

    Map<String, Socket> stringSocketMapRx = obj.rxMethodWithHandlerAsyncResultMapOfJavaTypeParam().toBlocking().value();

    for (Socket socket : socketsRxList) {
      assertFalse(socket.isConnected());
    }

    for (Socket socket : socketSetRx) {
      assertFalse(socket.isConnected());
    }


    for (Map.Entry<String, Socket> entry : stringSocketMapRx.entrySet()) {
      assertEquals("1", entry.getKey());
      assertFalse(entry.getValue().isConnected());
    }

    assertEquals(1, socketsRxList.size());
    assertEquals(1, socketSetRx.size());
    assertEquals(1, stringSocketMapRx.size());
  }
}
