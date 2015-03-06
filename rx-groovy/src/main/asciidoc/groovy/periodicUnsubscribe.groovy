import io.vertx.groovy.core.Vertx
import rx.Subscriber

Vertx vertx = Vertx.vertx()

// tag::example[]
vertx.periodicStream(1000).
    toObservable().
    subscribe(new Subscriber<Long>() {
      public void onNext(Long aLong) {
        // Callback
        unsubscribe()
      }
      public void onError(Throwable e) {}
      public void onCompleted() {}
    })
// end::example[]
