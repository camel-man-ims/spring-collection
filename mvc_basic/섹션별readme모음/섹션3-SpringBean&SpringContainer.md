# 섹션3 - SpringBean & SpringContainer

## 1. ApplicationContext

### 1-1) Application Context란?

```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
```
* Spring Container
  * ApplicationContext를 Spring Container라고 한다.
    * ApplicationContext는 interface이다.
    * AnnotationConfigApplicationContext는 구현체다.
  * XML기반 ApplicationContext도 존재하지만 잘 사용되지 않는다.
    * Annotation기반이 훨씬 편리하고 Spring Boot에서는 Annotation기반을 추천한다.

### 1-2) 생성과정

1. `AppConfig.class`를 구성정보로 스프링 컨테이너를 생성한다.
   * `new AnnotationConfigApplicationContext(AppConfig.class);`

2. Spring Bean 등록

<img width="600" alt="2022-08-03_10 41 37" src="https://user-images.githubusercontent.com/51740388/182505864-ea3e4c37-46aa-44fd-8ecf-c22f3cb927a4.png">

* 빈 이름은 default값으로 메서드 이름으로 설정된다.
  * 빈 이름을 직접 지정할 수도 있다.
    * `@Bean(name="얼쑤")`
  * 이 때, 빈 이름은 항상 다른 이름을 부여해야 한다. 안 그러면 충돌이 생긴다.

3. 설정 정보를 참고해서 의존관계를 설정

<img width="600" alt="image" src="https://user-images.githubusercontent.com/51740388/182506082-595e06aa-06b8-49a4-ba8c-79e5766a45a1.png">

* 설정 정보를 **참고** 해서 의존관계를 주입한다는 것이 포인트다.
  * 단순히 자바 코드를 호출하는 것 같지만, 차이가 존재한다.

## 2. Spring Bean 테스트

### 2-1) 모든 Spring Bean 출력하기

> * [모든 Spring Bean 출력하기](../core/src/test/java/hello/beanfind/ApplicationContextInfoTest.java)

```java
@Test
@DisplayName("Application bean 출력")
void findApplicationBean(){
    String[] beanDefinitionNames = ac.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
        BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

        if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION){
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("bean = " + bean);
        }
    }
}
```
* `getBeanDefinitionNames()`
  * 모든 빈을 출력한다.
* `getRole()`
  * `ROLE_APPLICATION`
    * 사용자가 정의한 bean
  * `ROLE_INFRASTRUCTURE`
    * 스프링 내부에서 사용하는 빈

### 2-2) Spring ac.getBean() 테스트

> * [Spring ac.getBean() 테스트](../core/src/test/java/hello/beanfind/ApplicationContextBasicFindTest.java)

* ac.getBean()을 할 때, 기본적인 방법은 parameter에 bean이름, 클래스 타입을 넘겨주는 방식이다.
  * ex) `ac.getBean("memberService", MemberService.class)`
* 이 때, bean이름은 생략이 가능하다.
  * 객체 타입은 추상체가 아니라 구현체 클래스(`MemberServiceImpl`)이 넘어가도 되는데, 객체지향의 입장에서 권장되지 않는다.

### 2-3) 같은 Bean type을 두개 이상 조회시 

> * [같은 Bean type을 두개 이상 조회시](../core/src/test/java/hello/beanfind/ApplicationContextSameBeanFindTest.java)

* 같은 Bean Type이 2개 이상인데, `ac.getBean(MemberRepository.class)`와 같이 타입만 넘겨서 찾으려고 한다면 Spring에서는 n개 중 무엇을 넘길 지 모르므로 `NoUniqueBeanDefinitionException` 에러가 터진다.
* Bean 이름을 지정해서 해결할 수 있다.
* `ac.getBeansOfType(MemberRepository.class)` 로 같은 타입을 갖는 모든 bean을 조회할 수 있다.

### 2-4) 상속관계에서 Bean 조회시

> * [상속관계에서 Bean 조회시](../core/src/test/java/hello/beanfind/ApplicationContextExtendFindTest.java)

<img width="497" alt="image" src="https://user-images.githubusercontent.com/51740388/182509212-74356cdf-89c7-45e0-a46a-b9a2a2ab2e20.png">

* 부모를 조회하면 자식들도 다같이 딸려나온다.
* 부모 타입으로 조회를 할 때 부모 타입을 return 하는 bean이 2개 이상이라면 에러가 생긴다.
  * bean을 찾을 때 bean이름을 지정해서 찾거나, 하위 타입(상속)으로 지정해서 찾는다.
    * 하위 타입으로 지정해서 찾는 방법은 권장되지 않는다.
* Object를 찾으면 하위 bean들이 모두 딸려나온다.

## 3. BeanFactory, ApplicationContext

### 3-1) BeanFactory, ApplicationContext 설명

<img width="474" alt="image" src="https://user-images.githubusercontent.com/51740388/182510730-9479b105-58a6-4091-a90d-e27064398e42.png">

* BeanFactory가 bean factory의 대부분의 기능을 제공한다.
  * ApplicationContext는 BeanFactory의 하위타입으로, 부가기능을 제공한다.
  * ex) 운영환경, 국제화
    * 개발 환경, 테스트 환경, 운영 환경등을 분리해서 제공할 수 있게 해준다.
    * 한국에서 들어오면 한국어, 영어에서 들어오면 영어출력

* BeanFactory를 사용할 일은 거의 없고, 사실 ApplicationContext만 사용한다.

### 3-2) XML 설정

<img width="474" alt="image" src="https://user-images.githubusercontent.com/51740388/182511495-c9533d21-fe32-4b51-b0a3-ba6a625b522e.png">

> * [xml](../core/src/test/java/hello/xml/XmlAppContext.java)

* 잘 사용되지는 않으나, 많은 레거시들이 아직 XML로 돼기 때문에 알아두면 좋다.
* XML을 사용했을 때 이점은 컴파일 과정 없이 Bean 설정정보를 교체할 수 있다는 것이다.
* main > resources > appConfig.xml 설정하면 된다.
  * test코드 실행하면 에러가 뜨는데, 중요한 것은 아니므로 넘어간다.

## 4. BeanDefinition

### 4-1) BeanDefinition이란

<img width="479" alt="image" src="https://user-images.githubusercontent.com/51740388/182512955-3afa9cc5-3cec-469d-a627-ae5b20ac24a8.png">

* 어떻게 xml로 등록하든, java로 등록하든 다 설정정보를 잘 등록해줄까?
  * Spring Container는 `BeanDefinition`만 반환받으면 된다.
  * 하위 구현체들은 내부적 로직을 통해 코드를 읽고, `BeanDefinition`을 반환한다.

### 4-2) Spring에 bean을 등록하는 방법 & FactoryMethod

* 크게 2가지 방법이 존재한다.
  1. xml과 같이 통해 직접 bean등록을 하는 방식
  2. factory method를 통해 등록하는 방법
     * 우리가 지금까지 작성했던 `AppConfig.java` 의 메서드를 통해 우회적으로 bean을 등록하는 방법을 팩토리 메서드를 통해 bean을 제공한다고 지칭한다.
     * factory method방식은 외부에서 메서드를 호출해서 객체를 생성 및 반환받는 방식이다.
