# 섹션 8 - Spring AOP 개념

## 1. 핵심 기능과 부가기능

<img width="558" alt="image" src="https://user-images.githubusercontent.com/51740388/191018637-f1b60d08-76c7-43c8-9d9d-45f1280258b2.png">

* 보통 부가기능은 여러 클래스에 걸쳐서 함께 사용된다.
* 이런 Application 전반에 걸쳐서 적용되는 문제는 일반적인 OOP 방식으로는 해결이 힘들다.

## 2. Aspect

* Aspect란?
    * 부가기능과 부가기능을 어디에 적용할 지 선택하는 기능을 합해서 하나의 모듈로 만든 것
    * 우리말로 해석하면 관점이라는 뜻이다.
    * 말 그대로, App을 바라보는 관점을 하나하나의 기능에서, 횡단 관심사의 관점으로 달리 보는 것이다.
* `@Aspect`가 바로 해당 Aspect의 일종이다.
* Aspect를 사용한 프로그래밍 방식을 관점지향 프로그래밍(AOP, Aspect-Oriented Programming) 이라고 한다.
* AOP는 OOP를 대체하기 위한 것이 아니라, 횡단 관심사를 깔끔하게 처리하기 어려운 OOP를 보조하기 위한 목적으로 개발됐다.
* AspectJ
    * AspectJ의 기능은 매우 방대하다. Spring은 그 중 실무적인 부분만 가져와서 제공한다.
    * AspectJ는 횡단 관심사의 깔끔한 모듈화를 지원한다.
        * 오류 검사 및 처리
        * 동기화
        * 성능 최적화(캐싱)
        * 모니터링 및 로깅

## 3. AOP 적용 방식

### 3-1) 3가지 방법

1. 컴파일 시점
2. 클래스 로딩 시점
3. 런타임 시점(Proxy)

#### 1) 컴파일 시점

<img width="568" alt="image" src="https://user-images.githubusercontent.com/51740388/191020108-8a73d1c3-c13d-4b34-a65c-7c773ea3f999.png">

* `.java` 소스 코드를 compile할 때, `.class`를 만드는 시점에 부가 기능 로직을 추가할 수 있다.
    * 이 때, AspectJ가 제공하는 특별한 컴파일러를 사용해야 한다.
* 컴파일된 `.class`를 디-컴파일 해보면 Aspect 관련 호출 코드가 들어간다.
    * 이 때, AspectJ 컴파일러는 Aspect를 확인해서 해당 클래스가 적용 대상인지 먼저 확인하고, 적용 대상이면 부가 기능 로직을 적용한다.
* 즉, 그냥 직접적으로 부가기능 코드를 주요 코드 위 아래로 코드로 집어넣어버린다라고 생각해버리면 된다.
* Weaving
    * 이런 식으로 원본 로직에 부가 기능 로직이 추가되는 것을 Weaving이라고 한다.
    * Weaving
        * 옷감을 짜다, 직조하다.
        * 옷을 입는다라고 생각하면 될 것 같다.

<br/>

* 컴파일 시점의 단점
    * 부가기능을 적용하려면 특별한 컴파일러(AspectJ 컴파일러)도 필요하고, 설정도 복잡하다.

#### 2) 클래스 로딩 시점

<img width="554" alt="image" src="https://user-images.githubusercontent.com/51740388/191022225-d10ed499-830a-411d-9b18-31fdf030cb73.png">

* 자바 실행 시, 자바 언어는 `.class`파일을 JVM 내부 클래스 로더에 등록한다.
* 이 때, `.class` 파일을 등록하는 시점에 `.class`파일을 조작해서 JVM에 올릴 수 있다.
    * Java는 JVM에 저장하기 이전에 조작할 수 있는 기능을 제공해준다.(java instrumentation을 찾아보라.)
        * 수 많은 모니터링 툴들이 해당 방식을 사용한다.
* 해당 시점에 Apsect를 적용하는 것을 Load Time Weaving(로드 타임 위빙)이라고 한다.

<br/>

* 클래스 로딩 시점의 단점
    * Java를 실행할 때 특별한 옵션(java -javaagent)를 통해 클래스 로더 조작기를 지정해야 하는데, 해당 부분이 번거롭고 운영하기도 힘들다.

#### 3) 런타임 시점

<img width="572" alt="image" src="https://user-images.githubusercontent.com/51740388/191024812-1129b910-0ee1-493d-ac0c-3fbd6664071d.png">

* 런타임 시점은 컴파일도 다 끝나고, 클래스 로더에 다 올라가서 이미 자바의 main 메서드가 실행된 다음에 조작하는 것이다.
    * 따라서 Java가 제공하는 범위 안에서 부가 기능을 적용해야 한다.
    * Spring Container, Proxy, DI, Bean Processor 등의 개념을 총 동원해야 한다.
    * 위의 방식이 Proxy 방식의 AOP인 것이다.
* 일부제약이 존재한다.
    * overriding 형식으로 AOP를 제공하기 때문에, 상속이 불가능한 것에는 적용이 안된다.
        * final 생성자에는 적용이 안된다.
    * 즉, 다형성이 적용되는 메서드에만 적용이 될 수 있다.

#### 4) Spring AOP

* Join Point(조인 포인트)
    * AOP를 적용할 수 있는 지점을 Join Point라고 한다.
* Proxy를 사용하는 Spring AOP는 메서드 실행 시점에만 AOP를 적용할 수 있다.
    * Spring AOP의 조인 포인트는 메서드 실행으로 제한된다.
* **Proxy방식을 사용하는 Spring AOP는 Spring Container가 관리할 수 있는 Spring Bean에만 AOP를 적용할 수 있다.**

<br/>

* Spring은 AspectJ의 문법을 차용하고, Proxy 방식의 AOP를 적용한다.
    * 즉, AspectJ를 직접 사용하는 것이 아니다.
* 그렇다면 AspectJ를 직접 사용하는 것이 좋지 않을까?
    * AspectJ를 직접 사용하려면 많이 복잡하고, 공부할 내용도 너무 많다.
    * 현업에서 사용하는 99.9%의 문제는 Spring AOP로 해결할 수 있다.
        * 그리고 Spring AOP에서 제공하는 기능도 사실 많다.

## 4. AOP 용어 정리

<img width="565" alt="image" src="https://user-images.githubusercontent.com/51740388/191027317-971e47ac-ae52-4453-9df7-305dff3b0ba3.png">

* 조인 포인트(Join Point)
    * AOP를 적용할 수 있는 모든 지점
    * Spring AOP는 Proxy 방식을 사용하므로, **조인 포인트는 항상 메서드 실행 지점으로 제한된다**
* 포인트 컷(Pointcut)
    * Advice가 적용될 위치를 선별하는 기능
    * 주로 AspectJ 표현식을 사용해서 지정
* 타겟(Target)
    * Advice를 적용할 실제 객체
* 어드바이스(Advice)
    * 부가 기능
    * Around, Before, After 등 다양한 종류의 어드바이스가 존재한다.
* 애스팩트(Aspect)
    * 어드바이스 + 포인트컷을 **모듈화** 한 것
    * `@Aspect`를 생각하면 된다.
* 어드바이저(Advisor)
    * Advice + Pointcut
    * Spring AOP에만 사용되는 특별한 용어
* Weaving
    * Pointcut으로 필터링한 Target의 Joinpoint에 Advice를 적용하는 것
    * Weaving을 통해 핵심코드에 영향을 주지 않고 부가기능을 추가할 수 있다.
    * 적용 시점
        * 컴파일 타임
        * 로드 타임
        * 런타임 - Spring AOP, Proxy 방식
* AOP 프록시
    * AOP 기능을 구현하기 위해 만든 Proxy 객체
    * JDK Dynamic Proxy 또는 CGLIB Proxy