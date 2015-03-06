import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.file.AsyncFile
import io.vertx.rx.groovy.RxHelper
import rx.Observable

class MyPojo {
}

def vertx = Vertx.vertx()

// tag::example[]
def fileSystem = vertx.fileSystem()
fileSystem.open("/data.txt", [:], { result ->
  AsyncFile file = result.result()
  Observable<Buffer> observable = file.toObservable()
  observable.lift(RxHelper.unmarshaller(MyPojo.class)).subscribe({ mypojo ->
      // Process the object
  })
})
// end::example[]
