# 섹션4 - 싱글톤 컨테이너

## 1. Singleton의 필요 이유

<img width="468" alt="image" src="https://user-images.githubusercontent.com/51740388/182768255-89c1eb14-4d5c-40d8-92ed-a943c5fc9ac3.png">

* 현재 AppConfig.java의 경우 호출이 될 때마다 객체가 새로 생성된다.
  * 생성하는 비용이 1000이라고 하면, 참조하는 비용은 1정도다.
    * 요청이 올 때마다 객체를 생성하는 방법은 비효율적일 수 밖에 없다.
  * 해결 방법은 싱글톤으로 하나의 객체만 공유하도록 코드를 짜서, 해당 객체를 공유하도록 하는 방법이다.

## 2. 싱글톤 패턴 생성

### 2-1) 싱글톤 패턴 코드 설명

> * [싱글톤 패턴 생성](../core/src/test/java/hello/singleton/SingletonService.java)
> * [싱글톤 패턴 테스트](../core/src/test/java/hello/singleton/SingletonTest.java)

* 생성자를 private으로 막아놓아서 new 연산자를 통해 새로운 객체가 생성되는 것을 막는다.
* assertj를 사용한 경우
  * `Assertions.assertThat(객체).isSameAs(비교객체)`
    * isSameAs
      * == 비교
    * isEqualTo
      * equals() 비교

### 2-2) 싱글톤 패턴 단점

* 싱글톤 패턴을 생성하는 코드가 추가로 들어간다.
* DIP를 위반한다.
  * 만약 `AppConfig.java`에서 MemberServiceImpl을 가져온다고 하면 MemberService.getInstance() 식으로 가져와야 한다. 
  * 즉, client가 getInstance()와 같은 식으로 호출해야 되기 때문에 DIP를 위반한다.
* OCP 위반 가능성
  * 구체 클래스에 의존하기 때문에 OCP 위반가능성이 높다.
* 테스트하기 어렵다.
* private 생성자를 갖기 때문에 자식 클래스를 만들기 어렵다.
* 결론적으로 유연성이 떨어진다.
* 위 같은 단점들 때문에 안티패턴으로 불리기도 한다.

### 2-3) SpringContainer에서 제공하는 싱글톤

<img width="470" alt="image" src="https://user-images.githubusercontent.com/51740388/182779612-7b52d604-09bf-4a5a-9fe2-88827808d621.png">

* 객체의 생성과 주입을 SpringContainer가 싱글톤으로 알아서 관리해주기 때문에 OCP,DIP 위반같은 여러 문제들이 해결됐다.

### 2-4) Singleton 설계시 주의점

> * [Singleton 설계시 주의점](../core/src/test/java/hello/singleton/SingletonTest.java)

```java
public class StatefulService {
    private int price;

    public void order(String name, int price){
        System.out.println("name = " + name + " price " + price);
        // 문제 발생
        this.price=price;
    }

    public int getPrice(){
        return price;
    }
}
```

* 위 코드에서는 공유변수에 값을 할당하기 때문에 문제가 발생한다.

```java
public class StatefulService {
    public int order(String name, int price){
        System.out.println("name = " + name + " price " + price);
        return price;
    }
}
```

* 위 같이 공유변수를 사용하지 않도록 변경함으로써 문제를 해결할 수 있다.
  * 그냥 지역변수를 return하게 함으로써 해결한다.

## 3. @Configuration

### 3-1) 싱글톤 보장?

* 현재 `AppConfig.java`의 코드를 살펴보자.

```java
@Configuration
public class AppConfig {
    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService(){
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();
    }
}
```

* memberService에서 `memberRepository()` 메서드를 호출하고 있다.
  * `memberRepository()` 메서드는 new 연산자를 통해 새로운 객체를 반환한다.
* orderService에서도 `memberRepository()` 메서드를 호출한다.
  * 그러면 다시 **새로운 객체를 생성해서 반환받는다**
    * 그렇다면, 싱글톤이 깨지는 것이 아닌가?

* 테스트를 해보자.

### 3-2) 싱글톤은 보장된다!

> * [Singleton 보장 테스트](../core/src/test/java/hello/singleton/SingletonConfigurationTest.java)
>   * `@DisplayName("싱글톤 보장 테스트")` 참조

* 놀랍게도 memberServiceImpl에서 반환한 memberRepository, orderServiceImpl에서 반환한 memberRepository, 그리고 그냥 memberRepository가 모두 같은 객체임을 볼 수 있다.

```java
@Configuration
public class AppConfig {
    @Bean
    public MemberService memberService(){
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService(){
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();
    }
}
```
* 그렇다면 위와 같이 메서드가 몇 번 호출되는 지 알기 위해 logging을 해보자.
* 예상값으로는
  * memberSerivceImpl -> memberRepository -> orderSerivceImpl -> memberRepository -> memberRepository이다.
  * 그렇기 때문에 각각의 호출마다 log가 찍혀야 될 것 같지만, `memberRepository` call은 놀랍게도 딱 한 번밖에 찍히지 않는다.

## 4. @Configuration과 바이트코드 조작의 마법 & CGLIB

### 4-1) CGLIB

* 위와 같은 일이 가능한 이유는 스프링 컨테이너가 AppConfig를 등록해놓는게 아니라, AppConfig를 상속받아서 조작한 AppConfig@CGLIB 객체를 주입해놓았기 때문이다.

<img width="455" alt="image" src="https://user-images.githubusercontent.com/51740388/182788602-e3eceb79-2f30-4842-ad5a-ba2c3d41ba43.png">

```java
ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
AppConfig bean = ac.getBean(AppConfig.class);
System.out.println("bean.getClass() = " + bean.getClass());
```

* 결과

```
bean.getClass() = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$2cd7a939
```

* hello.core.AppConfig 까지만 찍혀야 일반 클래스인데, 뒤에 CGLIB관련해서 추가적으로 classType이 붙는 것을 확인할 수 있다.

### 4-2) CGLIB 예상코드

* CGLIB의 코드는 매우 복잡하게 짜여있지만, 대충 예상해보면 아래와 같이 짜여있을 것이다.

```java
@Bean
public MemberRepository memberRepository() {
    if (memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면?) {
        return 스프링 컨테이너에서 찾아서 반환;
    } else { //스프링 컨테이너에 없으면
        기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록
        return 반환
    }
}
```

* 즉 있으면 있는거 반환하고, 없으면 생성 및 등록 후 반환해준다.
* **즉, @Configuration을 붙이면 바이트 코드를 조작하는 CGLIB 기술을 사용해서 싱글톤을 보장해준다.**

### 4-3) 만약 @Configuration -> @Bean으로 바꾸면?

```
bean.getClass() = class hello.core.AppConfig
```

* 이렇게 바꾸면 CGLIB기술 없이 그냥 순수한 자바 객체로 스프링 컨테이너에 등록된다.
* 그리고 애초에 예상했던 결과처럼 memberRepository가 3번 호출되고(총5번 호출), 싱글톤은 보장되지 않는다.
  * 즉, orderSerivceImpl, memberServiceImpl의 memberRepository는 서로 다른 객체이다.
* 덧붙여서, 객체의 주입이 스프링 컨테이너에 의해 되는 것이 아니라, 그냥 개발자가 new 연산자를 통해 주입해준 것이다.
* 즉, DI가 스프링 컨테이너에 의해 일어나지 않는다.  

