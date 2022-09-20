# 섹션 9 - Spring AOP 구현

## 1. 환경설정

* start.spring.io에서 프로젝트를 만들고, 해당 import는 lombok만 한다.

```
implementation 'org.springframework.boot:spring-boot-starter-aop'
testCompileOnly 'org.projectlombok:lombok'
testAnnotationProcessor 'org.projectlombok:lombok'
```

* 추가로 위의 import도 해준다.
* `@Aspect`를 포함한 `org.aspectj` 패키지 관련 기능은 `aspectjweaver.jar` 라이브러리가 제공하는 기능이다.
* `spring-boot-start-aop` 라이브러리에는 Spring AOP 관련 기능과 함께, `aspectjweaver.jar`도 함께 사용할 수 있게 의존 관계에 포함된다.
    * 이 때, Spring에서는 AspectJ가 제공하는 Annotation이나 인터페이스만 사용하는 것이고, 실제 AspectJ가 제공하는 컴파일이나 로드타임 위버 등을 사용하는 것은 아니다.

## 2. Aspect 버전별 변화

### 2-1) AspectV1: `@Around()`와 `@Aspect()`를 이용해서 AOP를 구현

* [AspectV1](../src/main/java/hello/aop/order/aop/AspectV1.java)
* [실행](../src/test/java/hello/aop/AopTest.java)
* Pointcut Signature
    * `joinPoint.getSignature()`
    * 메서드 이름과 파라미터를 합쳐서 포인트컷 시그니쳐라고 한다.
    * 메서드 관련 정보를 추출한다.
        * ex) `void hello.aop.order.OrderService.orderItem(String)`
* Spring Bean으로 등록하는 방법 3가지
    * `@Bean`을 사용해서 수동등록
    * `@Component`를 이용해서 컴포넌트 스캔을 사용해서 자동등록
    * `@Import`를 사용해서 수동등록
        * 주로 설정 파일을 추가할 때 사용(`@Configuration`)
        * @Configuration이 돼있지 않더라도, 직접 등록이 된다고 생각하면 될 것 같다.

### 2-2) AspectV2: `@Pointcut`을 따로 분리

* [AspectV2](../src/main/java/hello/aop/order/aop/AspectV2.java)
* [실행](../src/test/java/hello/aop/AopTest.java)
* 위 코드에서 포인트컷 시그니처는 `allOrder()`이다.
* `@Pointcut`규칙
    * 메서드 반환 타입은 void여야 한다.
    * 코드 내용은 비워둔다.
* private을 사용해도 되지만, 다른 Aspect를 참조하려면 public을 사용해야 한다.

### 2-3) AspectV3: 특정 객체에만 Advice 적용

* [AspectV3](../src/main/java/hello/aop/order/aop/AspectV3.java)
    * 만약에 Service계층에만 Trasaction을 넣고 싶다면?
    * 실제 Service계층에 비지니스 로직을 몰아넣기 때문에, Service로직이 끝날 때 Commit 이나 Rollback을 수행한다.
    * `@Around("allOrder() && allService()")`
        * `allOrder()` 와 `allService()` Pointcut이 적용될 때 Advice가 적용된다.

<img width="508" alt="image" src="https://user-images.githubusercontent.com/51740388/191176165-83f3cf5b-0883-48df-b6bc-aa3683d1df51.png">

### 2-4) AspectV4: Pointcut 따로 분리

* [AspectV4Pointcut](../src/main/java/hello/aop/order/aop/AspectV4Pointcut.java)
    * `hello.aop.order.aop.Pointcuts.allOrder()`
    * 패키지와 클래스, 그리고 객체의 메서드를 명시해주어야 한다.
* [AspectV4Pointcut](../src/main/java/hello/aop/order/aop/Pointcuts.java)
    * 분리된 Pointcut

### 2-5) AspectV5: Advice 실행순서 정하기

* [AspectV5Order](../src/main/java/hello/aop/order/aop/AspectV5Order.java)
    * static inner class를 생성해서 해당 클래스마다 `@Aspect`를 붙여준다.
    * `@Order()`로 실행순서를 명시해준다.
* `@Aspect`의 경우 클래스 단위로 적용이 되기 때문에, 메서드마다 실행순서를 정해줄 수 없다.
* 그래서 Advice의 실행순서를 정해야 할 경우에는 내부에 static inner class를 생성해서 적용해주도록 하자.
    * `@Order()`의 숫자가 작을수록, 먼저 실행된다.

<img width="559" alt="image" src="https://user-images.githubusercontent.com/51740388/191240555-ad3f0d66-7511-412c-9984-54904c0f4992.png">

## 3. Advice의 종류

### 3-1) AspectV6 코드

* [AspectV6Advice](../src/main/java/hello/aop/order/aop/AspectV6Advice.java)
    * `@Before` , `@AfterReturning`, `@AfterThrowing`, `@After`, 코드

<img width="569" alt="image" src="https://user-images.githubusercontent.com/51740388/191237709-dd836c4f-33bb-4e66-ac57-41e0c9d14873.png">

### 3-2) 종류

* `@Around`
    * 메서드 호출 전과 후, 예외처리 등 모든 상황에 부가기능을 붙여줄 수 있는 가장 강력한 Advice
* `@Before`
    * 조인 포인트 실행 이전에 실행
* `@AfterReturning`
    * 조인포인트가 정상 완료 후 실행
* `@After`
    * 조인 포인트가 정상 또는 예외에 관계없이 실행(finally)
* `@AfterThrowing`
    * 예외를 던지는 경우 실행

### 3-3) Advice @annotation들 설명

#### @Around

* `@Around`를 제외한 Advice들은 사실 `@Around`가 할 수 있는 기능의 일부분만 제공할 뿐이다.
* `@Around`를 제외한 모든 Advice는 `org.aspectj.lang.JoinPoint`를 첫번째 parameter로 사용할 수 있다.
    * 생략 가능하다.
* 그러나 `@Around`는 `ProceedingJoinPoint`를 사용해야 한다.
    * `ProceedingJoinPoint` 는 `org.aspectj.lang.JoinPoint`의 하위타입이다.
    * `ProceedingJoinPoint`인터페이스의 주요 기능은 `proceed()` 메서드로써, 다음 어드바이스나 타겟을 호출한다.
* `joinPoint.proceed()`를 통해 호출 여부를 선택할 수 있다.
* `proceed()`의 경우 여러 번 실행할 수도 있다.

#### @Before

* 메서드 종료시 자동으로 다음 target이 호출된다.
    * 물론 예외가 발생하면 다음 코드가 호출되지 않는다.

#### @AfterReturning

* returning 속성에 사용된 이름은 Advice method의 parameter 변수 이름과 일치해야 한다.
    * type이 일치하지 않는다면 실행이 되지 않는다.
* `@Around`와 다르게 반환되는 객체를 변경할 수는 없다.

### 3-4) @Around 외 다른 Advice들이 존재하는 이유?

* cf) 실행흐름

<img width="556" alt="image" src="https://user-images.githubusercontent.com/51740388/191241939-4ab6335f-bb1c-4ce3-8b2e-3bb7d99ddc4a.png">

1. `@Around`를 사용하면 실수로 `joinPoint.proceed()`를 호출하지 않을 수도 있다.
2. `@Before`, `@AfterReturning` 등 그 외 Advice들을 사용하면 의도가 명확히 드러난다.
    * **좋은 설계는 제약이 있는 것이다.**
    * 제약 덕분에 역할이 명확해진다.
    * 코드의 의도를 파악하기가 쉽다. 