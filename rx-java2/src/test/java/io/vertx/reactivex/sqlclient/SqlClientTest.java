/*
 * Copyright (c) 2011-2018 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.vertx.reactivex.sqlclient;

import io.reactivex.Maybe;
import io.reactivex.functions.Function;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.test.core.VertxTestBase;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Thomas Segismont
 */
public class SqlClientTest extends VertxTestBase {

  protected static final List<String> NAMES = Arrays.asList("John", "Paul", "Peter", "Andrew", "Peter", "Steven");

  @ClassRule
  public static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:10.10"));

  protected PgPool pool;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    PgConnectOptions connectOptions = new PgConnectOptions();
    connectOptions.setHost(container.getHost());
    connectOptions.setPort(container.getMappedPort(5432));
    connectOptions.setDatabase(container.getDatabaseName());
    connectOptions.setUser(container.getUsername());
    connectOptions.setPassword(container.getPassword());
    pool = PgPool.newInstance(io.vertx.pgclient.PgPool.pool(connectOptions, new PoolOptions()));
    pool
      .query("drop table if exists folks")
      .rxExecute()
      .flatMap(res -> pool.query("create table folks (firstname varchar(255) not null)").rxExecute())
      .flatMap(res -> pool.preparedQuery("insert into folks (firstname) values ($1)").rxExecuteBatch(NAMES.stream().map(Tuple::of).collect(Collectors.toList())))
      .blockingGet();
  }

  @Override
  public void tearDown() throws Exception {
    pool.close();
  }

  @Test
  public void testStream() {
    Maybe<List<String>> single = pool.rxWithTransaction((Function<SqlConnection, Maybe<List<String>>>) conn -> conn
      .rxPrepare("SELECT * FROM folks")
      .flatMapPublisher(pq -> pq.createStream(2).toFlowable())
      .<List<String>>collect(ArrayList::new, (l, r) -> l.add(r.getString(0)))
      .toMaybe());
    single.subscribe(list -> {
      assertEquals(list, NAMES);
      testComplete();
    }, this::fail);
    await();
  }
}
