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
 * @author Thomas Segismont
 */
public class InTransactionMaybe<T> implements MaybeTransformer<T, T> {

  private final SQLConnection sqlConnection;

  public InTransactionMaybe(SQLConnection sqlConnection) {
    this.sqlConnection = sqlConnection;
  }

  @Override
  public MaybeSource<T> apply(Maybe<T> upstream) {
    return sqlConnection.rxSetAutoCommit(false)
      .andThen(upstream)
      .flatMap(item -> commitAndThen(Maybe.just(item)), Maybe::error, () -> commitAndThen(Maybe.empty()))
      .onErrorResumeNext(throwable -> {
        return sqlConnection.rxRollback().onErrorComplete()
          .andThen(sqlConnection.rxSetAutoCommit(true).onErrorComplete())
          .andThen(Maybe.error(throwable));
      }).flatMap(item -> setAutoCommitAndThen(Maybe.just(item)), Maybe::error, () -> setAutoCommitAndThen(Maybe.empty()));
  }

  private Maybe<T> setAutoCommitAndThen(Maybe<T> next) {
    return sqlConnection.rxSetAutoCommit(true).andThen(next);
  }

  private Maybe<T> commitAndThen(Maybe<T> next) {
    return sqlConnection.rxCommit().andThen(next);
  }
}
