package io.vertx.lang.rx;

import io.vertx.codegen.ClassModel;
import io.vertx.codegen.MethodInfo;
import io.vertx.codegen.MethodKind;
import io.vertx.codegen.ParamInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeInfo;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base for RxJava1 and RxJava2 generators created in Vert.x 3
 */
public abstract class Vertx3RxGeneratorBase extends AbstractRxGenerator {

  public Vertx3RxGeneratorBase(String id) {
    super(id);
  }

  protected abstract MethodInfo genOverloadedMethod(MethodInfo method);

  protected abstract void genRxMethod(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer);

  @Override
  protected final void genMethods(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer) {
    genMethod(model, method, cacheDecls, genBody, writer);
    MethodInfo overload = genOverloadedMethod(method);
    if (overload != null) {
      genMethod(model, overload, cacheDecls, genBody, writer);
    }
  }

  private void genMethod(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer) {
    if (method.getKind() == MethodKind.FUTURE) {
      // Generate 3 methods
      // 1/ the handler based method: void WriteStream#end(Handler<AsyncResult<Void>>)
      // 2/ the fire and forget overload: void WriteStream#end()
      // 3/ the single: Completable WriteStream#rxEnd()

      genSimpleMethod(model, method, cacheDecls, genBody, writer);

      MethodInfo copy = method.copy();
      copy.getParams().remove(copy.getParams().size() - 1);
      Optional<MethodInfo> any = Stream.concat(model.getMethods().stream(), model.getAnyJavaTypeMethods().stream()).filter(m -> foo(m, copy)).findAny();
      if (!any.isPresent()) {
        startMethodTemplate(model.getType(), copy, "", writer);
        if (genBody) {
          writer.println(" {");
          writer.print("    ");
          if (!copy.getReturnType().isVoid()) {
            writer.println("return ");
          }
          writer.print(method.getName());
          writer.print("(");
          writer.print(copy.getParams().stream().map(ParamInfo::getName).collect(Collectors.joining(", ")));
          if (copy.getParams().size() > 0) {
            writer.print(", ");
          }
          writer.println("ar -> { });");
          writer.println("  }");
          writer.println();
        } else {
          writer.println(";");
          writer.println();
        }
      }

      genRxMethod(model, method, cacheDecls, genBody, writer);
    } else {
      genSimpleMethod(model, method, cacheDecls, genBody, writer);
    }
  }

  private boolean foo(MethodInfo m1, MethodInfo m2) {
    int numParams = m1.getParams().size();
    if (m1.getName().equals(m2.getName()) && numParams == m2.getParams().size()) {
      for (int index = 0; index < numParams; index++) {
        TypeInfo t1 = unwrap(m1.getParam(index).getType());
        TypeInfo t2 = unwrap(m2.getParam(index).getType());
        if (!t1.equals(t2)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private static TypeInfo unwrap(TypeInfo type) {
    if (type instanceof ParameterizedTypeInfo) {
      return type.getRaw();
    } else {
      return type;
    }
  }
}
