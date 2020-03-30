package io.vertx.lang.rx;

import io.vertx.codegen.*;
import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.codegen.doc.Doc;
import io.vertx.codegen.doc.Tag;
import io.vertx.codegen.doc.Token;
import io.vertx.codegen.type.*;

import javax.lang.model.element.Element;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vertx.codegen.type.ClassKind.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public abstract class AbstractRxGenerator extends Generator<ClassModel> {

  private String id;
  private Map<MethodInfo, Map<TypeInfo, String>> methodTypeArgMap = new HashMap<>();

  public AbstractRxGenerator(String id) {
    this.id = id;
    this.kinds = Collections.singleton("class");
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

    writer.println("import java.util.Map;");
    genRxImports(model, writer);

    writer.println();
    generateDoc(model, writer);
    writer.println();

    writer.print("@io.vertx.lang.rx.RxGen(");
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

    if (model.isConcrete() && model.getConcreteSuperType() != null) {
      writer.print(" extends ");
      writer.print(genTypeName(model.getConcreteSuperType()));
    }

    List<String> interfaces = new ArrayList<>();
    if ("io.vertx.core.buffer.Buffer".equals(type.getName())) {
      interfaces.add("io.vertx.core.shareddata.impl.ClusterSerializable");
    }
    interfaces.addAll(model.getAbstractSuperTypes().stream().map(this::genTypeName).collect(toList()));
    if (model.isHandler()) {
      interfaces.add("io.vertx.core.Handler<" + genTypeName(model.getHandlerArg()) + ">");
    }
    if (model.isIterable()) {
      interfaces.add("java.lang.Iterable<" + genTypeName(model.getIterableArg()) + ">");
    }
    if (model.isIterator()) {
      interfaces.add("java.util.Iterator<" + genTypeName(model.getIteratorArg()) + ">");
    }
    if (model.isFunction()) {
      TypeInfo[] functionArgs = model.getFunctionArgs();
      interfaces.add("java.util.function.Function<" + genTypeName(functionArgs[0]) + ", " + genTypeName(functionArgs[1]) + ">");
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
        genMethod(model, method, Collections.emptyList(), false, writer);
      }

      if (type.getRaw().getName().equals("io.vertx.core.streams.ReadStream")) {
        genReadStream(type.getParams(), writer);
      }
    }

    // Gen newInstance
    writer.println();
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

    // Gen parameterized newInstance
    if (type.getParams().size() > 0) {
      writer.println();
      writer.print("  public static ");
      writer.print(genTypeParamsDecl(type));
      writer.print(" ");
      writer.print(type.getSimpleName());
      writer.print(genTypeParamsDecl(type));
      writer.print(" newInstance(");
      writer.print(type.getName());
      writer.print(" arg");
      for (TypeParamInfo typeParam : type.getParams()) {
        writer.print(", io.vertx.lang.rx.TypeArg<");
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

  private void generateIterableMethod(ClassModel model, PrintWriter writer) {
    if (model.getMethods().stream().noneMatch(it -> it.getParams().isEmpty() && "iterator".equals(it.getName()))) {
      TypeInfo iterableArg = model.getIterableArg();

      writer.println("  @Override");
      writer.printf("  public java.util.Iterator<%s> iterator() {%n", genTypeName(iterableArg));

      if (iterableArg.getKind() == ClassKind.API) {
        writer.format("    java.util.function.Function<%s, %s> conv = %s::newInstance;%n", iterableArg.getName(), genTypeName(iterableArg.getRaw()), genTypeName(iterableArg));
        writer.println("    return new io.vertx.lang.rx.MappingIterator<>(delegate.iterator(), conv);");
      } else if (iterableArg.isVariable()) {
        String typeVar = iterableArg.getSimpleName();
        writer.format("    java.util.function.Function<%s, %s> conv = (java.util.function.Function<%s, %s>) __typeArg_0.wrap;%n", typeVar, typeVar, typeVar, typeVar);
        writer.println("    return new io.vertx.lang.rx.MappingIterator<>(delegate.iterator(), conv);");
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
      writer.printf("  public %s next() {%n", genTypeName(iteratorArg));

      if (iteratorArg.getKind() == ClassKind.API) {
        writer.format("    return %s.newInstance(delegate.next());%n", genTypeName(iteratorArg));
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
      writer.printf("  public %s apply(%s in) {%n", genTypeName(outArg), genTypeName(inArg));
      writer.printf("    %s ret;%n", outArg.getName());

      if (inArg.getKind() == ClassKind.API) {
        writer.println("    ret = getDelegate().apply(in.getDelegate());");
      } else if (inArg.isVariable()) {
        String typeVar = inArg.getSimpleName();
        writer.format("    java.util.function.Function<%s, %s> inConv = (java.util.function.Function<%s, %s>) __typeArg_0.unwrap;%n", typeVar, typeVar, typeVar, typeVar);
        writer.println("    ret = getDelegate().apply(inConv.apply);");
      } else {
        writer.println("    ret = getDelegate().apply(in);");
      }

      if (outArg.getKind() == ClassKind.API) {
        writer.format("    java.util.function.Function<%s, %s> outConv = %s::newInstance;%n", outArg.getName(), genTypeName(outArg.getRaw()), genTypeName(outArg));
        writer.println("    return outConv.apply(ret);");
      } else if (outArg.isVariable()) {
        String typeVar = outArg.getSimpleName();
        writer.format("    java.util.function.Function<%s, %s> outConv = (java.util.function.Function<%s, %s>) __typeArg_1.wrap;%n", typeVar, typeVar, typeVar, typeVar);
        writer.println("    return outConv.apply(ret);");
      } else {
        writer.println("    return delegate.iterator();");
      }

      writer.println("  }");
      writer.println();
    }
  }

  protected abstract void genReadStream(List<? extends TypeParamInfo> typeParams, PrintWriter writer);

  private void generateClassBody(ClassModel model, String constructor, PrintWriter writer) {
    ClassTypeInfo type = model.getType();
    String simpleName = type.getSimpleName();
    if (model.isConcrete()) {
      writer.print("  public static final io.vertx.lang.rx.TypeArg<");
      writer.print(simpleName);
      writer.print("> __TYPE_ARG = new io.vertx.lang.rx.TypeArg<>(");
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
      writer.print(typeParams.stream().map(TypeParamInfo.Class::getName).collect(joining(",", "<", ">")));
    }
    writer.println(" delegate;");

    for (TypeParamInfo.Class typeParam : typeParams) {
      writer.print("  public final io.vertx.lang.rx.TypeArg<");
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
      writer.print(" = io.vertx.lang.rx.TypeArg.unknown();");
    }
    writer.println("  }");
    writer.println();

    // Object constructor
    writer.print("  public ");
    writer.print(constructor);
    writer.print("(Object delegate");
    for (TypeParamInfo.Class typeParam : typeParams) {
      writer.print(", io.vertx.lang.rx.TypeArg<");
      writer.print(typeParam.getName());
      writer.print("> typeArg_");
      writer.print(typeParam.getIndex());
    }
    writer.println(") {");
    if (model.isConcrete() && model.getConcreteSuperType() != null) {
      // This is incorrect it will not pass the generic type in some case
      // we haven't yet ran into that bug
      writer.print("    super((");
      writer.print(Helper.getNonGenericType(model.getIfaceFQCN()));
      writer.println(")delegate);");
    }
    writer.print("    this.delegate = (");
    writer.println(Helper.getNonGenericType(model.getIfaceFQCN()));
    writer.println(")delegate;");
    for (TypeParamInfo.Class typeParam : typeParams) {
      writer.print("    this.__typeArg_");
      writer.print(typeParam.getIndex());
      writer.print(" = typeArg_");
      writer.print(typeParam.getIndex());
      writer.println(";");
    }
    writer.println("  }");
    writer.println();

    writer.print("  public ");
    writer.print(type.getName());
    writer.println(" getDelegate() {");
    writer.println("    return delegate;");
    writer.println("  }");
    writer.println();

    if (model.isReadStream()) {
      genToObservable(model.getReadStreamArg(), writer);
    }
    if (model.isWriteStream()) {
      genToSubscriber(model.getWriteStreamArg(), writer);
    }
    List<MethodInfo> methods = new ArrayList<>();
    methods.addAll(model.getMethods());
    methods.addAll(model.getAnyJavaTypeMethods());
    int count = 0;
    for (MethodInfo method : methods) {
      TypeInfo returnType = method.getReturnType();
      if (returnType instanceof ParameterizedTypeInfo) {
        ParameterizedTypeInfo parameterizedType = (ParameterizedTypeInfo)returnType;
        List<TypeInfo> typeArgs = parameterizedType.getArgs();
        Map<TypeInfo, String> typeArgMap = new HashMap<>();
        for (TypeInfo typeArg : typeArgs) {
          if (typeArg.getKind() == API && !containsTypeVariableArgument(typeArg)) {
            String typeArgRef = "TYPE_ARG_" + count++;
            typeArgMap.put(typeArg, typeArgRef);
            genTypeArgDecl(typeArg, method, typeArgRef, writer);
          }
        }
        methodTypeArgMap.put(method, typeArgMap);
      }
    }
    // Cosmetic space
    if (methodTypeArgMap.size() > 0) {
      writer.println();
    }

    List<String> cacheDecls = new ArrayList<>();
    for (MethodInfo method : methods) {
      genMethods(model, method, cacheDecls, true, writer);
    }

    for (ConstantInfo constant : model.getConstants()) {
      genConstant(model, constant, writer);
    }

    for (String cacheDecl : cacheDecls) {
      writer.print("  ");
      writer.print(cacheDecl);
      writer.println(";");
    }
  }

  protected abstract void genToObservable(TypeInfo streamType, PrintWriter writer);

  protected abstract void genToSubscriber(TypeInfo streamType, PrintWriter writer);

  protected abstract void genMethods(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer);

  protected abstract void genRxMethod(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer);

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
  protected final void genMethod(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer) {
    if (method.getKind() == MethodKind.FUTURE) {
      genSimpleMethod(model, method, cacheDecls, genBody, writer);

      // Generate the missing method
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

  private void genConstant(ClassModel model, ConstantInfo constant, PrintWriter writer) {
    Doc doc = constant.getDoc();
    if (doc != null) {
      writer.println("  /**");
      Token.toHtml(doc.getTokens(), "   *", this::renderLinkToHtml, "\n", writer);
      writer.println("   */");
    }
    writer.print(model.isConcrete() ? "  public static final" : "");
    writer.format(" %s %s = %s;\n",
      genTypeName(constant.getType()),
      constant.getName(),
      genConvReturn(constant.getType(), null, model.getType().getName() + "." + constant.getName()));
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
      writer.print(method.getTypeParams().stream().map(TypeParamInfo::getName).collect(joining(", ", "<", ">")));
      writer.print(" ");
    }
    writer.print(genTypeName(method.getReturnType()));
    writer.print(" ");
    writer.print(method.getName());
    writer.print("(");
    writer.print(method.getParams().stream().map(it -> genTypeName(it.getType()) + " " + it.getName()).collect(joining(", ")));
    writer.print(")");

  }

  protected final String genTypeName(TypeInfo type) {
    if (type.isParameterized()) {
      ParameterizedTypeInfo pt = (ParameterizedTypeInfo) type;
      return genTypeName(pt.getRaw()) + pt.getArgs().stream().map(this::genTypeName).collect(joining(", ", "<", ">"));
    } else if (type.getKind() == ClassKind.API) {
      return type.translateName(id);
    } else {
      return type.getSimpleName();
    }
  }

  protected String genFutureMethodName(MethodInfo method) {
    return "rx" + Character.toUpperCase(method.getName().charAt(0)) + method.getName().substring(1);
  }

  protected final void genSimpleMethod(ClassModel model, MethodInfo method, List<String> cacheDecls, boolean genBody, PrintWriter writer) {
    ClassTypeInfo type = model.getType();
    startMethodTemplate(type, method, "", writer);
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
          cachedType = genTypeName(returnType);
        }
        writer.print("    ");
        writer.print(genTypeName(returnType));
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
    } else {
      writer.println(";");
    }
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
          "        " + genTypeName(retType) + " ret = " + expr + ".apply(" + genConvReturn(argType, method, "arg") + ");\n" +
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
          return "io.vertx.lang.rx.TypeArg.of(" + typeArg.getParam().getName() + ")";
        } else {
          return typeArg.getParam().getName() + ".__typeArg_" + typeArg.getIndex();
        }
      }
    }
    return null;
  }

  private void genTypeArgDecl(TypeInfo typeArg, MethodInfo method, String typeArgRef, PrintWriter writer) {
    StringBuilder sb = new StringBuilder();
    genTypeArg(typeArg, method, 1, sb);
    writer.print("  private static final io.vertx.lang.rx.TypeArg<");
    writer.print(typeArg.translateName(id));
    writer.print("> ");
    writer.print(typeArgRef);
    writer.print(" = ");
    writer.print(sb);
    writer.println(";");
  }

  private void genTypeArg(TypeInfo arg, MethodInfo method, int depth, StringBuilder sb) {
    ClassKind argKind = arg.getKind();
    if (argKind == API) {
      sb.append("new io.vertx.lang.rx.TypeArg<").append(arg.translateName(id))
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
      String typeArg = "io.vertx.lang.rx.TypeArg.unknown()";
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
    ClassKind kind = arg.getKind();
    if (kind == ClassKind.API) {
      StringBuilder sb = new StringBuilder();
      genTypeArg(arg, method, 0, sb);
      return sb.toString();
    } else {
      String typeArg = "io.vertx.lang.rx.TypeArg.unknown()";
      if (arg.isVariable()) {
        String resolved = genTypeArg((TypeVariableInfo) arg, method);
        if (resolved != null) {
          typeArg = resolved;
        }
      }
      return typeArg;
    }
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
          return "new Handler<AsyncResult<" + genTypeName(tutu) + ">>() {\n" +
            "      public void handle(AsyncResult<" + genTypeName(tutu) + "> ar) {\n" +
            "        if (ar.succeeded()) {\n" +
            "          " + expr + ".handle(io.vertx.core.Future.succeededFuture(" + genConvParam(tutu, method, "ar.result()") + "));\n" +
            "        } else {\n" +
            "          " + expr + ".handle(io.vertx.core.Future.failedFuture(ar.cause()));\n" +
            "        }\n" +
            "      }\n" +
            "    }";
        } else {
          return "new Handler<" + genTypeName(abc) + ">() {\n" +
            "      public void handle(" + genTypeName(abc) + " event) {\n" +
            "          " + expr + ".handle(" + genConvParam(abc, method, "event") + ");\n" +
            "      }\n" +
            "    }";
        }
      } else if (kind == LIST || kind == SET) {
        return expr + ".stream().map(elt -> " + genConvReturn(parameterizedTypeInfo.getArg(0), method, "elt") + ").collect(java.util.stream.Collectors.to" + type.getRaw().getSimpleName() + "())";
      } else if (kind == MAP) {
        return expr + ".entrySet().stream().collect(java.util.stream.Collectors.toMap(_e -> _e.getKey(), _e -> " + genConvReturn(parameterizedTypeInfo.getArg(1), method, "_e.getValue()") + "))";
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

  private boolean containsTypeVariableArgument(TypeInfo type) {
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
