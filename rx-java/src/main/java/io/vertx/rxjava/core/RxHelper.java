package io.vertx.rxjava.core;

import io.vertx.core.http.HttpMethod;
import io.vertx.rx.java.ContextScheduler;
import io.vertx.rx.java.UnmarshallerOperator;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientRequest;
import io.vertx.rxjava.core.http.HttpClientResponse;
import rx.Observable;
import rx.Scheduler;
import rx.plugins.RxJavaSchedulersHook;

/**
 * A set of helpers for RxJava and Vert.x.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RxHelper {

  /**
   * Create a scheduler hook for a {@link io.vertx.rxjava.core.Vertx} object.
   *
   * @param vertx the vertx object
   * @return the scheduler hook
   */
  public static RxJavaSchedulersHook schedulerHook(Vertx vertx) {
    return io.vertx.rx.java.RxHelper.schedulerHook(vertx.delegate);
  }

  /**
   * Create a scheduler hook for a {@link io.vertx.rxjava.core.Context} object.
   *
   * @param context the context object
   * @return the scheduler hook
   */
  public static RxJavaSchedulersHook schedulerHook(Context context) {
    return io.vertx.rx.java.RxHelper.schedulerHook(context.delegate);
  }

  /**
   * Create a scheduler for a {@link Vertx} object, actions are executed on the event loop of the current context.
   *
   * @param vertx the vertx object
   * @return the scheduler
   */
  public static Scheduler scheduler(Vertx vertx) {
    return new ContextScheduler(vertx.delegate, false);
  }

  /**
   * Create a scheduler for a {@link Context}, actions are executed on the event loop of this context.
   *
   * @param context the context object
   * @return the scheduler
   */
  public static Scheduler scheduler(Context context) {
    return new ContextScheduler(context.delegate, false);
  }

  /**
   * Create a scheduler for a {@link Vertx} object, actions can be blocking, they are not executed
   * on Vertx event loop.
   *
   * @param vertx the vertx object
   * @return the scheduler
   */
  public static Scheduler blockingScheduler(Vertx vertx) {
    return new ContextScheduler(vertx.delegate.getOrCreateContext(), true);
  }

  /**
   * Returns a json unmarshaller for the specified java type as a {@link rx.Observable.Operator} instance.<p/>
   *
   * The marshaller can be used with the {@link rx.Observable#lift(rx.Observable.Operator)} method to transform
   * a {@literal Observable<Buffer>} into a {@literal Observable<T>}.<p/>
   *
   * The unmarshaller buffers the content until <i>onComplete</i> is called, then unmarshalling happens.<p/>
   *
   * Note that the returned observable will emit at most a single object.
   *
   * @param mappedType the type to unmarshall
   * @return the unmarshaller operator
   */
  public static <T> Observable.Operator<T, Buffer> unmarshaller(Class<T> mappedType) {
    return new UnmarshallerOperator<T, Buffer>(mappedType) {
      @Override
      public io.vertx.core.buffer.Buffer unwrap(Buffer buffer) {
        return ((io.vertx.core.buffer.Buffer) buffer.getDelegate());
      }
    };
  }

  /**
   * @see #get(HttpClient, int, String, String, MultiMap)
   */
  public static Observable<HttpClientResponse> get(HttpClient client, String requestURI) {
    return Observable.create(subscriber -> {
      HttpClientRequest req = client.get(requestURI);
      Observable<HttpClientResponse> resp = req.toObservable();
      resp.subscribe(subscriber);
      req.end();
    });
  }

  /**
   * @see #get(HttpClient, int, String, String, MultiMap)
   */
  public static Observable<HttpClientResponse> get(HttpClient client, String host, String requestURI) {
    return Observable.create(subscriber -> {
      HttpClientRequest req = client.get(host, requestURI);
      Observable<HttpClientResponse> resp = req.toObservable();
      resp.subscribe(subscriber);
      req.end();
    });
  }

  /**
   * @see #get(HttpClient, int, String, String, MultiMap)
   */
  public static Observable<HttpClientResponse> get(HttpClient client, int port, String host, String requestURI) {
    return Observable.create(subscriber -> {
      HttpClientRequest req = client.get(port, host, requestURI);
      Observable<HttpClientResponse> resp = req.toObservable();
      resp.subscribe(subscriber);
      req.end();
    });
  }

  /**
   * Returns an {@code Observable<HttpClientResponse>} that performs a <i>get</i> request each time it is subscribed. The
   * returned observable can be used to consume the response.<p>
   *
   * This is different from the {@link HttpClientRequest#toObservable()} that should be subscribed before the request is ended
   * and should be consumed immediatly and once.
   *
   * @param client the http client
   * @param port the remote port
   * @param host the remote host
   * @param requestURI the request URI
   * @param headers the request headers
   * @return the response observable
   */
  public static Observable<HttpClientResponse> get(HttpClient client, int port, String host, String requestURI, MultiMap headers) {
    return Observable.create(subscriber -> {
      HttpClientRequest req = client.get(port, host, requestURI);
      req.headers().addAll(headers);
      Observable<HttpClientResponse> resp = req.toObservable();
      resp.subscribe(subscriber);
      req.end();
    });
  }
}
