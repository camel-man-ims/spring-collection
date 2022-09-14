# 섹션 4 - 프록시 패턴 & 데코레이터 패턴

## 1. 프로젝트 상황

### 1-1) base packages

```java
@Import(AppV1Config.class)
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의
public class ProxyApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}
}
```

<img width="290" alt="image" src="https://user-images.githubusercontent.com/51740388/189772692-15fcdbfb-2f5b-46af-bf61-6405f8371036.png">

* V1, V2, V3를 다르게 사용하기 위해 scanBasePackages를 지정해주었다.
* config폴더에서 configuration을 다르게 준다.

### 1-2) 무엇을 할 것인가?: 사전준비

* v1
    * 인터페이스와 구현 클래스
        * Spring Bean으로 수동등록
    * [v1](../src/main/java/hello/proxy/app/v1/)
* v2
    * 인터페이스가 없는 구체 클래스
        * Spring Bean으로 수동등록
    * [v2](../src/main/java/hello/proxy/app/v2/)
    * `@Import({AppconfigV1.class, AppconfigV2.class})`
        * import를 사용해서 해당 Bean등록을 해줄 수 있다.
* v3
    * Component Scan으로 Spring Bean 자동등록
    * [v3](../src/main/java/hello/proxy/app/v3/)

### 1-3) 요구사항

* 로깅을 하고, 걸린 시간을 출력하고, HTTP로 요청을 구분하고 등등을 지난 섹션에서 진행했다.

```
- 모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력
- 애플리케이션의 흐름을 변경하면 안됨
- 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안됨
- 메서드 호출에 걸린 시간
- 정상 흐름과 예외 흐름 구분
- 예외 발생시 예외 정보가 남아야 함
- 메서드 호출의 깊이 표현
- HTTP 요청을 구분
- HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함
- 트랜잭션 ID (DB 트랜잭션X)
```

* 지난 흐름 예시

```
정상 요청
[796bccd9] OrderController.request()
[796bccd9] |-->OrderService.orderItem()
[796bccd9] |   |-->OrderRepository.save()
[796bccd9] |   |<--OrderRepository.save() time=1004ms
[796bccd9] |<--OrderService.orderItem() time=1014ms
[796bccd9] OrderController.request() time=1016ms

예외 발생
[b7119f27] OrderController.request()
[b7119f27] |-->OrderService.orderItem()
[b7119f27] |   |-->OrderRepository.save()
[b7119f27] |   |<X-OrderRepository.save() time=0ms 
ex=java.lang.IllegalStateException: 예외 발생!
[b7119f27] |<X-OrderService.orderItem() time=10ms 
ex=java.lang.IllegalStateException: 예외 발생!
[b7119f27] OrderController.request() time=11ms 
ex=java.lang.IllegalStateException: 예외 발생!
```

* 문제는 위의 요구사항을 만족하려면 **기존코드**를 많이 수정해야 한다는 것이다.
    * 코드 수정을 최소화 하기 위해 템플릿 메서드 패턴과 콜백 패턴을 사용하긴 했지만, 로그를 남기고 싶은 클래스가 수백개라면 어쨋든 수백개의 클래스를 모두 고쳐야 한다.
    * 그래서 아래와 같은 요구사항이 추가됐다.
* 요구사항
    * 원본 코드를 **전혀** 수정하지 않고, 로그 추적기를 적용할 것.
    * 보안상 일부 로그는 출력하지 말 것.
    * v1,v2,v3와 같은 케이스에 적용할 수 있어야 한다.
        * v1
            * 인터페이스가 있는 구체 클래스
        * v2
            * 인터페이스가 없는 구체 클래스
        * v3
            * 컴포넌트 스캔 대상 클래스

## 2. Proxy

### 2-1) Proxy란?

* Client - Server
    * client server개념은 상당히 넓게 사용된다.
    * client가 server에 자원을 요청하고, server가 자원을 반환하면 그 어떤 것이든지 client - server 관계가 된다.

<img width="470" alt="image" src="https://user-images.githubusercontent.com/51740388/190047445-28bd1644-fbaa-41ff-946b-41b040b16b09.png">

* 이 때, 위와 같이 proxy를 통한 간접호출을 할 수 있다.
    * 위와 같은 대리자를 proxy라고 한다.

### 2-2) Proxy의 주요 기능

* Proxy는 크게 2가지 기능을 가질 수 있다.
* 접근 제어
    * 권한에 따른 접근 차단
    * 캐싱
        * 캐싱 역시 이미 데이터가 있다면 원본 데이터에 접근하지 않는다는 것이므로, 접근제어이다.
    * 지연 로딩
        * 실제 사용될 때 원본 데이터에 접근하므로, 이 역시 접근제어이다.
* 부가 기능 추가
    * ex) 실행시간 측정
    * ex) 값 변형

* proxy 예시
    * 접근제어,캐싱
        * 엄마한테 라면사달라고 했는데, 라면이 이미 집에 있을 수 있다.
    * 부가 기능 추가
        * 친구한테 주유를 부탁했는데, 친구가 세차까지 해줄 수도 있다.
    * 프록시 체인
        * 대리자가 또 다른 대리자를 호출할 수도 있다.
        * 첫쨰가 둘째한테 라면을 끓이라고 했더니, 둘째가 다시 셋째한테 라면을 끓이라고 할 수 있다.
        * 이 때 중요한 점은, 나는 첫째는 둘째한테만 요청을 하고 그 이후의 과정에 대해서는 모른다는 점이다.
            * 첫째 입장에서는 둘째를 통해 라면이 도착하기만 하면 된다.

<img width="461" alt="image" src="https://user-images.githubusercontent.com/51740388/190048131-17874eb1-75ed-4d4d-a5b5-7dc5eb794d0a.png">

### 2-3) proxy의 대체가능성

<img width="482" alt="image" src="https://user-images.githubusercontent.com/51740388/190048228-12ec8a3f-5b69-4606-8bd9-8e439f5fe6b4.png">

* client입장에서는 원본 객체가 해당 데이터를 가져온 것인지, proxy가 가져온 것인지 몰라야 한다.
* 그러려면 위와 같이 인터페이스 & DI 를 통해서 작동해야 한다.
    * 서버와 proxy는 같은 인터페이스를 사용해야 한다.
    * client의 코드를 고치지 않고 동작하기 위해 DI를 사용한다.

## 3. 프록시 패턴 & 데코레이터 패턴

### 3-1) 프록시 & 데코레이터 패턴 설명

* 둘다 프록시를 사용한다.
* 이 때 둘은 의도(intent)에 따라 다르게 구분된다.
* 프록시 패턴
    * 접근 제어가 목적
* 데코레이터 패턴
    * 부가기능 추가(새로운 기능 추가)가 목적

### 3-2) 프록시 패턴 예제

<img width="463" alt="image" src="https://user-images.githubusercontent.com/51740388/190048681-10701c8b-85ac-4341-8269-074ab9b9ffec.png">

* 코드
    * [프록시 패턴 코드 폴더](../src/test/java/hello/proxy/code/pureproxy/)
    * [프록시 패턴 핵심 코드](../src/test/java/hello/proxy/code/pureproxy/CacheProxy.java)
    * [프록시 패턴 실행 테스트](../src/test/java/hello/proxy/code/pureproxy/ProxyPatternTest.java)
* 동작의도
    * 캐시를 적용한다.
    * 만약 한 번 호출이 된 상태면, 이후에 실제 객체는 호출되지 않도록 한다.
        * 실제 객체가 호출된다는 가정하에 실제 객체에는 `TimeUnit.Sleep`을 걸어두었다.
* target
    * 프록시는 최종적으로 실제 객체를 호출해야 한다.
        * 이 때의 객체를 관용적으로 target이라고 한다.
* 동작
    * 한 번 호출된 이후에는 proxy내부에 캐시 데이터를 통해 해당 캐시 데이터를 반환한다.
    * 테스트를 보자.
        * `Client`는 `Proxy`객체를 받고, `Proxy`객체는 `RealSubject`를 받는다.

### 3-3) 데코레이터 패턴 예제

<img width="462" alt="image" src="https://user-images.githubusercontent.com/51740388/190049380-d564d105-2020-4b1e-a071-f706b6b12712.png">

* 코드
    * [데코레이터 패턴 코드 폴더](../src/test/java/hello/proxy/code/decorator/)
    * [데코레이터 패턴 핵심 코드(MessageDecorator)](../src/test/java/hello/proxy/code/decorator/MessageDecorator.java)
    * [데코레이터 패턴 체이닝 : 실행시간 찍기(TimeDecorator)](../src/test/java/hello/proxy/code/decorator/TimeDecorator.java)
    * [데코레이터 패턴 테스트](../src/test/java/hello/proxy/code/decorator/DecoratorPatternTest.java)
* 사실 프록시 패턴과 크게 다른 것은 없어 보인다.
* intent가 다르다는 것에 집중하자.
* 이 때, chaning이 일어날 수 있다.
    * `TimeDecorator`를 만들어서, `MessageDecorator` 를 parameter로 받는다.
    * 그러면 `MessageDecorator`까지 붙여온 기능에 다시 추가적으로 기능을 붙일 수 있다.

<img width="478" alt="image" src="https://user-images.githubusercontent.com/51740388/190052084-2fa4ba0d-ede6-47b4-b730-70051e3d792a.png">

* 추상화
    * 이 때, 각 데코레이터 패턴마다 생성자와 인터페이스 선언 부분은 중복되기 때문에 추상화해 줄 수 있다.