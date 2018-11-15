/*
 * Copyright 2018 Red Hat, Inc.
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

package io.vertx.reactivex.impl;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.vertx.core.streams.WriteStream;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Thomas Segismont
 */
public class WriteStreamObserver<R, T> implements Observer<R> {

  private final WriteStream<T> writeStream;
  private final Consumer<Throwable> onError;
  private final Runnable onComplete;
  private final Function<R, T> adapter;

  public WriteStreamObserver(WriteStream<T> writeStream, Function<R, T> adapter, Consumer<Throwable> onError, Runnable onComplete) {
    Objects.requireNonNull(writeStream, "writeStream");
    Objects.requireNonNull(adapter, "adapter");
    Objects.requireNonNull(onError, "onError");
    Objects.requireNonNull(onComplete, "onComplete");
    this.writeStream = writeStream;
    this.adapter = adapter;
    this.onError = onError;
    this.onComplete = onComplete;
  }

  @Override
  public void onSubscribe(Disposable d) {
  }

  @Override
  public void onNext(R r) {
    writeStream.write(adapter.apply(r));
  }

  @Override
  public void onError(Throwable t) {
    onError.accept(t);
  }

  @Override
  public void onComplete() {
    onComplete.run();
  }
}
