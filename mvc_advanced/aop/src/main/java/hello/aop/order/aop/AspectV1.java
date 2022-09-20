package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {

    @Around("execution(* hello.aop.order..*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
        // join point signature
        // 메서드 관련 정보
        // ex) void hello.aop.order.OrderService.orderItem(String)
        log.info("[log] {}",joinPoint.getSignature());
        return joinPoint.proceed();
    }
}
