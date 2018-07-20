package io.vertx.lang.rxjava;

import io.vertx.codegen.*;
import io.vertx.codegen.Helper;
import io.vertx.codegen.doc.Doc;
import io.vertx.codegen.doc.Tag;
import io.vertx.codegen.doc.Token;
import io.vertx.codegen.type.*;

import javax.lang.model.element.Element;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vertx.codegen.type.ClassKind.*;

public abstract class AbstractRxGenerator extends Generator<ClassModel> {
  private String id;

  public AbstractRxGenerator(String id) {
    this.id = id;
    this.kinds = Collections.singleton("class");
  }

  @Override
  public String filename(ClassModel model) {
    ModuleInfo module = model.getModule();
    return module.translateQualifiedName(model.getFqn(), id) + ".java";
  }

  @Override
  public String render(ClassModel model, int index, int size, Map<String, Object> session) {
    StringWriter sw = new StringWriter();
    PrintWriter writer = new PrintWriter(sw);
    generateClass(model, writer);
    return sw.toString();
  }

  private void generateClass(ClassModel model, PrintWriter writer) {
    ClassTypeInfo type = model.getType();

    generateLicense(writer);

    writer.print("package ");
    writer.print(type.translatePackageName(id));
    writer.println(";");
    writer.println();

    writer.println("import java.util.Map;");
    genRxImports(model, writer);

    writer.println();
    generateDoc(model, writer);
    writer.println();

    writer.print("@io.vertx.lang.");
    writer.print(id);
    writer.print(".RxGen(");
    writer.print(type.getName());
    writer.println(".class)");

    writer.print("public ");
    if (model.isConcrete()) {
      writer.print("class");
    } else {
      writer.print("interface");
    }
    writer.print(" ");
    writer.print(Helper.getSimpleName(model.getIfaceFQCN()));

    if ("io.vertx.core.buffer.Buffer".equals(type.getName())) {
      writer.print(" implements io.vertx.core.shareddata.impl.ClusterSerializable");
    }
    if (model.isConcrete() && model.getConcreteSuperType() != null) {
      writer.print(" extends ");
      writer.print(model.getConcreteSuperType().getSimpleName());
    }
    List<TypeInfo> abstractSuperTypes = model.getAbstractSuperTypes();
    if (abstractSuperTypes.size() > 0) {
      writer.print(" ");
      if (model.isConcrete()) {
        writer.print("implements");
      } else {
        writer.print("extends");
      }
      writer.print(abstractSuperTypes.stream().map(it -> " " + it.getSimpleName()).collect(Collectors.joining(", ")));
    }
    TypeInfo handlerType = model.getHandlerType();
    if (handlerType != null) {
      if (abstractSuperTypes.isEmpty()) {
        writer.print(" ");
        if (model.isConcrete()) {
          writer.print("implements ");
        } else {
          writer.print("extends ");
        }
      } else {
        writer.print(", ");
      }
      writer.print("io.vertx.core.Handler<");
      writer.print(handlerType.getSimpleName());
      writer.print(">");
    }
    writer.println(" {");
    writer.println();

    String simpleName = type.getSimpleName();
    if (model.isConcrete()) {
      if ("io.vertx.core.buffer.Buffer".equals(type.getName())) {
        writer.println("  @Override");
        writer.println("  public void writeToBuffer(io.vertx.core.buffer.Buffer buffer) {");
        writer.println("    delegate.writeToBuffer(buffer);");
        writer.println("  }");
        writer.println();
        writer.println("  @Override");
        writer.println("  public int readFromBuffer(int pos, io.vertx.core.buffer.Buffer buffer) {");
        writer.println("    return delegate.readFromBuffer(pos, buffer);");
        writer.println("  }");
        writer.println();
      }

      List<MethodInfo> methods = model.getMethods();
      if (methods.stream().noneMatch(it -> it.getParams().isEmpty() && "toString".equals(it.getName()))) {
        writer.println("  @Override");
        writer.println("  public String toString() {");
        writer.println("    return delegate.toString();");
        writer.println("  }");
        writer.println();
      }

      writer.println("  @Override");
      writer.println("  public boolean equals(Object o) {");
      writer.println("    if (this == o) return true;");
      writer.println("    if (o == null || getClass() != o.getClass()) return false;");
      writer.print("    ");
      writer.print(simpleName);
      writer.print(" that = (");
      writer.print(simpleName);
      writer.println(") o;");
      writer.println("    return delegate.equals(that.delegate);");
      writer.println("  }");
      writer.println("  ");

      writer.println("  @Override");
      writer.println("  public int hashCode() {");
      writer.println("    return delegate.hashCode();");
      writer.println("  }");
      writer.println();

      generateClassBody(model, model.getIfaceSimpleName(), writer);
    } else {
      writer.print("  ");
      writer.print(type.getName());
      writer.println(" getDelegate();");
      writer.println();

      for (MethodInfo method : model.getMethods()) {
        startMethodTemplate(type, method, "", writer);
        writer.println(";");
        writer.println();
      }

      if (type.getRaw().getName().equals("io.vertx.core.streams.ReadStream")) {
        genReadStream(type.getParams(), writer);
      }
    }
    writer.println();
    writer.print("  public static ");
    writer.print(genOptTypeParamsDecl(type, " "));
    writer.print(type.getSimpleName());
    writer.print(genOptTypeParamsDecl(type, ""));
    writer.print(" newInstance(");
    writer.print(type.getName());
    writer.println(" arg) {");

    writer.print("    return arg != null ? new ");
    writer.print(type.getSimpleName());
    if (!model.isConcrete()) {
      writer.print("Impl");
    }
    writer.print(genOptTypeParamsDecl(type, ""));
    writer.println("(arg) : null;");
    writer.println("  }");

    if (type.getParams().size() > 0) {
      writer.println();
      writer.print("  public static ");
      writer.print(genOptTypeParamsDecl(type, " "));
      writer.print(type.getSimpleName());
      writer.print(genOptTypeParamsDecl(type, ""));
      writer.print(" newInstance(");
      writer.print(type.getName());
      writer.print(" arg");
      for (TypeParamInfo typeParam : type.getParams()) {
        writer.print(", io.vertx.lang.");
        writer.print(id);
        writer.print(".TypeArg<");
        writer.print(typeParam.getName());
        writer.print("> __typeArg_");
        writer.print(typeParam.getName());
      }
      writer.println(") {");

      writer.print("    return arg != null ? new ");
      writer.print(type.getSimpleName());
      if (!model.isConcrete()) {
        writer.print("Impl");
      }
      writer.print(genOptTypeParamsDecl(type, ""));
      writer.print("(arg");
      for (TypeParamInfo typeParam : type.getParams()) {
        writer.print(", __typeArg_");
        writer.print(typeParam.getName());
      }
      writer.println(") : null;");
      writer.println("  }");
    }
    writer.println("}");

    if (!model.isConcrete()) {
      writer.println();
      writer.print("class ");
      writer.print(type.getSimpleName());
      writer.print("Impl");
      writer.print(genOptTypeParamsDecl(type, ""));
      writer.print(" implements ");
      writer.print(Helper.getSimpleName(model.getIfaceFQCN()));
      writer.println(" {");
      generateClassBody(model, type.getSimpleName() + "Impl", writer);
      writer.println("}");
    }
  }

  protected abstract void genReadStream(List<? extends TypeParamInfo> typeParams, PrintWriter writer);

  private void generateClassBody(ClassModel model, String constructor, PrintWriter writer) {
    ClassTypeInfo type = model.getType();
    String simpleName = type.getSimpleName();
    if (model.isConcrete()) {
      writer.print("  public static final io.vertx.lang.");
      writer.print(id);
      writer.print(".TypeArg<");
      writer.print(simpleName);
      writer.print("> __TYPE_ARG = new io.vertx.lang.");
      writer.print(id);
      writer.println(".TypeArg<>(");
      writer.print("    obj -> new ");
      writer.print(simpleName);
      writer.print("((");
      writer.print(type.getName());
      writer.println(") obj),");
      writer.print("    ");
      writer.print(simpleName);
      writer.println("::getDelegate");
      writer.println("  );");
      writer.println();
    }
    writer.print("  private final ");
    writer.print(Helper.getNonGenericType(model.getIfaceFQCN()));
    List<TypeParamInfo.Class> typeParams = model.getTypeParams();
    if (typeParams.size() > 0) {
      writer.print(typeParams.stream().map(TypeParamInfo.Class::getName).collect(Collectors.joining(",", "<", ">")));
    }
    writer.println(" delegate;");

    for (TypeParamInfo.Class typeParam : typeParams) {
      writer.print("  public final io.vertx.lang.");
      writer.print(id);
      writer.print(".TypeArg<");
      writer.print(typeParam.getName());
      writer.print("> __typeArg_");
      writer.print(typeParam.getIndex());
      writer.println(";");
    }
    writer.println("  ");

    writer.print("  public ");
    writer.print(constructor);
    writer.print("(");
    writer.print(Helper.getNonGenericType(model.getIfaceFQCN()));
    writer.println(" delegate) {");

    if (model.isConcrete() && model.getConcreteSuperType() != null) {
      writer.println("    super(delegate);");
    }
    writer.println("    this.delegate = delegate;");
    for (TypeParamInfo.Class typeParam : typeParams) {
      writer.print("    this.__typeArg_");
      writer.print(typeParam.getIndex());
      writer.print(" = io.vertx.lang.");
      writer.print(id);
      writer.println(".TypeArg.unknown();");
    }
    writer.println("  }");
    writer.println();

    if (typeParams.size() > 0) {
      writer.print("  public ");
      writer.print(constructor);
      writer.print("(");
      writer.print(Helper.getNonGenericType(model.getIfaceFQCN()));
      writer.print(" delegate");
      for (TypeParamInfo.Class typeParam : typeParams) {
        writer.print(", io.vertx.lang.");
        writer.print(id);
        writer.print(".TypeArg<");
        writer.print(typeParam.getName());
        writer.print("> typeArg_");
        writer.print(typeParam.getIndex());
      }
      writer.println(") {");
      if (model.isConcrete() && model.getConcreteSuperType() != null) {
        writer.println("    super(delegate);");
      }
      writer.println("    this.delegate = delegate;");
      for (TypeParamInfo.Class typeParam : typeParams) {
        writer.print("    this.__typeArg_");
        writer.print(typeParam.getIndex());
        writer.print(" = typeArg_");
        writer.print(typeParam.getIndex());
        writer.println(";");
      }
      writer.println("  }");
      writer.println();
    }

    writer.print("  public ");
    writer.print(type.getName());
    writer.println(" getDelegate() {");
    writer.println("    return delegate;");
    writer.println("  }");
    writer.println();

    ApiTypeInfo api = (ApiTypeInfo) type;
    if (api.isReadStream()) {
      genToObservable(api, writer);
    }
    List<String> cacheDecls = new ArrayList<>();
    for (MethodInfo method : model.getMethods()) {
      genMethods(model, method, cacheDecls, writer);
    }

    for (String cacheDecl : cacheDecls) {
      writer.print("  ");
      writer.print(cacheDecl);
      writer.println(";");
    }
  }

  protected abstract void genToObservable(ApiTypeInfo type, PrintWriter writer);

  protected abstract void genMethods(ClassModel model, MethodInfo method, List<String> cacheDecls, PrintWriter writer);

  protected abstract void genRxMethod(ClassModel model, MethodInfo method, PrintWriter writer);

  protected final void genMethod(ClassModel model, MethodInfo method, List<String> cacheDecls, PrintWriter writer) {
    genSimpleMethod(model, method, cacheDecls, writer);
    if (method.getKind() == MethodKind.FUTURE) {
      genRxMethod(model, method, writer);
    }
  }

  protected void startMethodTemplate(ClassTypeInfo type, MethodInfo method, String deprecated, PrintWriter writer) {
    Doc doc = method.getDoc();
    if (doc != null) {
      writer.println("  /**");
      Token.toHtml(doc.getTokens(), "   *", this::renderLinkToHtml, "\n", writer);
      for (ParamInfo param : method.getParams()) {
        writer.print("   * @param ");
        writer.print(param.getName());
        writer.print(" ");
        if (param.getDescription() != null) {
          Token.toHtml(param.getDescription().getTokens(), "", this::renderLinkToHtml, "", writer);
        }
        writer.println();
      }
      if (!method.getReturnType().getName().equals("void")) {
        writer.print("   * @return ");
        if (method.getReturnDescription() != null) {
          Token.toHtml(method.getReturnDescription().getTokens(), "", this::renderLinkToHtml, "", writer);
        }
        writer.println();
      }
      if (deprecated != null && deprecated.length() > 0) {
        writer.print("   * @deprecated ");
        writer.println(deprecated);
      }
      writer.println("   */");
    }
    if (method.isDeprecated() || deprecated != null && deprecated.length() > 0) {
      writer.println("  @Deprecated()");
    }
    writer.print("  public ");
    if (method.isStaticMethod()) {
      writer.print("static ");
    }
    if (method.getTypeParams().size() > 0) {
      writer.print(method.getTypeParams().stream().map(TypeParamInfo::getName).collect(Collectors.joining(", ", "<", ">")));
      writer.print(" ");
    }
    writer.print(method.getReturnType().getSimpleName());
    writer.print(" ");
    writer.print(method.getName());
    writer.print("(");
    writer.print(method.getParams().stream().map(it -> it.getType().getSimpleName() + " " + it.getName()).collect(Collectors.joining(", ")));
    writer.print(")");

  }

  protected String genFutureMethodName(MethodInfo method) {
    return "rx" + Character.toUpperCase(method.getName().charAt(0)) + method.getName().substring(1);
  }

  private void genSimpleMethod(ClassModel model, MethodInfo method, List<String> cacheDecls, PrintWriter writer) {
    ClassTypeInfo type = model.getType();
    startMethodTemplate(type, method, "", writer);
    writer.println(" { ");
    if (method.isFluent()) {
      writer.print("    ");
      writer.print(genInvokeDelegate(model, method));
      writer.println(";");
      if (method.getReturnType().isVariable()) {
        writer.print("    return (");
        writer.print(method.getReturnType().getName());
        writer.println(") this;");
      } else {
        writer.println("    return this;");
      }
    } else if (method.getReturnType().getName().equals("void")) {
      writer.print("    ");
      writer.print(genInvokeDelegate(model, method));
      writer.println(";");
    } else {
      if (method.isCacheReturn()) {
        writer.print("    if (cached_");
        writer.print(cacheDecls.size());
        writer.println(" != null) {");

        writer.print("      return cached_");
        writer.print(cacheDecls.size());
        writer.println(";");
        writer.println("    }");
      }
      String cachedType;
      TypeInfo returnType = method.getReturnType();
      if (method.getReturnType().getKind() == PRIMITIVE) {
        cachedType = ((PrimitiveTypeInfo) returnType).getBoxed().getName();
      } else {
        cachedType = returnType.getSimpleName();
      }
      writer.print("    ");
      writer.print(returnType.getSimpleName());
      writer.print(" ret = ");
      writer.print(genConvReturn(returnType, method, genInvokeDelegate(model, method)));
      writer.println(";");
      if (method.isCacheReturn()) {
        writer.print("    cached_");
        writer.print(cacheDecls.size());
        writer.println(" = ret;");
        cacheDecls.add("private" + (method.isStaticMethod() ? " static" : "") + " " + cachedType + " cached_" + cacheDecls.size());
      }
      writer.println("    return ret;");
    }
    writer.println("  }");
    writer.println();
  }

//  private void genInvokeDelegate(MethodInfo method, PrintWriter writer) {
//
//  }

  private void generateDoc(ClassModel model, PrintWriter writer) {
    ClassTypeInfo type = model.getType();
    Doc doc = model.getDoc();
    if (doc != null) {
      writer.println("/**");
      Token.toHtml(doc.getTokens(), " *", this::renderLinkToHtml, "\n", writer);
      writer.println(" *");
      writer.println(" * <p/>");
      writer.print(" * NOTE: This class has been automatically generated from the {@link ");
      writer.print(type.getName());
      writer.println(" original} non RX-ified interface using Vert.x codegen.");
      writer.println(" */");
    }


  }

  protected void genRxImports(ClassModel model, PrintWriter writer) {
    for (ClassTypeInfo importedType : model.getImportedTypes()) {
      if (importedType.getKind() == ClassKind.API) {
        if (!model.getIfacePackageName().equals(importedType.getPackageName())) {
          addImport(importedType, true, writer);
        }
      } else {
        if (!importedType.getPackageName().equals("java.lang")) {
          addImport(importedType, false, writer);
        }
      }
    }
  }

  private void addImport(ClassTypeInfo type, boolean translate, PrintWriter writer) {
    writer.print("import ");
    if (translate) {
      writer.print(type.translateName(id));
    } else {
      writer.print(type.toString());
    }
    writer.println(";");
  }

  private String genInvokeDelegate(ClassModel model, MethodInfo method) {
    StringBuilder ret;
    if (method.isStaticMethod()) {
      ret = new StringBuilder(Helper.getNonGenericType(model.getIfaceFQCN()));
    } else {
      ret = new StringBuilder("delegate");
    }
    ret.append(".").append(method.getName()).append("(");
    int index = 0;
    for (ParamInfo param : method.getParams()) {
      if (index > 0) {
        ret.append(", ");
      }
      TypeInfo type = param.getType();
      if (type.isParameterized() && type.getRaw().getName().equals("rx.Observable")) {
        String adapterFunction;
        ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) type;

        if (parameterizedType.getArg(0).isVariable()) {
          adapterFunction = "java.util.function.Function.identity()";
        } else {
          adapterFunction = "obj -> (" + parameterizedType.getArg(0).getRaw().getName() + ")obj.getDelegate()";
        }
        ret.append("io.vertx.rx.java.ReadStreamSubscriber.asReadStream(").append(param.getName()).append(",").append(adapterFunction).append(").resume()");
      } else if (type.isParameterized() && (type.getRaw().getName().equals("io.reactivex.Flowable") || type.getRaw().getName().equals("io.reactivex.Observable"))) {
        String adapterFunction;
        ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) type;
        if (parameterizedType.getArg(0).isVariable()) {
          adapterFunction = "java.util.function.Function.identity()";
        } else {
          adapterFunction = "obj -> (" + parameterizedType.getArg(0).getRaw().getName() + ")obj.getDelegate()";
        }
        ret.append("io.vertx.reactivex.impl.ReadStreamSubscriber.asReadStream(").append(param.getName()).append(",").append(adapterFunction).append(").resume()");
      } else {
        ret.append(genConvParam(type, method, param.getName()));
      }
      index = index + 1;
    }
    ret.append(")");
    return ret.toString();
  }

  private boolean isSameType(TypeInfo type, MethodInfo method) {
    ClassKind kind = type.getKind();
    if (kind.basic || kind.json || kind == DATA_OBJECT || kind == ENUM || kind == OTHER || kind == THROWABLE || kind == VOID) {
      return true;
    } else if (kind == OBJECT) {
      if (type.isVariable()) {
        return !isReified((TypeVariableInfo) type, method);
      } else {
        return true;
      }
    } else if (type.isParameterized()) {
      ParameterizedTypeInfo parameterizedTypeInfo = (ParameterizedTypeInfo) type;
      if (kind == LIST || kind == SET || kind == ASYNC_RESULT) {
        return isSameType(parameterizedTypeInfo.getArg(0), method);
      } else if (kind == MAP) {
        return isSameType(parameterizedTypeInfo.getArg(1), method);
      } else if (kind == HANDLER) {
        return isSameType(parameterizedTypeInfo.getArg(0), method);
      } else if (kind == FUNCTION) {
        return isSameType(parameterizedTypeInfo.getArg(0), method) && isSameType(parameterizedTypeInfo.getArg(1), method);
      }
    }
    return false;
  }

  private String genConvParam(TypeInfo type, MethodInfo method, String expr) {
    ClassKind kind = type.getKind();
    if (isSameType(type, method)) {
      return expr;
    } else if (kind == OBJECT) {
      if (type.isVariable()) {
        String typeArg = genTypeArg((TypeVariableInfo) type, method);
        if (typeArg != null) {
          return typeArg + ".<" + type.getName() + ">unwrap(" + expr + ")";
        }
      }
      return expr;
    } else if (kind == API) {
      return expr + ".getDelegate()";
    } else if (kind == CLASS_TYPE) {
      return "io.vertx.lang." + id + ".Helper.unwrap(" + expr + ")";
    } else if (type.isParameterized()) {
      ParameterizedTypeInfo parameterizedTypeInfo = (ParameterizedTypeInfo) type;
      if (kind == HANDLER) {
        TypeInfo eventType = parameterizedTypeInfo.getArg(0);
        ClassKind eventKind = eventType.getKind();
        if (eventKind == ASYNC_RESULT) {
          TypeInfo resultType = ((ParameterizedTypeInfo) eventType).getArg(0);
          return "new Handler<AsyncResult<" + resultType.getName() + ">>() {\n" +
            "      public void handle(AsyncResult<" + resultType.getName() + "> ar) {\n" +
            "        if (ar.succeeded()) {\n" +
            "          " + expr + ".handle(io.vertx.core.Future.succeededFuture(" + genConvReturn(resultType, method, "ar.result()") + "));\n" +
            "        } else {\n" +
            "          " + expr + ".handle(io.vertx.core.Future.failedFuture(ar.cause()));\n" +
            "        }\n" +
            "      }\n" +
            "    }";
        } else {
          return "new Handler<" + eventType.getName() + ">() {\n" +
            "      public void handle(" + eventType.getName() + " event) {\n" +
            "        " + expr + ".handle(" + genConvReturn(eventType, method, "event") + ");\n" +
            "      }\n" +
            "    }";
        }
      } else if (kind == FUNCTION) {
        TypeInfo argType = parameterizedTypeInfo.getArg(0);
        TypeInfo retType = parameterizedTypeInfo.getArg(1);
        return "new java.util.function.Function<" + argType.getName() + "," + retType.getName() + ">() {\n" +
          "      public " + retType.getName() + " apply(" + argType.getName() + " arg) {\n" +
          "        " + retType.getSimpleName() + " ret = " + expr + ".apply(" + genConvReturn(argType, method, "arg") + ");\n" +
          "        return " + genConvParam(retType, method, "ret") + ";\n" +
          "      }\n" +
          "    }";
      } else if (kind == LIST || kind == SET) {
        return expr + ".stream().map(elt -> " + genConvParam(parameterizedTypeInfo.getArg(0), method, "elt") + ").collect(java.util.stream.Collectors.to" + type.getRaw().getSimpleName() + "())";
      } else if (kind == MAP) {
        return expr + ".entrySet().stream().collect(java.util.stream.Collectors.toMap(e -> e.getKey(), e -> " + genConvParam(parameterizedTypeInfo.getArg(1), method, "e.getValue()") + "))";
      }
    }
    return expr;
  }

  private boolean isReified(TypeVariableInfo typeVar, MethodInfo method) {
    if (typeVar.isClassParam()) {
      return true;
    } else {
      TypeArgExpression typeArg = method.resolveTypeArg(typeVar);
      return typeArg != null && typeArg.isClassType();
    }
  }

  private String genTypeArg(TypeVariableInfo typeVar, MethodInfo method) {
    if (typeVar.isClassParam()) {
      return "__typeArg_" + typeVar.getParam().getIndex();
    } else {
      TypeArgExpression typeArg = method.resolveTypeArg(typeVar);
      if (typeArg != null) {
        if (typeArg.isClassType()) {
          return "io.vertx.lang." + id + ".TypeArg.of(" + typeArg.getParam().getName() + ")";
        } else {
          return typeArg.getParam().getName() + ".__typeArg_" + typeArg.getIndex();
        }
      }
    }
    return null;
  }

  private String genConvReturn(TypeInfo type, MethodInfo method, String expr) {
    ClassKind kind = type.getKind();
    if (kind == OBJECT) {
      if (type.isVariable()) {
        String typeArg = genTypeArg((TypeVariableInfo) type, method);
        if (typeArg != null) {
          return "(" + type.getName() + ")" + typeArg + ".wrap(" + expr + ")";
        }
      }
      return "(" + type.getSimpleName() + ") " + expr;
    } else if (isSameType(type, method)) {
      return expr;
    } else if (kind == API) {
      StringBuilder tmp = new StringBuilder(type.getRaw().getSimpleName());
      tmp.append(".newInstance(");
      tmp.append(expr);
      if (type.isParameterized()) {
        ParameterizedTypeInfo parameterizedTypeInfo = (ParameterizedTypeInfo) type;
        for (TypeInfo arg : parameterizedTypeInfo.getArgs()) {
          tmp.append(", ");
          ClassKind argKind = arg.getKind();
          if (argKind == API) {
            tmp.append(arg.translateName(id)).append(".__TYPE_ARG");
          } else {
            String typeArg = "io.vertx.lang." + id + ".TypeArg.unknown()";
            if (argKind == OBJECT && arg.isVariable()) {
              String resolved = genTypeArg((TypeVariableInfo) arg, method);
              if (resolved != null) {
                typeArg = resolved;
              }
            }
            tmp.append(typeArg);
          }
        }
      }
      tmp.append(")");
      return tmp.toString();
    } else if (type.isParameterized()) {
      ParameterizedTypeInfo parameterizedTypeInfo = (ParameterizedTypeInfo) type;
      if (kind == HANDLER) {
        TypeInfo abc = parameterizedTypeInfo.getArg(0);
        if (abc.getKind() == ASYNC_RESULT) {
          TypeInfo tutu = ((ParameterizedTypeInfo) abc).getArg(0);
          return "new Handler<AsyncResult<" + tutu.getSimpleName() + ">>() {\n" +
            "      public void handle(AsyncResult<" + tutu.getSimpleName() + "> ar) {\n" +
            "        if (ar.succeeded()) {\n" +
            "          " + expr + ".handle(io.vertx.core.Future.succeededFuture(" + genConvParam(tutu, method, "ar.result()") + "));\n" +
            "        } else {\n" +
            "          " + expr + ".handle(io.vertx.core.Future.failedFuture(ar.cause()));\n" +
            "        }\n" +
            "      }\n" +
            "    }";
        } else {
          return "new Handler<" + abc.getSimpleName() + ">() {\n" +
            "      public void handle(" + abc.getSimpleName() + " event) {\n" +
            "          " + expr + ".handle(" + genConvParam(abc, method, "event") + ");\n" +
            "      }\n" +
            "    }";
        }
      } else if (kind == LIST || kind == SET) {
        return expr + ".stream().map(elt -> " + genConvReturn(parameterizedTypeInfo.getArg(0), method, "elt") + ").collect(java.util.stream.Collectors.to" + type.getRaw().getSimpleName() + "())";
      }
    }
    return expr;
  }

//  private boolean hasReadStream(MethodInfo method) {
//    for (ParamInfo param : method.getParams()) {
//      if (param.getType().isParameterized() && param.getType().getRaw().getName().equals("io.vertx.core.streams.ReadStream")) {
//        return true;
//      }
//    }
//    return false;
//  }

  private String genOptTypeParamsDecl(ClassTypeInfo type, String deflt) {
    if (type.getParams().size() > 0) {
      return type.getParams().stream().map(TypeParamInfo::getName).collect(Collectors.joining(",", "<", ">"));
    } else {
      return deflt;
    }
  }


  private void generateLicense(PrintWriter writer) {
    writer.println("/*");
    writer.println(" * Copyright 2014 Red Hat, Inc.");
    writer.println(" *");
    writer.println(" * Red Hat licenses this file to you under the Apache License, version 2.0");
    writer.println(" * (the \"License\"); you may not use this file except in compliance with the");
    writer.println(" * License.  You may obtain a copy of the License at:");
    writer.println(" *");
    writer.println(" * http://www.apache.org/licenses/LICENSE-2.0");
    writer.println(" *");
    writer.println(" * Unless required by applicable law or agreed to in writing, software");
    writer.println(" * distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT");
    writer.println(" * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the");
    writer.println(" * License for the specific language governing permissions and limitations");
    writer.println(" * under the License.");
    writer.println(" */");
    writer.println();
  }

  private String renderLinkToHtml(Tag.Link link) {
    ClassTypeInfo rawType = link.getTargetType().getRaw();
    if (rawType.getModule() != null) {
      String label = link.getLabel().trim();
      if (rawType.getKind() == DATA_OBJECT) {
        return "{@link " + rawType.getName() + "}";
      } else {
        if (rawType.getKind() == ClassKind.API) {
          Element elt = link.getTargetElement();
          String eltKind = elt.getKind().name();
          String ret = "{@link " + rawType.translateName(id);
          if ("METHOD".equals(eltKind)) {
            /* todo find a way for translating the complete signature */
            ret += "#" + elt.getSimpleName().toString();
          }
          if (label.length() > 0) {
            ret += " " + label;
          }
          ret += "}";
          return ret;
        }
      }
    }
    return "{@link " + rawType.getName() + "}";
  }
}
