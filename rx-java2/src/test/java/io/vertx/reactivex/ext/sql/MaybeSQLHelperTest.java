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
import io.reactivex.Maybe;
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

import static java.util.stream.Collectors.*;

/**
 * @author Thomas Segismont
 */
public class MaybeSQLHelperTest extends VertxTestBase {

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
    MaybeSQLHelper.withConnection(client, this::uniqueNames).test()
      .await()
      .assertValue(NAMES.stream().sorted().distinct().collect(toList()));
  }

  private Maybe<List<String>> uniqueNames(SQLConnection conn) {
    return conn.rxQuery(UNIQUE_NAMES_SQL)
      .flatMapPublisher(resultSet -> Flowable.fromIterable(resultSet.getResults()))
      .map(row -> row.getString(0))
      .<List<String>>collect(ArrayList::new, List::add)
      .toMaybe();
  }

  @Test
  public void inTransactionSuccess() throws Exception {
    inTransaction(false, conn -> Completable.complete())
      .assertComplete();
  }

  @Test
  public void inTransactionSuccessNoValue() throws Exception {
    inTransaction(true, conn -> Completable.complete())
      .assertComplete();
  }

  @Test
  public void inTransactionFailure() throws Exception {
    inTransaction(false, conn -> conn.rxExecute("update folks set firstname = null where firstname = 'Peter'"))
      .assertError(SQLIntegrityConstraintViolationException.class);
    assertTableContainsInitData();
  }

  @Test
  public void inTransactionFailureNoValue() throws Exception {
    inTransaction(true, conn -> conn.rxExecute("update folks set firstname = null where firstname = 'Peter'"))
      .assertError(SQLIntegrityConstraintViolationException.class);
    assertTableContainsInitData();
  }

  private TestObserver<List<String>> inTransaction(boolean noValue, Function<SQLConnection, Completable> postInsert) throws Exception {
    return MaybeSQLHelper.inTransaction(client, conn -> {
      return conn.rxExecute(String.format(INSERT_FOLK_SQL, "Georges"))
        .andThen(conn.rxExecute(String.format(INSERT_FOLK_SQL, "Henry")))
        .andThen(uniqueNames(conn))
        .flatMap(val -> noValue ? Maybe.empty() : Maybe.just(val))
        .flatMap(val -> postInsert.apply(conn).andThen(Maybe.just(val)), Maybe::error, () -> postInsert.apply(conn).andThen(Maybe.empty()))
        .compose(upstream -> postInsert.apply(conn).andThen(upstream));
    }).test().await();
  }

  @Override
  public void tearDown() throws Exception {
    client.rxClose().blockingAwait();
  }
}
