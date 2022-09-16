package hello.proxyfactory.advice;

import hello.common.service.ConcreteService;
import hello.common.service.ServiceImpl;
import hello.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있을 시 JDK Dynamic Proxy 사용한다.")
    void interfaceProxy(){
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass ={}",target.getClass());
        log.info("proxyClass ={}",proxy.getClass());

        proxy.save();

        boolean aopProxy = AopUtils.isAopProxy(proxy);
        System.out.println("aopProxy = " + aopProxy);
        boolean jdkDynamicProxy = AopUtils.isJdkDynamicProxy(proxy);
        System.out.println("jdkDynamicProxy = " + jdkDynamicProxy);
        boolean cglibProxy = AopUtils.isCglibProxy(proxy);
        System.out.println("cglibProxy = " + cglibProxy);
    }

    @Test
    @DisplayName("구체 클래스만 있을 시 CGLIB을 사용한다.")
    void concreteProxy(){
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass ={}",target.getClass());
        log.info("proxyClass ={}",proxy.getClass());

        proxy.call();

        boolean aopProxy = AopUtils.isAopProxy(proxy);
        System.out.println("aopProxy = " + aopProxy);
        boolean jdkDynamicProxy = AopUtils.isJdkDynamicProxy(proxy);
        System.out.println("jdkDynamicProxy = " + jdkDynamicProxy);
        boolean cglibProxy = AopUtils.isCglibProxy(proxy);
        System.out.println("cglibProxy = " + cglibProxy);
    }

    @Test
    @DisplayName("proxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB을 사용하고, 클래스 기반 프록시를 사용한다.")
    void proxyTargetClass(){
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // 이 부분 추가
        // ServiceImpl(구체클래스)을 상속받아서 CGLIB Proxy를 만든다.
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass ={}",target.getClass());
        log.info("proxyClass ={}",proxy.getClass());

        proxy.save();

        boolean aopProxy = AopUtils.isAopProxy(proxy);
        System.out.println("aopProxy = " + aopProxy);
        boolean jdkDynamicProxy = AopUtils.isJdkDynamicProxy(proxy);
        System.out.println("jdkDynamicProxy = " + jdkDynamicProxy);
        boolean cglibProxy = AopUtils.isCglibProxy(proxy);
        System.out.println("cglibProxy = " + cglibProxy);
    }
}
