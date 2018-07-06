package io.vertx.lang.reactivex;

import io.vertx.codegen.ClassModel;
import io.vertx.codegen.MethodInfo;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ClassKind;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.lang.rxjava.AbstractRxGenerator;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

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

  @Override
  protected void genToObservable(ApiTypeInfo type, PrintWriter writer) {
    TypeInfo streamType = type.getReadStreamArg();
    String simpleName = streamType.getSimpleName();
    writer.print("  private io.reactivex.Observable<");
    writer.print(simpleName);
    writer.print("> observable;");

    writer.print("  private io.reactivex.Flowable<");
    writer.print(simpleName);
    writer.print("> flowable;");

    genToXXXAble(streamType, "Observable", "observable", writer);
    genToXXXAble(streamType, "Flowable", "flowable", writer);

  }

  private void genToXXXAble(TypeInfo streamType, String rxType, String rxName, PrintWriter writer){
    String simpleName = streamType.getSimpleName();
    writer.print("  public synchronized io.reactivex.");
    writer.print(rxType);
    writer.print("<");
    writer.print(simpleName);
    writer.print("> to");
    writer.print(rxType);
    writer.println("() {");

    writer.print("    ");
    writer.print("if (");
    writer.print(rxName);
    writer.println(" == null) {");

    if (streamType.getKind() == ClassKind.API) {
      writer.print("      java.util.function.Function<");
      writer.print(streamType.getName());
      writer.print(", ");
      writer.print(simpleName);
      writer.print("> conv = ");
      writer.print(simpleName);
      writer.println("::newInstance;");

      writer.print("      ");
      writer.print(rxName);
      writer.print(" = io.vertx.reactivex.");
      writer.print(rxType);
      writer.print("Helper.to");
      writer.print(rxType);
      writer.println("(delegate, conv);");
    } else if (streamType.isVariable()) {
      writer.print("      java.util.function.Function<");
      writer.print(simpleName);
      writer.print(", ");
      writer.print(simpleName);
      writer.print("> conv = (java.util.function.Function<");
      writer.print(simpleName);
      writer.print(", ");
      writer.print(simpleName);
      writer.println(">) __typeArg_0.wrap;");

      writer.print("      ");
      writer.print(rxName);
      writer.print(" = io.vertx.reactivex.");
      writer.print(rxType);
      writer.print("Helper.to");
      writer.print(rxType);
      writer.println("(delegate, conv);");
    } else {
      writer.print("      ");
      writer.print(rxName);
      writer.print(" = io.vertx.reactivex.");
      writer.print(rxType);
      writer.print("Helper.to");
      writer.print(rxType);
      writer.println("(this.getDelegate());");
    }

    writer.println("    }");
    writer.print("return ");
    writer.print(rxName);
    writer.println(";");
    writer.println("}");
    writer.println();
  }

  @Override
  protected void genMethods(ClassModel model, MethodInfo method, List<String> cacheDecls, PrintWriter writer) {

  }

  @Override
  protected void genRxMethod(ClassModel model, MethodInfo method, PrintWriter writer) {

  }
}
