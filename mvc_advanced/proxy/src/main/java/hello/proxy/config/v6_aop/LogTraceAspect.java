package hello.proxy.config.v6_aop;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    // pointcut
    @Around("execution(* hello.proxy.app..*(..))")
    // advise 로직
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{
        String message = joinPoint.getSignature().toShortString();
        TraceStatus status = logTrace.begin(message);
        Object result = joinPoint.proceed();
        logTrace.end(status);
        return result;
    }
}
