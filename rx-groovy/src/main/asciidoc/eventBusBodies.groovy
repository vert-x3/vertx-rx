import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.eventbus.EventBus
import io.vertx.groovy.core.eventbus.MessageConsumer
import rx.Observable;

Vertx vertx = Vertx.vertx()

// tag::example[]
EventBus eb = vertx.eventBus()
MessageConsumer<String> consumer = eb.<String>consumer("the-address")
Observable<String> observable = consumer.bodyStream().toObservable()
// end::example[]
