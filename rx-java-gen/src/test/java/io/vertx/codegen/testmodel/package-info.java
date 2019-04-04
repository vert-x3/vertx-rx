/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@ModuleGen(
  name = "testmodel",
  groupPackage = "io.vertx",
  codecs = {
    MyPojoToJsonArray.MyPojoToJsonArrayCodec.class,
    MyPojoToJsonObject.MyPojoToJsonObjectCodec.class,
    MyPojoToInteger.MyPojoToIntegerCodec.class,
    ZonedDateTimeCodec.class
  }
)
package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.ModuleGen;
