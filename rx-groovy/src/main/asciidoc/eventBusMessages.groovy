import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.eventbus.EventBus
import io.vertx.groovy.core.eventbus.Message
import io.vertx.groovy.core.eventbus.MessageConsumer
import rx.Subscription
import rx.Observable

Vertx vertx = Vertx.vertx()

// tag::example[]
EventBus eb = vertx.eventBus()
MessageConsumer<String> consumer = eb.<String>consumer("the-address")
Observable<Message<String>> observable = consumer.toObservable()
Subscription sub = observable.subscribe({ msg ->
  // Got message
});

// Unregisters the stream after 10 seconds
vertx.setTimer(10000, { id ->
  sub.unsubscribe()
});
// end::example[]
