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

import org.junit.Test;
import rx.Completable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Thomas Segismont
 */
public class InTransactionCompletableTest extends SQLTestBase {

  @Test
  public void inTransactionSuccess() throws Exception {
    inTransaction(null).test()
      .awaitTerminalEvent()
      .assertCompleted();
  }

  @Test
  public void inTransactionFailure() throws Exception {
    Exception error = new Exception();
    inTransaction(error).test()
      .awaitTerminalEvent()
      .assertError(error);
    assertTableContainsInitDataOnly();
  }

  private Completable inTransaction(Exception e) throws Exception {
    return client.rxGetConnection().flatMapCompletable(conn -> {
      return rxInsertExtraFolks(conn)
        .andThen(uniqueNames(conn))
        .<List<String>>collect(ArrayList::new, List::add).toSingle()
        .flatMapCompletable(names -> rxAssertEquals(Arrays.asList(namesWithExtraFolks()), names))
        .compose(upstream -> e == null ? upstream : upstream.andThen(Completable.error(e)))
        .compose(SQLClientHelper.txCompletableTransformer(conn))
        .andThen(rxAssertAutoCommit(conn))
        .doAfterTerminate(conn::close);
    });
  }
}
