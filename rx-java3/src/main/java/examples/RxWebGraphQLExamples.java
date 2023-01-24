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

package examples;

import graphql.GraphQL;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.reactivex.rxjava3.core.Single;
import io.vertx.docgen.Source;
import io.vertx.rxjava3.ext.web.handler.graphql.instrumentation.MaybeAdapter;
import io.vertx.rxjava3.ext.web.handler.graphql.instrumentation.SingleAdapter;

import java.util.List;

@Source
public class RxWebGraphQLExamples {

  public void singleAndMaybeAdapters(GraphQL.Builder graphQLBuilder) {
    graphQLBuilder.instrumentation(new ChainedInstrumentation(SingleAdapter.create(), MaybeAdapter.create()));
  }

  static class Link {
  }

  public void singleDataFetcher() {
    DataFetcher<Single<List<Link>>> dataFetcher = environment -> {
      Single<List<Link>> single = retrieveLinksFromBackend(environment);
      return single;
    };
  }

  private Single<List<Link>> retrieveLinksFromBackend(DataFetchingEnvironment environment) {
    return null;
  }
}
