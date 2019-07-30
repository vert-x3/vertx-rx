/*
 * Copyright 2019 Red Hat, Inc.
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

package io.vertx.reactivex.ext.sql;

import io.reactivex.observers.BaseTestConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Thomas Segismont
 */
public class UsingConnectionSafetyTest extends VertxTestBase {

  private static final int MAX_POOL_SIZE = 5;

  private static final JsonObject config = new JsonObject()
    .put("driver_class", "org.hsqldb.jdbcDriver")
    .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
    .put("max_pool_size", MAX_POOL_SIZE);

  private SQLClient client;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    client = new JDBCClient(io.vertx.ext.jdbc.JDBCClient.createNonShared(vertx, config));
  }

  @Test
  public void testUsingConnectionFlowable() throws Exception {
    doTest(runtimeException -> SQLClientHelper.usingConnectionFlowable(client, conn -> {
      throw runtimeException;
    }).test());
  }

  @Test
  public void testUsingConnectionObservable() throws Exception {
    doTest(runtimeException -> SQLClientHelper.usingConnectionObservable(client, conn -> {
      throw runtimeException;
    }).test());
  }

  @Test
  public void testUsingConnectionSingle() throws Exception {
    doTest(runtimeException -> SQLClientHelper.usingConnectionSingle(client, conn -> {
      throw runtimeException;
    }).test());
  }

  @Test
  public void testUsingConnectionMaybe() throws Exception {
    doTest(runtimeException -> SQLClientHelper.usingConnectionMaybe(client, conn -> {
      throw runtimeException;
    }).test());
  }

  @Test
  public void testUsingConnectionCompletable() throws Exception {
    doTest(runtimeException -> SQLClientHelper.usingConnectionCompletable(client, conn -> {
      throw runtimeException;
    }).test());
  }

  private void doTest(Function<RuntimeException, BaseTestConsumer> test) {
    for (int i = 0; i < MAX_POOL_SIZE + 1; i++) {
      RuntimeException expected = new RuntimeException();
      BaseTestConsumer testConsumer = test.apply(expected);
      testConsumer.awaitDone(5, TimeUnit.SECONDS).assertNoValues().assertError(expected);
    }
  }

  @Override
  public void tearDown() throws Exception {
    client.rxClose().blockingAwait();
  }
}
