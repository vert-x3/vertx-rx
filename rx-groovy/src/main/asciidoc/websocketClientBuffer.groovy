import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.http.HttpClient
import io.vertx.groovy.core.http.WebSocket
import io.vertx.groovy.core.http.WebSocketStream
import rx.Observable

HttpClient client = vertx.createHttpClient()
WebSocketStream stream = client.websocketStream(8080, "localhost", "/the_uri")
Observable<WebSocket> socketObservable = stream.toObservable()

// tag::example[]
socketObservable.subscribe(
    { socket ->
      Observable<Buffer> dataObs = socket.toObservable()
      dataObs.subscribe({ buffer ->
        println("Got message ${buffer.toString("UTF-8")}")
      })
    })
// end::example[]