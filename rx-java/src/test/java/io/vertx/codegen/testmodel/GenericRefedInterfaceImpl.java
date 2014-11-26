package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.VertxGen;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GenericRefedInterfaceImpl<T> implements GenericRefedInterface<T> {

  private T value;

  @Override
  public void setValue(T value) {
    this.value = value;
  }

  @Override
  public T getValue() {
    return value;
  }
}
