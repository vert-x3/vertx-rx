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
package io.vertx.reactivex.core;

import io.vertx.core.Context;
import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AbstractVerticle extends io.vertx.core.AbstractVerticle {

  // Shadows the AbstractVerticle#vertx field
  protected io.vertx.reactivex.core.Vertx vertx;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    this.vertx = new io.vertx.reactivex.core.Vertx(vertx);
  }
}
