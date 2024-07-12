package io.vertx.lang.rx;

import io.vertx.codegen.processor.ClassModel;
import io.vertx.codegen.processor.MethodInfo;
import io.vertx.codegen.processor.MethodKind;
import io.vertx.codegen.processor.ParamInfo;
import io.vertx.codegen.processor.type.ParameterizedTypeInfo;
import io.vertx.codegen.processor.type.TypeInfo;

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

    // Generate up to 3 methods
    // - the regular methods
    // - the handler based + fire & forget overload + single version, e.g void WriteStream#end(Handler<AsyncResult<Void>>) / void WriteStream#end() / Completable end()
    // - the future base version + single version, e.g Future<Void> end() / Completable end()

    genSimpleMethod("public", model, method, cacheDecls, genBody, writer);

    if (method.getKind() == MethodKind.OTHER || method.getKind() == MethodKind.HANDLER) {
      return;
    }

    genRxMethod(model, method, cacheDecls, genBody, writer);
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
