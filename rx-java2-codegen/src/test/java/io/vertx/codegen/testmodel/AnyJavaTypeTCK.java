package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

@VertxGen()
public interface AnyJavaTypeTCK {

  @SuppressWarnings("codegen-allow-any-java-type") void methodWithJavaTypeParam(Socket socket);
  @SuppressWarnings("codegen-allow-any-java-type") void methodWithListOfJavaTypeParam(List<Socket> socketList);
  @SuppressWarnings("codegen-allow-any-java-type") void methodWithSetOfJavaTypeParam(Set<Socket> socketSet);
  @SuppressWarnings("codegen-allow-any-java-type") void methodWithMapOfJavaTypeParam(Map<String, Socket> socketMap);

  @SuppressWarnings("codegen-allow-any-java-type") Socket methodWithJavaTypeReturn();
  @SuppressWarnings("codegen-allow-any-java-type") List<Socket> methodWithListOfJavaTypeReturn();
  @SuppressWarnings("codegen-allow-any-java-type") Set<Socket> methodWithSetOfJavaTypeReturn();
  @SuppressWarnings("codegen-allow-any-java-type") Map<String, Socket> methodWithMapOfJavaTypeReturn();

  @SuppressWarnings("codegen-allow-any-java-type") void methodWithHandlerJavaTypeParam(Handler<Socket> socketHandler);
  @SuppressWarnings("codegen-allow-any-java-type") void methodWithHandlerListOfJavaTypeParam(Handler<List<Socket>> socketListHandler);
  @SuppressWarnings("codegen-allow-any-java-type") void methodWithHandlerSetOfJavaTypeParam(Handler<Set<Socket>> socketSetHandler);
  @SuppressWarnings("codegen-allow-any-java-type") void methodWithHandlerMapOfJavaTypeParam(Handler<Map<String, Socket>> socketMapHandler);

  @SuppressWarnings("codegen-allow-any-java-type") void methodWithHandlerAsyncResultJavaTypeParam(Handler<AsyncResult<Socket>> socketHandler);
  @SuppressWarnings("codegen-allow-any-java-type") void methodWithHandlerAsyncResultListOfJavaTypeParam(Handler<AsyncResult<List<Socket>>> socketListHandler);
  @SuppressWarnings("codegen-allow-any-java-type") void methodWithHandlerAsyncResultSetOfJavaTypeParam(Handler<AsyncResult<Set<Socket>>> socketSetHandler);
  @SuppressWarnings("codegen-allow-any-java-type") void methodWithHandlerAsyncResultMapOfJavaTypeParam(Handler<AsyncResult<Map<String, Socket>>> socketMapHandler);
}
