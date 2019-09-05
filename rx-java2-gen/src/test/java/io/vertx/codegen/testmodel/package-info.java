/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@ModuleGen(
  name = "testmodel",
  groupPackage = "io.vertx",
  mappers = {
    MyPojoToJsonArray.MyPojoToJsonArrayMapper.class,
    MyPojoToJsonObject.MyPojoToJsonObjectMapper.class,
    MyPojoToInteger.MyPojoToIntegerMapper.class,
    ZonedDateTimeMapper.class
  }
)
package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.ModuleGen;
