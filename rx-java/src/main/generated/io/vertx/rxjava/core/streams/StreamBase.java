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

package io.vertx.rxjava.core.streams;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.core.Handler;

/**
 * Base interface for a stream.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public interface StreamBase {

  Object getDelegate();

  StreamBase exceptionHandler(Handler<Throwable> handler);


  public static StreamBase newInstance(io.vertx.core.streams.StreamBase arg) {
    return new StreamBaseImpl(arg);
  }
}

class StreamBaseImpl implements StreamBase {
  final io.vertx.core.streams.StreamBase delegate;

  public StreamBaseImpl(io.vertx.core.streams.StreamBase delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Set an exception handler.
   *
   * @param handler the handler
   * @return a reference to this, so the API can be used fluently
   */
  public StreamBase exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

}
