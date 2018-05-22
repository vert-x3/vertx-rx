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

import io.reactivex.Observable;

import java.util.function.Function;

/**
 * @author Thomas Segismont
 */
public class ObservableSQLHelper {

  public static <T> Observable<T> withConnection(SQLClient sqlClient, Function<SQLConnection, Observable<T>> supplier) {
    return sqlClient.rxGetConnection().flatMapObservable(sqlConnection -> {
      return supplier.apply(sqlConnection)
        .doFinally(sqlConnection::close);
    });
  }

  public static <T> Observable<T> inTransaction(SQLClient sqlClient, Function<SQLConnection, Observable<T>> supplier) {
    return withConnection(sqlClient, sqlConnection -> {
      return supplier.apply(sqlConnection).compose(new InTransactionObservable<>(sqlConnection));
    });
  }

  private ObservableSQLHelper() {
    // Utility
  }
}
