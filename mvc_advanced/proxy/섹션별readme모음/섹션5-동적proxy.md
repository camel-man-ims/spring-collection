# 섹션 5 - 동적 proxy

## 1. reflection

### 1-1) reflection 설명

* java에서 기본적으로 제공하는 JDK 동적 프록시 기술이나, CGLIB같은 프록시 생성 오픈소스 기술을 활용하면 프록시 객체를 동적으로 만들어 낼 수 있다.
* reflection 기술을 사용하면 클래스나 메서드의 메타정보를 동적으로 획득하고, 코드도 동적으로 호출할 수 있다.
    * reflection은 컴파일 되고 나서, 실행될 때 코드를 바꾼다.
* [reflection test](../src/test/java/hello/jdkdynamic/ReflectionTest.java)

```java
Class classHello = Class.forName("hello.jdkdynamic.ReflectionTest$Hello");
Hello target = new Hello();
Method methodCallA = classHello.getMethod("callA");
Object result1 = methodCallA.invoke(target);
```

* `Class.forName()`
    * hello.jdkdynamic 폴더에 있는 ReflectionTest class에 Hello class를 가져온다.
* `Method.invoke()`
    * 이 부분이 reflection 부분인 것 같다.
    * Method 정보를 받아서 `invoke`를 시킨다.
* [reflection test](../src/test/java/hello/jdkdynamic/ReflectionTest.java)
    * 위 부분의 `reflection2()`의 `dynamicCall()` 을 보면, `method.invoke()` 부분이 추상화 된 것을 볼 수 있다. `Method` 객체를 parameter로 받기 때문에, 동적으로 호출이 되는 것이다.


### 1-2) reflection 주의점

* reflection은 런타임에 동작하기 때문에, 컴파일 타임에 에러체킹이 안된다.
* 즉, reflection은 일반적으로 사용되면 안된다.
* framework 개발이나, 공통부분 처리가 필요할 때만 사용돼야 한다.

## 2. JDK Dynamic Proxy

<img width="477" alt="image" src="https://user-images.githubusercontent.com/51740388/190305708-9111f3c3-2a9a-423f-998f-a1f75781ee11.png">

<img width="583" alt="image" src="https://user-images.githubusercontent.com/51740388/190305782-8dd3a0c6-1358-48fb-8d5d-27d2a08802c5.png">

<img width="476" alt="image" src="https://user-images.githubusercontent.com/51740388/190306623-8e35c183-b930-496f-b9d1-f3c3cc299fc5.png">

* 코드
    * [JDKDynamicProxyTest](../src/test/java/hello/jdkdynamic/JdkDynamicProxyTest.java)
    * [TimeInvocationHandler](../src/test/java/hello/jdkdynamic/code/TimeInvocationHandler.java)
* Proxy객체에 `AInterface`정보와 `TimeInvocationHandler`를 넘겨준다.
    * parameter로 클래스 로더 정보, 인터페이스, 핸들러 로직을 넣어준다.
    * `TimeInvocationHandler`는 `InvocationHandler` 를 implements한 클래스이며, 해당 `invoke()` 메서드 안에 구현하고자 하는 로직이 있다.
* 기존에는 각 클래스마다 프록시 객체를 생성해서 사용했지만, 현재는 `TimeInvocationHandler` 와 `Proxy` 객체를 사용하면 더 이상 프록시 객체를 생성하지 않고도 부가기능을 추가할 수 있다.

## 3. 원래 프로젝트(v1,v2,v3) 에 적용

* 코드
    * [InvocationHandler를 상속받은 LogTraceBasicHandler](../src/main/java/hello/proxy/config/v2_dynamicproxy/handler/LogTraceBasicHandler.java)
    * [DynamicProxyBasicConfig](../src/main/java/hello/proxy/config/v2_dynamicproxy/DynamicProxyBasicConfig.java)

* JDK Dynamic Proxy기술은 인터페이스가 필요하기 때문에 v1에 밖에 적용이 안된다.

<img width="647" alt="image" src="https://user-images.githubusercontent.com/51740388/190309164-3ae83dee-aa44-4cee-8496-b0a53428f750.png">

* 기존에는 매 클래스마다 Proxy객체를 생성하였고, 해당 Proxy객체를 주입하였다.

<img width="645" alt="image" src="https://user-images.githubusercontent.com/51740388/190309170-ff1b77f3-1ba7-4bd0-8ba5-b8a07df3112e.png">

* Proxy객체를 주입하는 것은 똑같지만, Proxy 객체를 매번 만드는 것이 아니라 InvocationHandler에 의존한다는 것이 다르다.

<img width="658" alt="image" src="https://user-images.githubusercontent.com/51740388/190309769-fdb74568-ba50-4b15-a650-70d3fb8a950b.png">

* 기존 런타임에서는 proxy 객체가 proxy 객체에 해당하는 타입의 Implmentation을 호출하였다.

<img width="648" alt="image" src="https://user-images.githubusercontent.com/51740388/190309773-0067ad1c-aa14-4bf7-9381-4d7dd055074a.png">

* JDK 동적 프록시를 쓴 런타임에서는 **Proxy객체가 Spring Bean에 등록이 되기 때문에**, handler.invoke()가 자동으로 호출되고, `logTraceBasicHandler` 는 다시 `orderControllerV1Impl` 을 호출한다.

#### 한가지 문제

* /no-log 에 들어가도 log가 찍힌다.
* 그 이유는 모든 메서드에 대해서 proxy기능이 들어가기 때문이다.

## 4. filter config : no-log 문제 해결

* 코드
    * [InvocationHandler를 상속받은 LogFilterHandler](../src/main/java/hello/proxy/config/v2_dynamicproxy/handler/LogTraceFilterHandler.java)
    * [DynamicProxyFilterConfig](../src/main/java/hello/proxy/config/v2_dynamicproxy/DynamicProxyFilterConfig.java)
* `DynamicProxyFilterConfig` 에 있는 `PATTERNS` 에 해당하는 메서드만 실행하게 한다.
    * `LogFilterHandler` 안에 `invoke()` 메서드에서 `PatternMatchUtils.simpleMatch()` 메서드를 통해 필터링을 진행한다.

## 5. CGLIB

### 5-1) CGLIB이란?

* CGLIB은 바이트코드를 조작해서 동적으로 클래스를 생성하는 기술을 제공하는 라이브러리이다.
* CGLIB을 사용하면 **인터페이스가 없어도** 구체 클래스만 갖고 동적 프록시를 만들어낼 수 있다.
* CGLIB은 원래 외부 라이브러리인데, 스프링이 스프링 내부 소스 코드에 포함하게 됐다.
    * 따라서 스프링을 사용한다면 외부 라이브러리 추가없이 사용할 수 있다.

### 5-2) CGLIB 적용 코드

* 코드
    * [CglibTest](../src/test/java/hello/cglib/CglibTest.java)
    * [TimeMethodInterceptor](../src/test/java/hello/cglib/code/TimeMethodInterceptor.java)

<img width="557" alt="image" src="https://user-images.githubusercontent.com/51740388/190316225-c7f2ba46-d196-4846-b9c1-804048195813.png">

* 동작과정은 잘 이해가 안된다.
* 그냥 JDP Dynamic Prxoy랑 비슷하게 동작한다라고만 이해하고 넘어가면 될 것 같다.
    * CGLIB을 직접 사용할 일은 (거의) 없다.