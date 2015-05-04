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
package io.vertx.rx.java.test;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ObservableTest extends VertxTestBase {

  @Test
  public void testDeploy() throws Exception {
    AtomicInteger count = new AtomicInteger();
    vertx.deployVerticle(new AbstractVerticle() {
      @Override
      public void start() throws Exception {
        HttpServer s1 = vertx.createHttpServer(new HttpServerOptions().setPort(8080)).requestHandler(req -> {
        });
        HttpServer s2 = vertx.createHttpServer(new HttpServerOptions().setPort(8081)).requestHandler(req -> {
        });
        Observable<HttpServer> f1 = s1.listenObservable();
        Observable<HttpServer> f2 = s2.listenObservable();
        Action1<HttpServer> done = server -> {
          if (count.incrementAndGet() == 2) {
            testComplete();
          }
        };
        f1.subscribe(done);
        f2.subscribe(done);
      }
    });
    await();
  }

  @Test
  public void testObservablePeriodic() throws Exception {
    Vertx vertx = new Vertx(this.vertx);
    Observable<Long> stream = vertx.periodicStream(1).toObservable();
    stream.subscribe(new Subscriber<Long>() {
      @Override
      public void onNext(Long aLong) {
        unsubscribe();
        testComplete();
      }
      @Override
      public void onCompleted() {
//        fail();
      }
      @Override
      public void onError(Throwable e) {
        fail(e.getMessage());
      }
    });
    await();
  }
}
