/*
 * Copyright 2016 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package examples;

import io.reactivex.Observable;
import io.vertx.docgen.Source;
import io.vertx.reactivex.kafka.client.consumer.KafkaConsumer;
import io.vertx.reactivex.kafka.client.consumer.KafkaConsumerRecord;

@Source
public class KafkaClientExamples {

  public void consumer(KafkaConsumer<String, Long> consumer) {

    Observable<KafkaConsumerRecord<String, Long>> observable = consumer.toObservable();

    observable
      .map(record -> record.value())
      .buffer(256)
      .map(
      list -> list.stream().mapToDouble(n -> n).average()
    ).subscribe(val -> {

      // Obtained an average

    });
  }
}
