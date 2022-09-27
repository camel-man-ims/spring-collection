package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello",String.class);
    }

    @Test
    void printMethod(){
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod = {}",helloMethod);
    }

    @Test
    @DisplayName("MemberService.Impl의 Hello() 메서드를 가져온다.")
    void exactMatch(){
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        System.out.println(pointcut.matches(helloMethod,MemberServiceImpl.class));
    }

    @Test
    @DisplayName("가장 많이 생략한 pointcut")
    void allMatch(){
        pointcut.setExpression("execution(* *(..)");
    }

    @Test
    void nameMatcher1(){
        pointcut.setExpression("execution(* hel*(..)");
    }

    @Test
    void nameMatcher2(){
        pointcut.setExpression("execution(* *el*(..)");
    }

    @Test
    void packageMatcher1(){
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod,MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatcher2(){
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod,MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchSubpackage1(){
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        assertThat(pointcut.matches(helloMethod,MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchSubpackage2(){
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod,MemberServiceImpl.class)).isTrue();
    }
    @Test
    void packageMatchSubpackage3(){
        pointcut.setExpression("execution(* hello..*.*(..))");
        assertThat(pointcut.matches(helloMethod,MemberServiceImpl.class)).isTrue();
    }
}
