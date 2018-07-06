package io.vertx.lang.rxjava;

import io.vertx.codegen.ClassModel;
import io.vertx.codegen.ModuleInfo;

import java.io.PrintWriter;
import java.util.Collections;

class RxJavaGenerator extends AbstractRxGenerator {
  RxJavaGenerator() {
    super("rxjava");
    this.name = "RxJava";
  }

  @Override
  protected void generateImport(ClassModel model, PrintWriter writer) {
    writer.println("import rx.Observable;");
    writer.println("import rx.Single;");
    super.generateImport(model, writer);
  }
}
