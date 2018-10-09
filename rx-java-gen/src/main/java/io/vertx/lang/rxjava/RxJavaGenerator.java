package io.vertx.lang.rxjava;

import io.vertx.codegen.ClassModel;
import io.vertx.codegen.MethodInfo;
import io.vertx.codegen.ParamInfo;
import io.vertx.codegen.TypeParamInfo;
import io.vertx.codegen.type.*;
import io.vertx.lang.rx.AbstractRxGenerator;

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
    writer.print("  private rx.Observable<");
    writer.print(genTypeName(streamType));
    writer.println("> observable;");
    writer.println();

    writer.print("  public synchronized rx.Observable<");
    writer.print(genTypeName(streamType));
    writer.println("> toObservable() {");

    writer.print("    ");
    writer.println("if (observable == null) {");

    if (streamType.getKind() == ClassKind.API) {
      writer.print("      java.util.function.Function<");
      writer.print(streamType.getName());
      writer.print(", ");
      writer.print(genTypeName(streamType));
      writer.print("> conv = ");
      writer.print(genTypeName(streamType.getRaw()));
      writer.println("::newInstance;");

      writer.println("      observable = io.vertx.rx.java.RxHelper.toObservable(delegate, conv);");
    } else if (streamType.isVariable()) {
      String typeVar = streamType.getSimpleName();
      writer.print("      java.util.function.Function<");
      writer.print(typeVar);
      writer.print(", ");
      writer.print(typeVar);
      writer.print("> conv = (java.util.function.Function<");
      writer.print(typeVar);
      writer.print(", ");
      writer.print(typeVar);
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
    String packageName = type.getPackageName();
    writer.print("  ");
    if (packageName.startsWith("io.vertx.codegen") ||
      packageName.startsWith("io.vertx.core") ||
      packageName.startsWith("io.vertx.ext.web") ||
      packageName.startsWith("io.vertx.ext.sql") ||
      packageName.startsWith("io.vertx.ext.jdbc") ||
      packageName.startsWith("io.vertx.ext.mongo") ||
      packageName.startsWith("io.vertx.ext.auth") ||
      packageName.startsWith("io.vertx.ext.jwt") ||
      packageName.startsWith("io.vertx.redis") ||
      packageName.startsWith("io.vertx.ext.mail") ||
      packageName.startsWith("io.vertx.ext.asyncsql") ||
      packageName.startsWith("io.vertx.ext.stomp") ||
      packageName.startsWith("io.vertx.ext.shell") ||
      packageName.startsWith("io.vertx.ext.dropwizard") ||
      packageName.startsWith("io.vertx.amqpbridge") ||
      packageName.startsWith("io.vertx.rabbitmq") ||
      packageName.startsWith("io.vertx.ext.unit")) {
      List<ParamInfo> futureParams = new ArrayList<>(method.getParams());
      ParamInfo futureParam = futureParams.remove(futureParams.size() - 1);
      ParameterizedTypeInfo handlerType = (ParameterizedTypeInfo) futureParam.getType();
      ParameterizedTypeInfo asyncResultType = (ParameterizedTypeInfo) handlerType.getArg(0);
      TypeInfo futureType = asyncResultType.getArg(0);
      ParameterizedTypeInfo futureReturnType = new ParameterizedTypeInfo(TypeReflectionFactory.create(rx.Observable.class).getRaw(), false, Collections.singletonList(futureType));
      MethodInfo futureMethod = method.copy().setName(method.getName() + "Observable").setParams(futureParams).setReturnType(futureReturnType);
      startMethodTemplate(type, futureMethod, "use {@link #" + genFutureMethodName(method) + "} instead", writer);
      writer.println(" { ");
      writer.print("    io.vertx.rx.java.ObservableFuture<");
      writer.print(genTypeName(futureType));
      writer.print("> ");
      writer.print(futureParam.getName());
      writer.println(" = io.vertx.rx.java.RxHelper.observableFuture();");
      writer.print("    ");
      writer.print(method.getName());
      writer.print("(");
      writer.print(futureParams.stream().map(ParamInfo::getName).collect(Collectors.joining(", ")));
      if (futureParams.size() > 0) {
        writer.print(", ");
      }
      writer.print(futureParam.getName());
      writer.println(".toHandler());");
      writer.print("    return ");
      writer.print(futureParam.getName());
      writer.println(";");
      writer.println("  }");
      writer.println();
    }
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
    return method.copy().setName(futMethodName).setParams(futParams).setReturnType(futReturnType);
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
      return method.copy().setParams(params);
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
