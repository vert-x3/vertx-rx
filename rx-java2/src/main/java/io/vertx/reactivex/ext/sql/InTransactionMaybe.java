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

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;

/**
 * Decorates a {@link Maybe} with transaction management for a given {@link SQLConnection}.
 * <p>
 * If the {@link Maybe} emits a value (<em>onSuccess</em>) or completes (<em>onComplete</em>), the transaction is committed.
 * If the {@link Maybe} emits an error (<em>onError</em>), the transaction is rollbacked.
 * <p>
 * Eventually, the given {@link SQLConnection} is put back in <em>autocommit</em> mode.
 *
 * @author Thomas Segismont
 */
public class InTransactionMaybe<T> implements MaybeTransformer<T, T> {

  private final SQLConnection sqlConnection;

  /**
   * @param sqlConnection the connection used for transaction management
   */
  public InTransactionMaybe(SQLConnection sqlConnection) {
    this.sqlConnection = sqlConnection;
  }

  @Override
  public MaybeSource<T> apply(Maybe<T> upstream) {
    return sqlConnection.rxSetAutoCommit(false)
      .andThen(upstream)
      .flatMap(item -> sqlConnection.rxCommit().andThen(Maybe.just(item)), Maybe::error, () -> sqlConnection.rxCommit().andThen(Maybe.empty()))
      .onErrorResumeNext(throwable -> {
        return sqlConnection.rxRollback().onErrorComplete()
          .andThen(sqlConnection.rxSetAutoCommit(true).onErrorComplete())
          .andThen(Maybe.error(throwable));
      }).flatMap(item -> sqlConnection.rxSetAutoCommit(true).andThen(Maybe.just(item)), Maybe::error, () -> sqlConnection.rxSetAutoCommit(true).andThen(Maybe.empty()));
  }
}
