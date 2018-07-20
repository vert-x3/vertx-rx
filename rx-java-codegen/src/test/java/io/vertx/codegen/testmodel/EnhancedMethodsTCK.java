package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

@VertxGen
public interface EnhancedMethodsTCK {

  void methodWithOtherParam(Socket socket);
  Socket methodWithOtherReturn();

  void methodWithListOfOtherParam(List<Socket> socketList);
  void methodWithSetOfOtherParam(Set<Socket> socketSet);
  void methodWithMapOfOtherParam(Map<String, Socket> socketMap);

  List<Socket> methodWithListOfOtherReturn();
  Set<Socket> methodWithSetOfOtherReturn();
  Map<String, Socket> methodWithMapOfOtherReturn();

  void methodWithHandlerListOfOtherParam(Handler<List<Socket>> socketList);
  void methodWithHandlerSetOfOtherParam(Handler<Set<Socket>> socketSet);
  void methodWithHandlerMapOfOtherParam(Handler<Map<String, Socket>> socketMap);


  void methodWithHandlerAsyncResultListOfOtherParam(Handler<AsyncResult<List<Socket>>> socketList);
  void methodWithHandlerAsyncResultSetOfOtherParam(Handler<AsyncResult<Set<Socket>>> socketSet);
  void methodWithHandlerAsyncResultMapOfOtherParam(Handler<AsyncResult<Map<String, Socket>>> socketMap);
}
