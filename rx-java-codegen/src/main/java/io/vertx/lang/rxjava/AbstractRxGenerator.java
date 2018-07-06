package io.vertx.lang.rxjava;

import io.vertx.codegen.ClassModel;
import io.vertx.codegen.Generator;
import io.vertx.codegen.ModuleInfo;
import io.vertx.codegen.doc.Doc;
import io.vertx.codegen.doc.Tag;
import io.vertx.codegen.doc.Token;
import io.vertx.codegen.type.ClassKind;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.TypeInfo;

import javax.lang.model.element.Element;
import java.io.PrintWriter;
import java.io.StringWriter;
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
          writer.print("implements");
        } else {
          writer.print("extends");
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


    writer.println("}");

    return sw.toString();
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
