import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpServer
import io.vertx.groovy.core.http.ServerWebSocket
import rx.Observable

Vertx vertx = Vertx.vertx()
HttpServer server = vertx.createHttpServer()

// tag::example[]
Observable<ServerWebSocket> socketObservable = server.websocketStream().toObservable()
socketObservable.subscribe(
    { socket -> println("Web socket connect") },
    { failure -> println("Should never be called") },
    { println("Subscription ended or server closed") }
)
// end::example[]