package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {
    @Around("hello.aop.order.aop.Pointcuts.allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("[log] {}",joinPoint.getSignature());
        return joinPoint.proceed();
    }
    /*
    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable{

        try{
            // @Before
            log.info("[트랜잭션 시작] {}",joinPoint.getSignature());
            // 주요 로직
            Object result = joinPoint.proceed();
            // @AfterReturning
            log.info("[트랜잭션 커밋] {}",joinPoint.getSignature());
            return result;
        }catch(Exception e){
            // @AfterThrowing
            log.info("[트랜잭션 롤백] {}",joinPoint.getSignature());
            throw e;
        }finally {
            // @After
            log.info("[리소스 릴리즈] {}",joinPoint.getSignature());
        }
    }
     */

    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint){
        log.info("[before] {}",joinPoint.getSignature());
    }

    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(){
        log.info("[before] parameter가 없어도 된다.");
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint,Object result){
        log.info("[return] {} return={}",joinPoint.getSignature(),result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doReturn(JoinPoint joinPoint,Exception ex){
        log.info("[ex]{}, message = {}",joinPoint.getSignature(),ex);
    }

    @After("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint){
        log.info("[after] {}",joinPoint.getSignature());
    }
}
