package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success(){
        log.info("memberService Proxy={}",memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect{
        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember(){}

        // 잘 사용되지는 않는 방법
        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{}, arg={}",joinPoint.getSignature(),arg1);
            return joinPoint.proceed();
        }

        // 위 방법을 깔끔하게 개선
        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint,Object arg) throws Throwable {
            log.info("[logArgs2]{}, arg={}",joinPoint.getSignature(),arg);
            return joinPoint.proceed();
        }

        // @Before를 사용해서 최적화
        @Before("allMember() && args(arg,..)")
        public void logArgs3(String arg){
            log.info("[logArg3], arg={}",arg);
        }

        // this: Proxy객체
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint,MemberService obj){
            log.info("[this]{}, obj={}",joinPoint.getSignature(),obj.getClass());
        }

        // target: 실제 객체(프록시가 적용안된)
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint,MemberService obj){
            log.info("[target]{}, obj={}",joinPoint.getSignature(),obj.getClass());
        }

        // annotation 전달
        @Before("allMember() && @target(annotation)")
        public void atTargetArgs(JoinPoint joinPoint, ClassAop annotation){
            log.info("[atTarget]{}, obj={}",joinPoint.getSignature(),annotation);
        }

        // 메서드의 annotation 전달
        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation){
            log.info("[atWithin]{}, obj={}",joinPoint.getSignature(),annotation);
        }
        // annotation에 할당돼있는 값 꺼내기 가능
        // [@annotation]String hello.aop.member.MemberServiceImpl.hello(String), annotationValue=test value
        @Before("allMember() && @annotation(annotation)")
        public void atWithin(JoinPoint joinPoint, MethodAop annotation){
            log.info("[@annotation]{}, annotationValue={}",joinPoint.getSignature(),annotation.value());
        }
    }
}
