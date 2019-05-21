/*
 * Copyright 2019 Red Hat, Inc.
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

package io.vertx.lang.rx;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author Thomas Segismont
 */
public class MappingIterator<U, V> implements Iterator<V> {

  private final Iterator<U> iterator;
  private final Function<U, V> mapping;

  public MappingIterator(Iterator<U> iterator, Function<U, V> mapping) {
    this.iterator = iterator;
    this.mapping = mapping;
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public V next() {
    return mapping.apply(iterator.next());
  }
}
