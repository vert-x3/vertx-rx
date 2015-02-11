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

package io.vertx.rxjava.core.shareddata;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;

/**
 * Local maps can be used to share data safely in a single Vert.x instance.
 * <p>
 * The map only allows immutable keys and values in the map, OR certain mutable objects such as {@link io.vertx.core.buffer.Buffer}
 * instances which will be copied when they are added to the map.
 * <p>
 * This ensures there is no shared access to mutable state from different threads (e.g. different event loops) in the
 * Vert.x instance, and means you don't have to protect access to that state using synchronization or locks.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class LocalMap<K,V> {

  final io.vertx.core.shareddata.LocalMap delegate;

  public LocalMap(io.vertx.core.shareddata.LocalMap delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Get a value from the map
   *
   * @param key  the key
   * @return  the value, or null if none
   */
  public V get(K key) {
    V ret = (V) this.delegate.get(key);
    return ret;
  }

  /**
   * Put an entry in the map
   *
   * @param key  the key
   * @param value  the value
   * @return  return the old value, or null if none
   */
  public V put(K key, V value) {
    V ret = (V) this.delegate.put(key, value);
    return ret;
  }

  /**
   * Remove an entry from the map
   *
   * @param key  the key
   * @return  the old value
   */
  public V remove(K key) {
    V ret = (V) this.delegate.remove(key);
    return ret;
  }

  /**
   * Clear all entries in the map
   */
  public void clear() {
    this.delegate.clear();
  }

  /**
   * Get the size of the map
   *
   * @return  the number of entries in the map
   */
  public int size() {
    int ret = this.delegate.size();
    return ret;
  }

  /**
   * @return true if there are zero entries in the map
   */
  public boolean isEmpty() {
    boolean ret = this.delegate.isEmpty();
    return ret;
  }

  /**
   * Put the entry only if there is no existing entry for that key
   *
   * @param key  the key
   * @param value  the value
   * @return  the old value or null, if none
   */
  public V putIfAbsent(K key, V value) {
    V ret = (V) this.delegate.putIfAbsent(key, value);
    return ret;
  }

  /**
   * Remove the entry only if there is an entry with the specified key and value
   *
   * @param key  the key
   * @param value  the value
   * @return true if removed
   */
  public boolean removeIfPresent(K key, V value) {
    boolean ret = this.delegate.removeIfPresent(key, value);
    return ret;
  }

  /**
   * Replace the entry only if there is an existing entry with the specified key and value
   *
   * @param key  the key
   * @param oldValue  the old value
   * @param newValue  the new value
   * @return true if removed
   */
  public boolean replaceIfPresent(K key, V oldValue, V newValue) {
    boolean ret = this.delegate.replaceIfPresent(key, oldValue, newValue);
    return ret;
  }

  /**
   * Replace the entry only if there is an existing entry with the key
   *
   * @param key  the key
   * @param value  the new value
   * @return  the old value
   */
  public V replace(K key, V value) {
    V ret = (V) this.delegate.replace(key, value);
    return ret;
  }

  /**
   * Close and release the map
   */
  public void close() {
    this.delegate.close();
  }


  public static <K, V> LocalMap<K,V> newInstance(io.vertx.core.shareddata.LocalMap arg) {
    return new LocalMap<K, V> (arg);
  }
}
