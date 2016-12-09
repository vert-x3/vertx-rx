package io.vertx.test.reactivex;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.vertx.reactivex.core.Future;
import io.vertx.reactivex.test.reactivex.stream.RxInterface;
import io.vertx.test.core.VertxTestBase;
import io.vertx.test.reactivex.stream.RxInterfaceImpl;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxInterfaceTest extends VertxTestBase {

  RxInterfaceImpl delegate;
  RxInterface itf;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    delegate = new RxInterfaceImpl();
    itf = new RxInterface(delegate);
  }

  @Test
  public void testSingleOnSuccess() throws Exception {
    Single<String> single = itf.rxMethodWithHandlerAsyncResultString(Future.succeededFuture("some-value"));
    assertEquals(0, delegate.stringCalls.get());
    for (int i = 0;i < 10;i++) {
      int val = i;
      AtomicInteger onSubscribe = new AtomicInteger();
      AtomicInteger onSuccess = new AtomicInteger();
      AtomicInteger onError = new AtomicInteger();
      single.subscribe(new SingleObserver<String>() {
        public void onSubscribe(Disposable d) {
          assertEquals(val, delegate.stringCalls.get());
          onSubscribe.incrementAndGet();
        }
        @Override
        public void onSuccess(String value) {
          assertEquals("some-value", value);
          assertEquals(val + 1, delegate.stringCalls.get());
          onSuccess.incrementAndGet();
        }
        public void onError(Throwable e) {
          onError.incrementAndGet();
        }
      });
      assertEquals(i + 1, delegate.stringCalls.get());
      assertEquals(0, delegate.stringFailures.get());
      assertEquals(1, onSubscribe.get());
      assertEquals(1, onSuccess.get());
      assertEquals(0, onError.get());
    }
  }

  @Test
  public void testSingleFailureOnSuccess() throws Exception {
    Single<String> single = itf.rxMethodWithHandlerAsyncResultString(Future.succeededFuture("some-value"));
    assertEquals(0, delegate.stringCalls.get());
    for (int i = 0;i < 10;i++) {
      AtomicInteger onSubscribe = new AtomicInteger();
      AtomicInteger onSuccess = new AtomicInteger();
      AtomicInteger onError = new AtomicInteger();
      single.subscribe(new SingleObserver<String>() {
        public void onSubscribe(Disposable d) {
          onSubscribe.incrementAndGet();
        }
        @Override
        public void onSuccess(String value) {
          onSuccess.incrementAndGet();
          throw new RuntimeException();
        }
        public void onError(Throwable e) {
          onError.incrementAndGet();
        }
      });
      assertEquals(i + 1, delegate.stringCalls.get());
      assertEquals(0, delegate.stringFailures.get());
      assertEquals(1, onSubscribe.get());
      assertEquals(1, onSuccess.get());
      assertEquals(0, onError.get());
    }
  }

  @Test
  public void testSingleOnError() throws Exception {
    RuntimeException cause = new RuntimeException();
    Single<String> single = itf.rxMethodWithHandlerAsyncResultString(Future.failedFuture(cause));
    assertEquals(0, delegate.stringCalls.get());
    for (int i = 0;i < 10;i++) {
      AtomicInteger onSubscribe = new AtomicInteger();
      AtomicInteger onSuccess = new AtomicInteger();
      AtomicInteger onError = new AtomicInteger();
      single.subscribe(new SingleObserver<String>() {
        public void onSubscribe(Disposable d) {
          onSubscribe.incrementAndGet();
        }
        @Override
        public void onSuccess(String value) {
          onSuccess.incrementAndGet();
        }
        public void onError(Throwable e) {
          onError.incrementAndGet();
          throw new RuntimeException();
        }
      });
      assertEquals(i + 1, delegate.stringCalls.get());
      assertEquals(0, delegate.stringFailures.get());
      assertEquals(1, onSubscribe.get());
      assertEquals(0, onSuccess.get());
      assertEquals(1, onError.get());
    }
  }

  @Test
  public void testSingleFailureOnError() throws Exception {
    RuntimeException cause = new RuntimeException();
    Single<String> single = itf.rxMethodWithHandlerAsyncResultString(Future.failedFuture(cause));
    assertEquals(0, delegate.stringCalls.get());
    for (int i = 0;i < 10;i++) {
      int val = i;
      AtomicInteger onSubscribe = new AtomicInteger();
      AtomicInteger onSuccess = new AtomicInteger();
      AtomicInteger onError = new AtomicInteger();
      single.subscribe(new SingleObserver<String>() {
        public void onSubscribe(Disposable d) {
          assertEquals(val, delegate.stringCalls.get());
          onSubscribe.incrementAndGet();
        }
        @Override
        public void onSuccess(String value) {
          onSuccess.incrementAndGet();
        }
        public void onError(Throwable e) {
          assertSame(cause, e);
          assertEquals(val + 1, delegate.stringCalls.get());
          onError.incrementAndGet();
        }
      });
      assertEquals(i + 1, delegate.stringCalls.get());
      assertEquals(0, delegate.stringFailures.get());
      assertEquals(1, onSubscribe.get());
      assertEquals(0, onSuccess.get());
      assertEquals(1, onError.get());
    }
  }

  @Test
  public void testSingleUnsubscribeOnSubscribe() throws Exception {
    Single<String> single = itf.rxMethodWithHandlerAsyncResultString(Future.succeededFuture("some-value"));
    AtomicInteger onSubscribe = new AtomicInteger();
    AtomicInteger onSuccess = new AtomicInteger();
    AtomicInteger onError = new AtomicInteger();
    single.subscribe(new SingleObserver<String>() {
      @Override
      public void onSubscribe(Disposable d) {
        onSubscribe.getAndIncrement();
        d.dispose();
      }
      @Override
      public void onSuccess(String value) {
        onSuccess.getAndIncrement();
      }
      @Override
      public void onError(Throwable e) {
        onError.getAndIncrement();
      }
    });
    assertEquals(0, delegate.stringCalls.get());
    assertEquals(0, delegate.stringFailures.get());
    assertEquals(1, onSubscribe.get());
    assertEquals(0, onSuccess.get());
    assertEquals(0, onError.get());
  }

  @Test
  public void testSingleFailOnSubscribe() throws Exception {
    Single<String> single = itf.rxMethodWithHandlerAsyncResultString(Future.succeededFuture("some-value"));
    AtomicInteger onSubscribe = new AtomicInteger();
    AtomicInteger onSuccess = new AtomicInteger();
    AtomicInteger onError = new AtomicInteger();
    RuntimeException cause = new RuntimeException();
    try {
      single.subscribe(new SingleObserver<String>() {
        @Override
        public void onSubscribe(Disposable d) {
          onSubscribe.getAndIncrement();
          throw cause;
        }
        @Override
        public void onSuccess(String value) {
          onSuccess.getAndIncrement();
        }
        @Override
        public void onError(Throwable e) {
          onError.getAndIncrement();
        }
      });
    } catch (NullPointerException expected) {
      // Thrown by RxJava2
    }
    assertEquals(0, delegate.stringCalls.get());
    assertEquals(0, delegate.stringFailures.get());
    assertEquals(1, onSubscribe.get());
    assertEquals(0, onSuccess.get());
    assertEquals(0, onError.get());
  }

  @Test
  public void testSingleUnsubscribeBeforeSuccess() throws Exception {
    Future<String> fut = Future.future();
    Single<String> single = itf.rxMethodWithHandlerAsyncResultString(fut);
    AtomicInteger onSubscribe = new AtomicInteger();
    AtomicInteger onSuccess = new AtomicInteger();
    AtomicInteger onError = new AtomicInteger();
    AtomicReference<Disposable> sub = new AtomicReference<>();
    single.subscribe(new SingleObserver<String>() {
      @Override
      public void onSubscribe(Disposable d) {
        onSubscribe.getAndIncrement();
        sub.set(d);
      }
      @Override
      public void onSuccess(String value) {
        onSuccess.getAndIncrement();
      }
      @Override
      public void onError(Throwable e) {
        onError.getAndIncrement();
      }
    });
    assertEquals(1, delegate.stringCalls.get());
    assertEquals(0, delegate.stringFailures.get());
    assertEquals(1, onSubscribe.get());
    assertEquals(0, onSuccess.get());
    assertEquals(0, onError.get());
    sub.get().dispose();
    fut.complete("some-result");
    assertEquals(1, delegate.stringCalls.get());
    assertEquals(1, onSubscribe.get());
    assertEquals(0, onSuccess.get());
    assertEquals(0, onError.get());
  }

  @Test
  public void testCompletableOnSuccess() throws Exception {
    Completable completable = itf.rxMethodWithHandlerAsyncResultVoid(Future.succeededFuture());
    assertEquals(0, delegate.voidCalls.get());
    for (int i = 0;i < 10;i++) {
      int val = i;
      AtomicInteger onSubscribe = new AtomicInteger();
      AtomicInteger onSuccess = new AtomicInteger();
      AtomicInteger onError = new AtomicInteger();
      completable.subscribe(new CompletableObserver() {
        public void onSubscribe(Disposable d) {
          assertEquals(val, delegate.voidCalls.get());
          onSubscribe.incrementAndGet();
        }
        @Override
        public void onComplete() {
          assertEquals(val + 1, delegate.voidCalls.get());
          onSuccess.incrementAndGet();
        }
        public void onError(Throwable e) {
          onError.incrementAndGet();
        }
      });
      assertEquals(i + 1, delegate.voidCalls.get());
      assertEquals(0, delegate.voidFailures.get());
      assertEquals(1, onSubscribe.get());
      assertEquals(1, onSuccess.get());
      assertEquals(0, onError.get());
    }
  }

  @Test
  public void testCompletableFailureOnSuccess() throws Exception {
    Completable completable = itf.rxMethodWithHandlerAsyncResultVoid(Future.succeededFuture());
    assertEquals(0, delegate.stringCalls.get());
    for (int i = 0;i < 10;i++) {
      AtomicInteger onSubscribe = new AtomicInteger();
      AtomicInteger onSuccess = new AtomicInteger();
      AtomicInteger onError = new AtomicInteger();
      completable.subscribe(new CompletableObserver() {
        public void onSubscribe(Disposable d) {
          onSubscribe.incrementAndGet();
        }
        @Override
        public void onComplete() {
          onSuccess.incrementAndGet();
          throw new RuntimeException();
        }
        public void onError(Throwable e) {
          onError.incrementAndGet();
        }
      });
      assertEquals(i + 1, delegate.voidCalls.get());
      assertEquals(0, delegate.voidFailures.get());
      assertEquals(1, onSubscribe.get());
      assertEquals(1, onSuccess.get());
      assertEquals(0, onError.get());
    }
  }

  @Test
  public void testCompletableOnError() throws Exception {
    RuntimeException cause = new RuntimeException();
    Completable completable = itf.rxMethodWithHandlerAsyncResultVoid(Future.failedFuture(cause));
    assertEquals(0, delegate.voidCalls.get());
    for (int i = 0;i < 10;i++) {
      AtomicInteger onSubscribe = new AtomicInteger();
      AtomicInteger onSuccess = new AtomicInteger();
      AtomicInteger onError = new AtomicInteger();
      completable.subscribe(new CompletableObserver() {
        public void onSubscribe(Disposable d) {
          onSubscribe.incrementAndGet();
        }
        @Override
        public void onComplete() {
          onSuccess.incrementAndGet();
        }
        public void onError(Throwable e) {
          onError.incrementAndGet();
          throw new RuntimeException();
        }
      });
      assertEquals(i + 1, delegate.voidCalls.get());
      assertEquals(0, delegate.voidFailures.get());
      assertEquals(1, onSubscribe.get());
      assertEquals(0, onSuccess.get());
      assertEquals(1, onError.get());
    }
  }

  @Test
  public void testCompletableFailureOnError() throws Exception {
    RuntimeException cause = new RuntimeException();
    Completable completable = itf.rxMethodWithHandlerAsyncResultVoid(Future.failedFuture(cause));
    assertEquals(0, delegate.voidCalls.get());
    for (int i = 0;i < 10;i++) {
      int val = i;
      AtomicInteger onSubscribe = new AtomicInteger();
      AtomicInteger onSuccess = new AtomicInteger();
      AtomicInteger onError = new AtomicInteger();
      completable.subscribe(new CompletableObserver() {
        public void onSubscribe(Disposable d) {
          assertEquals(val, delegate.voidCalls.get());
          onSubscribe.incrementAndGet();
        }
        @Override
        public void onComplete() {
          onSuccess.incrementAndGet();
        }
        public void onError(Throwable e) {
          assertSame(cause, e);
          assertEquals(val + 1, delegate.voidCalls.get());
          onError.incrementAndGet();
        }
      });
      assertEquals(i + 1, delegate.voidCalls.get());
      assertEquals(0, delegate.voidFailures.get());
      assertEquals(1, onSubscribe.get());
      assertEquals(0, onSuccess.get());
      assertEquals(1, onError.get());
    }
  }

  @Test
  public void testCompletableUnsubscribeOnSubscribe() throws Exception {
    Completable completable = itf.rxMethodWithHandlerAsyncResultVoid(Future.succeededFuture());
    AtomicInteger onSubscribe = new AtomicInteger();
    AtomicInteger onSuccess = new AtomicInteger();
    AtomicInteger onError = new AtomicInteger();
    completable.subscribe(new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        onSubscribe.getAndIncrement();
        d.dispose();
      }
      @Override
      public void onComplete() {
        onSuccess.getAndIncrement();
      }
      @Override
      public void onError(Throwable e) {
        onError.getAndIncrement();
      }
    });
    assertEquals(0, delegate.voidCalls.get());
    assertEquals(0, delegate.voidFailures.get());
    assertEquals(1, onSubscribe.get());
    assertEquals(0, onSuccess.get());
    assertEquals(0, onError.get());
  }

  @Test
  public void testCompletableFailOnSubscribe() throws Exception {
    Completable completable = itf.rxMethodWithHandlerAsyncResultVoid(Future.succeededFuture());
    AtomicInteger onSubscribe = new AtomicInteger();
    AtomicInteger onSuccess = new AtomicInteger();
    AtomicInteger onError = new AtomicInteger();
    RuntimeException cause = new RuntimeException();
    try {
      completable.subscribe(new CompletableObserver() {
        @Override
        public void onSubscribe(Disposable d) {
          onSubscribe.getAndIncrement();
          throw cause;
        }
        @Override
        public void onComplete() {
          onSuccess.getAndIncrement();
        }
        @Override
        public void onError(Throwable e) {
          onError.getAndIncrement();
        }
      });
    } catch (NullPointerException expected) {
      // Thrown by RxJava2
    }
    assertEquals(0, delegate.voidCalls.get());
    assertEquals(0, delegate.voidFailures.get());
    assertEquals(1, onSubscribe.get());
    assertEquals(0, onSuccess.get());
    assertEquals(0, onError.get());
  }

  @Test
  public void testCompletableUnsubscribeBeforeSuccess() throws Exception {
    Future<Void> fut = Future.future();
    Completable completable = itf.rxMethodWithHandlerAsyncResultVoid(fut);
    AtomicInteger onSubscribe = new AtomicInteger();
    AtomicInteger onSuccess = new AtomicInteger();
    AtomicInteger onError = new AtomicInteger();
    AtomicReference<Disposable> sub = new AtomicReference<>();
    completable.subscribe(new CompletableObserver() {
      @Override
      public void onSubscribe(Disposable d) {
        onSubscribe.getAndIncrement();
        sub.set(d);
      }
      @Override
      public void onComplete() {
        onSuccess.getAndIncrement();
      }
      @Override
      public void onError(Throwable e) {
        onError.getAndIncrement();
      }
    });
    assertEquals(1, delegate.voidCalls.get());
    assertEquals(0, delegate.voidFailures.get());
    assertEquals(1, onSubscribe.get());
    assertEquals(0, onSuccess.get());
    assertEquals(0, onError.get());
    sub.get().dispose();
    fut.complete();
    assertEquals(1, delegate.voidCalls.get());
    assertEquals(1, onSubscribe.get());
    assertEquals(0, onSuccess.get());
    assertEquals(0, onError.get());
  }
}
