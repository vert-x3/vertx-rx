/*
 * Copyright (c) 2020 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vertx.junit5.rxjava;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.Vertx;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
@DisplayName("Test the RxJava 1 support")
class RxJava1Test {

  @Test
  @DisplayName("Check the injection of a /io.vertx.rxjava.core.Vertx/ instance")
  void check_injection(Vertx vertx, VertxTestContext testContext) {
    testContext.verify(() -> {
      assertThat(vertx).isNotNull();
      testContext.completeNow();
    });
  }

  @Test
  @DisplayName("Check the deployment and interaction of a Rx1 verticle")
  void check_deployment_and_message_send(Vertx vertx, VertxTestContext testContext) {
    RxHelper
      .deployVerticle(vertx, new RxVerticle())
      .toSingle()
      .flatMap(id -> vertx.eventBus().rxRequest("check", "Ok?"))
      .subscribe(
        message -> testContext.verify(() -> {
          assertThat(message.body()).isEqualTo("Check!");
          testContext.completeNow();
        }),
        testContext::failNow);
  }

  static class RxVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
      vertx.eventBus().consumer("check", message -> message.reply("Check!"));
    }
  }
}
