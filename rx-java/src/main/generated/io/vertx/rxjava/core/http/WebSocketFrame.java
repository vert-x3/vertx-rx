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

/**
 * A WebSocket frame that represents either text or binary data.
 * <p>
 * A WebSocket message is composed of one or more WebSocket frames.
 * <p>
 * If there is a just a single frame in the message then a single text or binary frame should be created with final = true.
 * <p>
 * If there are more than one frames in the message, then the first frame should be a text or binary frame with
 * final = false, followed by one or more continuation frames. The last continuation frame should have final = true.
 *
 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
 * @author <a href="http://gleamynode.net/">Trustin Lee</a>
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @version $Rev: 2080 $, $Date: 2010-01-26 18:04:19 +0900 (Tue, 26 Jan 2010) $
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class WebSocketFrame {

  final io.vertx.core.http.WebSocketFrame delegate;

  public WebSocketFrame(io.vertx.core.http.WebSocketFrame delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a binary WebSocket frame.
   *
   * @param data  the data for the frame
   * @param isFinal  true if it's the final frame in the WebSocket message
   * @return the frame
   */
  public static WebSocketFrame binaryFrame(Buffer data, boolean isFinal) {
    WebSocketFrame ret= WebSocketFrame.newInstance(io.vertx.core.http.WebSocketFrame.binaryFrame((io.vertx.core.buffer.Buffer) data.getDelegate(), isFinal));
    return ret;
  }

  /**
   * Create a text WebSocket frame.
   *
   * @param str  the string for the frame
   * @param isFinal  true if it's the final frame in the WebSocket message
   * @return the frame
   */
  public static WebSocketFrame textFrame(String str, boolean isFinal) {
    WebSocketFrame ret= WebSocketFrame.newInstance(io.vertx.core.http.WebSocketFrame.textFrame(str, isFinal));
    return ret;
  }

  /**
   * Create a continuation frame
   *
   * @param data  the data for the frame
   * @param isFinal true if it's the final frame in the WebSocket message
   * @return the frame
   */
  public static WebSocketFrame continuationFrame(Buffer data, boolean isFinal) {
    WebSocketFrame ret= WebSocketFrame.newInstance(io.vertx.core.http.WebSocketFrame.continuationFrame((io.vertx.core.buffer.Buffer) data.getDelegate(), isFinal));
    return ret;
  }

  /**
   * @return true if it's a text frame
   */
  public boolean isText() {
    boolean ret = this.delegate.isText();
    return ret;
  }

  /**
   * @return true if it's a binary frame
   */
  public boolean isBinary() {
    boolean ret = this.delegate.isBinary();
    return ret;
  }

  /**
   * @return true if it's a continuation frame
   */
  public boolean isContinuation() {
    boolean ret = this.delegate.isContinuation();
    return ret;
  }

  /**
   * @return the content of this frame as a UTF-8 string and returns the
   * converted string. Only use this for text frames.
   */
  public String textData() {
    if (cached_0 != null) {
      return cached_0;
    }
    String ret = this.delegate.textData();
    cached_0 = ret;
    return ret;
  }

  /**
   * @return the data of the frame
   */
  public Buffer binaryData() {
    if (cached_1 != null) {
      return cached_1;
    }
    Buffer ret= Buffer.newInstance(this.delegate.binaryData());
    cached_1 = ret;
    return ret;
  }

  /**
   * @return true if this is the final frame.
   */
  public boolean isFinal() {
    boolean ret = this.delegate.isFinal();
    return ret;
  }

  private java.lang.String cached_0;
  private Buffer cached_1;

  public static WebSocketFrame newInstance(io.vertx.core.http.WebSocketFrame arg) {
    return new WebSocketFrame(arg);
  }
}
