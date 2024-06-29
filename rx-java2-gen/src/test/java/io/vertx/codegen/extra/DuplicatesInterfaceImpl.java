package io.vertx.codegen.extra;

import io.vertx.codegen.extra.duplicates.SomeRandomType;

public class DuplicatesInterfaceImpl implements DuplicatesInterface {
  @Override
  public void abc(SomeRandomType arg) {
  }

  @Override
  public void def(io.vertx.codegen.extra.duplicates.nested.SomeRandomType arg) {
  }
}
