package io.vertx.rx.java.test;

import com.acme.rxjava.pkg.AnotherInterface;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MethodWithClassParamTest {


  /**
   * Test reproducing https://github.com/vert-x3/vertx-rx/issues/71.
   */
  @Test
  public void testAMethodTakingAClassParameter() {
    AnotherInterface another = AnotherInterface.create();
    List list = another.methodWithClassParam(ArrayList.class);
    assertNotNull(list);
  }
}
