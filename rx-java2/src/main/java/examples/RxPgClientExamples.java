package examples;

import io.vertx.reactivex.pgclient.*;
import io.vertx.reactivex.sqlclient.*;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.docgen.Source;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Source
public class RxPgClientExamples {

  public void simpleQuery01Example(PgPool pool) {

    // A simple query
    Single<RowSet<Row>> single = pool.query("SELECT * FROM users WHERE id='julien'").rxExecute();

    // Execute the query
    single.subscribe(result -> {
      System.out.println("Got " + result.size() + " rows ");
    }, err -> {
      System.out.println("Failure: " + err.getMessage());
    });
  }

  public void streamingQuery01Example(PgPool pool) {

    // Create an Observable
    Observable<Row> observable = pool.rxGetConnection().flatMapObservable(conn -> conn
      .rxBegin()
      .flatMapObservable(tx ->
        conn
          .rxPrepare("SELECT * FROM users WHERE first_name LIKE $1")
          .flatMapObservable(preparedQuery -> {
            // Fetch 50 rows at a time
            RowStream<Row> stream = preparedQuery.createStream(50, Tuple.of("julien"));
            return stream.toObservable();
          })
          .doAfterTerminate(tx::commit)));

    // Then subscribe
    observable.subscribe(row -> {
      System.out.println("User: " + row.getString("last_name"));
    }, err -> {
      System.out.println("Error: " + err.getMessage());
    }, () -> {
      System.out.println("End of stream");
    });
  }

  public void streamingQuery02Example(PgPool pool) {

    // Create an Observable
    Flowable<Row> flowable = pool.rxGetConnection().flatMapPublisher(conn -> conn
      .rxBegin()
      .flatMapPublisher(tx ->
        conn
          .rxPrepare("SELECT * FROM users WHERE first_name LIKE $1")
          .flatMapPublisher(preparedQuery -> {
            // Fetch 50 rows at a time
            RowStream<Row> stream = preparedQuery.createStream(50, Tuple.of("julien"));
            return stream.toFlowable();
          })
          .doAfterTerminate(tx::commit)));

    // Then subscribe
    flowable.subscribe(new Subscriber<Row>() {

      private Subscription sub;

      @Override
      public void onSubscribe(Subscription subscription) {
        sub = subscription;
        subscription.request(1);
      }

      @Override
      public void onNext(Row row) {
        sub.request(1);
        System.out.println("User: " + row.getString("last_name"));
      }

      @Override
      public void onError(Throwable err) {
        System.out.println("Error: " + err.getMessage());
      }

      @Override
      public void onComplete() {
        System.out.println("End of stream");
      }
    });
  }

  public void transaction01Example(PgPool pool) {

    Completable completable = pool.rxGetConnection()
      .flatMapCompletable(conn -> conn
        .rxBegin()
        .flatMapCompletable(tx ->
        conn
          .query("INSERT INTO Users (first_name,last_name) VALUES ('Julien','Viet')")
          .rxExecute()
          .flatMap(result -> conn
            .query("INSERT INTO Users (first_name,last_name) VALUES ('Emad','Alblueshi')")
            .rxExecute())
          .flatMapCompletable(res -> tx.rxCommit())
      ));

    completable.subscribe(() -> {
      // Transaction succeeded
    }, err -> {
      // Transaction failed
    });
  }
}
