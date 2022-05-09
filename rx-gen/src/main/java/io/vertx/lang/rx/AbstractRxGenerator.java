package io.vertx.lang.rx;

import static io.vertx.codegen.type.ClassKind.API;
import static java.util.stream.Collectors.joining;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vertx.codegen.ClassModel;
import io.vertx.codegen.ConstantInfo;
import io.vertx.codegen.Helper;
import io.vertx.codegen.MethodInfo;
import io.vertx.codegen.TypeParamInfo;
import io.vertx.codegen.type.ClassTypeInfo;
import io.vertx.codegen.type.ParameterizedTypeInfo;
import io.vertx.codegen.type.TypeInfo;

public abstract class AbstractRxGenerator extends AbstractBaseVertxGenerator {

  public AbstractRxGenerator(String id) {
    super(id);
  }

  protected void generateClassBody(ClassModel model, String constructor, PrintWriter writer) {
    ClassTypeInfo type = model.getType();
    String simpleName = type.getSimpleName();
    if (model.isConcrete()) {
      writer.print("  public static final TypeArg<");
      writer.print(simpleName);
      writer.print("> __TYPE_ARG = new TypeArg<>(");
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
      writer.print("  public final TypeArg<");
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
      writer.print(" = TypeArg.unknown();");
    }
    writer.println("  }");
    writer.println();

    // Object constructor
    writer.print("  public ");
    writer.print(constructor);
    writer.print("(Object delegate");
    for (TypeParamInfo.Class typeParam : typeParams) {
      writer.print(", TypeArg<");
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
    writer.print(Helper.getNonGenericType(model.getIfaceFQCN()));
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

  @Override
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

  protected abstract void genToObservable(TypeInfo streamType, PrintWriter writer);

  protected abstract void genToSubscriber(TypeInfo streamType, PrintWriter writer);

  protected String genFutureMethodName(MethodInfo method) {
    return "rx" + Character.toUpperCase(method.getName().charAt(0)) + method.getName().substring(1);
  }

  @Override
  protected void generateExtraAnnotation(ClassModel model, PrintWriter writer, ClassTypeInfo type) {
    writer.print("@RxGen(");
    writer.print(type.getName());
    writer.println(".class)");
  }

}
