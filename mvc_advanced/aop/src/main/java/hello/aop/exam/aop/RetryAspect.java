package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {

//    @Around("@annotation(hello.aop.exam.annotation.Retry)")
//    public Object doRetryV1(ProceedingJoinPoint joinPoint){
//        return null;
//    }

    @Around("@annotation(retry)")
    public Object doRetryV2(ProceedingJoinPoint joinPoint, Retry retry) throws Exception {
        log.info("[retry] {}, retry={}",joinPoint.getSignature(),retry);

        int value = retry.value();
        Exception exceptionHolder = null;

        for(int rc = 1; rc<=value;rc++){
            try{
                log.info("[retry] {}, retryCount={}",joinPoint.getSignature(),rc);
                return joinPoint.proceed();
            } catch (Exception e){
                exceptionHolder = e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        throw exceptionHolder;
    }
}
