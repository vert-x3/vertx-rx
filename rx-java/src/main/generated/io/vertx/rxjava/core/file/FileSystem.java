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

import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.List;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Contains a broad set of operations for manipulating files on the file system.
 * <p>
 * A (potential) blocking and non blocking version of each operation is provided.
 * <p>
 * The non blocking versions take a handler which is called when the operation completes or an error occurs.
 * <p>
 * The blocking versions are named {@code xxxBlocking} and return the results, or throw exceptions directly.
 * In many cases, depending on the operating system and file system some of the potentially blocking operations
 * can return quickly, which is why we provide them, but it's highly recommended that you test how long they take to
 * return in your particular application before using them on an event loop.
 * <p>
 * Please consult the documentation for more information on file system support.
 *
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class FileSystem {

  final io.vertx.core.file.FileSystem delegate;

  public FileSystem(io.vertx.core.file.FileSystem delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Copy a file from the path {@code from} to path {@code to}, asynchronously.
   * <p>
   * The copy will fail if the destination already exists.
   *
   * @param from  the path to copy from
   * @param to  the path to copy to
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem copy(String from, String to, Handler<AsyncResult<Void>> handler) {
    this.delegate.copy(from, to, handler);
    return this;
  }

  public Observable<Void> copyObservable(String from, String to) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    copy(from, to, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #copy(String, String, Handler)}
   */
  public FileSystem copyBlocking(String from, String to) {
    this.delegate.copyBlocking(from, to);
    return this;
  }

  /**
   * Copy a file from the path {@code from} to path {@code to}, asynchronously.
   * <p>
   * If {@code recursive} is {@code true} and {@code from} represents a directory, then the directory and its contents
   * will be copied recursively to the destination {@code to}.
   * <p>
   * The copy will fail if the destination if the destination already exists.
   *
   * @param from  the path to copy from
   * @param to  the path to copy to
   * @param recursive
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem copyRecursive(String from, String to, boolean recursive, Handler<AsyncResult<Void>> handler) {
    this.delegate.copyRecursive(from, to, recursive, handler);
    return this;
  }

  public Observable<Void> copyRecursiveObservable(String from, String to, boolean recursive) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    copyRecursive(from, to, recursive, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #copyRecursive(String, String, boolean, Handler)}
   */
  public FileSystem copyRecursiveBlocking(String from, String to, boolean recursive) {
    this.delegate.copyRecursiveBlocking(from, to, recursive);
    return this;
  }

  /**
   * Move a file from the path {@code from} to path {@code to}, asynchronously.
   * <p>
   * The move will fail if the destination already exists.
   *
   * @param from  the path to copy from
   * @param to  the path to copy to
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem move(String from, String to, Handler<AsyncResult<Void>> handler) {
    this.delegate.move(from, to, handler);
    return this;
  }

  public Observable<Void> moveObservable(String from, String to) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    move(from, to, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #move(String, String, Handler)}
   */
  public FileSystem moveBlocking(String from, String to) {
    this.delegate.moveBlocking(from, to);
    return this;
  }

  /**
   * Truncate the file represented by {@code path} to length {@code len} in bytes, asynchronously.
   * <p>
   * The operation will fail if the file does not exist or {@code len} is less than {@code zero}.
   *
   * @param path  the path to the file
   * @param len  the length to truncate it to
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem truncate(String path, long len, Handler<AsyncResult<Void>> handler) {
    this.delegate.truncate(path, len, handler);
    return this;
  }

  public Observable<Void> truncateObservable(String path, long len) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    truncate(path, len, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #truncate(String, long, Handler)}
   */
  public FileSystem truncateBlocking(String path, long len) {
    this.delegate.truncateBlocking(path, len);
    return this;
  }

  /**
   * Change the permissions on the file represented by {@code path} to {@code perms}, asynchronously.
   * <p>
   * The permission String takes the form rwxr-x--- as
   * specified <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.
   *
   * @param path  the path to the file
   * @param perms  the permissions string
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem chmod(String path, String perms, Handler<AsyncResult<Void>> handler) {
    this.delegate.chmod(path, perms, handler);
    return this;
  }

  public Observable<Void> chmodObservable(String path, String perms) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    chmod(path, perms, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #chmod(String, String, Handler) }
   */
  public FileSystem chmodBlocking(String path, String perms) {
    this.delegate.chmodBlocking(path, perms);
    return this;
  }

  /**
   * Change the permissions on the file represented by {@code path} to {@code perms}, asynchronously.<p>
   * The permission String takes the form rwxr-x--- as
   * specified in {<a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>}.
   * <p>
   * If the file is directory then all contents will also have their permissions changed recursively. Any directory permissions will
   * be set to {@code dirPerms}, whilst any normal file permissions will be set to {@code perms}.
   *
   * @param path  the path to the file
   * @param perms  the permissions string
   * @param dirPerms  the directory permissions
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem chmodRecursive(String path, String perms, String dirPerms, Handler<AsyncResult<Void>> handler) {
    this.delegate.chmodRecursive(path, perms, dirPerms, handler);
    return this;
  }

  public Observable<Void> chmodRecursiveObservable(String path, String perms, String dirPerms) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    chmodRecursive(path, perms, dirPerms, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #chmodRecursive(String, String, String, Handler)}
   */
  public FileSystem chmodRecursiveBlocking(String path, String perms, String dirPerms) {
    this.delegate.chmodRecursiveBlocking(path, perms, dirPerms);
    return this;
  }

  /**
   * Change the ownership on the file represented by {@code path} to {@code user} and {code group}, asynchronously.
   *
   * @param path  the path to the file
   * @param user  the user name
   * @param group  the user group
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem chown(String path, String user, String group, Handler<AsyncResult<Void>> handler) {
    this.delegate.chown(path, user, group, handler);
    return this;
  }

  public Observable<Void> chownObservable(String path, String user, String group) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    chown(path, user, group, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #chown(String, String, String, Handler)}

   */
  public FileSystem chownBlocking(String path, String user, String group) {
    this.delegate.chownBlocking(path, user, group);
    return this;
  }

  /**
   * Obtain properties for the file represented by {@code path}, asynchronously.
   * <p>
   * If the file is a link, the link will be followed.
   *
   * @param path  the path to the file
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem props(String path, Handler<AsyncResult<FileProps>> handler) {
    this.delegate.props(path, new Handler<AsyncResult<io.vertx.core.file.FileProps>>() {
      public void handle(AsyncResult<io.vertx.core.file.FileProps> event) {
        AsyncResult<FileProps> f;
        if (event.succeeded()) {
          f = InternalHelper.<FileProps>result(new FileProps(event.result()));
        } else {
          f = InternalHelper.<FileProps>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<FileProps> propsObservable(String path) {
    io.vertx.rx.java.ObservableFuture<FileProps> handler = io.vertx.rx.java.RxHelper.observableFuture();
    props(path, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #props(String, Handler)}
   */
  public FileProps propsBlocking(String path) {
    FileProps ret= FileProps.newInstance(this.delegate.propsBlocking(path));
    return ret;
  }

  /**
   * Obtain properties for the link represented by {@code path}, asynchronously.
   * <p>
   * The link will not be followed.
   *
   * @param path  the path to the file
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem lprops(String path, Handler<AsyncResult<FileProps>> handler) {
    this.delegate.lprops(path, new Handler<AsyncResult<io.vertx.core.file.FileProps>>() {
      public void handle(AsyncResult<io.vertx.core.file.FileProps> event) {
        AsyncResult<FileProps> f;
        if (event.succeeded()) {
          f = InternalHelper.<FileProps>result(new FileProps(event.result()));
        } else {
          f = InternalHelper.<FileProps>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<FileProps> lpropsObservable(String path) {
    io.vertx.rx.java.ObservableFuture<FileProps> handler = io.vertx.rx.java.RxHelper.observableFuture();
    lprops(path, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #lprops(String, Handler)}
   */
  public FileProps lpropsBlocking(String path) {
    FileProps ret= FileProps.newInstance(this.delegate.lpropsBlocking(path));
    return ret;
  }

  /**
   * Create a hard link on the file system from {@code link} to {@code existing}, asynchronously.
   *
   * @param link  the link
   * @param existing  the link destination
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem link(String link, String existing, Handler<AsyncResult<Void>> handler) {
    this.delegate.link(link, existing, handler);
    return this;
  }

  public Observable<Void> linkObservable(String link, String existing) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    link(link, existing, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #link(String, String, Handler)}
   */
  public FileSystem linkBlocking(String link, String existing) {
    this.delegate.linkBlocking(link, existing);
    return this;
  }

  /**
   * Create a symbolic link on the file system from {@code link} to {@code existing}, asynchronously.
   *
   * @param link  the link
   * @param existing  the link destination
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem symlink(String link, String existing, Handler<AsyncResult<Void>> handler) {
    this.delegate.symlink(link, existing, handler);
    return this;
  }

  public Observable<Void> symlinkObservable(String link, String existing) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    symlink(link, existing, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #link(String, String, Handler)}
   */
  public FileSystem symlinkBlocking(String link, String existing) {
    this.delegate.symlinkBlocking(link, existing);
    return this;
  }

  /**
   * Unlinks the link on the file system represented by the path {@code link}, asynchronously.
   *
   * @param link  the link
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem unlink(String link, Handler<AsyncResult<Void>> handler) {
    this.delegate.unlink(link, handler);
    return this;
  }

  public Observable<Void> unlinkObservable(String link) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    unlink(link, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #unlink(String, Handler)}
   */
  public FileSystem unlinkBlocking(String link) {
    this.delegate.unlinkBlocking(link);
    return this;
  }

  /**
   * Returns the path representing the file that the symbolic link specified by {@code link} points to, asynchronously.
   *
   * @param link  the link
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem readSymlink(String link, Handler<AsyncResult<String>> handler) {
    this.delegate.readSymlink(link, handler);
    return this;
  }

  public Observable<String> readSymlinkObservable(String link) {
    io.vertx.rx.java.ObservableFuture<String> handler = io.vertx.rx.java.RxHelper.observableFuture();
    readSymlink(link, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #readSymlink(String, Handler)}
   */
  public String readSymlinkBlocking(String link) {
    String ret = this.delegate.readSymlinkBlocking(link);
    return ret;
  }

  /**
   * Deletes the file represented by the specified {@code path}, asynchronously.
   *
   * @param path  path to the file
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem delete(String path, Handler<AsyncResult<Void>> handler) {
    this.delegate.delete(path, handler);
    return this;
  }

  public Observable<Void> deleteObservable(String path) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    delete(path, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #delete(String, Handler)}
   */
  public FileSystem deleteBlocking(String path) {
    this.delegate.deleteBlocking(path);
    return this;
  }

  /**
   * Deletes the file represented by the specified {@code path}, asynchronously.
   * <p>
   * If the path represents a directory and {@code recursive = true} then the directory and its contents will be
   * deleted recursively.
   *
   * @param path  path to the file
   * @param recursive  delete recursively?
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem deleteRecursive(String path, boolean recursive, Handler<AsyncResult<Void>> handler) {
    this.delegate.deleteRecursive(path, recursive, handler);
    return this;
  }

  public Observable<Void> deleteRecursiveObservable(String path, boolean recursive) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    deleteRecursive(path, recursive, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #deleteRecursive(String, boolean, Handler)}
   */
  public FileSystem deleteRecursiveBlocking(String path, boolean recursive) {
    this.delegate.deleteRecursiveBlocking(path, recursive);
    return this;
  }

  /**
   * Create the directory represented by {@code path}, asynchronously.
   * <p>
   * The operation will fail if the directory already exists.
   *
   * @param path  path to the file
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem mkdir(String path, Handler<AsyncResult<Void>> handler) {
    this.delegate.mkdir(path, handler);
    return this;
  }

  public Observable<Void> mkdirObservable(String path) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    mkdir(path, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #mkdir(String, Handler)}
   */
  public FileSystem mkdirBlocking(String path) {
    this.delegate.mkdirBlocking(path);
    return this;
  }

  /**
   * Create the directory represented by {@code path}, asynchronously.
   * <p>
   * The new directory will be created with permissions as specified by {@code perms}.
   * <p>
   * The permission String takes the form rwxr-x--- as specified
   * in <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.
   * <p>
   * The operation will fail if the directory already exists.
   *
   * @param path  path to the file
   * @param perms  the permissions string
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem mkdir(String path, String perms, Handler<AsyncResult<Void>> handler) {
    this.delegate.mkdir(path, perms, handler);
    return this;
  }

  public Observable<Void> mkdirObservable(String path, String perms) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    mkdir(path, perms, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #mkdir(String, String, Handler)}
   */
  public FileSystem mkdirBlocking(String path, String perms) {
    this.delegate.mkdirBlocking(path, perms);
    return this;
  }

  /**
   * Create the directory represented by {@code path} and any non existent parents, asynchronously.
   * <p>
   * The operation will fail if the directory already exists.
   *
   * @param path  path to the file
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem mkdirs(String path, Handler<AsyncResult<Void>> handler) {
    this.delegate.mkdirs(path, handler);
    return this;
  }

  public Observable<Void> mkdirsObservable(String path) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    mkdirs(path, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #mkdirs(String, Handler)}
   */
  public FileSystem mkdirsBlocking(String path) {
    this.delegate.mkdirsBlocking(path);
    return this;
  }

  /**
   * Create the directory represented by {@code path} and any non existent parents, asynchronously.
   * <p>
   * The new directory will be created with permissions as specified by {@code perms}.
   * <p>
   * The permission String takes the form rwxr-x--- as specified
   * in <a href="http://download.oracle.com/javase/7/docs/api/java/nio/file/attribute/PosixFilePermissions.html">here</a>.
   * <p>
   * The operation will fail if the directory already exists.<p>
   *
   * @param path  path to the file
   * @param perms  the permissions string
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem mkdirs(String path, String perms, Handler<AsyncResult<Void>> handler) {
    this.delegate.mkdirs(path, perms, handler);
    return this;
  }

  public Observable<Void> mkdirsObservable(String path, String perms) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    mkdirs(path, perms, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #mkdirs(String, String, Handler)}
   */
  public FileSystem mkdirsBlocking(String path, String perms) {
    this.delegate.mkdirsBlocking(path, perms);
    return this;
  }

  /**
   * Read the contents of the directory specified by {@code path}, asynchronously.
   * <p>
   * The result is an array of String representing the paths of the files inside the directory.
   *
   * @param path  path to the file
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem readDir(String path, Handler<AsyncResult<List<String>>> handler) {
    this.delegate.readDir(path, handler);
    return this;
  }

  public Observable<List<String>> readDirObservable(String path) {
    io.vertx.rx.java.ObservableFuture<List<String>> handler = io.vertx.rx.java.RxHelper.observableFuture();
    readDir(path, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #readDir(String, Handler)}
   */
  public List<String> readDirBlocking(String path) {
    List<String> ret = this.delegate.readDirBlocking(path);
;
    return ret;
  }

  /**
   * Read the contents of the directory specified by {@code path}, asynchronously.
   * <p>
   * The parameter {@code filter} is a regular expression. If {@code filter} is specified then only the paths that
   * match  @{filter}will be returned.
   * <p>
   * The result is an array of String representing the paths of the files inside the directory.
   *
   * @param path  path to the directory
   * @param filter  the filter expression
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem readDir(String path, String filter, Handler<AsyncResult<List<String>>> handler) {
    this.delegate.readDir(path, filter, handler);
    return this;
  }

  public Observable<List<String>> readDirObservable(String path, String filter) {
    io.vertx.rx.java.ObservableFuture<List<String>> handler = io.vertx.rx.java.RxHelper.observableFuture();
    readDir(path, filter, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #readDir(String, String, Handler)}
   */
  public List<String> readDirBlocking(String path, String filter) {
    List<String> ret = this.delegate.readDirBlocking(path, filter);
;
    return ret;
  }

  /**
   * Reads the entire file as represented by the path {@code path} as a {@link Buffer}, asynchronously.
   * <p>
   * Do not user this method to read very large files or you risk running out of available RAM.
   *
   * @param path  path to the file
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem readFile(String path, Handler<AsyncResult<Buffer>> handler) {
    this.delegate.readFile(path, new Handler<AsyncResult<io.vertx.core.buffer.Buffer>>() {
      public void handle(AsyncResult<io.vertx.core.buffer.Buffer> event) {
        AsyncResult<Buffer> f;
        if (event.succeeded()) {
          f = InternalHelper.<Buffer>result(new Buffer(event.result()));
        } else {
          f = InternalHelper.<Buffer>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<Buffer> readFileObservable(String path) {
    io.vertx.rx.java.ObservableFuture<Buffer> handler = io.vertx.rx.java.RxHelper.observableFuture();
    readFile(path, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #readFile(String, Handler)}
   */
  public Buffer readFileBlocking(String path) {
    Buffer ret= Buffer.newInstance(this.delegate.readFileBlocking(path));
    return ret;
  }

  /**
   * Creates the file, and writes the specified {@code Buffer data} to the file represented by the path {@code path},
   * asynchronously.
   *
   * @param path  path to the file
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem writeFile(String path, Buffer data, Handler<AsyncResult<Void>> handler) {
    this.delegate.writeFile(path, (io.vertx.core.buffer.Buffer) data.getDelegate(), handler);
    return this;
  }

  public Observable<Void> writeFileObservable(String path, Buffer data) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    writeFile(path, data, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #writeFile(String, Buffer, Handler)}
   */
  public FileSystem writeFileBlocking(String path, Buffer data) {
    this.delegate.writeFileBlocking(path, (io.vertx.core.buffer.Buffer) data.getDelegate());
    return this;
  }

  /**
   * Open the file represented by {@code path}, asynchronously.
   * <p>
   * The file is opened for both reading and writing. If the file does not already exist it will be created.
   *
   * @param path  path to the file
   * @param options options describing how the file should be opened
   * @return a reference to this, so the API can be used fluently

   */
  public FileSystem open(String path, OpenOptions options, Handler<AsyncResult<AsyncFile>> handler) {
    this.delegate.open(path, options, new Handler<AsyncResult<io.vertx.core.file.AsyncFile>>() {
      public void handle(AsyncResult<io.vertx.core.file.AsyncFile> event) {
        AsyncResult<AsyncFile> f;
        if (event.succeeded()) {
          f = InternalHelper.<AsyncFile>result(new AsyncFile(event.result()));
        } else {
          f = InternalHelper.<AsyncFile>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<AsyncFile> openObservable(String path, OpenOptions options) {
    io.vertx.rx.java.ObservableFuture<AsyncFile> handler = io.vertx.rx.java.RxHelper.observableFuture();
    open(path, options, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #open(String, io.vertx.core.file.OpenOptions, Handler)}
   */
  public AsyncFile openBlocking(String path, OpenOptions options) {
    AsyncFile ret= AsyncFile.newInstance(this.delegate.openBlocking(path, options));
    return ret;
  }

  /**
   * Creates an empty file with the specified {@code path}, asynchronously.
   *
   * @param path  path to the file
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem createFile(String path, Handler<AsyncResult<Void>> handler) {
    this.delegate.createFile(path, handler);
    return this;
  }

  public Observable<Void> createFileObservable(String path) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    createFile(path, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #createFile(String, Handler)}
   */
  public FileSystem createFileBlocking(String path) {
    this.delegate.createFileBlocking(path);
    return this;
  }

  /**
   * Creates an empty file with the specified {@code path} and permissions {@code perms}, asynchronously.
   *
   * @param path  path to the file
   * @param perms  the permissions string
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem createFile(String path, String perms, Handler<AsyncResult<Void>> handler) {
    this.delegate.createFile(path, perms, handler);
    return this;
  }

  public Observable<Void> createFileObservable(String path, String perms) {
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    createFile(path, perms, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #createFile(String, String, Handler)}
   */
  public FileSystem createFileBlocking(String path, String perms) {
    this.delegate.createFileBlocking(path, perms);
    return this;
  }

  /**
   * Determines whether the file as specified by the path {@code path} exists, asynchronously.
   *
   * @param path  path to the file
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem exists(String path, Handler<AsyncResult<Boolean>> handler) {
    this.delegate.exists(path, handler);
    return this;
  }

  public Observable<Boolean> existsObservable(String path) {
    io.vertx.rx.java.ObservableFuture<Boolean> handler = io.vertx.rx.java.RxHelper.observableFuture();
    exists(path, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #exists(String, Handler)}
   */
  public boolean existsBlocking(String path) {
    boolean ret = this.delegate.existsBlocking(path);
    return ret;
  }

  /**
   * Returns properties of the file-system being used by the specified {@code path}, asynchronously.
   *
   * @param path  path to anywhere on the filesystem
   * @param handler  the handler that will be called on completion
   * @return a reference to this, so the API can be used fluently
   */
  public FileSystem fsProps(String path, Handler<AsyncResult<FileSystemProps>> handler) {
    this.delegate.fsProps(path, new Handler<AsyncResult<io.vertx.core.file.FileSystemProps>>() {
      public void handle(AsyncResult<io.vertx.core.file.FileSystemProps> event) {
        AsyncResult<FileSystemProps> f;
        if (event.succeeded()) {
          f = InternalHelper.<FileSystemProps>result(new FileSystemProps(event.result()));
        } else {
          f = InternalHelper.<FileSystemProps>failure(event.cause());
        }
        handler.handle(f);
      }
    });
    return this;
  }

  public Observable<FileSystemProps> fsPropsObservable(String path) {
    io.vertx.rx.java.ObservableFuture<FileSystemProps> handler = io.vertx.rx.java.RxHelper.observableFuture();
    fsProps(path, handler.toHandler());
    return handler;
  }

  /**
   * Blocking version of {@link #fsProps(String, Handler)}
   */
  public FileSystemProps fsPropsBlocking(String path) {
    FileSystemProps ret= FileSystemProps.newInstance(this.delegate.fsPropsBlocking(path));
    return ret;
  }


  public static FileSystem newInstance(io.vertx.core.file.FileSystem arg) {
    return new FileSystem(arg);
  }
}
