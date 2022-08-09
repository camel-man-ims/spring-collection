# 섹션7 - Bean LifeCycle, Bean Scope

## 1. Bean Life Cycle 도입

> * [테스트 NetWorkClientV1 객체](../core/src/test/java/hello/lifecycle/NetworkClientV1.java)
> * [테스트 실행](../core/src/test/java/hello/lifecycle/BeanLifeCycleTest.java)

* 문제상황
  * 현재 NetWorkClientV1을 실행하면 내부 url은 계속 null이 뜨게 된다.
  * 당연히 생성자 때 null 이었고, 생성자 이후에 값이 바뀌었으므로 null이 뜨는게 당연하다.
  * 즉, 멤버변수인 URL에 값을 주입해주고 싶다.

* Spring Bean은 아래와 같은 life cycle을 갖는다.
  * 객체 생성
  * -> (이후)
  * 의존관계 주입

* 이 때, 초기화 작업은 의존관계 주입이 모두 완료된 후에 호출해야 한다.

* 객체 생성과 초기화를 분리
  * 생성자 안에서 무거운 초기화 작업을 함께 하는 것보다, 객체 생성과 초기화 부분을 명확히 분리하는 것이 유지보수 관점에서 좋다.

* Spring Bean Event LifeCycle
  1. Spring Container 생성
  2. Spring Bean 생성
  3. DI ( 의존관계 주입 )
  4. 초기화 callback
  5. 사용
  6. 소멸전 callback
  7. Spring 종료

## 2. 빈 생명주기 콜백 3가지

### 2-1) Spring이 제공하는 인터페이스 활용

> * [테스트 NetWorkClientV2 객체](../core/src/test/java/hello/lifecycle/NetworkClientV2.java)
> * [테스트 실행](../core/src/test/java/hello/lifecycle/BeanLifeCycleTest.java)

* InitializingBean, DisposableBean을 상속받으면 setter를 이용한 주입이 객체 생성 이후에 주입이 돼서 url이 출력되는 것을 볼 수 있다.
  * 그러나, 해당 방법은 2003년도에 쓰이던 옛날 방법이라 최근에는 사용되지 않는다.

### 2-2) @Configuration내부의 @Bean(initMethod, destroyMethod) 지정

> * [테스트 NetWorkClientV3 객체](../core/src/test/java/hello/lifecycle/NetworkClientV3.java)
> * [테스트 실행](../core/src/test/java/hello/lifecycle/BeanLifeCycleTest.java)

```java
@Configuration
static class LifeCycleConfigV3 {
    @Bean(initMethod = "init", destroyMethod = "close")
    public NetworkClientV3 networkClient(){
        NetworkClientV3 networkClient = new NetworkClientV3();
        networkClient.setUrl("http://naver.com");
        return networkClient;
    }
}
```
* 테스트 객체안에 init, destroy method를 선언해놓고 해당 메서의 이름을 init, destroy에 등록해주면 된다.
* destroy같은 경우 대부분의 메서드 이름이 close로 등록돼있기 때문에, 알아서 inferred(추론)해서 close나 shutdown등의 이름이면 알아서 등록해준다.
* 위 방법보다 아래의 annotation을 사용하는 방법이 권장된다.

### 2-3) @PostContruct, @PreDestroy 활용

> * [테스트 NetWorkClientV4 객체](../core/src/test/java/hello/lifecycle/NetworkClientV4.java)
> * [테스트 실행](../core/src/test/java/hello/lifecycle/BeanLifeCycleTest.java)

* 해당 방법을 사용하면 된다.

```java
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
```
* javax
  * java에서 공식적으로 지원하는 라이브러리
  * 그래서 Spring이 아니더라도 다른 컨테이너에서도 지원이 된다.

* 테스트 객체의 init, destroy 메서드에 annotation를 다는 방법
* 최신 스프링에서 권장하는 방법이다.
* 유일한 단점은 외부 라이브러리에 적용할 수 없다는 것이다.
  * 외부 라이브러리를 초기화, 종료해야 하면 @Bean을 사용하자.

## 3. Bean Scope

### 3-1) Bean Scope란?

* Spring Bean이 스프링 컨테이너와 함께 생성되고, 스프링 컨테이너가 종료될 때 같이 destroy되는 이유는 Spring Bean이 기본값으로 singleton scope로 생성되기 때문이다.
* Scope
  * 빈이 존재할 수 있는 범위

### 3-2) scope!

* Scope
  * Singleton
  * Prototype
  * Web
    * request
    * session
    * application

### 3-3) scope 설명

* Singleton
  * default scope
  * 스프링 컨테이너 시작과 종료될 때까지 유지되는 가장 넓은 범위의 scope
* Prototype
  * Prototype bean의 생성과 DI까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 scope
* Web
  * request
    * 웹 요청이 들어오고 나갈 때까지
  * session
    * web session 생성 ~ 종료될때까지
  * applcation
    * web servlet context와 같은 범위로 유지

### 3-4) Prototype scope

> * [싱글톤 테스트](../core/src/test/java/hello/scope/SingletonTest.java)
> * [프로토타입 테스트](../core/src/test/java/hello/scope/PrototypeTest.java)
> * 예제에서, 객체에 @Component를 달지 않아도, 직접 SpringContainer의 argument로 넘기면 알아서 등록이 된다.

#### 싱글톤 요청일 때

<img width="474" alt="2022-08-08_21 20 24" src="https://user-images.githubusercontent.com/51740388/183416617-76bd874e-9965-4de6-ad10-73629935674f.png">

#### 프로토타입 요청일 때

<img width="489" alt="2022-08-08_21 20 38" src="https://user-images.githubusercontent.com/51740388/183416556-b4da588d-cb48-47a0-8348-e0725b05a4af.png">

* 즉, prototype bean은 요청마다 객체를 생성하고 반환한다.
  * prototype bean은 빈을 생성, 의존관계 주입, 초기화까지만 처리한다.
  * 대부분 singleton bean을 사용하고, 가끔 prototype bean을 사용한다.

* 싱글톤에서는 `ac.close()`를 하게 되면 `@PreDestroy`가 달린 메서드가 호출이 돼서 destroy가 되지만, 프로토타입에서는 `ac.close()`를 하더라도 destroy가 되지 않는다.
* 즉, 객체에 대한 소멸의 책임이 사용자한테 있는 것이다.

## 4. Singleton with Prototype

> * [싱글톤 - 프로토타입 테스트](../core/src/test/java/hello/scope/SingletonWithPrototype.java)

### 4-1) 문제상황

<img width="493" alt="image" src="https://user-images.githubusercontent.com/51740388/183422449-a6994d96-d853-4a83-b85c-c8ef27c489b3.png">

* signleton 내부 prototype.addCount() -> 각각 1씩 증가 예상
  * 그러나 총 2반환
  * prototype bean을 2개 조회해서, addCount()로 1씩 증가한다면 다른 두 객체는 1씩 증가한다.
  * 그렇다면 만약, Singleton 내부에 prototype bean이 존재하는 경우라면?
    * 싱글톤의 경우 생성될 때만 객체 주입이 일어나기 때문에, 내부의 prototype bean 역시 변경되지 않는다.
    * 즉, 최초에 주입된 객체가 끝날 때까지 유지된다.
  * 그래서 Singleton 내부의 prototype bean을 2번 호출하면 각각 새로운 prototype bean을 반환 받을 것을 기대하지만, 실제로는 하나의 prototype bean만을 반환받는다.

* 해결 방법 중 하나로 내부에 applicationContext를 생성해서 호출될 때마다 주입을 해주는 방법이 있다.
  * 좋은 방법은 아니라 사용되지 않는다.

### 4-2) ObjectProvider 이용

> * [싱글톤 - 프로토타입 테스트](../core/src/test/java/hello/scope/SingletonWithPrototypeProvider.java)

```java
static class ClientBean{
    @Autowired
    private ObjectProvider<PrototypeBean> prototypeBeanProvider;

    public int logic(){
        PrototypeBean prototype = prototypeBeanProvider.getObject();
        prototype.addCount();
        return prototype.getCount();
    }
}
```

* `ObjectProvider` 의 `getObject()`는 스프링 컨테이너를 통해 해당 bean을 찾아서 반환한다(DL)
  * 즉, 해당 예제에서 원하는 딱 그 정도의 기능을 제공한다.
* 별도의 라이브러리는 필요 없으나, 스프링에 의존적이다.

### 4-3) javax.inject.Provider 이용

> * [javax provider 이용](../core/src/test/java/hello/scope/SingletonWithPrototypeJavaxProvider.java)
> gradle에 implementation 'javax.inject:javax.inject:1' 추가

```java
static class ClientBean{
    @Autowired
    // 이 때의 provider는 [javax.inject.Provider] 여야 한다.
    private Provider<PrototypeBean> prototypeBeanProvider;

    public int logic(){
        PrototypeBean prototype = prototypeBeanProvider.get();
        prototype.addCount();
        return prototype.getCount();
    }
}
```

### 4-4) 정리

* 대부분 싱글톤으로 문제를 해결할 수 있기 때문에, Prototype빈을 직접 사용할 일은 사실상 없다.
  * 그러나 Spring의 핵심적인 개념이기 때문에 알아두자.

* javax.Provider , ObjectProvider 중 무엇을 사용해야 할까?
  * 스프링은 defacto(사실상 표준)이기 때문에, 왠만하면 그냥 Spring이 제공하는 ObjectProvider를 사용하는게 좋다.
  * Spring vs Java표준이 충돌한다면, 왠만하면 Spring이 제공하는 거를 사용하자.
    * 대부분 Spring이 더 다양한 기능을 제공해준다.

# 5. Web Scope

### 5-1) web scope 란?

> implementation 'org.springframework.boot:spring-boot-starter-web' 추가

<img width="483" alt="image" src="https://user-images.githubusercontent.com/51740388/183548191-11968cdf-9f01-4296-b35a-6e89e997c6da.png">

* http request에 맞춰서 각각 할당된다.
* http의 요청이 들어오고 나갈 때까지의 life cycle 동안은 같은 bean이 관리가 된다. 

### 5-2) request scope 목표

* 지금까지는 `AnnotationConfigApplicationContext`를 기반으로 개발했다면,web 에서는 추가적 설정이 필요하기에 `AnnotationConfigServletWebServerApplicationContext` 를 기반으로 구동한다.

```
[d06b992f...] request scope bean create
[d06b992f...][http://localhost:8080/log-demo] controller test
[d06b992f...][http://localhost:8080/log-demo] service id = testId
[d06b992f...] request scope bean close
```

* 위와 같이 로그를 찍도록 만들어보자.

### 5-3) request scope

> * [myLogger](../core/src/main/java/hello/core/common/MyLogger.java)
> * [controller](../core/src/main/java/hello/core/web/LogDemoControllerProblem.java)
> * [service](../core/src/main/java/hello/core/web/LogDemoService.java)

* 위와 같이 코드를 짜고 실행하면 에러가 발생한다.
* 그 이유는 `myLogger` bean의 scope가 request인데, 스프링 컨테이너가 뜰 시점에는 request를 주입할 수 없기 때문에 발생하는 문제다.
  * request는 사용자 요청이 들어오고 나갈때까지인데, 스프링 컨테이너가 뜰 시점에는 사용자 요청이 없으므로 request bean을 생성할 수 없기 때문이다.

```java
@Scope(value = "request")
public class MyLogger {
  ...
}
```

* 웹과 관련된 부분은 controller까지만 사용해야 한다.
  * service계층은 웹 기술에 종속적이지 않게 유지해야 유지보수에 좋다.

### 5-4) Provider를 사용해서 해결

> * [controller](../core/src/main/java/hello/core/web/LogDemoController.java)
> * [service](../core/src/main/java/hello/core/web/LogDemoService.java)

```java
private final MyLogger myLogger;
// -->
private final ObjectProvider<MyLogger> myLoggerProvider;
```

* `ObjectProvider<MyLogger>`를 사용하면 MyLogger를 주입받는 게 아니라, MyLogger를 찾을 수 있는(DL) Provider가 주입된다.
* Service역시 `MyLogger` -> `ObjectProvider<MyLogger>`로 변경해주었다.
* **ObjectProvider를 사용하면 Spring Container에게 bean을 달라는 요청을 지연할 수 있다.**
  * 그러면 SpringContainer는 요청 시점에 bean을 생성하게 된다.
* HTTP 같은 요청이면 같은 SpringBean이 반환된다.

### 5-5) Proxy 이용

> * [myLoggerProxy](../core/src/main/java/hello/core/common/MyLoggerProxy.java)

```java
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLoggerProxy {
  ...
}
```

* proxyMode추가
  * 클래스면 TARGET_CLASS
  * 인터페이스면 INTERFACES선택

* 그리고 해당 MyLogger를 찍어보면 CGLIB의 바이트 조작을 통한 가짜 객체가 주입된 것을 확인할 수 있다.
  * `myLogger = class hello.core.common.MyLogger$$EnhancerBySpringCGLIB$$b68b726d`
* 가짜 프록시 객체는 요청이 들어오면 그 때 내부에서 진짜 빈을 찾는 **위임**로직이 들어가있다.

<img width="466" alt="image" src="https://user-images.githubusercontent.com/51740388/183552591-6665c349-0193-41d3-aa22-468cd620f6a4.png">

* 다형성
  * 가짜 프록시 객체는 원본 클래스를 **상속**받아서 만들어졌기 때문에, client 입장에서는 원본인지 아닌지 상관없이 사용할 수 있다.

### 5-6) 정리

* 핵심은 Provider를 사용하든, Proxy를 사용하든 진짜 객체 조회가 필요한 시점까지 지연처리로 조회를 미룬다는 점이다.
* 해당 annotation 설정 변경만으로 원본 객체를 프록시 객체로 교체할 수 있다.
  * 다형성, DI container의 위력이다.
* 웹 scope가 아니더라도 proxy는 사용할 수 있다.
* aop역시 해당 원리로 돌아간다.
