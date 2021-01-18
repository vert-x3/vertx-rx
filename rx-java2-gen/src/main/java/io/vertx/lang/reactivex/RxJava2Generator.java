package io.vertx.lang.reactivex;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.vertx.codegen.ClassModel;
import io.vertx.codegen.MethodInfo;
import io.vertx.codegen.ParamInfo;
import io.vertx.codegen.TypeParamInfo;
import io.vertx.codegen.type.ClassKind;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.lang.rx.Vertx3RxGeneratorBase;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.vertx.codegen.type.ClassKind.VOID;

class RxJava2Generator extends Vertx3RxGeneratorBase {
  RxJava2Generator() {
    super("reactivex");
    this.kinds = Collections.singleton("class");
    this.name = "RxJava2";
  }

  @Override
  protected void genImports(ClassModel model, PrintWriter writer) {
    writer.println("import io.vertx.reactivex.RxHelper;");
    writer.println("import io.vertx.reactivex.ObservableHelper;");
    writer.println("import io.vertx.reactivex.FlowableHelper;");
    writer.println("import io.vertx.reactivex.impl.AsyncResultMaybe;");
    writer.println("import io.vertx.reactivex.impl.AsyncResultSingle;");
    writer.println("import io.vertx.reactivex.impl.AsyncResultCompletable;");
    writer.println("import io.vertx.reactivex.WriteStreamObserver;");
    writer.println("import io.vertx.reactivex.WriteStreamSubscriber;");
    super.genImports(model, writer);
  }

  @Override
  protected void genToObservable(TypeInfo streamType, PrintWriter writer) {
    writer.print("  private io.reactivex.Observable<");
    writer.print(genTranslatedTypeName(streamType));
    writer.println("> observable;");

    writer.print("  private io.reactivex.Flowable<");
    writer.print(genTranslatedTypeName(streamType));
    writer.println("> flowable;");

    writer.println();

    genToXXXAble(streamType, "Observable", "observable", writer);
    genToXXXAble(streamType, "Flowable", "flowable", writer);
  }

  private void genToXXXAble(TypeInfo streamType, String rxType, String rxName, PrintWriter writer) {
    writer.print("  public synchronized ");
    writer.print("io.reactivex.");
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
  protected void genRxMethod(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer) {
    MethodInfo futMethod = genFutureMethod(method);
    ClassTypeInfo raw = futMethod.getReturnType().getRaw();
    String methodSimpleName = raw.getSimpleName();
    String adapterType = "AsyncResult" + methodSimpleName + ".to" + methodSimpleName;
    String rxType = raw.getName();
    startMethodTemplate(model.getType(), futMethod, "", writer);
    if (genBody) {
      writer.println(" { ");
      writer.print("    return ");
      writer.print(adapterType);
      writer.println("($handler -> {");
      writer.print("      ");
      writer.print(method.getName());
      writer.print("(");
      List<ParamInfo> params = futMethod.getParams();
      writer.print(params.stream().map(ParamInfo::getName).collect(Collectors.joining(", ")));
      if (params.size() > 0) {
        writer.print(", ");
      }
      writer.println("$handler);");
      writer.println("    });");
      writer.println("  }");
    } else {
      writer.println(";");
    }
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

  @Override
  protected String genTypeName(TypeInfo type, boolean translate) {
    if (!translate) {
      if (type.isParameterized()) {
        String rawTypeName = type.getRaw().getName();
        if (rawTypeName.equals("io.reactivex.Single") || rawTypeName.equals("io.reactivex.Maybe")) {
          ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) type;
          return "io.vertx.core.Future<" + super.genTypeName(parameterizedType.getArg(0), translate) + ">";
        }
      } else if (type.getName().equals("io.reactivex.Completable")) {
        return "io.vertx.core.Future<Void>";
      }
    }
    return super.genTypeName(type, translate);
  }

  @Override
  protected String genConvParam(TypeInfo type, MethodInfo method, String expr) {
    if (type.isParameterized() && (type.getRaw().getName().equals("io.reactivex.Flowable") || type.getRaw().getName().equals("io.reactivex.Observable"))) {
      ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) type;
      String adapterFunction = "obj -> " + genConvParam(parameterizedType.getArg(0), method, "obj");
      return "io.vertx.reactivex.impl.ReadStreamSubscriber.asReadStream(" + expr + ", " + adapterFunction + ").resume()";
    } else if (type.isParameterized() && (type.getRaw().getName().equals("io.reactivex.Single"))) {
      ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) type;
      String adapterFunction = "obj -> " + genConvParam(parameterizedType.getArg(0), method, "obj");
      return "io.vertx.reactivex.SingleHelper.toFuture(" + expr + ", " + adapterFunction + ")";
    } else if (type.isParameterized() && (type.getRaw().getName().equals("io.reactivex.Maybe"))) {
      ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) type;
      String adapterFunction = "obj -> " + genConvParam(parameterizedType.getArg(0), method, "obj");
      return "io.vertx.reactivex.MaybeHelper.toFuture(" + expr + ", " + adapterFunction + ")";
    } else if ((type.getName().equals("io.reactivex.Completable"))) {
      return "io.vertx.reactivex.CompletableHelper.toFuture(" + expr + ")";
    } else if (type.isParameterized() && type.getRaw().getName().equals("io.reactivex.functions.Function")) {
      ParameterizedTypeInfo parameterizedTypeInfo = (ParameterizedTypeInfo) type;
      TypeInfo argType = parameterizedTypeInfo.getArg(0);
      TypeInfo retType = parameterizedTypeInfo.getArg(1);
      return "new Function<" + genTypeName(argType) + "," + genTypeName(retType) + ">() {\n" +
        "      public " + genTypeName(retType) + " apply(" + genTypeName(argType) + " arg) {\n" +
        "        " + genTranslatedTypeName(retType) + " ret;\n" +
        "        try {\n" +
        "          ret = " + expr + ".apply(" + genConvReturn(argType, method, "arg") + ");\n" +
        "        } catch (Exception e) {\n" +
        "          return io.vertx.core.Future.failedFuture(e);\n" +
        "        }\n" +
        "        return " + genConvParam(retType, method, "ret") + ";\n" +
        "      }\n" +
        "    }";
    } else {
      return super.genConvParam(type, method, expr);
    }
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

  protected MethodInfo genOverloadedMethod(MethodInfo method) {
    List<ParamInfo> params = null;
    int count = 0;
    for (ParamInfo param : method.getParams()) {
      TypeInfo paramType = genOverloadedType(param.getType());
      if (paramType != param.getType()) {
        if (params == null) {
          params = new ArrayList<>(method.getParams());
        }
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

  private TypeInfo genOverloadedType(TypeInfo type) {
    if (type.isParameterized() && type.getRaw().getName().equals("io.vertx.core.streams.ReadStream")) {
      return new io.vertx.codegen.type.ParameterizedTypeInfo(
        io.vertx.codegen.type.TypeReflectionFactory.create(Flowable.class).getRaw(),
        false,
        java.util.Collections.singletonList(((ParameterizedTypeInfo) type).getArg(0))
      );
    } else if (type.getKind() == ClassKind.FUNCTION) {
      ParameterizedTypeInfo functionType = (ParameterizedTypeInfo) type;
      TypeInfo argType = genOverloadedType(functionType.getArg(0));
      TypeInfo retType = genOverloadedType(functionType.getArg(1));
      if (argType != functionType.getArg(0) || retType != functionType.getArg(1)) {
        return new ParameterizedTypeInfo(
          io.vertx.codegen.type.TypeReflectionFactory.create(Function.class).getRaw(),
          functionType.isNullable(),
          Arrays.asList(argType, retType));
      }
    } else if (type.getKind() == ClassKind.FUTURE) {
      TypeInfo futType = ((ParameterizedTypeInfo) type).getArg(0);
      if (futType.getKind() == VOID) {
        return io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.Completable.class);
      } else if (futType.isNullable()) {
        return new io.vertx.codegen.type.ParameterizedTypeInfo(io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.Maybe.class).getRaw(), false, Collections.singletonList(futType));
      } else {
        return new io.vertx.codegen.type.ParameterizedTypeInfo(io.vertx.codegen.type.TypeReflectionFactory.create(io.reactivex.Single.class).getRaw(), false, Collections.singletonList(futType));
      }
    }
    return type;
  }
}
