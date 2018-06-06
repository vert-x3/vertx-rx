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
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas Segismont
 */
public class InTransactionMaybeTest extends SQLTestBase {

  @Test
  public void inTransactionSuccessWithValue() throws Exception {
    inTransaction(true, null).test()
      .await()
      .assertValue(namesWithExtraFolks());
  }

  @Test
  public void inTransactionSuccessWithoutValue() throws Exception {
    inTransaction(false, null).test()
      .await()
      .assertComplete();
  }

  @Test
  public void inTransactionFailureWithValue() throws Exception {
    Exception error = new Exception();
    inTransaction(true, error).test()
      .await()
      .assertError(error);
    assertTableContainsInitDataOnly();
  }

  @Test
  public void inTransactionFailureWithoutValue() throws Exception {
    Exception error = new Exception();
    inTransaction(true, error).test()
      .await()
      .assertError(error);
    assertTableContainsInitDataOnly();
  }

  private Maybe<List<String>> inTransaction(boolean withValue, Exception e) throws Exception {
    return client.rxGetConnection().flatMapMaybe(conn -> {
      return rxInsertExtraFolks(conn)
        .andThen(uniqueNames(conn))
        .<List<String>>collect(ArrayList::new, List::add)
        .flatMapMaybe(names -> withValue ? Maybe.just(names) : Maybe.empty())
        .compose(upstream -> e == null ? upstream : upstream.flatMap(names -> Maybe.error(e), Maybe::error, () -> Maybe.error(e)))
        .compose(SQLClientHelper.txMaybeTransformer(conn))
        .flatMap(names -> rxAssertAutoCommit(conn).andThen(Maybe.just(names)), Maybe::error, () -> rxAssertAutoCommit(conn).andThen(Maybe.empty()))
        .doFinally(conn::close);
    });
  }
}
