/*
 * Copyright 2014 Red Hat, Inc.
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
package io.vertx.reactivex.core;

import io.reactivex.Completable;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AbstractVerticle extends io.vertx.core.AbstractVerticle {

  // Shadows the AbstractVerticle#vertx field
  protected io.vertx.reactivex.core.Vertx vertx;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    this.vertx = new io.vertx.reactivex.core.Vertx(vertx);
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    Completable completable = rxStart();
    if (completable != null) {
      completable.subscribe(startFuture::complete, startFuture::fail);
    } else {
      super.start(startFuture);
    }
  }

  /**
   * Override to return a {@code Completable} that will complete the deployment of this verticle.
   * <p/>
   * When {@code null} is returned, the {@link #start()} will be called instead.
   *
   * @return the completable
   */
  public Completable rxStart() {
    return null;
  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    Completable completable = rxStop();
    if (completable != null) {
      completable.subscribe(stopFuture::complete, stopFuture::fail);
    } else {
      super.stop(stopFuture);
    }
  }

  /**
   * Override to return a {@code Completable} that will complete the undeployment of this verticle.
   * <p/>
   * When {@code null} is returned, the {@link #stop()} will be called instead.
   *
   * @return the completable
   */
  public Completable rxStop() {
    return null;
  }
}
