package io.vertx.lang.reactivex;

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

import static io.vertx.codegen.type.ClassKind.VOID;

class RxJava2Generator extends AbstractRxGenerator {
  RxJava2Generator() {
    super("reactivex");
    this.kinds = Collections.singleton("class");
    this.name = "RxJava2";
  }

  @Override
  protected void genRxImports(ClassModel model, PrintWriter writer) {
    writer.println("import io.reactivex.Observable;");
    writer.println("import io.reactivex.Flowable;");
    writer.println("import io.reactivex.Single;");
    writer.println("import io.reactivex.Completable;");
    writer.println("import io.reactivex.Maybe;");
    super.genRxImports(model, writer);
  }

  @Override
  protected void genToObservable(ApiTypeInfo type, PrintWriter writer) {
    TypeInfo streamType = type.getReadStreamArg();
    writer.print("  private io.reactivex.Observable<");
    writer.print(genTypeName(streamType));
    writer.println("> observable;");

    writer.print("  private io.reactivex.Flowable<");
    writer.print(genTypeName(streamType));
    writer.println("> flowable;");

    writer.println();

    genToXXXAble(streamType, "Observable", "observable", writer);
    genToXXXAble(streamType, "Flowable", "flowable", writer);

  }

  private void genToXXXAble(TypeInfo streamType, String rxType, String rxName, PrintWriter writer) {
    writer.print("  public synchronized io.reactivex.");
    writer.print(rxType);
    writer.print("<");
    writer.print(genTypeName(streamType));
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
      writer.print(genTypeName(streamType));
      writer.print("> conv = ");
      writer.print(genTypeName(streamType.getRaw()));
      writer.println("::newInstance;");

      writer.print("      ");
      writer.print(rxName);
      writer.print(" = io.vertx.reactivex.");
      writer.print(rxType);
      writer.print("Helper.to");
      writer.print(rxType);
      writer.println("(delegate, conv);");
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
    writer.print("    return ");
    writer.print(rxName);
    writer.println(";");
    writer.println("  }");
    writer.println();
  }

//  private String genFutureMethodName(MethodInfo method) {
//    return "rx" + Character.toUpperCase(method.getName().charAt(0)) + method.getName().substring(1);
//  }

  @Override
  protected void genMethods(ClassModel model, MethodInfo method, List<String> cacheDecls, PrintWriter writer) {
    genMethod(model, method, cacheDecls, writer);
    MethodInfo flowableOverload = genOverloadedMethod(method, io.reactivex.Flowable.class);
    MethodInfo observableOverload = genOverloadedMethod(method, io.reactivex.Observable.class);
    if (flowableOverload != null) {
      genMethod(model, flowableOverload, cacheDecls, writer);
    }
    if (observableOverload != null) {
      genMethod(model, observableOverload, cacheDecls, writer);
    }
  }

  @Override
  protected void genRxMethod(ClassModel model, MethodInfo method, PrintWriter writer) {
    MethodInfo futMethod = genFutureMethod(method);
    ClassTypeInfo raw = futMethod.getReturnType().getRaw();
    String methodSimpleName = raw.getSimpleName();
    String adapterType = "io.vertx.reactivex.impl.AsyncResult" + methodSimpleName + ".to" + methodSimpleName;
    String rxType = raw.getName();
    startMethodTemplate(model.getType(), futMethod, "", writer);
    writer.println(" { ");
    writer.print("    return ");
    writer.print(adapterType);
    writer.println("(handler -> {");
    writer.print("      ");
    writer.print(method.getName());
    writer.print("(");
    List<ParamInfo> params = futMethod.getParams();
    writer.print(params.stream().map(ParamInfo::getName).collect(Collectors.joining(", ")));
    if (params.size() > 0) {
      writer.print(", ");
    }
    writer.println("handler);");
    writer.println("    });");
    writer.println("  }");
    writer.println();
  }

  protected void genReadStream(List<? extends TypeParamInfo> typeParams, PrintWriter writer){
    writer.print("  io.reactivex.Observable<");
    writer.print(typeParams.get(0).getName());
    writer.println("> toObservable();");
    writer.println();

    writer.print("  io.reactivex.Flowable<");
    writer.print(typeParams.get(0).getName());
    writer.println("> toFlowable();");
    writer.println();
  }

  private MethodInfo genFutureMethod(MethodInfo method) {
    String futMethodName = genFutureMethodName(method);
    List<ParamInfo> futParams = new ArrayList<>();
    int count = 0;
    int size = method.getParams().size() - 1;
    while (count < size) {
      ParamInfo param = method.getParam(count);
      /* Transform ReadStream -> Flowable */
      futParams.add(param);
      count = count + 1;
    }
    ParamInfo futParam = method.getParam(size);
    TypeInfo futType = ((ParameterizedTypeInfo) ((ParameterizedTypeInfo) futParam.getType()).getArg(0)).getArg(0);
    TypeInfo futUnresolvedType = ((ParameterizedTypeInfo) ((ParameterizedTypeInfo) futParam.getUnresolvedType()).getArg(0)).getArg(0);
    TypeInfo futReturnType;
    if (futUnresolvedType.getKind() == VOID) {
      futReturnType = io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.Completable.class);
    } else if (futUnresolvedType.isNullable()) {
      futReturnType = new io.vertx.codegen.type.ParameterizedTypeInfo(io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.Maybe.class).getRaw(), false, Collections.singletonList(futType));
    } else {
      futReturnType = new io.vertx.codegen.type.ParameterizedTypeInfo(io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.Single.class).getRaw(), false, Collections.singletonList(futType));
    }
    return method.copy().setName(futMethodName).setReturnType(futReturnType).setParams(futParams);
  }

  private MethodInfo genOverloadedMethod(MethodInfo method, Class streamType) {
    List<ParamInfo> params = null;
    int count = 0;
    for (ParamInfo param : method.getParams()) {
      if (param.getType().isParameterized() && param.getType().getRaw().getName().equals("io.vertx.core.streams.ReadStream")) {
        if (params == null) {
          params = new ArrayList<>(method.getParams());
        }
        ParameterizedTypeInfo paramType = new io.vertx.codegen.type.ParameterizedTypeInfo(
          io.vertx.codegen.type.TypeReflectionFactory.create(streamType).getRaw(),
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
}
