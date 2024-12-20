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

package io.vertx.reactivex.tests;

import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpClientResponse;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.reactivex.tests.web.TestRouteHandler;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

public class WebTest extends VertxTestBase {

  private Vertx vertx;
  private Router router;
  private HttpServer server;
  private HttpClient client;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    vertx = new Vertx(super.vertx);
    router = Router.router(vertx);
    server = vertx.createHttpServer()
      .requestHandler(router)
      .rxListen(8080, "localhost")
      .blockingGet();
    client = vertx.createHttpClient();
  }

  @Test
  public void testOrderListenerIsInvoked() {
    router.get().handler(TestRouteHandler.create());
    int statusCode = client.rxRequest(HttpMethod.GET, 8080, "localhost", "/")
      .flatMap(request -> request
        .rxSend()
        .map(HttpClientResponse::statusCode))
      .blockingGet();
    assertEquals(200, statusCode);
  }

  @Override
  public void tearDown() throws Exception {
    if (client != null) {
      client.close();
    }
    if (server != null) {
      server.close();
    }
    super.tearDown();
  }
}
