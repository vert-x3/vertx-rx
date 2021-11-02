package io.vertx.lang.rx;

import java.io.PrintWriter;

import io.vertx.codegen.ClassModel;
import io.vertx.codegen.MethodInfo;
import io.vertx.codegen.type.ClassTypeInfo;

public abstract class AbstractRxGenerator extends AbstractBaseVertxGenerator {

  public AbstractRxGenerator(String id) {
    super(id);
  }

  @Override
  protected void genImports(ClassModel model, PrintWriter writer) {
    writer.println("import java.util.Map;");
    writer.println("import java.util.Set;");
    writer.println("import java.util.List;");
    writer.println("import java.util.Iterator;");
    writer.println("import java.util.function.Function;");
    writer.println("import java.util.stream.Collectors;");
    writer.println("import io.vertx.core.Handler;");
    writer.println("import io.vertx.core.AsyncResult;");
    writer.println("import io.vertx.core.json.JsonObject;");
    writer.println("import io.vertx.core.json.JsonArray;");
    writer.println("import io.vertx.lang.rx.RxGen;");
    writer.println("import io.vertx.lang.rx.TypeArg;");
    writer.println("import io.vertx.lang.rx.MappingIterator;");
  }

  protected String genFutureMethodName(MethodInfo method) {
    return "rx" + Character.toUpperCase(method.getName().charAt(0)) + method.getName().substring(1);
  }

  @Override
  protected void generateExtraAnnotation(ClassModel model, PrintWriter writer, ClassTypeInfo type) {
    writer.print("@RxGen(");
    writer.print(type.getName());
    writer.println(".class)");
  }

}
