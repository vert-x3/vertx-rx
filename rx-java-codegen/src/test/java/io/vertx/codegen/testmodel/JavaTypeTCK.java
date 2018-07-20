package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

@VertxGen(allowJavaTypes = true)
public interface JavaTypeTCK {

  void methodWithJavaTypeParam(Socket socket);
  void methodWithListOfJavaTypeParam(List<Socket> socketList);
  void methodWithSetOfJavaTypeParam(Set<Socket> socketSet);
  void methodWithMapOfJavaTypeParam(Map<String, Socket> socketMap);

  Socket methodWithJavaTypeReturn();
  List<Socket> methodWithListOfJavaTypeReturn();
  Set<Socket> methodWithSetOfJavaTypeReturn();
  Map<String, Socket> methodWithMapOfJavaTypeReturn();

  void methodWithHandlerJavaTypeParam(Handler<Socket> socketHandler);
  void methodWithHandlerListOfJavaTypeParam(Handler<List<Socket>> socketListHandler);
  void methodWithHandlerSetOfJavaTypeParam(Handler<Set<Socket>> socketSetHandler);
  void methodWithHandlerMapOfJavaTypeParam(Handler<Map<String, Socket>> socketMapHandler);

  void methodWithHandlerAsyncResultJavaTypeParam(Handler<AsyncResult<Socket>> socketHandler);
  void methodWithHandlerAsyncResultListOfJavaTypeParam(Handler<AsyncResult<List<Socket>>> socketListHandler);
  void methodWithHandlerAsyncResultSetOfJavaTypeParam(Handler<AsyncResult<Set<Socket>>> socketSetHandler);
  void methodWithHandlerAsyncResultMapOfJavaTypeParam(Handler<AsyncResult<Map<String, Socket>>> socketMapHandler);
}
