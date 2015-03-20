import io.vertx.groovy.core.Vertx
import rx.Observable
import rx.Scheduler

import java.util.concurrent.TimeUnit

def vertx = Vertx.vertx()
Observable<Integer> blockingObservable = null;

// tag::example[]
Scheduler scheduler = vertx.blockingScheduler();
Observable<Integer> obs = blockingObservable.observeOn(scheduler);
// end::example[]
