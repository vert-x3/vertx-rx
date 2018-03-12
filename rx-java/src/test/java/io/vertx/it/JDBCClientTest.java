package io.vertx.it;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import rx.Observable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JDBCClientTest extends VertxTestBase {

  private static final JsonObject config = new JsonObject()
    .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
    .put("driver_class", "org.hsqldb.jdbcDriver");

  @Test
  public void testStreamRX() {
    JDBCClient client = new JDBCClient(io.vertx.ext.jdbc.JDBCClient.createNonShared(vertx, config));
    try {
      client.getConnection(onSuccess(conn -> {

        List<String> SQL = new ArrayList<>();
        SQL.add("drop table if exists select_table;");
        SQL.add("create table select_table (id int, lname varchar(255), fname varchar(255) );");
        SQL.add("insert into select_table values (1, 'doe', 'john');");
        SQL.add("insert into select_table values (2, 'doe', 'jane');");
        try {
          for (String sql : SQL) {
            conn.getDelegate().<Connection>unwrap().createStatement().execute(sql);
          }
        } catch (SQLException e) {
          fail(e);
          return;
        }

        String sql = "SELECT ID, FNAME, LNAME FROM select_table ORDER BY ID";
        conn.queryStream(sql, onSuccess(res -> {
          final AtomicInteger cnt = new AtomicInteger(0);
          Observable<JsonArray> observable = res.toObservable();
          observable.subscribe(
            // handle one row
            row -> {
              assertEquals("doe", row.getString(res.column("lname")));
              cnt.incrementAndGet();
            },
            // it should not fail
            this::fail,
            () -> {
              assertEquals(2, cnt.get());
              testComplete();
            });
        }));
      }));
      await();
    } finally {
      client.close();
    }
  }

}
