package io.vertx.lang.reactivex;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Associate an RxJava generated class with its original type, used for mapping the generated
 * classes to their original type.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RxGen {

  Class value();

}
