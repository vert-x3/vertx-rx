import io.vertx.groovy.core.Vertx
import rx.Observable

import java.util.concurrent.TimeUnit;

Vertx vertx = Vertx.vertx()

// tag::example[]
Observable<Double> observable = vertx.eventBus().
<Double>consumer("heat-sensor").
    bodyStream().
    toObservable()

observable.
    buffer(1, TimeUnit.SECONDS).
    map({ samples -> samples.sum() }).
    subscribe({ heat ->
      vertx.eventBus().send("news-feed", "Current heat is " + heat)
    });
// end::example[]
