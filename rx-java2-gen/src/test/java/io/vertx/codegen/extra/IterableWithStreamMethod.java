package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.vertx.codegen.annotations.GenIgnore.PERMITTED_TYPE;

@VertxGen
public interface IterableWithStreamMethod extends Iterable<Foo> {
  @GenIgnore
  class Impl implements IterableWithStreamMethod {
    @Override
    public Iterator<Foo> iterator() {
      return Arrays.<Foo>asList(new Foo.Impl(), new Foo.Impl()).iterator();
    }
  }

  @GenIgnore(PERMITTED_TYPE)
  default Stream<Foo> stream() {
    return StreamSupport.stream(spliterator(), false);
  }
}
