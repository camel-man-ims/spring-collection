# 섹션7 - Bean 생명주기, Bean Scope

## 1. Bean Life Cycle 도입

> * [테스트 NetWorkClientV1 객체](../core/src/test/java/hello/lifecycle/NetworkClientV1.java)
> * [테스트 실행](../core/src/test/java/hello/lifecycle/BeanLifeCycleTest.java)

* 현재 NetWorkClientV1을 실행하면 내부 url은 계속 null이 뜨게 된다.
* 당연히 생성자 때 null 이었고, 생성자 이후에 값이 바뀌었으므로 null이 뜨는게 당연하다.

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

* Scope
  * Singleton
  * Prototype
  * Web
    * request
    * session
    * application

* Scope 설명
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

### 3-2) Prototype scope

> * [싱글톤 테스트](../core/src/test/java/hello/scope/SingletonTest.java)
> * [프로토타입 테스트](../core/src/test/java/hello/scope/PrototypeTest.java)
> * 예제에서, 객체에 @Component를 달지 않아도, 직접 SpringContainer의 argument로 넘기면 알아서 등록이 된다.

* 싱글톤 요청일 때

<img width="474" alt="2022-08-08_21 20 24" src="https://user-images.githubusercontent.com/51740388/183416617-76bd874e-9965-4de6-ad10-73629935674f.png">

* 프로토타입 요청일 때

<img width="489" alt="2022-08-08_21 20 38" src="https://user-images.githubusercontent.com/51740388/183416556-b4da588d-cb48-47a0-8348-e0725b05a4af.png">

* 즉, prototype bean은 요청마다 객체를 생성하고 반환한다.
  * prototype bean은 빈을 생성, 의존관계 주입, 초기화까지만 처리한다.
  * 대부분 singleton bean을 사용하고, 가끔 prototype bean을 사용한다.

* 싱글톤에서는 `ac.close()`를 하게 되면 `@PreDestroy`가 달린 메서드가 호출이 돼서 destroy가 되지만, 프로토타입에서는 `ac.close()`를 하더라도 destroy가 되지 않는다.
* 즉, 객체에 대한 소멸의 책임이 사용자한테 있는 것이다.

## 4. Singleton with Prototype

> * [싱글톤 - 프로토타입 테스트](../core/src/test/java/hello/scope/SingletonWithPrototype.java)

<img width="493" alt="image" src="https://user-images.githubusercontent.com/51740388/183422449-a6994d96-d853-4a83-b85c-c8ef27c489b3.png">

* prototype bean을 2개 조회해서, addCount()로 1씩 증가한다면 다른 두 객체는 1씩 증가한다.
* 그렇다면 만약, Singleton 내부에 prototype bean이 존재하는 경우라면?
  * 싱글톤의 경우 생성될 때만 객체 주입이 일어나기 때문에, 내부의 prototype bean 역시 변경되지 않는다.
  * 즉, 최초에 주입된 객체가 끝날 때까지 유지된다.
* 그래서 Singleton 내부의 prototype bean을 2번 호출하면 각각 새로운 prototype bean을 반환 받을 것을 기대하지만, 실제로는 하나의 prototype bean만을 반환받는다.
  * 해결 방법 중 하나로 내부에 applicationContext를 생성해서 호출될 때마다 주입을 해주는 방법이 있다.
    * 좋은 방법은 아니라 사용되지 않는다.

