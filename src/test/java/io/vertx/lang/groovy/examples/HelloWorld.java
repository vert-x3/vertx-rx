package io.vertx.lang.groovy.examples;

import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HelloWorld {
  public static void main(String[] args) throws Exception {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle("groovy:io.vertx.lang.groovy.verticles.HelloWorldHttpVerticle");
    System.in.read();
  }
}
