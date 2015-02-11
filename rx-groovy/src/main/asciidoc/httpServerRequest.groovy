import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.core.http.HttpServerRequest
import rx.Observable

Vertx vertx = Vertx.vertx()
HttpServer server = vertx.createHttpServer()

// tag::example[]
Observable<HttpServerRequest> requestObservable = server.requestStream().toObservable()
requestObservable.subscribe({ request ->
  // Process request
})
// end::example[]
