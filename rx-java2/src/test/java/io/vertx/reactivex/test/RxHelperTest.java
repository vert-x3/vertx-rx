/*
 * Copyright 2017 Red Hat, Inc.
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

package io.vertx.reactivex.test;

import io.reactivex.Single;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.RxHelper;
import io.vertx.reactivex.core.Vertx;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Thomas Segismont
 */
public class RxHelperTest {

  private Vertx vertx = Vertx.vertx();

  @After
  public void tearDown() {
    vertx.close();
  }

  @Test
  public void deployVerticle() throws Exception {
    CoreVerticle verticle = new CoreVerticle();
    Single<String> single = RxHelper.deployVerticle(vertx, verticle);
    assertNull(verticle.config);
    single.blockingGet();
    assertNotNull(verticle.config);
    assertTrue(verticle.config.isEmpty());
  }

  @Test
  public void deployVerticleWithOptions() throws Exception {
    CoreVerticle verticle = new CoreVerticle();
    JsonObject expected = new JsonObject()
      .put("bim", 1).put("bam", new JsonArray().add(1).add(2).add(3)).put("boum", new JsonObject().put("toto", "titi"));
    Single<String> single = RxHelper.deployVerticle(vertx, verticle, new DeploymentOptions().setConfig(expected));
    assertNull(verticle.config);
    single.blockingGet();
    assertEquals(expected, verticle.config);
  }

  @Test
  public void deployVerticleFailure() throws Exception {
    CoreVerticle verticle = new CoreVerticle(true);
    Single<String> single = RxHelper.deployVerticle(vertx, verticle);
    assertNull(verticle.config);
    try {
      single.blockingGet();
      fail("Verticle deployment should fail");
    } catch (RuntimeException e) {
      assertThat(e.getCause(), instanceOf(MyException.class));
      assertNotNull(verticle.config);
      assertTrue(verticle.config.isEmpty());
    }
  }

  public static class CoreVerticle extends AbstractVerticle {

    private final boolean failDeployment;
    private volatile JsonObject config;

    public CoreVerticle() {
      failDeployment = false;
    }

    public CoreVerticle(boolean failDeployment) {
      this.failDeployment = failDeployment;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
      config = config();
      if (failDeployment) {
        startFuture.fail(new MyException());
      } else {
        startFuture.complete();
      }
    }

  }

  static class MyException extends Exception {
  }
  }
