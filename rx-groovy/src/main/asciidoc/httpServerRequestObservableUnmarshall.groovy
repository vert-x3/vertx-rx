import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.core.http.HttpServerRequest
import io.vertx.rx.groovy.RxHelper
import rx.Observable

class MyPojo {
}

Vertx vertx = Vertx.vertx()
HttpServer server = vertx.createHttpServer()

// tag::example[]
Observable<HttpServerRequest> requestObservable = server.requestStream().toObservable()
requestObservable.subscribe({ request ->
  Observable<MyPojo> observable = request.
      toObservable().
      lift(RxHelper.unmarshaller(MyPojo.class))
})
// end::example[]
