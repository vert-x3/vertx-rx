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

package io.vertx.rxjava.tests.web.impl;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.impl.OrderListener;
import io.vertx.rxjava.tests.web.TestRouteHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestRouteHandlerImpl implements TestRouteHandler, OrderListener {

  private final AtomicBoolean called = new AtomicBoolean();

  @Override
  public void handle(RoutingContext rc) {
    if (called.get()) {
      rc.response().end();
    } else {
      rc.fail(500);
    }
  }

  @Override
  public void onOrder(int order) {
    called.set(true);
  }
}
