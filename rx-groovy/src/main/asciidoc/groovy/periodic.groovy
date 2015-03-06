import io.vertx.groovy.core.Vertx

Vertx vertx = Vertx.vertx()

// tag::example[]
vertx.periodicStream(1000).
    toObservable().
    subscribe({ id ->
          println("Callback every second")
        }
    )
// end::example[]
