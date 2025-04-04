package io.vertx.it;

import io.grpc.examples.rxjava3.helloworld.GreeterGrpcClient;
import io.grpc.examples.helloworld.GreeterGrpcService;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.examples.helloworld.HelloReply;
import io.reactivex.rxjava3.core.Flowable;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.grpc.client.GrpcClient;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GrpcTest extends VertxTestBase {

  @Test
  public void testHelloWorld() throws Exception {
    // Create gRPC Server
    GrpcServer grpcServer = GrpcServer.server(vertx);
    grpcServer.addService(new GreeterGrpcService() {
      @Override
      public Future<HelloReply> sayHello(HelloRequest request) {
        return Future.succeededFuture(HelloReply.newBuilder()
          .setMessage("Hello " + request.getName())
          .build());
      }
      @Override
      protected void sayHelloStreaming(ReadStream<HelloRequest> request, WriteStream<HelloReply> response) {
        request.handler(hello -> response.write(HelloReply.newBuilder().setMessage("Hello " + hello.getName()).build()));
        request.endHandler(v -> response.end());
      }
    });
    HttpServer httpServer = vertx.createHttpServer();
    httpServer.requestHandler(grpcServer)
      .listen(8080).toCompletionStage().toCompletableFuture().get(20, TimeUnit.SECONDS);

    // Create gRPC Client
    GrpcClient grpcClient = GrpcClient.client(new Vertx(vertx));
    GreeterGrpcClient client = GreeterGrpcClient.create(grpcClient, SocketAddress.inetSocketAddress(8080, "localhost"));

    HelloReply reply = client
      .sayHello(HelloRequest.newBuilder().setName("World").build())
      .blockingGet();
    assertEquals("Hello World", reply.getMessage());

    Flowable<HelloRequest> stream = Flowable.just("World", "Monde", "Mundo")
      .map(name -> HelloRequest.newBuilder().setName(name).build());

    List<String> result = client
      .sayHelloStreaming(stream)
      .map(HelloReply::getMessage)
      .collect(Collectors.toList())
      .blockingGet();
    assertEquals(Arrays.asList("Hello World", "Hello Monde", "Hello Mundo"), result);
  }
}
