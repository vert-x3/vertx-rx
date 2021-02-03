package examples;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.api.contract.RouterFactoryOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.docgen.Source;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.ext.auth.jwt.JWTAuth;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import io.vertx.rxjava3.ext.web.handler.JWTAuthHandler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Source
public class RxWebApiContractExamples {

  public void mainExample(Vertx vertx, Handler<RoutingContext> myValidationFailureHandler, JWTAuth jwtAuth) {
    OpenAPI3RouterFactory
      .create(vertx, "src/main/resources/petstore.yaml")
      .flatMap(routerFactory -> {
        // Spec loaded with success. router factory contains OpenAPI3RouterFactory
        // Set router factory options.
        RouterFactoryOptions options = new RouterFactoryOptions().setOperationModelKey("openapi_model");
        // Mount the options
        routerFactory.setOptions(options);
        // Add an handler with operationId
        routerFactory.addHandlerByOperationId("listPets", routingContext -> {
          // Handle listPets operation
          routingContext.response().setStatusMessage("Called listPets").end();
        });

        // Add a security handler
        routerFactory.addSecurityHandler("api_key", JWTAuthHandler.create(jwtAuth));

        // Now you have to generate the router
        Router router = routerFactory.getRouter();

        // Now you can use your Router instance
        HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"));
        return server.requestHandler(router).listen();
      })
      .subscribe(httpServer -> {
        // Server up and running
      }, throwable -> {
        // Error during router factory instantiation or http server start
      });
  }

}
