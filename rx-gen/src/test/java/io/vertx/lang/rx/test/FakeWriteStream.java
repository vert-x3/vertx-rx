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

package io.vertx.lang.rx.test;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.WriteStream;

/**
 * @author Thomas Segismont
 */
public class FakeWriteStream implements WriteStream<Integer> {

  private final io.vertx.core.Vertx vertx;

  private boolean writeQueueFull;
  private Handler<Void> drainHandler;
  private int last = -1;
  private volatile boolean drainHandlerInvoked;

  public FakeWriteStream(Vertx vertx) {
    this.vertx = vertx;
  }

  public boolean drainHandlerInvoked() {
    return drainHandlerInvoked;
  }

  @Override
  public WriteStream<Integer> exceptionHandler(Handler<Throwable> handler) {
    return null;
  }

  @Override
  public synchronized WriteStream<Integer> write(Integer data) {
    if (data == null) {
      throw new IllegalArgumentException("data is null");
    }
    if (data != last + 1) {
      throw new IllegalStateException("Expected " + (last + 1) + ", got " + data);
    }
    last = data;
    if (last == 3001) {
      writeQueueFull = true;
      vertx.setTimer(50, l -> {
        Handler<Void> h;
        synchronized (this) {
          writeQueueFull = false;
          h = drainHandler;
        }
        if (h != null) {
          drainHandlerInvoked = true;
          drainHandler.handle(null);
        }
      });
    }
    return this;
  }

  @Override
  public void end() {
  }

  @Override
  public WriteStream<Integer> setWriteQueueMaxSize(int maxSize) {
    return null;
  }

  @Override
  public synchronized boolean writeQueueFull() {
    return writeQueueFull;
  }

  @Override
  public synchronized WriteStream<Integer> drainHandler(@Nullable Handler<Void> drainHandler) {
    this.drainHandler = drainHandler;
    return this;
  }
}
