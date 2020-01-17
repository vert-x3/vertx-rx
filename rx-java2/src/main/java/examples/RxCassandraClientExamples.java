package examples;

import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.reactivex.cassandra.CassandraClient;
import io.vertx.reactivex.cassandra.CassandraRowStream;
import io.vertx.reactivex.core.Vertx;

public class RxCassandraClientExamples {

  public void createClient(Vertx vertx) {
    CassandraClientOptions options = new CassandraClientOptions()
      .addContactPoint("node1.corp.int", 9142)
      .addContactPoint("node2.corp.int", 9142)
      .addContactPoint("node3.corp.int", 9142);
    CassandraClient cassandraClient = CassandraClient.createShared(vertx, options);
  }

  public void simpleQueryStream(CassandraClient cassandraClient) {
    cassandraClient.rxQueryStream("SELECT my_key FROM my_keyspace.my_table where my_key = my_value")
      // Convert the stream to a Flowable
      .flatMapPublisher(CassandraRowStream::toFlowable)
      .subscribe(row -> {
        // Handle single row
      }, t -> {
        // Handle failure
      }, () -> {
        // End of stream
      });
  }

  public void simpleFullFetch(CassandraClient cassandraClient) {
    cassandraClient.rxExecuteWithFullFetch("SELECT my_key FROM my_keyspace.my_table where my_key = my_value")
      .subscribe(rows -> {
        // Handle list of rows
      }, throwable -> {
        // Handle failure
      });
  }
}
