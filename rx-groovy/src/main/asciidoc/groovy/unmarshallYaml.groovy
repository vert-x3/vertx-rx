import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
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
def mapper = new ObjectMapper(new YAMLFactory())
fileSystem.open("/data.txt", [:], { result ->
  AsyncFile file = result.result()
  Observable<Buffer> observable = file.toObservable()
  observable.lift(RxHelper.unmarshaller(MyPojo.class, mapper)).subscribe({ mypojo ->
      // Process the object
  })
})
// end::example[]
