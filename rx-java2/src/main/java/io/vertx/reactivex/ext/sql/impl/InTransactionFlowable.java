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

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.vertx.reactivex.ext.sql.SQLConnection;
import org.reactivestreams.Publisher;

/**
 * @author Thomas Segismont
 */
public class InTransactionFlowable<T> implements FlowableTransformer<T, T> {

  private final SQLConnection sqlConnection;

  /**
   * @param sqlConnection the connection used for transaction management
   */
  public InTransactionFlowable(SQLConnection sqlConnection) {
    this.sqlConnection = sqlConnection;
  }

  @Override
  public Publisher<T> apply(Flowable<T> upstream) {
    return sqlConnection.rxSetAutoCommit(false)
      .andThen(upstream)
      .concatWith(sqlConnection.rxCommit().toFlowable())
      .onErrorResumeNext(throwable -> {
        return sqlConnection.rxRollback().onErrorComplete()
          .andThen(sqlConnection.rxSetAutoCommit(true).onErrorComplete())
          .andThen(Flowable.error(throwable));
      }).concatWith(sqlConnection.rxSetAutoCommit(true).toFlowable());
  }
}
