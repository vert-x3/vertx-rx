import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.http.HttpServer
import io.vertx.groovy.core.Vertx
import rx.Observer
import rx.observers.Observers

def vertx = Vertx.vertx()

// tag::example[]
Observer<HttpServer> observer = Observers.create({ server -> println("Server started") })
Handler<AsyncResult<HttpServer>> handler = observer.toFuture()
vertx.createHttpServer([port:1234,host:"localhost"]).listen(handler)
// end::example[]
