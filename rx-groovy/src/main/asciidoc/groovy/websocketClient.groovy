import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpClient
import io.vertx.groovy.core.http.WebSocket
import io.vertx.groovy.core.http.WebSocketStream
import rx.Observable

Vertx vertx = Vertx.vertx()

// tag::example[]
HttpClient client = vertx.createHttpClient()
WebSocketStream stream = client.websocketStream(8080, "localhost", "/the_uri")
Observable<WebSocket> socketObservable = stream.toObservable()
socketObservable.subscribe(
    { ws ->
      // Use the websocket
    }, { error ->
  // Could not connect
})
// end::example[]
