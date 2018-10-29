package examples;

import io.vertx.rxjava.cassandra.CassandraClient;
import io.vertx.rxjava.cassandra.ResultSet;

public class CassandraClientExamples {

  public void connectPrepareExecuteAndDisconnect(CassandraClient cassandraClient) {
    cassandraClient.rxConnect()
      .flatMap(v -> cassandraClient.rxPrepare("SELECT * FROM my_keyspace.my_table where my_key = ?"))
      .flatMap(preparedStatement -> cassandraClient.rxExecute(preparedStatement.bind("my_value")))
      .flatMap(ResultSet::rxOne)
      .flatMap(row -> {
        // process the row here
        return cassandraClient.rxDisconnect();
      })
      .subscribe(success -> {
        System.out.println("We are done!");
      }, error -> {
        System.err.println("Whoops, something went wrong...");
        error.printStackTrace();
      });
  }
}
