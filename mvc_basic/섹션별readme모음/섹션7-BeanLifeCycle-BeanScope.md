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