import io.vertx.core.http.HttpMethod
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpClient
import io.vertx.groovy.core.http.HttpClientRequest

Vertx vertx = Vertx.vertx()
HttpClient client = vertx.createHttpClient()

// tag::example[]
HttpClientRequest request = client.request(HttpMethod.GET, 8080, "localhost", "/the_uri")
request.toObservable().subscribe({ response ->
  // Process the response
}, { error ->
  // Could not connect
})
request.end()
// end::example[]
