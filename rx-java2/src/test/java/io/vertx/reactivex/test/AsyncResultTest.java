/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.vertx.reactivex.test;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.plugins.RxJavaPlugins;
import io.vertx.core.Future;
import io.vertx.reactivex.impl.AsyncResultCompletable;
import io.vertx.reactivex.impl.AsyncResultMaybe;
import io.vertx.reactivex.impl.AsyncResultSingle;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class AsyncResultTest {

  @Test
  public void testSingle() {
    Future<String> fut = Future.future();
    try {
      Single justMe = Single.just("me");
      RxJavaPlugins.setOnSingleAssembly(single -> justMe);
      Single<String> single = AsyncResultSingle.toSingle(fut::setHandler);
      assertSame(single, justMe);
    } finally {
      RxJavaPlugins.reset();
    }
  }

  @Test
  public void testMaybe() {
    Future<String> fut = Future.future();
    try {
      Maybe justMe = Maybe.just("me");
      RxJavaPlugins.setOnMaybeAssembly(single -> justMe);
      Maybe<String> maybe = AsyncResultMaybe.toMaybe(fut::setHandler);
      assertSame(maybe, justMe);
    } finally {
      RxJavaPlugins.reset();
    }
  }

  @Test
  public void testCompletable() {
    Future<Void> fut = Future.future();
    try {
      Completable complete = Completable.complete();
      RxJavaPlugins.setOnCompletableAssembly(single -> complete);
      Completable completable = AsyncResultCompletable.toCompletable(fut::setHandler);
      assertSame(completable, complete);
    } finally {
      RxJavaPlugins.reset();
    }
  }
}
