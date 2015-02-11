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

package io.vertx.rxjava.core.buffer;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.core.shareddata.impl.ClusterSerializable;

/**
 * Most data is shuffled around inside Vert.x using buffers.
 * <p>
 * A buffer is a sequence of zero or more bytes that can read from or written to and which expands automatically as
 * necessary to accommodate any bytes written to it. You can perhaps think of a buffer as smart byte array.
 * <p>
 * Please consult the documentation for more information on buffers.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class Buffer {

  final io.vertx.core.buffer.Buffer delegate;

  public Buffer(io.vertx.core.buffer.Buffer delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a new, empty buffer.
   *
   * @return the buffer
   */
  public static Buffer buffer() {
    Buffer ret= Buffer.newInstance(io.vertx.core.buffer.Buffer.buffer());
    return ret;
  }

  /**
   * Create a new buffer given the initial size hint.
   * <p>
   * If you know the buffer will require a certain size, providing the hint can prevent unnecessary re-allocations
   * as the buffer is written to and resized.
   *
   * @param initialSizeHint  the hint, in bytes
   * @return the buffer
   */
  public static Buffer buffer(int initialSizeHint) {
    Buffer ret= Buffer.newInstance(io.vertx.core.buffer.Buffer.buffer(initialSizeHint));
    return ret;
  }

  /**
   * Create a new buffer from a string. The string will be UTF-8 encoded into the buffer.
   *
   * @param string  the string
   * @return  the buffer
   */
  public static Buffer buffer(String string) {
    Buffer ret= Buffer.newInstance(io.vertx.core.buffer.Buffer.buffer(string));
    return ret;
  }

  /**
   * Create a new buffer from a string and using the specified encoding.
   * The string will be encoded into the buffer using the specified encoding.
   *
   * @param string  the string
   * @return  the buffer
   */
  public static Buffer buffer(String string, String enc) {
    Buffer ret= Buffer.newInstance(io.vertx.core.buffer.Buffer.buffer(string, enc));
    return ret;
  }

  /**
   * Returns a {@code String} representation of the Buffer with the encoding specified by {@code enc}
   */
  public String toString(String enc) {
    String ret = this.delegate.toString(enc);
    return ret;
  }

  /**
   * Returns the {@code byte} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 1} is greater than the length of the Buffer.
   */
  public byte getByte(int pos) {
    byte ret = this.delegate.getByte(pos);
    return ret;
  }

  /**
   * Returns the {@code int} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 4} is greater than the length of the Buffer.
   */
  public int getInt(int pos) {
    int ret = this.delegate.getInt(pos);
    return ret;
  }

  /**
   * Returns the {@code long} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 8} is greater than the length of the Buffer.
   */
  public long getLong(int pos) {
    long ret = this.delegate.getLong(pos);
    return ret;
  }

  /**
   * Returns the {@code double} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 8} is greater than the length of the Buffer.
   */
  public double getDouble(int pos) {
    double ret = this.delegate.getDouble(pos);
    return ret;
  }

  /**
   * Returns the {@code float} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 4} is greater than the length of the Buffer.
   */
  public float getFloat(int pos) {
    float ret = this.delegate.getFloat(pos);
    return ret;
  }

  /**
   * Returns the {@code short} at position {@code pos} in the Buffer.
   *
   * @throws IndexOutOfBoundsException if the specified {@code pos} is less than {@code 0} or {@code pos + 2} is greater than the length of the Buffer.
   */
  public short getShort(int pos) {
    short ret = this.delegate.getShort(pos);
    return ret;
  }

  /**
   * Returns a copy of a sub-sequence the Buffer as a {@link io.vertx.core.buffer.Buffer} starting at position {@code start}
   * and ending at position {@code end - 1}
   */
  public Buffer getBuffer(int start, int end) {
    Buffer ret= Buffer.newInstance(this.delegate.getBuffer(start, end));
    return ret;
  }

  /**
   * Returns a copy of a sub-sequence the Buffer as a {@code String} starting at position {@code start}
   * and ending at position {@code end - 1} interpreted as a String in the specified encoding
   */
  public String getString(int start, int end, String enc) {
    String ret = this.delegate.getString(start, end, enc);
    return ret;
  }

  /**
   * Returns a copy of a sub-sequence the Buffer as a {@code String} starting at position {@code start}
   * and ending at position {@code end - 1} interpreted as a String in UTF-8 encoding
   */
  public String getString(int start, int end) {
    String ret = this.delegate.getString(start, end);
    return ret;
  }

  /**
   * Appends the specified {@code Buffer} to the end of this Buffer. The buffer will expand as necessary to accommodate
   * any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together.
   */
  public Buffer appendBuffer(Buffer buff) {
    this.delegate.appendBuffer((io.vertx.core.buffer.Buffer) buff.getDelegate());
    return this;
  }

  /**
   * Appends the specified {@code Buffer} starting at the {@code offset} using {@code len} to the end of this Buffer. The buffer will expand as necessary to accommodate
   * any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together.
   */
  public Buffer appendBuffer(Buffer buff, int offset, int len) {
    this.delegate.appendBuffer((io.vertx.core.buffer.Buffer) buff.getDelegate(), offset, len);
    return this;
  }

  /**
   * Appends the specified {@code byte} to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together.
   */
  public Buffer appendByte(byte b) {
    this.delegate.appendByte(b);
    return this;
  }

  /**
   * Appends the specified {@code int} to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together.
   */
  public Buffer appendInt(int i) {
    this.delegate.appendInt(i);
    return this;
  }

  /**
   * Appends the specified {@code long} to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together.
   */
  public Buffer appendLong(long l) {
    this.delegate.appendLong(l);
    return this;
  }

  /**
   * Appends the specified {@code short} to the end of the Buffer.The buffer will expand as necessary to accommodate any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together.
   */
  public Buffer appendShort(short s) {
    this.delegate.appendShort(s);
    return this;
  }

  /**
   * Appends the specified {@code float} to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together.
   */
  public Buffer appendFloat(float f) {
    this.delegate.appendFloat(f);
    return this;
  }

  /**
   * Appends the specified {@code double} to the end of the Buffer. The buffer will expand as necessary to accommodate any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together.
   */
  public Buffer appendDouble(double d) {
    this.delegate.appendDouble(d);
    return this;
  }

  /**
   * Appends the specified {@code String} to the end of the Buffer with the encoding as specified by {@code enc}.<p>
   * The buffer will expand as necessary to accommodate any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together.<p>
   */
  public Buffer appendString(String str, String enc) {
    this.delegate.appendString(str, enc);
    return this;
  }

  /**
   * Appends the specified {@code String str} to the end of the Buffer with UTF-8 encoding.<p>
   * The buffer will expand as necessary to accommodate any bytes written.<p>
   * Returns a reference to {@code this} so multiple operations can be appended together<p>
   */
  public Buffer appendString(String str) {
    this.delegate.appendString(str);
    return this;
  }

  /**
   * Sets the {@code byte} at position {@code pos} in the Buffer to the value {@code b}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  public Buffer setByte(int pos, byte b) {
    this.delegate.setByte(pos, b);
    return this;
  }

  /**
   * Sets the {@code int} at position {@code pos} in the Buffer to the value {@code i}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  public Buffer setInt(int pos, int i) {
    this.delegate.setInt(pos, i);
    return this;
  }

  /**
   * Sets the {@code long} at position {@code pos} in the Buffer to the value {@code l}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  public Buffer setLong(int pos, long l) {
    this.delegate.setLong(pos, l);
    return this;
  }

  /**
   * Sets the {@code double} at position {@code pos} in the Buffer to the value {@code d}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  public Buffer setDouble(int pos, double d) {
    this.delegate.setDouble(pos, d);
    return this;
  }

  /**
   * Sets the {@code float} at position {@code pos} in the Buffer to the value {@code f}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  public Buffer setFloat(int pos, float f) {
    this.delegate.setFloat(pos, f);
    return this;
  }

  /**
   * Sets the {@code short} at position {@code pos} in the Buffer to the value {@code s}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  public Buffer setShort(int pos, short s) {
    this.delegate.setShort(pos, s);
    return this;
  }

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the bytes represented by the {@code Buffer b}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  public Buffer setBuffer(int pos, Buffer b) {
    this.delegate.setBuffer(pos, (io.vertx.core.buffer.Buffer) b.getDelegate());
    return this;
  }

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the bytes represented by the {@code Buffer b} on the given {@code offset} and {@code len}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  public Buffer setBuffer(int pos, Buffer b, int offset, int len) {
    this.delegate.setBuffer(pos, (io.vertx.core.buffer.Buffer) b.getDelegate(), offset, len);
    return this;
  }

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the value of {@code str} encoded in UTF-8.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  public Buffer setString(int pos, String str) {
    this.delegate.setString(pos, str);
    return this;
  }

  /**
   * Sets the bytes at position {@code pos} in the Buffer to the value of {@code str} encoded in encoding {@code enc}.<p>
   * The buffer will expand as necessary to accommodate any value written.
   */
  public Buffer setString(int pos, String str, String enc) {
    this.delegate.setString(pos, str, enc);
    return this;
  }

  /**
   * Returns the length of the buffer, measured in bytes.
   * All positions are indexed from zero.
   */
  public int length() {
    int ret = this.delegate.length();
    return ret;
  }

  /**
   * Returns a copy of the entire Buffer.
   */
  public Buffer copy() {
    Buffer ret= Buffer.newInstance(this.delegate.copy());
    return ret;
  }

  /**
   * Returns a slice of this buffer. Modifying the content
   * of the returned buffer or this buffer affects each other's content
   * while they maintain separate indexes and marks.
   */
  public Buffer slice() {
    Buffer ret= Buffer.newInstance(this.delegate.slice());
    return ret;
  }

  /**
   * Returns a slice of this buffer. Modifying the content
   * of the returned buffer or this buffer affects each other's content
   * while they maintain separate indexes and marks.
   */
  public Buffer slice(int start, int end) {
    Buffer ret= Buffer.newInstance(this.delegate.slice(start, end));
    return ret;
  }


  public static Buffer newInstance(io.vertx.core.buffer.Buffer arg) {
    return new Buffer(arg);
  }
}
