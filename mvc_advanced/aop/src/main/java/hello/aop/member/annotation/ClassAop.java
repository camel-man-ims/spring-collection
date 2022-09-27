package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
// runtime = runtime일 때까지 해당 annotation이 살아있는다.
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassAop {
}
