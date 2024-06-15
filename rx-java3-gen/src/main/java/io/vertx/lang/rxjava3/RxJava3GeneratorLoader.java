package io.vertx.lang.rxjava3;

import io.vertx.codegen.processor.Generator;
import io.vertx.codegen.processor.GeneratorLoader;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.stream.Stream;

public class RxJava3GeneratorLoader implements GeneratorLoader {
  @Override
  public Stream<Generator<?>> loadGenerators(ProcessingEnvironment processingEnv) {
    return Stream.of(new RxJava3Generator());
  }
}
