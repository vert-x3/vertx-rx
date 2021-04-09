package examples;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.docgen.Source;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.ext.web.client.HttpResponse;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.codec.BodyCodec;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Source
public class RxWebClientExamples {

  public void simpleGet(WebClient client) {

    // Create the RxJava single for an HttpRequest
    // at this point no HTTP request has been sent to the server
    Single<HttpResponse<Buffer>> single = client
      .get(8080, "myserver.mycompany.com", "/some-uri")
      .send();

    // Send a request upon subscription of the Single
    single.subscribe(response -> System.out.println("Received 1st response with status code" + response.statusCode()), error -> System.out.println("Something went wrong " + error.getMessage()));

    // Send another request
    single.subscribe(response -> System.out.println("Received 2nd response with status code" + response.statusCode()), error -> System.out.println("Something went wrong " + error.getMessage()));
  }

  public void flatMap(WebClient client) {

    // Obtain an URL Single from myserver.mycompany.com
    Single<String> url = client
      .get(8080, "myserver.mycompany.com", "/some-uri")
      .send()
      .map(HttpResponse::bodyAsString);

    // Use the flatMap operator to make a request on the URL Single
    url
      .flatMap(u -> client.getAbs(u).send())
      .subscribe(response -> System.out.println("Received response with status code" + response.statusCode()), error -> System.out.println("Something went wrong " + error.getMessage()));
  }

  public void moreComplex(WebClient client) {
    Single<HttpResponse<JsonObject>> single = client
      .get(8080, "myserver.mycompany.com", "/some-uri")
      .putHeader("some-header", "header-value")
      .addQueryParam("some-param", "param value")
      .as(BodyCodec.jsonObject())
      .send();
    single.subscribe(resp -> {
      System.out.println(resp.statusCode());
      System.out.println(resp.body());
    });
  }

  private Flowable<Buffer> getPayload() {
    throw new UnsupportedOperationException();
  }

  public void sendFlowable(WebClient client) {

    Flowable<Buffer> body = getPayload();

    Single<HttpResponse<Buffer>> single = client
      .post(8080, "myserver.mycompany.com", "/some-uri")
      .sendStream(body);
    single.subscribe(resp -> {
      System.out.println(resp.statusCode());
      System.out.println(resp.body());
    });
  }
}
