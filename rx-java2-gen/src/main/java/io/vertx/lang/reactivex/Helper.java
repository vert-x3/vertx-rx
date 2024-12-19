/*
 * Copyright 2024 Red Hat, Inc.
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

package io.vertx.lang.reactivex;

import io.vertx.core.Handler;
import io.vertx.lang.rx.DelegatingHandler;
import io.vertx.lang.rx.RxDelegate;
import io.vertx.lang.rx.RxGen;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Helper {

  /**
   * Unwrap the type used in RxJava.
   *
   * @param type the type to unwrap
   * @return the unwrapped type
   */
  public static Class unwrap(Class<?> type) {
    if (type != null) {
      RxGen rxgen = type.getAnnotation(RxGen.class);
      if (rxgen != null) {
        return rxgen.value();
      }
    }
    return type;
  }

  /**
   * Convert a handler for a generated reactive type to a handler for the corresponding core type.
   */
  public static <CORE, REACTIVE> Handler<CORE> convertHandler(Handler<REACTIVE> rxHandler, Function<CORE, REACTIVE> mapper) {
    if (rxHandler.getClass().isAnnotationPresent(RxGen.class) && rxHandler instanceof RxDelegate) {
      RxDelegate rxDelegate = (RxDelegate) rxHandler;
      return (Handler<CORE>) rxDelegate.getDelegate();
    }
    return new DelegatingHandler<>(rxHandler, mapper);
  }
}
