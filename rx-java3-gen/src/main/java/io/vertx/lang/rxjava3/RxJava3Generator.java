package io.vertx.lang.rxjava3;

import io.reactivex.rxjava3.core.Flowable;
import io.vertx.codegen.*;
import io.vertx.codegen.type.ClassKind;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.lang.rx.AbstractRxGenerator;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.vertx.codegen.type.ClassKind.VOID;

class RxJava3Generator extends AbstractRxGenerator {
  RxJava3Generator() {
    super("rxjava3");
    this.kinds = Collections.singleton("class");
    this.name = "RxJava3";
  }

  @Override
  protected void genImports(ClassModel model, PrintWriter writer) {
    writer.println("import io.vertx.rxjava3.RxHelper;");
    writer.println("import io.vertx.rxjava3.ObservableHelper;");
    writer.println("import io.vertx.rxjava3.FlowableHelper;");
    writer.println("import io.vertx.rxjava3.impl.AsyncResultMaybe;");
    writer.println("import io.vertx.rxjava3.impl.AsyncResultSingle;");
    writer.println("import io.vertx.rxjava3.impl.AsyncResultCompletable;");
    writer.println("import io.vertx.rxjava3.WriteStreamObserver;");
    writer.println("import io.vertx.rxjava3.WriteStreamSubscriber;");
    super.genImports(model, writer);
  }

  @Override
  protected void genToObservable(TypeInfo streamType, PrintWriter writer) {
    writer.print("  private io.reactivex.rxjava3.core.Observable<");
    writer.print(genTranslatedTypeName(streamType));
    writer.println("> observable;");

    writer.print("  private io.reactivex.rxjava3.core.Flowable<");
    writer.print(genTranslatedTypeName(streamType));
    writer.println("> flowable;");

    writer.println();

    genToXXXAble(streamType, "Observable", "observable", writer);
    genToXXXAble(streamType, "Flowable", "flowable", writer);
  }

  private void genToXXXAble(TypeInfo streamType, String rxType, String rxName, PrintWriter writer) {
    writer.print("  public synchronized ");
    writer.print("io.reactivex.rxjava3.core.");
    writer.print(rxType);
    writer.print("<");
    writer.print(genTranslatedTypeName(streamType));
    writer.print("> to");
    writer.print(rxType);
    writer.println("() {");

    writer.print("    ");
    writer.print("if (");
    writer.print(rxName);
    writer.println(" == null) {");

    if (streamType.getKind() == ClassKind.API) {
      writer.print("      Function<");
      writer.print(streamType.getName());
      writer.print(", ");
      writer.print(genTranslatedTypeName(streamType));
      writer.print("> conv = ");
      writer.print(genTranslatedTypeName(streamType.getRaw()));
      writer.println("::newInstance;");

      writer.print("      ");
      writer.print(rxName);
      writer.print(" = ");
      writer.print(rxType);
      writer.print("Helper.to");
      writer.print(rxType);
      writer.println("(delegate, conv);");
    } else if (streamType.isVariable()) {
      String typeVar = streamType.getSimpleName();
      writer.print("      Function<");
      writer.print(typeVar);
      writer.print(", ");
      writer.print(typeVar);
      writer.print("> conv = (Function<");
      writer.print(typeVar);
      writer.print(", ");
      writer.print(typeVar);
      writer.println(">) __typeArg_0.wrap;");

      writer.print("      ");
      writer.print(rxName);
      writer.print(" = ");
      writer.print(rxType);
      writer.print("Helper.to");
      writer.print(rxType);
      writer.println("(delegate, conv);");
    } else {
      writer.print("      ");
      writer.print(rxName);
      writer.print(" = ");
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

  @Override
  protected void genToSubscriber(TypeInfo streamType, PrintWriter writer) {
    writer.format("  private WriteStreamObserver<%s> observer;%n", genTranslatedTypeName(streamType));
    writer.format("  private WriteStreamSubscriber<%s> subscriber;%n", genTranslatedTypeName(streamType));
    writer.println();
    genToXXXEr(streamType, "Observer", "observer", writer);
    genToXXXEr(streamType, "Subscriber", "subscriber", writer);
  }

  private void genToXXXEr(TypeInfo streamType, String rxType, String rxName, PrintWriter writer) {
    writer.format("  public synchronized WriteStream%s<%s> to%s() {%n", rxType, genTranslatedTypeName(streamType), rxType);
    writer.format("    if (%s == null) {%n", rxName);
    if (streamType.getKind() == ClassKind.API) {
      writer.format("      Function<%s, %s> conv = %s::getDelegate;%n", genTranslatedTypeName(streamType.getRaw()), streamType.getName(), genTranslatedTypeName(streamType));
      writer.format("      %s = RxHelper.to%s(getDelegate(), conv);%n", rxName, rxType);
    } else if (streamType.isVariable()) {
      String typeVar = streamType.getSimpleName();
      writer.format("      Function<%s, %s> conv = (Function<%s, %s>) __typeArg_0.unwrap;%n", typeVar, typeVar, typeVar, typeVar);
      writer.format("      %s = RxHelper.to%s(getDelegate(), conv);%n", rxName, rxType);
    } else {
      writer.format("      %s = RxHelper.to%s(getDelegate());%n", rxName, rxType);
    }
    writer.println("    }");
    writer.format("    return %s;%n", rxName);
    writer.println("  }");
    writer.println();
  }

  @Override
  protected void genMethods(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer) {
    if (method.getName().equals("authenticate") && method.getParams().size() >= 1 && method.getParam(0).getType().getSimpleName().equals("Credentials") && method.isDeprecated()) {
      // Work around for now
      return;
    }
    if (method.getKind() == MethodKind.FUTURE) {
      genRxMethod(model, method, genBody, writer);
      genLazyRxMethod(model, method, genBody, writer);
    } else {
      genSimpleMethod("public", model, method, cacheDecls, genBody, writer);
    }
  }

  private void genRxMethod(ClassModel model, MethodInfo method, boolean genBody, PrintWriter writer) {
    MethodInfo futMethod = genFutureMethod(method);
    startMethodTemplate("public", model.getType(), futMethod, "", writer);
    if (genBody) {
      String rxName = genFutureMethodName(method);
      writer.println(" { ");
      writer.print("    ");
      writer.print(genReturnTypeDecl(futMethod.getReturnType()));
      writer.print(" ret = ");
      writer.print(rxName);
      writer.print("(");
      List<ParamInfo> params = futMethod.getParams();
      writer.print(params.stream().map(ParamInfo::getName).collect(Collectors.joining(", ")));
      writer.println(");");
      writer.println("    ret = ret.cache();");
      writer.print("    ret.subscribe(io.vertx.rxjava3.");
      writer.print(futMethod.getReturnType().getRaw().getSimpleName());
      writer.println("Helper.nullObserver());");
      writer.println("    return ret;");
      writer.println("  }");
    } else {
      writer.println(";");
    }
    writer.println();
  }

  private void genLazyRxMethod(ClassModel model, MethodInfo method, boolean genBody, PrintWriter writer) {
    MethodInfo futMethod = genFutureMethod(method);
    futMethod.setName(genFutureMethodName(futMethod));
    ClassTypeInfo raw = futMethod.getReturnType().getRaw();
    String methodSimpleName = raw.getSimpleName();
    String adapterType = "AsyncResult" + methodSimpleName + ".to" + methodSimpleName;
    startMethodTemplate("public", model.getType(), futMethod, "", writer);
    if (genBody) {
      writer.println(" {");
      if (method.getKind() == MethodKind.FUTURE) {
        writer.print("    return ");
        writer.print(adapterType);
        writer.print("(() -> ");
        writer.print(genInvokeDelegate(model, method));
        if (!futMethod.getReturnType().getSimpleName().equals("Completable")) {
          writer.print(", __value -> ");
          TypeInfo asyncType = ((ParameterizedTypeInfo) method.getReturnType()).getArg(0);
          writer.print(genConvReturn(asyncType, method, "__value"));
        }
        writer.println(");");
      } else {
        writer.print("    return ");
        writer.print(adapterType);
        writer.print("( ");
        writer.print(method.getParam(futMethod.getParams().size()).getName());
        writer.println(" -> {");
        writer.print("      ");
        writer.print(genInvokeDelegate(model, method));
        writer.println(";");
        writer.println("    });");
      }
      writer.println("  }");
    } else {
      writer.println(";");
    }
    writer.println();
  }

  protected void genReadStream(List<? extends TypeParamInfo> typeParams, PrintWriter writer){
    writer.print("  io.reactivex.rxjava3.core.Observable<");
    writer.print(typeParams.get(0).getName());
    writer.println("> toObservable();");
    writer.println();
    writer.print("  io.reactivex.rxjava3.core.Flowable<");
    writer.print(typeParams.get(0).getName());
    writer.println("> toFlowable();");
    writer.println();
  }

  @Override
  protected void genWriteStream(List<? extends TypeParamInfo> typeParams, PrintWriter writer) {
    writer.print("  WriteStreamObserver<");
    writer.print(typeParams.get(0).getName());
    writer.println("> toObserver();");
    writer.println();
    writer.print("  WriteStreamSubscriber<");
    writer.print(typeParams.get(0).getName());
    writer.println("> toSubscriber();");
    writer.println();
  }

  private TypeInfo rewriteParamType(TypeInfo type) {
    if (type.isParameterized()) {
      if (type.getRaw().getName().equals("io.vertx.core.streams.ReadStream")) {
        return new io.vertx.codegen.type.ParameterizedTypeInfo(
          io.vertx.codegen.type.TypeReflectionFactory.create(Flowable.class).getRaw(),
          false,
          java.util.Collections.singletonList(((ParameterizedTypeInfo) type).getArg(0))
        );
      } else if (type.getKind() == ClassKind.FUTURE) {
        TypeInfo futType = ((ParameterizedTypeInfo) type).getArg(0);
        if (futType.getKind() == VOID) {
          return io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.rxjava3.core.Completable.class);
        } else if (futType.isNullable()) {
          return new io.vertx.codegen.type.ParameterizedTypeInfo(io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.rxjava3.core.Maybe.class).getRaw(), false, Collections.singletonList(futType));
        } else {
          return new io.vertx.codegen.type.ParameterizedTypeInfo(io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.rxjava3.core.Single.class).getRaw(), false, Collections.singletonList(futType));
        }
      } else if (type.getKind() == ClassKind.FUNCTION) {
        ParameterizedTypeInfo functionType = (ParameterizedTypeInfo) type;
        TypeInfo argType = functionType.getArg(0); // Return not param
        TypeInfo retType = rewriteParamType(functionType.getArg(1));
        if (argType != functionType.getArg(0) || retType != functionType.getArg(1)) {
          return new ParameterizedTypeInfo(
            functionType.getRaw(),
            functionType.isNullable(),
            Arrays.asList(argType, retType));
        }
      } else if (type.getKind() == ClassKind.SUPPLIER) {
        ParameterizedTypeInfo supplierType = (ParameterizedTypeInfo) type;
        TypeInfo retType = rewriteParamType(supplierType.getArg(0));
        if (retType != supplierType.getArg(0)) {
          return new ParameterizedTypeInfo(
            supplierType.getRaw(),
            supplierType.isNullable(),
            Arrays.asList(retType));
        }
      }
    }
    return type;
  }

  @Override
  protected String genParamTypeDecl(TypeInfo type) {
    return super.genParamTypeDecl(rewriteParamType(type));
  }

  @Override
  protected String genConvParam(TypeInfo type, MethodInfo method, String expr) {
    if (type.isParameterized()) {
      if (type.getRaw().getName().equals("io.vertx.core.streams.ReadStream")) {
        ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) type;
        String adapterFunction = "obj -> " + genConvParam(parameterizedType.getArg(0), method, "obj");
        return "io.vertx.rxjava3.impl.ReadStreamSubscriber.asReadStream(" + expr + ", " + adapterFunction + ").resume()";
      } else if (type.getKind() == ClassKind.FUTURE) {
        TypeInfo futType = ((ParameterizedTypeInfo) type).getArg(0);
        if (futType.getKind() == VOID) {
          return "io.vertx.rxjava3.CompletableHelper.toFuture(" + expr + ")";
        } else if (futType.isNullable()) {
          ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) type;
          String adapterFunction = "obj -> " + genConvParam(parameterizedType.getArg(0), method, "obj");
          return "io.vertx.rxjava3.MaybeHelper.toFuture(" + expr + ", " + adapterFunction + ")";
        } else {
          ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) type;
          String adapterFunction = "obj -> " + genConvParam(parameterizedType.getArg(0), method, "obj");
          return "io.vertx.rxjava3.SingleHelper.toFuture(" + expr + ", " + adapterFunction + ")";
        }
      }
    }
    return super.genConvParam(type, method, expr);
  }

  private MethodInfo genFutureMethod(MethodInfo method) {
    List<ParamInfo> futParams;
    TypeInfo futType;
    TypeInfo futUnresolvedType;
    if (method.getKind() == MethodKind.FUTURE) {
      futParams = new ArrayList<>(method.getParams());
      futType = ((ParameterizedTypeInfo) method.getReturnType()).getArg(0);
      futUnresolvedType = futType;
    } else {
      futParams = new ArrayList<>();
      int count = 0;
      int size = method.getParams().size() - 1;
      while (count < size) {
        ParamInfo param = method.getParam(count);
        /* Transform ReadStream -> Flowable */
        futParams.add(param);
        count = count + 1;
      }
      ParamInfo futParam = method.getParam(size);
      futType = ((ParameterizedTypeInfo) ((ParameterizedTypeInfo) futParam.getType()).getArg(0)).getArg(0);
      futUnresolvedType = ((ParameterizedTypeInfo) ((ParameterizedTypeInfo) futParam.getUnresolvedType()).getArg(0)).getArg(0);
    }
    TypeInfo futReturnType;
    if (futUnresolvedType.getKind() == VOID) {
      futReturnType = io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.rxjava3.core.Completable.class);
    } else if (futUnresolvedType.isNullable()) {
      futReturnType = new io.vertx.codegen.type.ParameterizedTypeInfo(io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.rxjava3.core.Maybe.class).getRaw(), false, Collections.singletonList(futType));
    } else {
      futReturnType = new io.vertx.codegen.type.ParameterizedTypeInfo(io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.rxjava3.core.Single.class).getRaw(), false, Collections.singletonList(futType));
    }
    return method.copy().setReturnType(futReturnType).setParams(futParams);
  }
}
