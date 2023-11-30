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

import io.vertx.core.net.impl.SocketAddressImpl;
import io.vertx.core.buffer.Buffer;
import io.vertx.rxjava3.core.net.SocketAddress;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Thomas Segismont
 */
public class ToStringTest {

  @Test
  public void testBufferToString() {
    String string = "The quick brown fox jumps over the lazy dog";
    assertEquals(string, Buffer.buffer(string).toString());
  }

  @Test
  public void testSocketAddressToString() {
    io.vertx.core.net.SocketAddress socketAddress = new SocketAddressImpl(8888, "guest");
    SocketAddress rxSocketAddress = SocketAddress.newInstance(socketAddress);
    assertEquals(socketAddress.toString(), rxSocketAddress.toString());
  }
}
