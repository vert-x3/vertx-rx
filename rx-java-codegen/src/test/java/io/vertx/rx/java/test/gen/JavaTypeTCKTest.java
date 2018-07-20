package io.vertx.rx.java.test.gen;

import io.vertx.codegen.testmodel.JavaTypeTCKImpl;
import io.vertx.rxjava.codegen.testmodel.JavaTypeTCK;
import org.junit.Test;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.vertx.rx.java.test.gen.ApiTCKTest.get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JavaTypeTCKTest {

  final JavaTypeTCK obj = new JavaTypeTCK(new JavaTypeTCKImpl());

  @Test
  public void testHandlersWithAsyncResult() throws Exception {
    List<Socket> socketList = get(obj.methodWithHandlerAsyncResultListOfJavaTypeParamObservable());
    List<Socket> socketsRxList = obj.rxMethodWithHandlerAsyncResultListOfJavaTypeParam().toBlocking().value();

    Set<Socket> socketSet = get(obj.methodWithHandlerAsyncResultSetOfJavaTypeParamObservable());
    Set<Socket> socketSetRx = obj.rxMethodWithHandlerAsyncResultSetOfJavaTypeParam().toBlocking().value();

    Map<String, Socket> stringSocketMap = get(obj.methodWithHandlerAsyncResultMapOfJavaTypeParamObservable());
    Map<String, Socket> stringSocketMapRx = obj.rxMethodWithHandlerAsyncResultMapOfJavaTypeParam().toBlocking().value();

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
