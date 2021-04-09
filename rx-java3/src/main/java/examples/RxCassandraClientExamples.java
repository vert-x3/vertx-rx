package examples;

import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.rxjava3.cassandra.CassandraClient;
import io.vertx.rxjava3.cassandra.CassandraRowStream;
import io.vertx.rxjava3.core.Vertx;

public class RxCassandraClientExamples {

  public void createClient(Vertx vertx) {
    CassandraClientOptions options = new CassandraClientOptions()
      .addContactPoint("node1.corp.int", 7000)
      .addContactPoint("node2.corp.int", 7000)
      .addContactPoint("node3.corp.int", 7000);
    CassandraClient cassandraClient = CassandraClient.createShared(vertx, options);
  }

  public void simpleQueryStream(CassandraClient cassandraClient) {
    cassandraClient.queryStream("SELECT my_key FROM my_keyspace.my_table where my_key = my_value")
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
    cassandraClient.executeWithFullFetch("SELECT my_key FROM my_keyspace.my_table where my_key = my_value")
      .subscribe(rows -> {
        // Handle list of rows
      }, throwable -> {
        // Handle failure
      });
  }
}
