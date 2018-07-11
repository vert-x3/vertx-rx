package io.vertx.lang.rxjava;

import io.vertx.codegen.ClassModel;
import io.vertx.codegen.MethodInfo;
import io.vertx.codegen.ParamInfo;
import io.vertx.codegen.TypeParamInfo;
import io.vertx.codegen.type.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class RxJavaGenerator extends AbstractRxGenerator {
  RxJavaGenerator() {
    super("rxjava");
    this.name = "RxJava";
  }

  @Override
  protected void genRxImports(ClassModel model, PrintWriter writer) {
    writer.println("import rx.Observable;");
    writer.println("import rx.Single;");
    super.genRxImports(model, writer);
  }

  @Override
  protected void genToObservable(ApiTypeInfo type, PrintWriter writer) {
    TypeInfo streamType = type.getReadStreamArg();
    String simpleName = streamType.getSimpleName();
    writer.print("  private rx.Observable<");
    writer.print(simpleName);
    writer.println("> observable;");
    writer.println();

    writer.print("  public synchronized rx.Observable<");
    writer.print(simpleName);
    writer.println("> toObservable() {");

    writer.print("    ");
    writer.println("if (observable == null) {");

    if (streamType.getKind() == ClassKind.API) {
      writer.print("      java.util.function.Function<");
      writer.print(streamType.getName());
      writer.print(", ");
      writer.print(simpleName);
      writer.print("> conv = ");
      writer.print(streamType.getRaw().getSimpleName());
      writer.println("::newInstance;");

      writer.println("      observable = io.vertx.rx.java.RxHelper.toObservable(delegate, conv);");
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

      writer.println("      observable = io.vertx.rx.java.RxHelper.toObservable(delegate, conv);");
    } else {
      writer.println("      observable = io.vertx.rx.java.RxHelper.toObservable(this.getDelegate());");
    }

    writer.println("    }");
    writer.println("    return observable;");
    writer.println("  }");
    writer.println();
  }

  @Override
  protected void genMethods(ClassModel model, MethodInfo method, List<String> cacheDecls, PrintWriter writer) {
    genMethod(model, method, cacheDecls, writer);
    MethodInfo overloaded = genOverloadedMethod(method);
    if (overloaded != null) {
      genMethod(model, overloaded, cacheDecls, writer);
    }
  }

  @Override
  protected void genRxMethod(ClassModel model, MethodInfo method, PrintWriter writer) {
    ClassTypeInfo type = model.getType();
    writer.print("  ");
    MethodInfo futMethod = genFutureMethod(method);
    startMethodTemplate(type, futMethod, "", writer);
    writer.println(" { ");
    writer.println("    return Single.create(new io.vertx.rx.java.SingleOnSubscribeAdapter<>(fut -> {");
    writer.print("      ");
    writer.print(method.getName());
    writer.print("(");
    writer.print(futMethod.getParams().stream().map(ParamInfo::getName).collect(Collectors.joining(", ")));
    if (futMethod.getParams().size() > 0) {
      writer.print(", ");
    }
    writer.println("fut);");
    writer.println("    }));");
    writer.println("  }");
    writer.println();

  }

//  private String genFutureMethodName(MethodInfo method) {
//    return "rx" + Character.toUpperCase(method.getName().charAt(0)) + method.getName().substring(1);
//  }

  private MethodInfo genFutureMethod(MethodInfo method) {
    String futMethodName = genFutureMethodName(method);
    List<ParamInfo> futParams = new ArrayList<>();
    int count = 0;
    int size = method.getParams().size() - 1;
    while (count < size) {
      ParamInfo param = method.getParam(count);
      /* Transform ReadStream -> Observable */
      futParams.add(param);
      count = count + 1;
    }
    ParamInfo futParam = method.getParam(size);
    TypeInfo futType = ((ParameterizedTypeInfo) ((ParameterizedTypeInfo) futParam.getType()).getArg(0)).getArg(0);
    ParameterizedTypeInfo futReturnType = new io.vertx.codegen.type.ParameterizedTypeInfo(io.vertx.codegen.type.TypeReflectionFactory.create(rx.Single.class).getRaw(), false, java.util.Collections.singletonList(futType));
    return new io.vertx.codegen.MethodInfo(
      method.getOwnerTypes(), futMethodName,
      method.getKind(),
      futReturnType,
      null,
      method.isFluent(),
      method.isCacheReturn(),
      futParams,
      method.getComment(),
      method.getDoc(),
      method.isStaticMethod(),
      method.isDefaultMethod(),
      method.getTypeParams(),
      method.isDeprecated());
  }

  private MethodInfo genOverloadedMethod(MethodInfo method) {
    List<ParamInfo> params = null;
    int count = 0;
    for (ParamInfo param : method.getParams()) {
      if (param.getType().isParameterized() && param.getType().getRaw().getName().equals("io.vertx.core.streams.ReadStream")) {
        if (params == null) {
          params = new java.util.ArrayList<>(method.getParams());
        }
        ParameterizedTypeInfo paramType = new io.vertx.codegen.type.ParameterizedTypeInfo(
          io.vertx.codegen.type.TypeReflectionFactory.create(rx.Observable.class).getRaw(),
          false,
          java.util.Collections.singletonList(((ParameterizedTypeInfo) param.getType()).getArg(0))
        );
        params.set(count, new io.vertx.codegen.ParamInfo(
          param.getIndex(),
          param.getName(),
          param.getDescription(),
          paramType
        ));
      }
      count = count + 1;
    }
    if (params != null) {
      return new io.vertx.codegen.MethodInfo(
        method.getOwnerTypes(),
        method.getName(),
        method.getKind(),
        method.getReturnType(),
        null,
        method.isFluent(),
        method.isCacheReturn(),
        params,
        method.getComment(),
        method.getDoc(),
        method.isStaticMethod(),
        method.isDefaultMethod(),
        method.getTypeParams(),
        method.isDeprecated());
    }
    return null;
  }

  protected void genReadStream(List<? extends TypeParamInfo> typeParams, PrintWriter writer) {
    writer.print("  rx.Observable<");
    writer.print(typeParams.get(0).getName());
    writer.println("> toObservable();");
    writer.println();
  }
}
