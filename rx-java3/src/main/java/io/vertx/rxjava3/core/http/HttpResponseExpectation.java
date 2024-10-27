package io.vertx.rxjava3.core.http;

import io.reactivex.rxjava3.core.SingleOperator;
import io.vertx.rxjava3.core.ExpectationOperator;

import java.util.List;

/**
 * Common expectations for HTTP responses.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HttpResponseExpectation {

  private HttpResponseExpectation() {
  }

  /**
   * Creates an expectation asserting that the status response code is equal to {@code statusCode}.
   *
   * @param statusCode the expected status code
   */
  public static <H extends HttpResponseHead> SingleOperator<H, H> status(int statusCode) {
    return new ExpectationOperator<>(io.vertx.core.http.HttpResponseExpectation.status(statusCode), HttpResponseHead::getDelegate);
  }

  /**
   * Creates an expectation asserting that the status response code is between to {@code min} (inclusive) and {@code max} (exclusive).
   *
   * @param min the min status code
   * @param max the max status code
   */
  public static <H extends HttpResponseHead> SingleOperator<H, H> status(int min, int max) {
    return new ExpectationOperator<>(io.vertx.core.http.HttpResponseExpectation.status(min, max), HttpResponseHead::getDelegate);
  }

  /**
   * Creates an expectation validating the response has a {@code content-type} header matching the {@code mimeType}.
   *
   * @param mimeType the mime type
   */
  public static <H extends HttpResponseHead> SingleOperator<H, H> contentType(String mimeType) {
    return new ExpectationOperator<>(io.vertx.core.http.HttpResponseExpectation.contentType(mimeType), HttpResponseHead::getDelegate);
  }

  /**
   * Creates an expectation validating the response has a {@code content-type} header matching one of the {@code mimeTypes}.
   *
   * @param mimeTypes the list of mime types
   */
  public static <H extends HttpResponseHead> SingleOperator<H, H> contentType(String... mimeTypes) {
    return new ExpectationOperator<>(io.vertx.core.http.HttpResponseExpectation.contentType(mimeTypes), HttpResponseHead::getDelegate);
  }

  /**
   * Creates an expectation validating the response has a {@code content-type} header matching one of the {@code mimeTypes}.
   *
   * @param mimeTypes the list of mime types
   */
  public static <H extends HttpResponseHead> SingleOperator<H, H> contentType(List<String> mimeTypes) {
    return new ExpectationOperator<>(io.vertx.core.http.HttpResponseExpectation.contentType(mimeTypes), HttpResponseHead::getDelegate);
  }
}
