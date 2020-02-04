package io.vertx.reactivex.test;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.vertx.core.file.OpenOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import org.apache.commons.lang.ArrayUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileSaveTest {

  private Vertx vertx;

  @Before
  public void before() {
    vertx = Vertx.vertx();
  }

  @After
  public void after() {
    vertx.close();
  }

  @Test
  public void flowSaveWorks() throws IOException {
    for (int i = 0; i < 10_000; i++) {
      final String hello = "hello-world!!!";
      final Flowable<Byte> flow = Flowable.fromArray(ArrayUtils.toObject(hello.getBytes()));
      final Path file = Files.createTempFile("hello", ".txt");
      file.toFile().delete();
      vertx.fileSystem().rxOpen(file.toString(), new OpenOptions().setWrite(true))
        .flatMapCompletable(asyncFile ->
          Completable.create(
            emitter ->
              flow.buffer(1024)
                .map(bytes -> Buffer.buffer(ArrayUtils.toPrimitive(bytes.toArray(new Byte[0]))))
                .subscribe(asyncFile.toSubscriber().onComplete(emitter::onComplete))
          )
        ).blockingAwait();
      MatcherAssert.assertThat(new String(Files.readAllBytes(file)), Matchers.equalTo(hello));
    }
  }
}
