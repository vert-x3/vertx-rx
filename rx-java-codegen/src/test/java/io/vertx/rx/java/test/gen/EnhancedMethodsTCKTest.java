package io.vertx.rx.java.test.gen;

import io.vertx.codegen.testmodel.EnhancedMethodsTCKImpl;
import io.vertx.rxjava.codegen.testmodel.EnhancedMethodsTCK;
import org.junit.Test;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.vertx.rx.java.test.gen.ApiTCKTest.get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EnhancedMethodsTCKTest {

  final EnhancedMethodsTCK obj = new EnhancedMethodsTCK(new EnhancedMethodsTCKImpl());

  @Test
  public void testHandlersWithAsyncResult() throws Exception {
    List<Socket> socketList = get(obj.methodWithHandlerAsyncResultListOfOtherParamObservable());
    List<Socket> socketsRxList = obj.rxMethodWithHandlerAsyncResultListOfOtherParam().toBlocking().value();

    Set<Socket> socketSet = get(obj.methodWithHandlerAsyncResultSetOfOtherParamObservable());
    Set<Socket> socketSetRx = obj.rxMethodWithHandlerAsyncResultSetOfOtherParam().toBlocking().value();

    Map<String, Socket> stringSocketMap = get(obj.methodWithHandlerAsyncResultMapOfOtherParamObservable());
    Map<String, Socket> stringSocketMapRx = obj.rxMethodWithHandlerAsyncResultMapOfOtherParam().toBlocking().value();

    for (Socket socket : socketsRxList) {
      assertFalse(socket.isConnected());
    }
    for (Socket socket : socketList) {
      assertFalse(socket.isConnected());
    }

    for (Socket socket : socketSet) {
      assertFalse(socket.isConnected());
    }

    for (Socket socket : socketSetRx) {
      assertFalse(socket.isConnected());
    }

    for (Map.Entry<String, Socket> entry : stringSocketMap.entrySet()) {
      assertEquals("1", entry.getKey());
      assertFalse(entry.getValue().isConnected());
    }

    for (Map.Entry<String, Socket> entry : stringSocketMapRx.entrySet()) {
      assertEquals("1", entry.getKey());
      assertFalse(entry.getValue().isConnected());
    }

    assertEquals(1, socketList.size());
    assertEquals(1, socketsRxList.size());
    assertEquals(1, socketSet.size());
    assertEquals(1, socketSetRx.size());
    assertEquals(1, stringSocketMap.size());
    assertEquals(1, stringSocketMapRx.size());
  }
}
