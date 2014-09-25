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
package io.vertx.lang.groovy;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.java.core.http.HttpServer;
import io.vertx.lang.java.AbstractVerticle;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DeployTest {

  @Test
  public void testDeploy() throws Exception {
    io.vertx.core.Vertx vertx = io.vertx.core.Vertx.vertx();
    CountDownLatch latch = new CountDownLatch(1);
    vertx.deployVerticle(new AbstractVerticle() {
      @Override
      public void start() throws Exception {
        HttpServer s1 = vertx.createHttpServer(new HttpServerOptions().setPort(8080)).requestHandler(req -> {});
        HttpServer s2 = vertx.createHttpServer(new HttpServerOptions().setPort(8081)).requestHandler(req -> {
        });
        CompletableFuture<HttpServer> f1 =  s1.listenFuture();
        CompletableFuture<HttpServer> f2 =  s2.listenFuture();
        f1.runAfterBoth(f2, latch::countDown);
      }
    });
    latch.await();
    vertx.close();
  }
}
