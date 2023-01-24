/*
 * Copyright 2023 Red Hat, Inc.
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

package io.vertx.rxjava3.ext.web.handler.graphql.instrumentation;

import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import io.reactivex.rxjava3.core.Maybe;
import io.vertx.ext.web.handler.graphql.instrumentation.impl.ToCompletionStage;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Instrument data fetchers so that {@link Maybe} results are automatically converted to {@link CompletionStage}.
 */
@SuppressWarnings("rawtypes")
public class MaybeAdapter extends SimpleInstrumentation {

  private final ToCompletionStage<Maybe> toCompletionStage;

  private MaybeAdapter(Class<Maybe> targetType, Function<Maybe, CompletionStage<?>> converter) {
    toCompletionStage = new ToCompletionStage<>(targetType, converter);
  }

  @Override
  public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters, InstrumentationState state) {
    return toCompletionStage.instrumentDataFetcher(dataFetcher, parameters, state);
  }

  public static MaybeAdapter create() {
    return new MaybeAdapter(Maybe.class, Maybe::toCompletionStage);
  }
}
