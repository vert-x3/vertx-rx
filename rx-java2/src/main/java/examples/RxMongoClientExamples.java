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
package examples;

import io.reactivex.Flowable;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.streams.ReadStream;
import io.vertx.reactivex.ext.mongo.MongoClient;

/**
 * @author Thomas Segismont
 */
public class RxMongoClientExamples {

  public void createClient(Vertx vertx, JsonObject config) {
    MongoClient client = MongoClient.createShared(vertx, config);
  }

  public void findBatch(MongoClient mongoClient) {
    // Will match all Tolkien books
    JsonObject query = new JsonObject()
      .put("author", "J. R. R. Tolkien");

    ReadStream<JsonObject> books = mongoClient.findBatch("book", query);

    // Convert the stream to a Flowable
    Flowable<JsonObject> flowable = books.toFlowable();

    flowable.subscribe(doc -> {
      System.out.println("Found doc: " + doc.encodePrettily());
    }, throwable -> {
      throwable.printStackTrace();
    }, () -> {
      System.out.println("End of research");
    });
  }
}
