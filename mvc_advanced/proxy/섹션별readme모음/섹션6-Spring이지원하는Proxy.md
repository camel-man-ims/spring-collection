# 섹션 6 - Spring이 지원하는 Proxy

## 1. Proxy Factory

### 1-1) Proxy Factory 도입

* 인터페이스가 있는 경우 JDK Dynamic Proxy를 적용하고, 인터페이스가 없는 경우에는 CGLIB을 적용하려고 한다.
* 그렇다면 JDK Dynamic Prxoy가 제공하는 `InvocationHandler`와 , CGLIB이 제공하는 `MethodInterceptor`를 중복으로 사용해서 관리해야 할까?
* Spring이 제공하는 ProxyFactory를 사용하면 위의 문제를 해결할 수 있다.

<img width="573" alt="image" src="https://user-images.githubusercontent.com/51740388/190549757-767d3e45-f8d9-4fca-9202-5312fbe92f52.png">

* 위 그림을 보면 client는 ProxyFactory에게 Proxy객체를 요구하고, 내부적으로 ProxyFactory가 알아서 JDK Dynamic Proxy, CGLIB중에서 구현을 해서 반환해주는 것을 볼 수 있다.

### 1-2) Advice

* 개발자는 `InvocationHandler` 나, `MethodInterceptor` 등을 신경쓰지 않고, `Advice`만 호출하면 된다.
    * `ProxyFactory` 의 `Advice`는 내부적으로 `InvocationHandler` ,`MethodInterceptor`를 호출한다.

<img width="570" alt="image" src="https://user-images.githubusercontent.com/51740388/190550060-e89bdd21-e677-4d51-9278-ea030cef44e2.png">

* Spring Factory에서 Proxy를 만들면, 해당 Proxy는 내부적으로 `adviceInvocationHandler`, `adviceMethodInterceptor` 둘다 모두 `Advice`를 호출한다.
* 개발자는 `Advice`내부에 부가 로직을 만들면 된다.

<img width="565" alt="image" src="https://user-images.githubusercontent.com/51740388/190550121-0691df19-505c-4f37-af5d-f51ea5c3ed47.png">

* `Jdk Proxy`나 `Cglib Proxy`가 만들어진다.
* 이후, 각각에 해당하는 핸들러와 인터셉터는 `Advice`를 호출한다.
* 이 때, 만약 특정 메서드 이름의 조건에 맞을 때만 프록시 부가 기능이 적용되는 코드를 만들고 싶다면?
    * `PointCut`이라는 Spring이 제공하는 개념을 활용하면 된다.

### 1-3) ProxyFactory 구현

* [TimeAdvice](../src/test/java/hello/proxyfactory/advice/TimeAdvice.java)
    * `aopalliance` 에서 제공하는 (cglib library 가 아니다. 조심.) `MethodInterceptor`를 상속받는다.
    * `invocation.proceed()` 를 하면, 해당 target을 실행시켜준다.
* [ProxyFactory](../src/test/java/hello/proxyfactory/advice/ProxyFactoryTest.java)
    * 인터페이스가 있을시, JDK Dynamic Proxy를, 없을 시 CGLIB Proxy를 사용한다.
    * 인터페이스가 있어도 `proxyFactory.setProxyTargetClass(true)` 를 주면 해당 구체 클래스를 상속받아서 CGLIB Proxy를 사용한다.
    * 흐름도
        * `ProxyFactory`를 생성하고, 해당 `ProxyFactory`에 실제 구체인 `target`을 넘겨준다.
        * `proxyFactory.addAdvice(new TimeAdvice())` 를 통해 `Advice`를 넘겨준다.
            * <u>즉, target과 advice를 ProxyFactory에 넘겨준다.</u>
        * 그런다음에 `(타입캐스팅) proxyFactory.getProxy()` 으로 해당 객체를 가져와서 실행시켜보면, 해당 객체의 메서드에 부가기능이 적용된 것을 확인할 수 있다.
* `AopUtils` 에서 제공하는 메서드로 해당 Proxy가 AopProxy인지(Spring 에서 제공하는 Proxy) 확인할 수 있다.
    * JdkProxy인지, CglibProxy인지도 확인할 수 있다.
    * 물론 `proxy.getClass()`를 통해 해당 객체가 갖고 있는 클래스를 직접 찍어볼 수도 있다.

## 2. Pointcut, Advice, Advisor

* Pointcut
    * 필터링 로직
    * 어디에 부가기능을 적용할 지, 어디에 적용하지 않을 지에 대한 기준이다.
* Advice
    * 프록시가 호출하는 부가기능
    * 다른 말로는 프록시 로직이다.
* Advisor
    * 하나의 포인트 컷과, 하나의 어드바이스를 갖고 있는 객체
    * 조언자는 어디에 조언할 지를 알고 있어야 한다.

<img width="473" alt="image" src="https://user-images.githubusercontent.com/51740388/190563206-4368f0ad-0f18-4387-8d93-002dccd85604.png">

* Advisor를 통해 Pointcut을 통해 어디에 적용할 지 정하고, Advice로 부가기능을 적용한다.

* [DefaultPointcutAdvisor](../src/test/java/hello/advisor/AdvisorTest.java)
* <u>ProxyFactory를 사용할 때, Advisor는 필수!</u>
    * 그런데, 전의 예제에서는 `proxyFactory.addAdvice()`를 통해 advice만 추가하지 않았나?
    * 해당 메서드를 타고 들어가보면 Advisor를 추가해주는 로직이 있다. 즉, `addAdvice()`는 편의 메서드인 것이다.

<img width="599" alt="image" src="https://user-images.githubusercontent.com/51740388/190563669-17e7293e-e1b1-4d26-9168-ad9e24969224.png">

## 3. Pointcut 구현

### 3-1) 직접 만든 Pointcut

* [직접 만든 Pointcut](../src/test/java/hello/advisor/CustomPointcutTest.java)
* Pointcut은 크게 `ClassFilter`와 `MethodMatcher`로 이루어진다.
    * 클래스와 메서드 둘다 맞아야(true를 반환해야) Pointcut을 통과한다.
* `MyMethodMatcher`
    * 해당 `matches()` 메서드 안의 조건이 method를 거르는 조건이다.
    * 참조
        * `isRuntime()`
            * true면 `Object... args`를 parameter로 갖고 있는 `matches()` 메서드가 호출된다.
        * 캐싱 관련해서, 인수가 동적이면 캐시가 불가능하므로, 정적일 때는 위의 코드, 동적일 때는 아래 코드를 사용한다.
            * 크게 중요한 부분은 아니다. 그냥 알고만 있자.
  
<img width="497" alt="image" src="https://user-images.githubusercontent.com/51740388/190565688-9747c50c-36c3-4b35-882f-588403e1d787.png">

### 3-2) Spring이 제공하는 Pointcut

* [Spring이 제공하는 Pointcut](../src/test/java/hello/advisor/SpringPointcutTest.java)
* `NameMatchMethodPointcut` 객체의 `setMappedNames()`를 이용하면 손쉽게 Pointcut을 구현해낼 수 있다.
* 스프링이 제공하는 Pointcut 종류들
    * TruePointcut
    * AnnotationMatchingPointcut
    * AspectJExpressionPointcut
        * aspjectJ 표현식으로 매칭한다.
    * `AspectJExpressionPointcut` 를 실무때 사용한다. 뒤에서 자세히 다룬다.

### 3-3) Advisor 여러개 적용하기

* [MultiAdvisor 적용](../src/test/java/hello/advisor/MultiAdvisorTest.java)
* 첫번째 방법: N개의 ProxyFactory생성하기
    * ProxyFactory생성해서 Advisor적용하고, proxy객체 꺼내와서 다시 ProxyFactory에 넘겨준다.
    * 그리고 다시 Advisor를 적용해준다.
    * 이렇게 하는 방법은 N개를 생성해야 되기 때문에 비효율적이다.
* 두번째 방법: 하나의 Proxy에 여러개의 Advisor 등록하기
    * 그냥 `proxyFactory.addAdvisor()` 를 통해 등록해주면 된다.
        * 이 때, 가장 최신에 등록된 advisor가 먼저 적용된다.
* 두번째 방법을 그냥 처음부터 보여주면 되는데, 굳이 처음 방법을 알려준 이유는 AOP 적용 수만큼 프록시가 생성된다고 착각할 수 있기 때문이다. Spring은 AOP를 적용할 때 최적화를 진행해서 지금처럼 Proxy는 하나만 만들고, 하나의 프록시에 여러개의 Advisor를 적용한다.
    * 정리하면 하나의 target에 여러 AOP가 동시에 적용되도, Spring AOP는 target마다 하나의 Proxy만 생성한다.

## 4. v1,v2에 적용하기

* [LogTraceAdvice](../src/main/java/hello/proxy/config/v3_proxyfactory/advice/LogTraceAdvice.java)
* [ProxyFactoryV1](../src/main/java/hello/proxy/config/v3_proxyfactory/advice/../ProxyFactoryConfigV1.java)
* [ProxyFactoryV2](../src/main/java/hello/proxy/config/v3_proxyfactory/advice/../ProxyFactoryConfigV2.java)
* 위의 3까지의 예제코드에 대한 응용이다.

## 5. 마무리

* ProxyFactory를 활용해서 매우 편리하게 Proxy를 생성할 수 있었다.
    * 추가로 Pointcut, Advisor, Advice 등을 활용해서 역할과 책임도 나눌 수 있었다.
* 문제들
    * 1. 너무 많은 설정
        * 그러나 위의 코드들 역시 부가 기능을 추가하기 위해 너무나 많은 설정을 해주어야 한다.
        * Spring Bean이 100개라면, 100개에 대해서 모두 동적 프록시 코드를 생성해주어야 한다.
    * 2. 컴포넌트 스캔
        * 이미 Bean으로 등록된 객체들에 대해서는 위의 코드로는 Proxy를 등록할 수 없다.
* 위의 문제들을 해결할 수 있는 해결책이 빈 후처리기이다.