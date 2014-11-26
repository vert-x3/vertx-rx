package io.vertx.rx.js.test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface VertxAssert {

  void testComplete();
  void assertEquals(Object expected, Object actual);
  void assertEquals(long expected, long actual);
  void assertTrue(boolean b);
  void assertFalse(boolean b);
  void fail(String msg);

}
