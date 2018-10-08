package io.vertx.reactivex.test;

import io.vertx.codegen.testmodel.AnyJavaTypeTCKImpl;
import io.vertx.reactivex.codegen.testmodel.AnyJavaTypeTCK;
import org.junit.Test;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AnyJavaTypeTCKTest {

  final AnyJavaTypeTCK obj = new AnyJavaTypeTCK(new AnyJavaTypeTCKImpl());

  @Test
  public void testHandlersWithAsyncResult() {
    List<Socket> socketsRxList = obj.rxMethodWithHandlerAsyncResultListOfJavaTypeParam().blockingGet();

    Set<Socket> socketSetRx = obj.rxMethodWithHandlerAsyncResultSetOfJavaTypeParam().blockingGet();

    Map<String, Socket> stringSocketMapRx = obj.rxMethodWithHandlerAsyncResultMapOfJavaTypeParam().blockingGet();

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
