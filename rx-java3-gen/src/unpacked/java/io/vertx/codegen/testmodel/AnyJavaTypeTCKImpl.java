package io.vertx.codegen.testmodel;

import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class AnyJavaTypeTCKImpl implements AnyJavaTypeTCK {

  @Override
  public void methodWithJavaTypeParam(Socket socket) {
    assertNotNull(socket);
  }

  @Override
  public void methodWithListOfJavaTypeParam(List<Socket> socketList) {
    for (Socket socket : socketList) {
      assertNotNull(socket);
    }
  }

  @Override
  public void methodWithSetOfJavaTypeParam(Set<Socket> socketSet) {
    for (Socket socket : socketSet) {
      assertNotNull(socket);
    }
  }

  @Override
  public void methodWithMapOfJavaTypeParam(Map<String, Socket> socketMap) {
    for (Map.Entry<String, Socket> stringSocketEntry : socketMap.entrySet()) {
      assertNotNull(stringSocketEntry.getValue());
    }
  }

  @Override
  public Socket methodWithJavaTypeReturn() {
    return new Socket();
  }

  @Override
  public List<Socket> methodWithListOfJavaTypeReturn() {
    Socket socket = new Socket();
    ArrayList<Socket> sockets = new ArrayList<>();
    sockets.add(socket);
    return sockets;
  }

  @Override
  public Set<Socket> methodWithSetOfJavaTypeReturn() {
    Socket socket = new Socket();
    Set<Socket> sockets = new HashSet<>();
    sockets.add(socket);
    return sockets;
  }

  @Override
  public Map<String, Socket> methodWithMapOfJavaTypeReturn() {
    Socket socket = new Socket();
    Map<String, Socket> sockets = new HashMap<>();
    sockets.put("1", socket);
    return sockets;
  }

  @Override
  public void methodWithHandlerJavaTypeParam(Handler<Socket> socketHandler) {
    assertNotNull(socketHandler);
    socketHandler.handle(new Socket());
  }

  @Override
  public void methodWithHandlerListOfJavaTypeParam(Handler<List<Socket>> socketListHandler) {
    assertNotNull(socketListHandler);
    Socket socket = new Socket();
    ArrayList<Socket> sockets = new ArrayList<>();
    sockets.add(socket);
    socketListHandler.handle(sockets);
  }

  @Override
  public void methodWithHandlerSetOfJavaTypeParam(Handler<Set<Socket>> socketSetHandler) {
    assertNotNull(socketSetHandler);
    Socket socket = new Socket();
    Set<Socket> sockets = new HashSet<>();
    sockets.add(socket);
    socketSetHandler.handle(sockets);
  }

  @Override
  public void methodWithHandlerMapOfJavaTypeParam(Handler<Map<String, Socket>> socketMapHandler) {
    assertNotNull(socketMapHandler);
    Socket socket = new Socket();
    Map<String, Socket> sockets = new HashMap<>();
    sockets.put("1", socket);
    socketMapHandler.handle(sockets);
  }

  @Override
  public Future<Socket> methodWithHandlerAsyncResultJavaTypeParam() {
    Socket socket = new Socket();
    return Future.succeededFuture(socket);
  }

  @Override
  public Future<List<Socket>> methodWithHandlerAsyncResultListOfJavaTypeParam() {
    Socket socket = new Socket();
    ArrayList<Socket> sockets = new ArrayList<>();
    sockets.add(socket);
    return Future.succeededFuture(sockets);
  }

  @Override
  public Future<Set<Socket>> methodWithHandlerAsyncResultSetOfJavaTypeParam() {
    Socket socket = new Socket();
    Set<Socket> sockets = new HashSet<>();
    sockets.add(socket);
    return Future.succeededFuture(sockets);
  }

  @Override
  public Future<Map<String, Socket>> methodWithHandlerAsyncResultMapOfJavaTypeParam() {
    Socket socket = new Socket();
    Map<String, Socket> sockets = new HashMap<>();
    sockets.put("1", socket);
    return Future.succeededFuture(sockets);
  }
}
