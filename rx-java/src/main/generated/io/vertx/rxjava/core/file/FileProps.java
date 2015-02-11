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

package io.vertx.rxjava.core.file;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;

/**
 * Represents properties of a file on the file system.
 * <p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class FileProps {

  final io.vertx.core.file.FileProps delegate;

  public FileProps(io.vertx.core.file.FileProps delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * The date the file was created
   */
  public long creationTime() {
    long ret = this.delegate.creationTime();
    return ret;
  }

  /**
   * The date the file was last accessed
   */
  public long lastAccessTime() {
    long ret = this.delegate.lastAccessTime();
    return ret;
  }

  /**
   * The date the file was last modified
   */
  public long lastModifiedTime() {
    long ret = this.delegate.lastModifiedTime();
    return ret;
  }

  /**
   * Is the file a directory?
   */
  public boolean isDirectory() {
    boolean ret = this.delegate.isDirectory();
    return ret;
  }

  /**
   * Is the file some other type? (I.e. not a directory, regular file or symbolic link)
   */
  public boolean isOther() {
    boolean ret = this.delegate.isOther();
    return ret;
  }

  /**
   * Is the file a regular file?
   */
  public boolean isRegularFile() {
    boolean ret = this.delegate.isRegularFile();
    return ret;
  }

  /**
   * Is the file a symbolic link?
   */
  public boolean isSymbolicLink() {
    boolean ret = this.delegate.isSymbolicLink();
    return ret;
  }

  /**
   * The size of the file, in bytes
   */
  public long size() {
    long ret = this.delegate.size();
    return ret;
  }


  public static FileProps newInstance(io.vertx.core.file.FileProps arg) {
    return new FileProps(arg);
  }
}
