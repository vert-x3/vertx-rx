package io.vertx.lang.rxjava;

import io.vertx.codegen.ClassModel;
import io.vertx.codegen.MethodInfo;
import io.vertx.codegen.ParamInfo;
import io.vertx.codegen.TypeParamInfo;
import io.vertx.codegen.type.ClassKind;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.lang.rx.AbstractRxGenerator;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class RxJavaGenerator extends AbstractRxGenerator {
  RxJavaGenerator() {
    super("rxjava");
    this.name = "RxJava";
  }

  @Override
  protected boolean isImported(TypeInfo type) {
    return type.getName().startsWith("rx.") || super.isImported(type);
  }

  @Override
  protected void genImports(ClassModel model, PrintWriter writer) {
    writer.println("import rx.Observable;");
    writer.println("import rx.Single;");
    writer.println("import io.vertx.rx.java.RxHelper;");
    writer.println("import io.vertx.rx.java.WriteStreamSubscriber;");
    writer.println("import io.vertx.rx.java.SingleOnSubscribeAdapter;");
    super.genImports(model, writer);
  }

  @Override
  protected void genToObservable(TypeInfo streamType, PrintWriter writer) {
    writer.print("  private Observable<");
    writer.print(genTranslatedTypeName(streamType));
    writer.println("> observable;");
    writer.println();

    writer.print("  public synchronized Observable<");
    writer.print(genTranslatedTypeName(streamType));
    writer.println("> toObservable() {");

    writer.print("    ");
    writer.println("if (observable == null) {");

    if (streamType.getKind() == ClassKind.API) {
      writer.print("      Function<");
      writer.print(streamType.getName());
      writer.print(", ");
      writer.print(genTranslatedTypeName(streamType));
      writer.print("> conv = ");
      writer.print(genTranslatedTypeName(streamType.getRaw()));
      writer.println("::newInstance;");

      writer.println("      observable = RxHelper.toObservable(delegate, conv);");
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

      writer.println("      observable = RxHelper.toObservable(delegate, conv);");
    } else {
      writer.println("      observable = RxHelper.toObservable(this.getDelegate());");
    }

    writer.println("    }");
    writer.println("    return observable;");
    writer.println("  }");
    writer.println();
  }

  @Override
  protected void genToSubscriber(TypeInfo streamType, PrintWriter writer) {
    writer.format("  private WriteStreamSubscriber<%s> subscriber;%n", genTranslatedTypeName(streamType));
    writer.println();

    writer.format("  public synchronized WriteStreamSubscriber<%s> toSubscriber() {%n", genTranslatedTypeName(streamType));

    writer.println("    if (subscriber == null) {");

    if (streamType.getKind() == ClassKind.API) {
      writer.format("      Function<%s, %s> conv = %s::getDelegate;%n", genTranslatedTypeName(streamType.getRaw()), streamType.getName(), genTranslatedTypeName(streamType));

      writer.println("      subscriber = RxHelper.toSubscriber(getDelegate(), conv);");
    } else if (streamType.isVariable()) {
      String typeVar = streamType.getSimpleName();
      writer.format("      Function<%s, %s> conv = (Function<%s, %s>) __typeArg_0.unwrap;%n", typeVar, typeVar, typeVar, typeVar);

      writer.println("      subscriber = RxHelper.toSubscriber(getDelegate(), conv);");
    } else {
      writer.println("      subscriber = RxHelper.toSubscriber(getDelegate());");
    }

    writer.println("    }");
    writer.println("    return subscriber;");
    writer.println("  }");
    writer.println();
  }

  @Override
  protected void genMethods(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer) {
    genMethod(model, method, cacheDecls, genBody, writer);
    MethodInfo overloaded = genOverloadedMethod(method);
    if (overloaded != null) {
      genMethod(model, overloaded, cacheDecls, genBody, writer);
    }
  }

  private static TypeInfo unwrap(TypeInfo type) {
    if (type instanceof ParameterizedTypeInfo) {
      return type.getRaw();
    } else {
      return type;
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

  @Override
  protected void genRxMethod(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer) {
    ClassTypeInfo type = model.getType();
    String packageName = type.getPackageName();
    writer.print("  ");
    MethodInfo futMethod = genFutureMethod(method);
    startMethodTemplate(type, futMethod, "", writer);
    if (genBody) {
      writer.println(" { ");
      writer.println("    return Single.create(new SingleOnSubscribeAdapter<>(fut -> {");
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
    } else {
      writer.println(";");
    }
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

  @Override
  protected String genConvParam(TypeInfo type, MethodInfo method, String expr) {
    if (type.isParameterized() && type.getRaw().getName().equals("rx.Observable")) {
      String adapterFunction;
      ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) type;

      if (parameterizedType.getArg(0).isVariable()) {
        adapterFunction = "Function.identity()";
      } else {
        adapterFunction = "obj -> (" + parameterizedType.getArg(0).getRaw().getName() + ")obj.getDelegate()";
      }
      return "io.vertx.rx.java.ReadStreamSubscriber.asReadStream(" + expr + "," + adapterFunction + ").resume()";
    }
    return super.genConvParam(type, method, expr);
  }
}
