import io.vertx.groovy.core.Vertx
import rx.Observable

import java.util.concurrent.TimeUnit

def vertx = Vertx.vertx()

// tag::example[]
Observable<Long> timer = Observable.timer(100, 100, TimeUnit.MILLISECONDS, vertx.scheduler())
// end::example[]
