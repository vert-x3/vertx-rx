import io.vertx.groovy.core.Vertx

Vertx vertx = Vertx.vertx()

// tag::example[]
vertx.timerStream(1000).
    toObservable().
    subscribe({ id ->
          println("Callback after 1 second")
        }
    )
// end::example[]
