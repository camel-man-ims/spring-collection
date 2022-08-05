# @ComponentScan

## 1. ComponentScan 설명

* @Bean 등록을 일일히 다 해주는 것은 반복이 생겨서 비효율적이고, 누락이 생길 위험이 있다.
* Spring은 설정 정보가 없어도 자동으로 Spring Bean을 등록하는 ComponentScan기술을 제공한다.
  * + 의존관계를 자동으로 주입해주는 @Autowired기능도 제공한다.

* 설정 정보에 @ComponentScan을 붙여주면 현재의 package와 모든 하위 package의 @Component가 붙은 모든 클래스들을 scan해서 Spring bean으로 등록한다.
  * @Configuration 이 붙은 class 역시 Componentscan의 대상이 되는데, 그 이유는 @Configuration 내부에 @Component annotation이 붙어있기 때문이다.

* cf)
  * [AutoAppConfig](../core/src/main/java/hello/core/AutoAppConfig.java)
    * excludeFilter ~
      * 위 처럼 필터를 붙인 이유는 테스트 했을 때의 코드들과 기존의 AppConfig 코드들도 Component의 대상이 되는 것을 방지하기 위함이다.
      * 즉, @Configuration이 붙은 코드들을 제외했다.

## 2. ComponentScan의 동작원리

* @ComponentScan은 @Component가 붙은 모든 클래스를 Spring Bean으로 등록한다.
  * 이 때, 이름은 클래스명을 사용하되, 앞글자만 소문자를 사용한다.
    * MemberServiceImpl -> memberServiceImpl
  * 이름을 따로 부여하고 싶다면, `@Component("다른이름ㅋ")` 과 같이 부여하면 된다.

* @Autowired
  * 스프링 컨테이너가 자동으로 해당 Spring Bean을 탐색해서 주입한다.
  * 이 때, 기본 조회 전략은 type이 같은 bean이다.
  * `ac.getBean(MemberService.class)` 와 같은 동작원리라고 이해하면 된다.

## 3. ComponentScan 탐색범위

```java
@ComponentScan(
    basePackages = "hello.core, hello.service, ..."
)
```

* basePackages
  * 탐색할 패키지 시작 위치를 지정한다.
  * 만약 지정하지 않는다면, default 값으로 @ComponentScan이 붙은 설정 정보의 클래스 패키지가 시작 위치가 된다.
* @SpringBootApplication
  * @SpringBootApplication annotation 안에 @ComponentScan이 붙어있다.

* Annotation들
  * @Component
  * @Controller
    * Spring MVC controller로 인식
  * @Service
    * 별 역할x
    * 그냥 개발자가 핵심 비지니스 로직이 있는 위치로 인식가능
  * @Configuration
    * 스프링 정보로 인식, Spring Bean이 싱글톤을 유지하도록 처리
  * @Repository
    * DAO로 인식, 데이터 계층의 예외를 Spring 예외로 변환

## 4. includeFilter && excludeFilter

> * [incldueFilter](../core/src/test/java/hello/scan/filter/MyIncludeComponent.java)
> * [excldueFilter](../core/src/test/java/hello/scan/filter/MyExcludeComponent.java)
> * [BeanA](../core/src/test/java/hello/scan/filter/BeanA.java)
> * [BeanB](../core/src/test/java/hello/scan/filter/BeanB.java)
> * [실행 테스트](../core/src/test/java/hello/scan/filter/ComponentFilterAppConfigTest.java)

* includeFilter
  * componentScan에서 스캔할 대상을 지정
* excludeFilter
  * componentScan에서 제외할 대상을 지정

* Filter option
  * annotation (default)
    * ex) org.example.SomeAnnotation
  * ... 외 등등

* @Component면 충분하기 때문에 include, exclude filter를 사용할 일은 거의 없다.

## 5. @Component 중복 등록과 충돌

* 자동 빈 등록 vs 자동 빈 등록
  * `@Component("service")` 와 같이 여러 개 class에 이름을 중복으로 등록해놓으면 자동 vs 자동상황이다.
  * 이 때는 에러를 발생시킨다.
* 수동 빈 등록 vs 자동 빈 등록
  * 원래는 수동 빈 등록이 우선권을 갖고 overriding하게 설정이 돼 있었으나, 최근의 SpringBoot는 error를 발생시키게 변화했다.
  * `spring.main.allow-bean-definition-overriding=true`
    * 해당 옵션을 주면 수동 빈 등록이 우선권을 갖고 overriding하게 설정할 수 있다.
    * 기본값은 false다.