/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.reactivex.test;

import io.vertx.reactivex.codegen.extra.Bar;
import io.vertx.reactivex.codegen.extra.Foo;
import io.vertx.reactivex.codegen.extra.Generic1;
import io.vertx.reactivex.codegen.extra.Generic2;
import io.vertx.reactivex.codegen.extra.NestedParameterizedType;
import org.junit.Test;

/**
 * @author Thomas Segismont
 */
public class GenericsTest {

  @Test
  public void testNestedParameterizedTypes() {
    // Test we don't get class cast when types are unwrapped or rewrapped
    Generic2<Generic1<Foo>, Generic2<Foo, Bar>> o = NestedParameterizedType.someGeneric();
    Generic1<Foo> value1 = o.getValue1();
    Foo nested1 = value1.getValue();
    value1.setValue(nested1);
    Generic2<Foo, Bar> value2 = o.getValue2();
    o.setValue2(value2);
    Foo nested2 = value2.getValue1();
    value2.setValue1(nested2);
    Bar nested3 = value2.getValue2();
    value2.setValue2(nested3);
  }
}
