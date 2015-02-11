import io.vertx.core.Handler
import io.vertx.groovy.core.Vertx
import rx.Observer
import rx.observers.Observers

def vertx = Vertx.vertx()

// tag::example[]
Observer<Long> observer = Observers.create({ item -> println("Timer fired!") })
Handler<Long> handler = observer.toHandler()
vertx.setTimer(1000, handler)
// end::example[]
