package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
// RUNTIME으로 해야 동적으로 소스를 읽을 수 있다.
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAop {
    String value();
}
