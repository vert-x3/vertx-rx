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

import java.util.function.Function;

/**
 * @author Thomas Segismont
 */
public class CompletableSQLHelper {

  public static Completable withConnection(SQLClient sqlClient, Function<SQLConnection, Completable> supplier) {
    return sqlClient.rxGetConnection().flatMapCompletable(sqlConnection -> {
      return supplier.apply(sqlConnection)
        .doFinally(sqlConnection::close);
    });
  }

  public static Completable inTransaction(SQLClient sqlClient, Function<SQLConnection, Completable> supplier) {
    return withConnection(sqlClient, sqlConnection -> {
      return supplier.apply(sqlConnection).compose(new InTransactionCompletable(sqlConnection));
    });
  }

  private CompletableSQLHelper() {
    // Utility
  }
}
