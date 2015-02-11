/*
 * Copyright 2014 Red Hat, Inc.
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

package io.vertx.rxjava.core.datagram;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.net.SocketAddress;

/**
 * A received datagram packet (UDP) which contains the data and information about the sender of the data itself.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class DatagramPacket {

  final io.vertx.core.datagram.DatagramPacket delegate;

  public DatagramPacket(io.vertx.core.datagram.DatagramPacket delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Returns the {@link io.vertx.core.net.SocketAddress} of the sender that sent
   * this {@link io.vertx.core.datagram.DatagramPacket}.
   *
   * @return the address of the sender
   */
  public SocketAddress sender() {
    SocketAddress ret= SocketAddress.newInstance(this.delegate.sender());
    return ret;
  }

  /**
   * Returns the data of the {@link io.vertx.core.datagram.DatagramPacket}
   *
   * @return the data
   */
  public Buffer data() {
    Buffer ret= Buffer.newInstance(this.delegate.data());
    return ret;
  }


  public static DatagramPacket newInstance(io.vertx.core.datagram.DatagramPacket arg) {
    return new DatagramPacket(arg);
  }
}
