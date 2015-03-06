import io.vertx.core.http.HttpMethod
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.http.HttpClient
import io.vertx.groovy.core.http.HttpClientRequest

Vertx vertx = Vertx.vertx()
HttpClient client = vertx.createHttpClient()
HttpClientRequest request = client.request(HttpMethod.GET, 8080, "localhost", "/the_uri")

// tag::example[]
request.toObservable().
    flatMap({resp -> resp.&toObservable}).
    forEach(
        { buffer ->
      // Process buffer
    })
// end::example[]
