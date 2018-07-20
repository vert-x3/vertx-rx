package io.vertx.codegen.testmodel;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnhancedMethodsTCKImpl implements EnhancedMethodsTCK {

  @Override
  public void methodWithOtherParam(Socket socket) {
    System.out.println(socket.toString());
  }

  @Override
  public Socket methodWithOtherReturn() {
    return new Socket();
  }

  @Override
  public void methodWithListOfOtherParam(List<Socket> socketList) {
    for (Socket socket : socketList) {
      System.out.println(socket.toString());
    }
  }

  @Override
  public void methodWithSetOfOtherParam(Set<Socket> socketSet) {
    for (Socket socket : socketSet) {
      System.out.println(socket.toString());
    }
  }

  @Override
  public void methodWithMapOfOtherParam(Map<String, Socket> socketMap) {
    for (String s : socketMap.keySet()) {
      System.out.println(s);
    }
  }

  @Override
  public List<Socket> methodWithListOfOtherReturn() {
    Socket socket = new Socket();
    ArrayList<Socket> sockets = new ArrayList<>();
    sockets.add(socket);
    return sockets;
  }

  @Override
  public Set<Socket> methodWithSetOfOtherReturn() {
    Socket socket = new Socket();
    Set<Socket> sockets = new HashSet<>();
    sockets.add(socket);
    return sockets;
  }

  @Override
  public Map<String, Socket> methodWithMapOfOtherReturn() {
    Socket socket = new Socket();
    Map<String, Socket> sockets = new HashMap<>();
    sockets.put("1", socket);
    return sockets;
  }

  @Override
  public void methodWithHandlerListOfOtherParam(Handler<List<Socket>> socketList) {
    Socket socket = new Socket();
    ArrayList<Socket> sockets = new ArrayList<>();
    sockets.add(socket);
    socketList.handle(sockets);
  }

  @Override
  public void methodWithHandlerSetOfOtherParam(Handler<Set<Socket>> socketSet) {
    Socket socket = new Socket();
    Set<Socket> sockets = new HashSet<>();
    sockets.add(socket);
    socketSet.handle(sockets);
  }

  @Override
  public void methodWithHandlerMapOfOtherParam(Handler<Map<String, Socket>> socketMap) {
    Socket socket = new Socket();
    Map<String, Socket> sockets = new HashMap<>();
    sockets.put("1", socket);
    socketMap.handle(sockets);
  }

  @Override
  public void methodWithHandlerAsyncResultListOfOtherParam(Handler<AsyncResult<List<Socket>>> socketList) {
    Socket socket = new Socket();
    ArrayList<Socket> sockets = new ArrayList<>();
    sockets.add(socket);
    socketList.handle(Future.succeededFuture(sockets));
  }

  @Override
  public void methodWithHandlerAsyncResultSetOfOtherParam(Handler<AsyncResult<Set<Socket>>> socketSet) {
    Socket socket = new Socket();
    Set<Socket> sockets = new HashSet<>();
    sockets.add(socket);
    socketSet.handle(Future.succeededFuture(sockets));
  }

  @Override
  public void methodWithHandlerAsyncResultMapOfOtherParam(Handler<AsyncResult<Map<String, Socket>>> socketMap) {
    Socket socket = new Socket();
    Map<String, Socket> sockets = new HashMap<>();
    sockets.put("1", socket);
    socketMap.handle(Future.succeededFuture(sockets));
  }
}
