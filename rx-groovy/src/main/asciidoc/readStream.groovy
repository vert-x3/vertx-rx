import io.vertx.groovy.core.Vertx

def vertx = Vertx.vertx()

// tag::example[]
def fs = vertx.fileSystem()
fs.open("/data.txt", [:], { result ->
  def file = result.result()
  def observable = file.toObservable()
  observable.forEach({ data -> println("Read data: ${data.toString("UTF-8")}") })
})
// end::example[]
