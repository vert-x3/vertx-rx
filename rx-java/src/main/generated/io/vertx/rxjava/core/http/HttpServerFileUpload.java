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

package io.vertx.rxjava.core.http;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.core.Handler;

/**
 * Represents an file upload from an HTML FORM.
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class HttpServerFileUpload implements ReadStream<Buffer> {

  final io.vertx.core.http.HttpServerFileUpload delegate;

  public HttpServerFileUpload(io.vertx.core.http.HttpServerFileUpload delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<Buffer> observable;

  public synchronized rx.Observable<Buffer> toObservable() {
    if (observable == null) {
      java.util.function.Function<io.vertx.core.buffer.Buffer, Buffer> conv = Buffer::newInstance;
      io.vertx.lang.rxjava.ReadStreamAdapter<io.vertx.core.buffer.Buffer, Buffer> adapter = new io.vertx.lang.rxjava.ReadStreamAdapter<>(this, conv);
      observable = rx.Observable.create(adapter);
    }
    return observable;
  }

  public HttpServerFileUpload exceptionHandler(Handler<Throwable> handler) {
    this.delegate.exceptionHandler(handler);
    return this;
  }

  public HttpServerFileUpload handler(Handler<Buffer> handler) {
    this.delegate.handler(new Handler<io.vertx.core.buffer.Buffer>() {
      public void handle(io.vertx.core.buffer.Buffer event) {
        handler.handle(new Buffer(event));
      }
    });
    return this;
  }

  public HttpServerFileUpload endHandler(Handler<Void> endHandler) {
    this.delegate.endHandler(endHandler);
    return this;
  }

  public HttpServerFileUpload pause() {
    this.delegate.pause();
    return this;
  }

  public HttpServerFileUpload resume() {
    this.delegate.resume();
    return this;
  }

  /**
   * Stream the content of this upload to the given file on storage.
   *
   * @param filename  the name of the file
   */
  public HttpServerFileUpload streamToFileSystem(String filename) {
    this.delegate.streamToFileSystem(filename);
    return this;
  }

  /**
   * @return the filename which was used when upload the file.
   */
  public String filename() {
    String ret = this.delegate.filename();
    return ret;
  }

  /**
   * @return the name of the attribute
   */
  public String name() {
    String ret = this.delegate.name();
    return ret;
  }

  /**
   * @return  the content type for the upload
   */
  public String contentType() {
    String ret = this.delegate.contentType();
    return ret;
  }

  /**
   * @return the contentTransferEncoding for the upload
   */
  public String contentTransferEncoding() {
    String ret = this.delegate.contentTransferEncoding();
    return ret;
  }

  /**
   * @return the charset for the upload
   */
  public String charset() {
    String ret = this.delegate.charset();
    return ret;
  }

  /**
   * The size of the upload may not be available until it is all read.
   * Check {@link #isSizeAvailable} to determine this
   *
   * @return the size of the upload (in bytes)
   */
  public long size() {
    long ret = this.delegate.size();
    return ret;
  }

  /**
   * @return true if the size of the upload can be retrieved via {@link #size()}.
   */
  public boolean isSizeAvailable() {
    boolean ret = this.delegate.isSizeAvailable();
    return ret;
  }


  public static HttpServerFileUpload newInstance(io.vertx.core.http.HttpServerFileUpload arg) {
    return new HttpServerFileUpload(arg);
  }
}
