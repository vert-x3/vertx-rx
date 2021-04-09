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

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.rxjava3.impl.AsyncResultCompletable;
import io.vertx.rxjava3.impl.AsyncResultMaybe;
import io.vertx.rxjava3.impl.AsyncResultSingle;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class AsyncResultTest {

  @Test
  public void testSingle() {
    Promise<String> promise = Promise.promise();
    try {
      Single justMe = Single.just("me");
      RxJavaPlugins.setOnSingleAssembly(single -> justMe);
      Single<String> single = AsyncResultSingle.toSingle(promise.future()::onComplete);
      assertSame(single, justMe);
    } finally {
      RxJavaPlugins.reset();
    }
  }

  @Test
  public void testMaybe() {
    Promise<String> promise = Promise.promise();
    try {
      Maybe justMe = Maybe.just("me");
      RxJavaPlugins.setOnMaybeAssembly(single -> justMe);
      Maybe<String> maybe = AsyncResultMaybe.toMaybe(promise.future()::onComplete);
      assertSame(maybe, justMe);
    } finally {
      RxJavaPlugins.reset();
    }
  }

  @Test
  public void testCompletable() {
    Promise<Void> promise = Promise.promise();
    try {
      Completable complete = Completable.complete();
      RxJavaPlugins.setOnCompletableAssembly(single -> complete);
      Completable completable = AsyncResultCompletable.toCompletable(promise.future()::onComplete);
      assertSame(completable, complete);
    } finally {
      RxJavaPlugins.reset();
    }
  }
}
