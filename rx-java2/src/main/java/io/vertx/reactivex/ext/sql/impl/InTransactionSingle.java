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

package io.vertx.reactivex.ext.sql.impl;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.vertx.reactivex.ext.sql.SQLConnection;

/**
 * @author Thomas Segismont
 */
public class InTransactionSingle<T> implements SingleTransformer<T, T> {

  private final SQLConnection sqlConnection;

  /**
   * @param sqlConnection the connection used for transaction management
   */
  public InTransactionSingle(SQLConnection sqlConnection) {
    this.sqlConnection = sqlConnection;
  }

  @Override
  public SingleSource<T> apply(Single<T> upstream) {
    return sqlConnection.rxSetAutoCommit(false)
      .andThen(upstream)
      .flatMap(item -> sqlConnection.rxCommit().andThen(Single.just(item)))
      .onErrorResumeNext(throwable -> {
        return sqlConnection.rxRollback().onErrorComplete()
          .andThen(sqlConnection.rxSetAutoCommit(true).onErrorComplete())
          .andThen(Single.error(throwable));
      }).flatMap(item -> sqlConnection.rxSetAutoCommit(true).andThen(Single.just(item)));
  }
}
