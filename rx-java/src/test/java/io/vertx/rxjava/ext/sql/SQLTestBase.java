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

package io.vertx.rxjava.ext.sql;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import io.vertx.test.core.VertxTestBase;
import org.junit.Assert;
import rx.Completable;
import rx.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author Thomas Segismont
 */
public abstract class SQLTestBase extends VertxTestBase {

  protected static final List<String> NAMES = Arrays.asList("John", "Paul", "Peter", "Andrew", "Peter", "Steven");

  protected static final String UNIQUE_NAMES_SQL = "select distinct firstname from folks order by firstname asc";

  protected static final String INSERT_FOLK_SQL = "insert into folks (firstname) values ('%s')";

  private static final JsonObject config = new JsonObject()
    .put("driver_class", "org.hsqldb.jdbcDriver")
    .put("url", "jdbc:hsqldb:mem:test?shutdown=true");

  protected SQLClient client;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    client = new JDBCClient(io.vertx.ext.jdbc.JDBCClient.createNonShared(vertx, config));
    client.rxGetConnection().flatMapCompletable(conn -> {
      Completable setup = conn.rxExecute("drop table folks if exists").toCompletable()
        .andThen(conn.rxExecute("create table folks (firstname varchar(255) not null)").toCompletable());
      for (String name : NAMES) {
        setup = setup.andThen(conn.rxExecute(String.format(INSERT_FOLK_SQL, name)).toCompletable());
      }
      return setup.doAfterTerminate(conn::close);
    }).await();
  }

  protected void assertTableContainsInitDataOnly() throws Exception {
    client.rxGetConnection().flatMapObservable(conn -> {
      return uniqueNames(conn).doAfterTerminate(conn::close);
    }).test()
      .awaitTerminalEvent()
      .assertCompleted()
      .assertValues(NAMES.stream().sorted().distinct().toArray(String[]::new));
  }

  protected Observable<String> uniqueNames(SQLConnection conn) {
    return conn.rxQuery(UNIQUE_NAMES_SQL)
      .flatMapObservable(resultSet -> Observable.from(resultSet.getResults()))
      .map(row -> row.getString(0));
  }

  protected Completable rxInsertExtraFolks(SQLConnection conn) {
    return conn.rxExecute(String.format(INSERT_FOLK_SQL, "Georges")).toCompletable()
      .andThen(conn.rxExecute(String.format(INSERT_FOLK_SQL, "Henry")).toCompletable());
  }

  protected String[] namesWithExtraFolks() {
    return Stream.concat(NAMES.stream(), Stream.of("Georges", "Henry")).sorted().distinct().toArray(String[]::new);
  }

  protected Completable rxAssertEquals(Object expected, Object actual) {
    Completable completable;
    try {
      Assert.assertEquals(expected, actual);
      completable = Completable.complete();
    } catch (AssertionError error) {
      completable = Completable.error(error);
    }
    return completable;
  }

  protected Completable rxAssertAutoCommit(SQLConnection conn) {
    String testName = UUID.randomUUID().toString();
    return conn.rxExecute(String.format(INSERT_FOLK_SQL, testName)).toCompletable()
      .andThen(client.rxGetConnection().flatMapCompletable(other -> {
        return uniqueNames(other).contains(testName).toSingle()
          .flatMapCompletable(contains -> {
            if (contains) {
              return Completable.complete();
            }
            return Completable.error(new AssertionError("Connection should be back in autocommit mode"));
          })
          .doAfterTerminate(other::close);
      }));
  }

  @Override
  public void tearDown() throws Exception {
    client.rxClose().toCompletable().await();
  }
}
