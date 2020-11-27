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

package io.vertx.reactivex.jdbcclient;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.SqlClient;
import io.vertx.reactivex.sqlclient.SqlConnection;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author Thomas Segismont
 */
public class JDBCTest extends VertxTestBase {

  protected static final List<String> NAMES = Arrays.asList("John", "Paul", "Peter", "Andrew", "Peter", "Steven");

  protected static final String UNIQUE_NAMES_SQL = "select distinct firstname from folks order by firstname asc";

  protected static final String INSERT_FOLK_SQL = "insert into folks (firstname) values ('%s')";

  private static final JsonObject config = new JsonObject()
    .put("driver_class", "org.hsqldb.jdbcDriver")
    .put("url", "jdbc:hsqldb:mem:test?shutdown=true");

  protected JDBCPool client;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    client = JDBCPool.pool(Vertx.newInstance(vertx), config);
    client.rxGetConnection().flatMapCompletable(conn -> {
      Single<RowSet<Row>> setup = conn
        .query("drop table folks if exists")
        .rxExecute()
        .flatMap(res -> conn
          .query("create table folks (firstname varchar(255) not null)")
          .rxExecute()
        );
      for (String name : NAMES) {
        setup = setup.flatMap(res -> conn.query(String.format(INSERT_FOLK_SQL, name)).rxExecute());
      }
      return setup.ignoreElement().doFinally(conn::close);
    }).blockingAwait();
  }

  @Test
  public void testWithTransactionSuccess() throws Exception {
    withTransaction(null).test()
      .await()
      .assertValue(namesWithExtraFolks());
  }

  @Test
  public void testWithTransactionFailure() throws Exception {
    Exception error = new Exception();
    withTransaction(error).test()
      .await()
      .assertFailure(err -> error == err);
    assertTableContainsInitDataOnly();
  }

  private Maybe<List<String>> withTransaction(Exception e) {
    return client.rxWithTransaction((Function<SqlConnection, Maybe<List<String>>>) sqlClient ->
      rxInsertExtraFolks(sqlClient)
        .flatMapPublisher(res -> uniqueNames(sqlClient))
        .<List<String>>collect(ArrayList::new, List::add)
        .compose(upstream -> e == null ? upstream : upstream.flatMap(names -> Single.error(e)))
        .toMaybe());
  }

  protected Single<RowSet<Row>> rxInsertExtraFolks(SqlClient conn) {
    return conn
      .query(String.format(INSERT_FOLK_SQL, "Georges"))
      .rxExecute()
      .flatMap(res -> conn
        .query(String.format(INSERT_FOLK_SQL, "Henry"))
        .rxExecute()
      );
  }

  protected Flowable<String> uniqueNames(SqlClient conn) {
    return conn.query(UNIQUE_NAMES_SQL).rxExecute()
      .flatMapPublisher(Flowable::fromIterable)
      .map(row -> row.getString(0));
  }

  protected void assertTableContainsInitDataOnly() throws Exception {
    client
      .rxGetConnection()
      .flatMapPublisher(conn -> uniqueNames(conn).doFinally(conn::close))
      .test()
      .await()
      .assertComplete()
      .assertValueSequence(NAMES.stream().sorted().distinct().collect(toList()));
  }

  protected List<String> namesWithExtraFolks() {
    return Stream.concat(NAMES.stream(), Stream.of("Georges", "Henry")).sorted().distinct().collect(toList());
  }

  @Override
  public void tearDown() throws Exception {
    client.rxClose().blockingAwait();
  }
}
