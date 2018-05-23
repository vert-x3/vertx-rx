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

package io.vertx.reactivex.ext.sql;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * @author Thomas Segismont
 */
public class SingleSQLHelperTest extends VertxTestBase {

  private static final List<String> NAMES = Arrays.asList("John", "Paul", "Peter", "Andrew", "Peter", "Steven");

  private static final String UNIQUE_NAMES_SQL = "select distinct firstname from folks order by firstname asc";

  private static final String INSERT_FOLK_SQL = "insert into folks (firstname) values ('%s')";

  private static final JsonObject config = new JsonObject()
    .put("driver_class", "org.hsqldb.jdbcDriver")
    .put("url", "jdbc:hsqldb:mem:test?shutdown=true");

  private JDBCClient client;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    client = new JDBCClient(io.vertx.ext.jdbc.JDBCClient.createNonShared(vertx, config));
    client.rxGetConnection().flatMapCompletable(conn -> {
      Completable setup = conn.rxExecute("drop table folks if exists")
        .andThen(conn.rxExecute("create table folks (firstname varchar(255) not null)"));
      for (String name : NAMES) {
        setup = setup.andThen(conn.rxExecute(String.format(INSERT_FOLK_SQL, name)));
      }
      return setup.doFinally(conn::close);
    }).blockingAwait();
  }

  @Test
  public void withConnection() throws Exception {
    assertTableContainsInitData();
  }

  private void assertTableContainsInitData() throws Exception {
    SingleSQLHelper.withConnection(client, this::uniqueNames).test()
      .await()
      .assertValue(NAMES.stream().sorted().distinct().collect(toList()));
  }

  private Single<List<String>> uniqueNames(SQLConnection conn) {
    return conn.rxQuery(UNIQUE_NAMES_SQL)
      .flatMapPublisher(resultSet -> Flowable.fromIterable(resultSet.getResults()))
      .map(row -> row.getString(0))
      .collect(ArrayList::new, List::add);
  }

  @Test
  public void inTransactionSuccess() throws Exception {
    inTransaction(conn -> Completable.complete())
      .assertValue(Stream.concat(NAMES.stream(), Stream.of("Georges", "Henry")).sorted().distinct().collect(toList()));
  }

  @Test
  public void inTransactionFailure() throws Exception {
    inTransaction(conn -> conn.rxExecute("update folks set firstname = null where firstname = 'Peter'"))
      .assertError(SQLIntegrityConstraintViolationException.class);
    assertTableContainsInitData();
  }

  private TestObserver<List<String>> inTransaction(Function<SQLConnection, Completable> postInsert) throws Exception {
    return SingleSQLHelper.inTransaction(client, conn -> {
      return conn.rxExecute(String.format(INSERT_FOLK_SQL, "Georges"))
        .andThen(conn.rxExecute(String.format(INSERT_FOLK_SQL, "Henry")))
        .andThen(uniqueNames(conn))
        .flatMap(names -> postInsert.apply(conn).andThen(Single.just(names)));
    }).test().await();
  }

  @Override
  public void tearDown() throws Exception {
    client.rxClose().blockingAwait();
  }
}
