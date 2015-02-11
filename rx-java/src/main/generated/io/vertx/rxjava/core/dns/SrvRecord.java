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

package io.vertx.rxjava.core.dns;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;

/**
 * Represent a Service-Record (SRV) which was resolved for a domain.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class SrvRecord {

  final io.vertx.core.dns.SrvRecord delegate;

  public SrvRecord(io.vertx.core.dns.SrvRecord delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Returns the priority for this service record.
   */
  public int priority() {
    int ret = this.delegate.priority();
    return ret;
  }

  /**
   * Returns the weight of this service record.
   */
  public int weight() {
    int ret = this.delegate.weight();
    return ret;
  }

  /**
   * Returns the port the service is running on.
   */
  public int port() {
    int ret = this.delegate.port();
    return ret;
  }

  /**
   * Returns the name for the server being queried.
   */
  public String name() {
    String ret = this.delegate.name();
    return ret;
  }

  /**
   * Returns the protocol for the service being queried (i.e. "_tcp").
   */
  public String protocol() {
    String ret = this.delegate.protocol();
    return ret;
  }

  /**
   * Returns the service's name (i.e. "_http").
   */
  public String service() {
    String ret = this.delegate.service();
    return ret;
  }

  /**
   * Returns the name of the host for the service.
   */
  public String target() {
    String ret = this.delegate.target();
    return ret;
  }


  public static SrvRecord newInstance(io.vertx.core.dns.SrvRecord arg) {
    return new SrvRecord(arg);
  }
}
