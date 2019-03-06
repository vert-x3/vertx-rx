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

import io.vertx.reactivex.codegen.extra.AnotherInterface;
import org.junit.Test;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

/**
 * @author Thomas Segismont
 */
public class EqualityTest {

  @Test
  public void testAnotherInterfaceEquality() {
    AnotherInterface ai1 = AnotherInterface.create();
    AnotherInterface ai2 = AnotherInterface.create();
    assertNotSame(ai1, ai2);
    assertNotEquals(ai1, ai2);
  }

  @Test
  public void testAnotherInterfaceSet() {
    AnotherInterface ai1 = AnotherInterface.create();
    AnotherInterface ai2 = AnotherInterface.create();
    assertEquals(2, Stream.of(ai1, ai2).collect(toSet()).size());
  }
}
