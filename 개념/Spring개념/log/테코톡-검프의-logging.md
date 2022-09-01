# 테코톡 - 검프의 logging

## 1. 로깅 설명

### 1-1) 로깅이란?

* 프로그램시 동작하는 발생하는 모든 일을 기록하는 행위
    * 이 때, [모든 일], [기록] 이라는 것 모두 애매하다.

* 로깅 : 모든 일
    * 서비스 동작 상태(Http 통신, 트랜잭션, DB요청, 의도를 가진 exception 등)
    * 장애(exception,error,의도하지 않은 exception)

* 상황마다 로깅을 해야하는 시점은 다르다.

### 1-2) 로그레벨

<img width="700" alt="image" src="https://user-images.githubusercontent.com/51740388/187910159-c4c306fa-e52f-4274-9e1b-a35b400533c9.png">

* 회원 가입 시, DB에 동일한 email을 가진 회원이 있을 때 DuplicationException을 던진다면, 해당 이벤트의 로그는 어떤 레벨을 적용할까?
    * `INFO`
* 주어진 선을 기준으로 위쪽으로는 의도치 않은 예외, 아래쪽은 의도한 예외이다.

<img width="800" alt="image" src="https://user-images.githubusercontent.com/51740388/187910345-a2b0a7eb-7b0c-4cb9-92d6-9358e36f461e.png">

### 1-3) 로깅 vs 디버깅

* 디버깅은 더 자세히 구동환경을 볼 수 있지만, 실제 서버가 구동될 때는 디버깅을 활용하기 힘들다. 
    * 이 때 로그를 활용할 수 있다.

* SLF4J
    * Simple Logging Facade For Java
    * 로킹 프레임워크에 대한 추상화(interface) 역할
        * 단독으로는 사용이 불가능하다.

## 2. 동작과정

<img width="700" alt="image" src="https://user-images.githubusercontent.com/51740388/187910778-fdb8d2f9-eedd-4688-8d37-c271e6ea5d54.png">

* Bridge
    * 다른 로깅 API로의 Logger 호출을 SLF4J API로 연결한다.
    * 일종의 adapter 역할을 하는 라이브러리
    * 레거시 로깅 프레임워크를 위한 라이브러리다.

* Logback
    * SLF4J의 구현체
    * Log4J를 토대로 만든 프레임워크

## 3. logback

<img width="600" alt="image" src="https://user-images.githubusercontent.com/51740388/187913542-8bc39b6d-98de-4c14-80cf-dac13d6881a3.png">

* Logback의 구조
    * logback-core
        * 다른 두 모듈을 위한 기반 역할을 하는 모듈
        * Appender와 Layout인터페이스가 해당 모듈에 속한다.

<img width="700" alt="image" src="https://user-images.githubusercontent.com/51740388/187913831-b773cf19-3336-4cb3-a369-2d9684cc6626.png">

<img width="700" alt="image" src="https://user-images.githubusercontent.com/51740388/187913894-3e885210-9d33-4dd0-9b71-e8dc26b20a42.png">

* RollingFileAppender
    * 파일을 조건에 맞게 따로 저장한다.

<img width="700" alt="image" src="https://user-images.githubusercontent.com/51740388/187913997-24fe4e89-aff4-4a0f-bbcb-540760a925de.png">

* 로그 이벤트를 바이트 배열로 변환하고, 해당 바이트 배열을 OutputStream에 쓰는 작업을 담당한다.
    * 즉, appender에 포함돼서 사용자가 지정한 형식으로 표현될 로그 메세지를 변환하는 역할을 담당한다.

## 4. 실습

* 요구사항
    * 테스트 개발 환경에서는 Info레벨 로그를 console에 찍는다.
    * 운영 환경에서는 Info, Warn, Error 레벨별 로그를 파일로 남긴다.

