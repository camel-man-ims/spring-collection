# Spring 역사

## 1. EJB의 등장배경

* EJB 설명
  * EJB는자바당 정파 기술이었다.
  * 오픈소스는 사파취급 받았다.
  * EJB는 java 진영에서 표준으로 인정한 기술이었다.
  * 고급기술들이 지원이 잘 됐다.
    * 트랜잭션
    * 분산 등

* EJB의 단점
  * 사용하려면 비용이 비쌌다.
  * 그리고 진짜 복잡하고 느렸다.
  * EJB에 의존적으로 개발을 해야 했다.
  * 컨테이너 하나 띄우는 데 오래 걸린다.

* Spring, Hibernate(JPA) 등장 배경
  * EJB를 쓸 바에 POJO로 돌아가자는 말이 나올 정도였다.
  * 그러다 Hibernate를 만든 Gavin king, Spring을 만든 Rod Johnson이 등장한다.
    * 둘다 오픈소스로 만든다.
  * EJB로 프로젝트를 진행했던 Rod Johnson이 EJB를 비판하면서 책을 쓴다.
    * 더 단순하면서 좋은 방법으로 개발한 방법을 제시했다.
    * 이게 후에 Spring이 된다.
  * Gavin King이 만든 Hibernate는 EJB 엔티티빈 기술을 대체한다.

* 별첨
  * 2002년에 Rod Johnson이 30000줄 이상의 코드를 책에 제시했다.
    * 그리고 Juergen Hoeller(유겐 휠러)가 로드 존슨에게 오픈소스 프로젝트를 제안했다.
    * Spring의 대다수 코드는 유겐 휠러가 개발했다.

## 2. Spring이란

* Spring 생태계
  * Spring은 하나가 아니라, 여러 기술들의 모음이라고 볼 수 있다.

* Spring Boot
  * Tomcat같은 웹 서버를 내장해서 별도의 웹 서버를 설치하지 않아도 된다.
  * 손 쉬운 build 구성 starter 종속성 제공
    * ex) spring boot aop starter
  * 3rd parth(외부) 라이브러리 자동 구성
    * 메이저 라이브러리 버전에 대해서 크게 고민하지 않아도 된다.
    * 알아서 잘 맞는 버전을 맞춰준다.

* Spring을 왜 만들었을까?
  * 핵심 컨셉?
    * 핵심 컨셉은 항상 단순하게 시작한다.
    * Java 언어 기반의 Framework이다.
      * 객체 지향 언어가 가진 강력한 특징을 살려내는 framework이다.

## 3. 좋은 객체지향의 특징

* 다형성(Polymorphism)
  * 좋은 객체지향 프로그래밍은 유연하고 변경이 용이하다.
  * 세상을 `역할`과 `구현`으로 구분해보자.
  * 운전자는 자동차 역할에만 의존하고 있다.
    * 이렇게 한 이유는 운전자를 위한 것이다.
  * 이렇게 하면 자동차의 세상을 무한히 확장 가능하다.
    * Client에 영향을 주지 않고 새로운 기능을 제공할 수 있다.
  * 역할이 중요하지, 배우가 중요하지 않다.

<img width="450" alt="image" src="https://user-images.githubusercontent.com/51740388/181572009-649648a2-5095-4872-9d4d-01f022503133.png">

<img width="450" alt="image" src="https://user-images.githubusercontent.com/51740388/181572420-469269eb-93bf-4b59-918e-5cf94f415e26.png">

* 핵심은 `Client`다.
  * Client는 내부 구조를 몰라도 된다.
  * 내부 구조가 변경되도 영향을 받지 않는다.
  * 이 때 Client는 다시 Server가 될 수 있다.

* 역할
  * interface
* 구현
  * interface를 구현한 구현 객체

* 협력
  * 혼자있는 객체는 존재하지 않는다.
    * 객체는 기본적으로 협력이다.

* 다형성의 본질
  * ex) Overriding
  * 다형성으로 인터페이스를 구현한 객체를 **실행시점에** 유연하게 변경할 수 있다.
  * client를 변경하지 않고, 서버의 구현 기능을 유연하게 변경할 수 있다.

<img width="650" alt="image" src="https://user-images.githubusercontent.com/51740388/181573566-3be03f93-8a71-4fb0-93ad-71ebc4f00501.png">

* 그런데 역할(interface)자체가 변하면 client, server에 모두 큰 변경이 일어난다.
  * 그렇기 때문에 interface를 안정적으로 설계하는 것이 중요하다.

* 다형성이 가장 중요하다.
  * IoC, DI는 다형성을 활용해서 역할과 구현을 편리하게 다룰 수 있도록 지원하는 것이다.

## 4. SOLID

* CleanCode로 유명한 로버트 마틴이 좋은 객체 지향 설계의 5가지 원칙을 정리했다.

* SRP (Single Responsibility Principle)
  * 단일 책임의 원칙
  * 하나의 클래스는 하나의 책임만 갖는다.
  * 실무에서 하나의 책임이라는 것은 모호하다.
    * 중요한 것은 `변경`이다.
    * 변경을 했을 떄 파급효과가 적으면 SRP를 잘 따른것이다.

* OCP (Open Closed Principle)
  * 개방 폐쇄의 원칙
  * 확장에는 열려있고, 변경에는 닫혀있어야 한다.
    * `객체의 입장에서` 확장에는 열려있고, `Client 입장에서` 변경에는 닫혀있어야 한다.

* OCP 문제 상황

```java
MemberRepository m = new MemoryRepository();
MemberRepository m = new JdbcRepository();
```

* 위에서 아래와 같이 변경하고자 할 때, 개발자가 직접 위에서 아래 코드를 작성하면 OCP 위반이다.
  * Client코드를 고쳐야하기 때문이다.
  * 해결하려면 별도의 조립자가 필요하다.
    * 해당 별도의 조립자가 Spring이다.

* LSP ( Liskov Subsititution Principle)
  * 리스코브 치환의 원칙
  * 하위 타입의 인스턴스를 상위 타입에 대입했을 때에도 동일한 동작을 보장해야 한다.
    * 서브타입은 기반타입으로 교체될 수 있어야 한다.
  * 즉, 인터페이스 규약을 다 지키면서 개발해야 한다는 말이다.
    * 예를 들어, 패달을 밟을 때 앞으로 나가게 설계를 했는데, 하위 타입에서 -10이 되게 동작을 하게 한다면 이는 LSP 위반이다.
    * 느리더라도 앞으로 나가면 LSP를 지킨것이다.
  * [직사각형<- 정사각형 ===> 사각형 <- 정사각형, 직사각형](https://blog.itcode.dev/posts/2021/08/15/liskov-subsitution-principle)
    * 직사각형을 정사각형이 상속받는다.
      * 정사각형에서는 `height*height`으로 넓이를 구한다.
      * 직사각형을 정사각형으로 대치하면 다른 값이 나온다.
    * 위 링크처럼 직사각형에 의존하던 정사각형에서 발생하던 문제가 사각형이라는 인터페이스를 만들어줌으로써 해결된다.

* ISP ( Interface Segregation Principle)
  * 인터페이스 여러개가 범용 인터페이스 하나보다 낫다.
  * ex) 자동차 인터페이스
    * 운전 인터페이스, 정비 인터페이스로 분리

* DIP ( Dependency Inversion Principle)
  * 추상화에 의존해야지, 구체화에 의존하면 안된다.
  * 인터페이스에 의존하라는 뜻이다.
  * 즉, 역할에 의존해야 한다는 말이다.
  * 즉 시스템을 언제나 갈아낄 수 있게 설계해야 한다.

* DIP 위반

```java
MemberRepository m = new MemoryRepository();
MemberRepository m = new JdbcRepository();
```

* 위 코드를 다시 보자.
  * m은 MemberRepository뿐만 아니다, new로 생성되는 구현체들에 대해서도 `의존`하고 있다.
  * 의존이라는 말은 `알고있다`는 뜻이다.
  * 즉, MemberService client가 구현 클래스를 직접 선택하는 것에서 DIP를 위반하는 것이다.

## 5. 정리

* DI, DI Container
  * Spring은 DI, DI Container 제공으로 다형성,OCP,DIP를 가능하게 지원한다.

* 모든 설계에 역할과 구현을 분리해야 한다.
  * 이상적으로는 모든 설계에 인터페이스를 부여하는 게 좋다.
  * 그러나 인터페이스를 도입하면 `추상화`라는 비용이 발생한다.
    * 즉, 코드를 한 번더 열어봐야 한다.
  * 추천
    * 기능을 확장할 가능성이 없다면, 구체 클래스를 직접 사용한다.
    * 향후 기능 확장이 있다면 refactoring을 통해 interface를 도입한다.

