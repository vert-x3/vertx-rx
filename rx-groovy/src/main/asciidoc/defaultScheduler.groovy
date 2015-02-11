import io.vertx.groovy.core.Vertx
import io.vertx.rx.groovy.RxHelper
import rx.plugins.RxJavaPlugins
import rx.plugins.RxJavaSchedulersHook

import java.util.concurrent.TimeUnit;

def vertx = Vertx.vertx()

// tag::example[]
RxJavaSchedulersHook hook = RxHelper.schedulerHook(vertx)
RxJavaPlugins.getInstance().registerSchedulersHook(hook)
// end::example[]
