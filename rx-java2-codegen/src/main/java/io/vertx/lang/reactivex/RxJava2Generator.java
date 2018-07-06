package io.vertx.lang.reactivex;

import io.vertx.codegen.ClassModel;
import io.vertx.lang.rxjava.AbstractRxGenerator;

import java.io.PrintWriter;
import java.util.Collections;

class RxJava2Generator extends AbstractRxGenerator {
  RxJava2Generator() {
    super("reactivex");
    this.kinds = Collections.singleton("class");
    this.name = "RxJava2";
  }

  @Override
  protected void generateImport(ClassModel model, PrintWriter writer) {
    writer.println("import io.reactivex.Observable;");
    writer.println("import io.reactivex.Flowable;");
    writer.println("import io.reactivex.Single;");
    writer.println("import io.reactivex.Completable;");
    writer.println("import io.reactivex.Maybe;");
    super.generateImport(model, writer);
  }
}
