import io.vertx.core.http.HttpMethod
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.http.HttpClient
import io.vertx.groovy.core.http.HttpClientRequest
import rx.Observable

Vertx vertx = Vertx.vertx()
HttpClient client = vertx.createHttpClient()

// tag::example[]
HttpClientRequest request = client.request(HttpMethod.GET, 8080, "localhost", "/the_uri")
request.toObservable().
    subscribe(
        { response ->
          Observable<Buffer> observable = response.toObservable()
          observable.forEach(
              { buffer ->
                // Process buffer
              })
        })
// end::example[]
