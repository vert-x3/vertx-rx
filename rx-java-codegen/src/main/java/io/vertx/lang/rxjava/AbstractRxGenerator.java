package io.vertx.lang.rxjava;

import io.vertx.codegen.*;
import io.vertx.codegen.Helper;
import io.vertx.codegen.doc.Doc;
import io.vertx.codegen.doc.Tag;
import io.vertx.codegen.doc.Token;
import io.vertx.codegen.type.ApiTypeInfo;
import io.vertx.codegen.type.ClassKind;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.TypeInfo;

import javax.lang.model.element.Element;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractRxGenerator extends Generator<ClassModel> {
  private String id;

  public AbstractRxGenerator(String id) {
    this.id = id;
    this.kinds = Collections.singleton("class");
  }

  @Override
  public String relativeFilename(ClassModel model) {
    ModuleInfo module = model.getModule();
    return module.translateQualifiedName(model.getFqn(), id) + ".java";
  }

  @Override
  public String render(ClassModel model, int index, int size, Map<String, Object> session) {
    StringWriter sw = new StringWriter();
    PrintWriter writer = new PrintWriter(sw);

    ClassTypeInfo type = model.getType();

    generateLicense(writer);

    writer.print("package ");
    writer.print(type.translatePackageName(id));
    writer.println(";");
    writer.println();

    writer.println("import java.util.Map;");
    generateImport(model, writer);
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
    writer.print(model.getIfaceSimpleName());

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
        writer.print("implements ");
      } else {
        writer.print("extends ");
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
      if (methods.stream().anyMatch(it -> it.getParams().isEmpty() && "toString".equals(it.getName()))) {
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
      writer.println();

      writer.println("  @Override");
      writer.println("  public int hashCode() {");
      writer.println("    return delegate.hashCode();");
      writer.println("  }");
    } else {

    }

    writer.println("}");

    return sw.toString();
  }

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
      writer.print("public final io.vertx.lang.");
      writer.print(id);
      writer.print(".TypeArg<");
      writer.print(typeParam.getName());
      writer.print("> __typeArg_");
      writer.print(typeParam.getIndex());
      writer.println(";");
    }
    writer.println();

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
      writer.print("this.__typeArg_");
      writer.print(typeParam.getIndex());
      writer.print(" = io.vertx.lang.");
      writer.print(id);
      writer.print(".TypeArg.unknown();");
    }
    writer.println("}");
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
        writer.print("this.__typeArg_");
        writer.print(typeParam.getIndex());
        writer.print(" = typeArg_");
        writer.print(typeParam.getIndex());
        writer.print(";");
      }
      writer.println("}");
      writer.println();
    }

    writer.print("  public ");
    writer.print(type.getName());
    writer.println(" getDelegate() {");
    writer.println("    return delegate;");
    writer.println("}");
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
      writer.print("");
      writer.println(cacheDecl);
    }
  }

  protected abstract void genToObservable(ApiTypeInfo type, PrintWriter writer);

  protected abstract void genMethods(ClassModel model, MethodInfo method, List<String> cacheDecls, PrintWriter writer);

  protected abstract void genRxMethod(ClassModel model, MethodInfo method, PrintWriter writer);

  protected final void genMethod(ClassModel model, MethodInfo method, PrintWriter writer) {
    genSimpleMethod(model.getType(), method, writer);
    if (method.getKind() == MethodKind.FUTURE) {
      genRxMethod(model, method, writer);
    }
  }

  protected void startMethodTemplate(ClassTypeInfo type, MethodInfo method, String deprecated, PrintWriter writer) {
    Doc doc = method.getDoc();
    if (doc != null) {
      writer.println("  /**");
      Token.toHtml(doc.getTokens(), "   *", renderLinkToHtml(type), "\n", writer);
    }
    for (ParamInfo param : method.getParams()) {
      writer.print("   * @param ");
      writer.print(param.getName());
      writer.print("");
      if (param.getDescription() != null) {
        Token.toHtml(param.getDescription().getTokens(), "", renderLinkToHtml(type), "\n", writer);
      }
      writer.println();
    }
    if (!method.getReturnType().getName().equals("void")) {
      writer.print("   * @return ");
      if (method.getReturnDescription() != null) {
        Token.toHtml(method.getReturnDescription().getTokens(), "", renderLinkToHtml(type), "\n", writer);
      }
      writer.println();
    }
    if (deprecated != null && deprecated.length() > 0) {
      writer.print("   * @deprecated ");
      writer.println(deprecated);
    }
    writer.println("   */");
    if (deprecated != null && deprecated.length() > 0) {
      writer.println("  @Deprecated()");
    }
    writer.print("  public ");
    if (method.isStaticMethod()) {
      writer.print("static ");
    }
    if (method.getTypeParams().size() > 0) {
      writer.print(method.getTypeParams().stream().map(TypeParamInfo::getName).collect(Collectors.joining(", ", "<", ">")));
    }
    writer.print(method.getReturnType().getSimpleName());
    writer.print(" ");
    writer.print(method.getName());
    writer.print("(");
    writer.print(method.getParams().stream().map(it -> it.getType().getSimpleName() + " " + it.getName()).collect(Collectors.joining(", ")));
    writer.print(")");

  }

  protected String genFutureMethodName(MethodInfo method) {
    return "rx" + java.lang.Character.toUpperCase(method.getName().charAt(0)) + method.getName().substring(1);
  }

  private void genSimpleMethod(ClassTypeInfo type, MethodInfo method, PrintWriter writer) {
    startMethodTemplate(type, method, "", writer);
    if (method.isFluent()) {
      writer.print("    ");
      genInvokeDelegate(method, writer);
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
      genInvokeDelegate(method, writer);
      writer.println(";");
    } else {
      writer.print("");
      if (method.isCacheReturn()) {

      }
    }
  }

  private void genInvokeDelegate(MethodInfo method, PrintWriter writer) {

  }

  private void generateDoc(ClassModel model, PrintWriter writer) {
    ClassTypeInfo type = model.getType();
    Doc doc = model.getDoc();
    if (doc != null) {
      writer.println("/**");
      Token.toHtml(doc.getTokens(), " *", this.renderLinkToHtml(type), "\n", writer);
      writer.println(" *");
      writer.println(" * <p/>");
      writer.println(" * NOTE: This class has been automatically generated from the {@link @{type.name} original} non RX-ified interface using Vert.x codegen.");
      writer.println(" */");
    }


  }

  protected void generateImport(ClassModel model, PrintWriter writer) {
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

  private Function<Tag.Link, String> renderLinkToHtml(ClassTypeInfo type) {
    return link -> {
      ClassTypeInfo rawType = link.getTargetType().getRaw();
      if (rawType.getModule() != null) {
        String label = link.getLabel().trim();
        if (rawType.getKind() == ClassKind.DATA_OBJECT) {
          return "{@link " + rawType.getName() + "}";
        } else {
          if (type.getKind() == ClassKind.API) {
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
    };
  }
}
