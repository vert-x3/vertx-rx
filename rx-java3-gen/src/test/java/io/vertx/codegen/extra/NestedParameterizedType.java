package io.vertx.codegen.extra;

import io.vertx.codegen.annotations.VertxGen;

@VertxGen
public interface NestedParameterizedType {

  static Generic2<Generic1<Foo>, Generic2<Foo, Bar>> someGeneric() {
    return new Generic2.Impl<Generic1<Foo>, Generic2<Foo, Bar>>()
      .setValue1(new Generic1.Impl<Foo>().setValue(new Foo.Impl()))
      .setValue2(new Generic2.Impl<Foo, Bar>().setValue1(new Foo.Impl()).setValue2(new Bar.Impl()));
  }
}
