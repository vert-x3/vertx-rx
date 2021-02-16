/*
 * Copyright 2020 Red Hat, Inc.
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

package io.vertx.it;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.vertx.core.file.OpenOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.file.AsyncFile;
import io.vertx.test.core.Repeat;
import io.vertx.test.core.TestUtils;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * @author Thomas Segismont
 */
public class AsyncFileTest extends VertxTestBase {

  private Vertx vertx;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    vertx = new Vertx(super.vertx);
  }

  @Test
  public void flowableToAsyncFile() throws Exception {
    sourceToAsyncFile((flow, asyncFile) -> Completable.create(emitter ->
      flow.subscribe(asyncFile.toSubscriber().onWriteStreamEnd(emitter::onComplete))
    ));
  }

  @Test
  public void observableToAsyncFile() throws Exception {
    sourceToAsyncFile((flow, asyncFile) -> Completable.create(emitter ->
      flow.toObservable().subscribe(asyncFile.toObserver().onWriteStreamEnd(emitter::onComplete))
    ));
  }

  private void sourceToAsyncFile(BiFunction<Flowable<Buffer>, AsyncFile, Completable> func) throws Exception {
    File file = TestUtils.tmpFile("txt");
    assertTrue(!file.exists() || file.delete());

    List<Byte> bytes = IntStream.range(0, 128 * 1024).boxed()
      .map(step -> (byte) TestUtils.randomChar())
      .collect(toList());

    Flowable<Buffer> flow = Flowable.fromIterable(bytes).buffer(256, () -> new ArrayList<>(256))
      .map(ba -> {
        Buffer buffer = Buffer.buffer();
        ba.forEach(buffer::appendByte);
        return buffer;
      });

    Completable writeToFile = vertx.fileSystem().rxOpen(file.toString(), new OpenOptions().setWrite(true))
      .flatMapCompletable(asyncFile -> func.apply(flow, asyncFile));

    writeToFile.andThen(vertx.fileSystem().rxReadFile(file.toString()))
      .test()
      .await()
      .assertValue(bytes.stream().<Buffer>collect(Buffer::buffer, Buffer::appendByte, Buffer::appendBuffer));
  }
}
