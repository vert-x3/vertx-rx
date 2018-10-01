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

package io.vertx.rxjava.ext.sql.impl;

import io.vertx.rxjava.ext.sql.SQLConnection;
import rx.Completable;
import rx.Completable.Transformer;

/**
 * @author Thomas Segismont
 */
public class InTransactionCompletable implements Transformer {

  private final SQLConnection sqlConnection;

  /**
   * @param sqlConnection the connection used for transaction management
   */
  public InTransactionCompletable(SQLConnection sqlConnection) {
    this.sqlConnection = sqlConnection;
  }

  @Override
  public Completable call(Completable upstream) {
    return sqlConnection.rxSetAutoCommit(false).toCompletable()
      .andThen(upstream)
      .andThen(sqlConnection.rxCommit().toCompletable())
      .onErrorResumeNext(throwable -> {
        return sqlConnection.rxRollback().toCompletable().onErrorComplete()
          .andThen(sqlConnection.rxSetAutoCommit(true).toCompletable().onErrorComplete())
          .andThen(Completable.error(throwable));
      }).andThen(sqlConnection.rxSetAutoCommit(true).toCompletable());
  }
}
