/*
 * Copyright 2021 Red Hat, Inc.
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

package io.vertx.junit5.rxjava3;

import io.vertx.core.VertxException;
import io.vertx.junit5.ParameterClosingConsumer;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxExtensionParameterProvider;
import io.vertx.rxjava3.core.Vertx;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import java.util.concurrent.TimeoutException;

import static io.vertx.junit5.VertxExtension.DEFAULT_TIMEOUT_DURATION;
import static io.vertx.junit5.VertxExtension.DEFAULT_TIMEOUT_UNIT;

/**
 * RxJava 3 Vertx parameter provider.
 */
public class VertxParameterProvider implements VertxExtensionParameterProvider<Vertx> {
  @Override
  public Class<Vertx> type() {
    return Vertx.class;
  }

  @Override
  public String key() {
    return VertxExtension.VERTX_INSTANCE_KEY;
  }

  @Override
  public Vertx newInstance(ExtensionContext extensionContext, ParameterContext parameterContext) {
    return Vertx.vertx();
  }

  @Override
  public ParameterClosingConsumer<Vertx> parameterClosingConsumer() {
    return vertx -> {
      try {
        if (!vertx.rxClose().blockingAwait(DEFAULT_TIMEOUT_DURATION, DEFAULT_TIMEOUT_UNIT)) {
          throw new TimeoutException("Closing the Vertx context timed out");
        }
      } catch (Throwable err) {
        if (err instanceof RuntimeException) {
          throw new VertxException(err.getCause());
        } else if (err instanceof Exception) {
          throw err;
        } else {
          throw new VertxException(err);
        }
      }
    };
  }
}
