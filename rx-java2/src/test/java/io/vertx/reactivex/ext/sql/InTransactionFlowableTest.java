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

import io.reactivex.Flowable;
import org.junit.Test;

/**
 * @author Thomas Segismont
 */
public class InTransactionFlowableTest extends SQLTestBase {

  @Test
  public void inTransactionSuccess() throws Exception {
    inTransaction(null).test()
      .await()
      .assertComplete()
      .assertValueSequence(namesWithExtraFolks());
  }

  @Test
  public void inTransactionFailure() throws Exception {
    Exception error = new Exception();
    inTransaction(error).test()
      .await()
      .assertError(error)
      .assertValueSequence(namesWithExtraFolks());
    assertTableContainsInitDataOnly();
  }

  private Flowable<String> inTransaction(Exception e) throws Exception {
    return client.rxGetConnection().flatMapPublisher(conn -> {
      return rxInsertExtraFolks(conn)
        .andThen(uniqueNames(conn))
        .compose(upstream -> e == null ? upstream : upstream.concatWith(Flowable.error(e)))
        .compose(SQLClientHelper.txFlowableTransformer(conn))
        .concatWith(rxAssertAutoCommit(conn).toFlowable())
        .doFinally(conn::close);
    });
  }
}
