package io.vertx.lang.rx;

import static io.vertx.codegen.type.ClassKind.API;
import static io.vertx.codegen.type.ClassKind.ASYNC_RESULT;
import static io.vertx.codegen.type.ClassKind.CLASS_TYPE;
import static io.vertx.codegen.type.ClassKind.ENUM;
import static io.vertx.codegen.type.ClassKind.FUNCTION;
import static io.vertx.codegen.type.ClassKind.FUTURE;
import static io.vertx.codegen.type.ClassKind.HANDLER;
import static io.vertx.codegen.type.ClassKind.LIST;
import static io.vertx.codegen.type.ClassKind.MAP;
import static io.vertx.codegen.type.ClassKind.OBJECT;
import static io.vertx.codegen.type.ClassKind.OTHER;
import static io.vertx.codegen.type.ClassKind.PRIMITIVE;
import static io.vertx.codegen.type.ClassKind.SET;
import static io.vertx.codegen.type.ClassKind.THROWABLE;
import static io.vertx.codegen.type.ClassKind.VOID;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;

import io.vertx.codegen.ClassModel;
import io.vertx.codegen.ConstantInfo;
import io.vertx.codegen.Generator;
import io.vertx.codegen.Helper;
import io.vertx.codegen.MethodInfo;
import io.vertx.codegen.ModuleInfo;
import io.vertx.codegen.ParamInfo;
import io.vertx.codegen.TypeArgExpression;
import io.vertx.codegen.TypeParamInfo;
import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.codegen.doc.Doc;
import io.vertx.codegen.doc.Tag;
import io.vertx.codegen.doc.Token;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ClassKind;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.PrimitiveTypeInfo;
import io.vertx.codegen.type.TypeInfo;
import io.vertx.codegen.type.TypeVariableInfo;

public abstract class AbstractBaseVertxGenerator extends Generator<ClassModel> {

  private final String id;

  protected Map<MethodInfo, Map<TypeInfo, String>> methodTypeArgMap = new HashMap<>();

  public AbstractBaseVertxGenerator(String id) {
    this.id = id;
    this.kinds = Collections.singleton("class");
  }

  public String id() {
    return id;
  }

  @Override
  public Collection<Class<? extends Annotation>> annotations() {
    return Arrays.asList(VertxGen.class, ModuleGen.class);
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
    methodTypeArgMap.clear();
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

    genImports(model, writer);

    writer.println();
    generateDoc(model, writer);
    writer.println();

    generateExtraAnnotation(model, writer, type);

    writer.print("public ");
    if (model.isConcrete()) {
      writer.print("class");
    } else {
      writer.print("interface");
    }
    writer.print(" ");
    writer.print(Helper.getSimpleName(model.getIfaceFQCN()));

    if (model.isConcrete() && model.getConcreteSuperType() != null) {
      writer.print(" extends ");
      writer.print(genTranslatedTypeName(model.getConcreteSuperType()));
    }

    List<String> interfaces = new ArrayList<>();
    if ("io.vertx.core.buffer.Buffer".equals(type.getName())) {
      interfaces.add("io.vertx.core.shareddata.impl.ClusterSerializable");
    }
    interfaces.addAll(model.getAbstractSuperTypes().stream().map(this::genTranslatedTypeName).collect(toList()));
    if (model.isHandler()) {
      interfaces.add("Handler<" + genTranslatedTypeName(model.getHandlerArg()) + ">");
    }
    if (model.isIterable()) {
      interfaces.add("Iterable<" + genTranslatedTypeName(model.getIterableArg()) + ">");
    }
    if (model.isIterator()) {
      interfaces.add("Iterator<" + genTranslatedTypeName(model.getIteratorArg()) + ">");
    }
    if (model.isFunction()) {
      TypeInfo[] functionArgs = model.getFunctionArgs();
      interfaces.add("Function<" + genTranslatedTypeName(functionArgs[0]) + ", " + genTranslatedTypeName(functionArgs[1]) + ">");
    }

    if (!interfaces.isEmpty()) {
      writer.print(interfaces.stream().collect(joining(", ", model.isConcrete() ? " implements " : " extends ", "")));
    }

    writer.println(" {");
    writer.println();

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
      writer.print(type.getSimpleName());
      writer.print(" that = (");
      writer.print(type.getSimpleName());
      writer.println(") o;");
      writer.println("    return delegate.equals(that.delegate);");
      writer.println("  }");
      writer.println("  ");

      writer.println("  @Override");
      writer.println("  public int hashCode() {");
      writer.println("    return delegate.hashCode();");
      writer.println("  }");
      writer.println();

      if (model.isIterable()) {
        generateIterableMethod(model, writer);
      }
      if (model.isIterator()) {
        generateIteratorMethods(model, writer);
      }
      if (model.isFunction()) {
        generateFunctionMethod(model, writer);
      }

      generateClassBody(model, model.getIfaceSimpleName(), writer);
    } else {
      writer.print("  ");
      writer.print(type.getName());
      writer.println(" getDelegate();");
      writer.println();

      for (MethodInfo method : model.getMethods()) {
        genMethods(model, method, Collections.emptyList(), false, writer);
      }

      if (type.getRaw().getName().equals("io.vertx.core.streams.ReadStream")) {
        genReadStream(type.getParams(), writer);
      }
    }

    // Gen newInstance
    writer.print("  public static ");
    if (type.getParams().size() > 0) {
      writer.print(genTypeParamsDecl(type));
      writer.print(" ");
    }
    writer.print(type.getSimpleName());
    writer.print(genTypeParamsDecl(type));
    writer.print(" newInstance(");
    writer.print(type.getName());
    writer.println(" arg) {");
    writer.print("    return arg != null ? new ");
    writer.print(type.getSimpleName());
    if (!model.isConcrete()) {
      writer.print("Impl");
    }
    writer.print(genTypeParamsDecl(type));
    writer.println("(arg) : null;");
    writer.println("  }");
    writer.println();

    // Gen parameterized newInstance
    if (type.getParams().size() > 0) {
      writer.print("  public static ");
      writer.print(genTypeParamsDecl(type));
      writer.print(" ");
      writer.print(type.getSimpleName());
      writer.print(genTypeParamsDecl(type));
      writer.print(" newInstance(");
      writer.print(type.getName());
      writer.print(" arg");
      for (TypeParamInfo typeParam : type.getParams()) {
        writer.print(", TypeArg<");
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
      writer.print(genTypeParamsDecl(type));
      writer.print("(arg");
      for (TypeParamInfo typeParam : type.getParams()) {
        writer.print(", __typeArg_");
        writer.print(typeParam.getName());
      }
      writer.println(") : null;");
      writer.println("  }");
      writer.println();
    }

    writer.println("}");

    if (!model.isConcrete()) {
      writer.println();
      writer.print("class ");
      writer.print(type.getSimpleName());
      writer.print("Impl");
      writer.print(genTypeParamsDecl(type));
      writer.print(" implements ");
      writer.print(Helper.getSimpleName(model.getIfaceFQCN()));
      writer.println(" {");
      generateClassBody(model, type.getSimpleName() + "Impl", writer);
      writer.println("}");
    }
  }

  protected void generateExtraAnnotation(ClassModel model, PrintWriter writer, ClassTypeInfo type) {
    // NOOP
  }

  private void generateIterableMethod(ClassModel model, PrintWriter writer) {
    if (model.getMethods().stream().noneMatch(it -> it.getParams().isEmpty() && "iterator".equals(it.getName()))) {
      TypeInfo iterableArg = model.getIterableArg();

      writer.println("  @Override");
      writer.printf("  public Iterator<%s> iterator() {%n", genTranslatedTypeName(iterableArg));

      if (iterableArg.getKind() == ClassKind.API) {
        writer.format("    Function<%s, %s> conv = %s::newInstance;%n", iterableArg.getName(), genTranslatedTypeName(iterableArg.getRaw()), genTranslatedTypeName(iterableArg));
        writer.println("    return new MappingIterator<>(delegate.iterator(), conv);");
      } else if (iterableArg.isVariable()) {
        String typeVar = iterableArg.getSimpleName();
        writer.format("    Function<%s, %s> conv = (Function<%s, %s>) __typeArg_0.wrap;%n", typeVar, typeVar, typeVar, typeVar);
        writer.println("    return new MappingIterator<>(delegate.iterator(), conv);");
      } else {
        writer.println("    return delegate.iterator();");
      }

      writer.println("  }");
      writer.println();
    }
  }

  private void generateIteratorMethods(ClassModel model, PrintWriter writer) {
    if (model.getMethods().stream().noneMatch(it -> it.getParams().isEmpty() && "hasNext".equals(it.getName()))) {
      writer.println("  @Override");
      writer.println("  public boolean hasNext() {");
      writer.println("    return delegate.hasNext();");
      writer.println("  }");
      writer.println();
    }
    if (model.getMethods().stream().noneMatch(it -> it.getParams().isEmpty() && "next".equals(it.getName()))) {
      TypeInfo iteratorArg = model.getIteratorArg();

      writer.println("  @Override");
      writer.printf("  public %s next() {%n", genTranslatedTypeName(iteratorArg));

      if (iteratorArg.getKind() == ClassKind.API) {
        writer.format("    return %s.newInstance(delegate.next());%n", genTranslatedTypeName(iteratorArg));
      } else if (iteratorArg.isVariable()) {
        writer.println("    return __typeArg_0.wrap(delegate.next());");
      } else {
        writer.println("    return delegate.next();");
      }

      writer.println("  }");
      writer.println();
    }
  }

  private void generateFunctionMethod(ClassModel model, PrintWriter writer) {
    if (model.getMethods().stream().noneMatch(it -> it.getParams().size() == 1 && "apply".equals(it.getName()))) {
      TypeInfo[] functionArgs = model.getFunctionArgs();
      TypeInfo inArg = functionArgs[0];
      TypeInfo outArg = functionArgs[1];

      writer.println("  @Override");
      writer.printf("  public %s apply(%s in) {%n", genTranslatedTypeName(outArg), genTranslatedTypeName(inArg));
      writer.printf("    %s ret;%n", outArg.getName());

      if (inArg.getKind() == ClassKind.API) {
        writer.println("    ret = getDelegate().apply(in.getDelegate());");
      } else if (inArg.isVariable()) {
        String typeVar = inArg.getSimpleName();
        writer.format("    Function<%s, %s> inConv = (Function<%s, %s>) __typeArg_0.unwrap;%n", typeVar, typeVar, typeVar, typeVar);
        writer.println("    ret = getDelegate().apply(inConv.apply);");
      } else {
        writer.println("    ret = getDelegate().apply(in);");
      }

      if (outArg.getKind() == ClassKind.API) {
        writer.format("    Function<%s, %s> outConv = %s::newInstance;%n", outArg.getName(), genTranslatedTypeName(outArg.getRaw()), genTranslatedTypeName(outArg));
        writer.println("    return outConv.apply(ret);");
      } else if (outArg.isVariable()) {
        String typeVar = outArg.getSimpleName();
        writer.format("    Function<%s, %s> outConv = (Function<%s, %s>) __typeArg_1.wrap;%n", typeVar, typeVar, typeVar, typeVar);
        writer.println("    return outConv.apply(ret);");
      } else {
        writer.println("    return delegate.iterator();");
      }

      writer.println("  }");
      writer.println();
    }
  }

  protected abstract void genReadStream(List<? extends TypeParamInfo> typeParams, PrintWriter writer);

  protected abstract void genMethods(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer);

  protected abstract void generateClassBody(ClassModel model, String string, PrintWriter writer);

  protected void genConstant(ClassModel model, ConstantInfo constant, PrintWriter writer) {
    Doc doc = constant.getDoc();
    if (doc != null) {
      writer.println("  /**");
      Token.toHtml(doc.getTokens(), "   *", this::renderLinkToHtml, "\n", writer);
      writer.println("   */");
    }
    writer.print(model.isConcrete() ? "  public static final" : "");
    writer.format(" %s %s = %s;\n",
      genTranslatedTypeName(constant.getType()),
      constant.getName(),
      genConvReturn(model, constant.getType(), null, model.getType().getName() + "." + constant.getName()));
  }

  protected void startMethodTemplate(String visibility, ClassTypeInfo type, MethodInfo method, String deprecated, PrintWriter writer) {
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
    writer.print("  ");
    writer.print(visibility);
    writer.print(" ");
    if (method.isStaticMethod()) {
      writer.print("static ");
    }
    if (method.getTypeParams().size() > 0) {
      writer.print(method.getTypeParams().stream().map(TypeParamInfo::getName).collect(joining(", ", "<", ">")));
      writer.print(" ");
    }
    writer.print(genReturnTypeDecl(method.getReturnType()));
    writer.print(" ");
    writer.print(method.getName());
    writer.print("(");
    writer.print(method.getParams().stream().map(it -> genParamTypeDecl(it.getType()) + " " + it.getName()).collect(joining(", ")));
    writer.print(")");
  }

  protected boolean isImported(TypeInfo type) {
    switch (type.getKind()) {
      case JSON_OBJECT:
      case JSON_ARRAY:
      case ASYNC_RESULT:
      case HANDLER:
      case LIST:
      case SET:
      case BOXED_PRIMITIVE:
      case STRING:
      case VOID:
      case FUNCTION:
        return true;
      default:
        return false;
    }
  }

  protected final String genTranslatedTypeName(TypeInfo type) {
    return genTypeName(translateType(type));
  }

  protected String genTypeName(TypeInfo type) {
    return type.getName();
  }

  protected TypeInfo translateType(TypeInfo type) {
    if (type.isParameterized()) {
      ParameterizedTypeInfo parameterized = (ParameterizedTypeInfo) type;
      TypeInfo raw = translateType(parameterized.getRaw());
      List<TypeInfo> args = parameterized.getArgs();
      if (raw != parameterized.getRaw()) {
        args = new ArrayList<>(args);
        for (int i = 0;i < args.size();i++) {
          args.set(i, translateType(args.get(i)));
        }
        return new ParameterizedTypeInfo((ClassTypeInfo) raw, parameterized.isNullable(), args);
      }
      for (int i = 0;i < args.size();i++) {
        TypeInfo arg = translateType(args.get(i));
        if (arg != args.get(i)) {
          if (args == parameterized.getArgs()) {
            args = new ArrayList<>(parameterized.getArgs());
          }
          args.set(i, arg);
        }
      }
      if (args != parameterized.getArgs()) {
        return new ParameterizedTypeInfo((ClassTypeInfo) raw, parameterized.isNullable(), args);
      }
    } else if (type.getKind() == API) {
      ApiTypeInfo api = (ApiTypeInfo) type;
      return new ApiTypeInfo(api.translateName(id), api.isConcrete(), api.getParams(), api.getHandlerArg(), api.getModule(), api.isNullable(), api.isProxyGen(), api.getDataObject());
    }
    return type;
  }

  protected final void genSimpleMethod(String visibility, ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer) {
    ClassTypeInfo type = model.getType();
    startMethodTemplate(visibility, type, method, "", writer);
    if (genBody) {
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
          cachedType = genReturnTypeDecl(returnType);
        }
        writer.print("    ");
        writer.print(genReturnTypeDecl(returnType));
        writer.print(" ret = ");
        writer.print(genConvReturn(model, returnType, method, genInvokeDelegate(model, method)));
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
    } else {
      writer.println(";");
    }
    writer.println();
  }

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
      writer.println(" original} non modified interface using Vert.x codegen.");
      writer.println(" */");
    }


  }

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

  protected String genInvokeDelegate(ClassModel model, MethodInfo method) {
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
      ret.append(genConvParam(model, type, method, param.getName()));
      index = index + 1;
    }
    ret.append(")");
    return ret.toString();
  }

  protected boolean isSameType(TypeInfo type, MethodInfo method) {
    ClassKind kind = type.getKind();
    if (type.isDataObjectHolder() || kind.basic || kind.json || kind == ENUM || kind == OTHER || kind == THROWABLE || kind == VOID) {
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

  protected String genParamTypeDecl(TypeInfo type) {
    return genTranslatedTypeName(type);
  }

  protected String genReturnTypeDecl(TypeInfo type) {
    return genTranslatedTypeName(type);
  }

  protected String genConvParam(ClassModel model, TypeInfo type, MethodInfo method, String expr) {
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
          String resultName = genTypeName(resultType);
          return "new Handler<AsyncResult<" + resultName + ">>() {\n" +
            "      public void handle(AsyncResult<" + resultName + "> ar) {\n" +
            "        if (ar.succeeded()) {\n" +
            "          " + expr + ".handle(io.vertx.core.Future.succeededFuture(" + genConvReturn(model, resultType, method, "ar.result()") + "));\n" +
            "        } else {\n" +
            "          " + expr + ".handle(io.vertx.core.Future.failedFuture(ar.cause()));\n" +
            "        }\n" +
            "      }\n" +
            "    }";
        } else {
          String eventName = genTypeName(eventType);
          return "new Handler<" + eventName + ">() {\n" +
            "      public void handle(" + eventName + " event) {\n" +
            "        " + expr + ".handle(" + genConvReturn(model, eventType, method, "event") + ");\n" +
            "      }\n" +
            "    }";
        }
      } else if (kind == FUNCTION) {
        TypeInfo argType = parameterizedTypeInfo.getArg(0);
        TypeInfo retType = parameterizedTypeInfo.getArg(1);
        String argName = genTypeName(argType);
        String retName = genTypeName(retType);
        return "new Function<" + argName + "," + retName + ">() {\n" +
          "      public " + retName + " apply(" + argName + " arg) {\n" +
          "        " + genParamTypeDecl(retType) + " ret = " + expr + ".apply(" + genConvReturn(model, argType, method, "arg") + ");\n" +
          "        return " + genConvParam(model, retType, method, "ret") + ";\n" +
          "      }\n" +
          "    }";
      } else if (kind == LIST || kind == SET) {
        return expr + ".stream().map(elt -> " + genConvParam(model, parameterizedTypeInfo.getArg(0), method, "elt") + ").collect(Collectors.to" + type.getRaw().getSimpleName() + "())";
      } else if (kind == MAP) {
        return expr + ".entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> " + genConvParam(model, parameterizedTypeInfo.getArg(1), method, "e.getValue()") + "))";
      } else if (kind == FUTURE) {
        ParameterizedTypeInfo futureType = (ParameterizedTypeInfo) type;
        return expr + ".map(val -> " + genConvParam(model, futureType.getArg(0), method, "val") + ")";
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

  protected String genTypeArg(TypeVariableInfo typeVar, MethodInfo method) {
    if (typeVar.isClassParam()) {
      return "__typeArg_" + typeVar.getParam().getIndex();
    } else {
      TypeArgExpression typeArg = method.resolveTypeArg(typeVar);
      if (typeArg != null) {
        if (typeArg.isClassType()) {
          return "TypeArg.of(" + typeArg.getParam().getName() + ")";
        } else if (!method.isStaticMethod()) {
          return typeArg.getParam().getName() + ".__typeArg_" + typeArg.getIndex();
        }
      }
    }
    return null;
  }

  protected void genTypeArgDecl(TypeInfo typeArg, MethodInfo method, String typeArgRef, PrintWriter writer) {
    StringBuilder sb = new StringBuilder();
    genTypeArg(typeArg, method, 1, sb);
    writer.print("  private static final TypeArg<");
    writer.print(typeArg.translateName(id));
    writer.print("> ");
    writer.print(typeArgRef);
    writer.print(" = ");
    writer.print(sb);
    writer.println(";");
  }

  protected void genTypeArg(TypeInfo arg, MethodInfo method, int depth, StringBuilder sb) {
    ClassKind argKind = arg.getKind();
    if (argKind == API) {
      sb.append("new TypeArg<").append(arg.translateName(id))
        .append(">(o").append(depth).append(" -> ");
      sb.append(arg.getRaw().translateName(id)).append(".newInstance((").append(arg.getRaw()).append(")o").append(depth);
      if (arg instanceof ParameterizedTypeInfo) {
        ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo) arg;
        List<TypeInfo> args = parameterizedType.getArgs();
        for (int i = 0; i < args.size(); i++) {
          sb.append(", ");
          genTypeArg(args.get(i), method, depth + 1, sb);
        }
      }
      sb.append(")");
      sb.append(", o").append(depth).append(" -> o").append(depth).append(".getDelegate())");
    } else {
      String typeArg = "TypeArg.unknown()";
      if (argKind == OBJECT && arg.isVariable()) {
        String resolved = genTypeArg((TypeVariableInfo) arg, method);
        if (resolved != null) {
          typeArg = resolved;
        }
      }
      sb.append(typeArg);
    }
  }

  private String genTypeArg(TypeInfo arg, MethodInfo method) {
    Map<TypeInfo, String> typeArgMap = methodTypeArgMap.get(method);
    if (typeArgMap != null) {
      String typeArgRef = typeArgMap.get(arg);
      if (typeArgRef != null) {
        return typeArgRef;
      }
    }
    StringBuilder sb = new StringBuilder();
    genTypeArg(arg, method, 0, sb);
    return sb.toString();
  }

  protected String genConvReturn(ClassModel model, TypeInfo type, MethodInfo method, String expr) {
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
      StringBuilder tmp = new StringBuilder(type.getRaw().translateName(id));
      tmp.append(".newInstance((");
      tmp.append(type.getRaw());
      tmp.append(")");
      tmp.append(expr);
      if (type.isParameterized()) {
        ParameterizedTypeInfo parameterizedTypeInfo = (ParameterizedTypeInfo) type;
        for (TypeInfo arg : parameterizedTypeInfo.getArgs()) {
          tmp.append(", ");
          tmp.append(genTypeArg(arg, method));
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
          return "new Handler<AsyncResult<" + genParamTypeDecl(tutu) + ">>() {\n" +
            "      public void handle(AsyncResult<" + genParamTypeDecl(tutu) + "> ar) {\n" +
            "        if (ar.succeeded()) {\n" +
            "          " + expr + ".handle(io.vertx.core.Future.succeededFuture(" + genConvParam(model, tutu, method, "ar.result()") + "));\n" +
            "        } else {\n" +
            "          " + expr + ".handle(io.vertx.core.Future.failedFuture(ar.cause()));\n" +
            "        }\n" +
            "      }\n" +
            "    }";
        } else {
          return "new Handler<" + genParamTypeDecl(abc) + ">() {\n" +
            "      public void handle(" + genParamTypeDecl(abc) + " event) {\n" +
            "          " + expr + ".handle(" + genConvParam(model, abc, method, "event") + ");\n" +
            "      }\n" +
            "    }";
        }
      } else if (kind == LIST || kind == SET) {
        return expr + ".stream().map(elt -> " + genConvReturn(model, parameterizedTypeInfo.getArg(0), method, "elt") + ").collect(Collectors.to" + type.getRaw().getSimpleName() + "())";
      } else if (kind == MAP) {
        return expr + ".entrySet().stream().collect(Collectors.toMap(_e -> _e.getKey(), _e -> " + genConvReturn(model, parameterizedTypeInfo.getArg(1), method, "_e.getValue()") + "))";
      } else if (kind == FUTURE) {
        ParameterizedTypeInfo futureType = (ParameterizedTypeInfo) type;
        return expr + ".map(val -> " + genConvReturn(model, futureType.getArg(0), method, "val") + ")";
      }
    }
    return expr;
  }

  private String genTypeParamsDecl(ClassTypeInfo type) {
    if (type.getParams().size() > 0) {
      return type.getParams().stream().map(TypeParamInfo::getName).collect(joining(",", "<", ">"));
    } else {
      return "";
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
    return "{@link " + rawType.getName() + "}";
  }

  protected boolean containsTypeVariableArgument(TypeInfo type) {
    if (type.isVariable()) {
      return true;
    } else if (type.isParameterized()) {
      List<TypeInfo> typeArgs = ((ParameterizedTypeInfo)type).getArgs();
      for (TypeInfo typeArg : typeArgs) {
        if (typeArg.isVariable()) {
          return true;
        } else if (typeArg.isParameterized() && containsTypeVariableArgument(typeArg)) {
          return true;
        }
      }
    }
    return false;
  }
}
