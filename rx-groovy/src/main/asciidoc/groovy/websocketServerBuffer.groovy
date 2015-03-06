import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.core.http.ServerWebSocket
import rx.Observable

Vertx vertx = Vertx.vertx()
HttpServer server = vertx.createHttpServer()

// tag::example[]
Observable<ServerWebSocket> socketObservable = server.websocketStream().toObservable()
socketObservable.subscribe({ socket ->
  Observable<Buffer> dataObs = socket.toObservable()
  dataObs.subscribe({ buffer ->
    println("Got message ${buffer.toString("UTF-8")}")
  })
})
// end::example[]