# chap1 - 도입 , 기본문법

## 1. 설정

* [해당 페이지](https://www.inflearn.com/questions/355723)의 `build.gradle`설정을 추가해준다.

<img width="511" alt="image" src="https://user-images.githubusercontent.com/51740388/194339782-05d01506-bca2-4afd-9914-e9eda98f7972.png">

* gradle > compileQuerydsl을 눌러주면 아래와 같이 class 파일이 하나 생긴다.
    * 해당 파일은 Q파일이라고 한다.
    * git 같은 곳에 올리면 안된다.
        * 시스템이 만들어주는 파일이기 떄문이다.(시스템마다 값이 다를 수 있다.)
        * 보통 build폴더는 ignore에 들어가기 때문에, 해당 방법처럼 build 폴더안에 들어가게 하면 신경 안 쓸 수 있다.

<img width="402" alt="image" src="https://user-images.githubusercontent.com/51740388/194340035-7d463d0c-957c-4f46-9852-3fb001ef49af.png">

<img width="800" alt="image" src="https://user-images.githubusercontent.com/51740388/194462288-5933c161-6cd6-4703-9a1a-f7e027f40ae0.png">

* 소스 파일의 root에서 
    * `./gradlew clean`
        * build 파일 삭제됨
    * `./gradlew compileQuerydsl`
        * 위 과정이 똑같이 생긴다.
    * `./gradlew compileJava`
        * java 컴파일 하는 과정에 queryDSL이 포함되기 때문에 역시 똑같이 생긴다.
* queryDSL은 JPQL의 builder역할을 한다.
    * queryDSL을 빌드하면 JPQL이 생성된다.

## 2. 첫번째 테스트

* [QuerydslBasicTest](../src/test/java/study/querydsl/QuerydslBasicTest.java)
* 먼저 queryDSL을 사용하려면 각 Entity에 대해서 Q타입이라는 것을 생성해야 한다.
* `JPAQueryFactory` 를 통해 JPAQuery를 생성한다.
    * 이 때 해당 값은 필드로 빼서 사용해도 된다.
        * JPAQueryFactory는 EntityManager에 의해 제공된다.
        * Spring은 여러 Thread에서 동시에 EntityManager에 접근해도 각 트랜잭션마다 별도의 영속성 컨텍스트를 제공하기 때문에 동시성 문제는 발생하지 않는다.

## 3. 검색

### 3-1) 기본 검색

* [SearchTest](../src/test/java/study/querydsl/SearchTest.java)

```java
member.username.eq("member1") // username = 'member1'
member.username.ne("member1") //username != 'member1'
member.username.eq("member1").not() // username != 'member1'
member.username.isNotNull() //이름이 is not null
member.age.in(10, 20) // age in (10,20)
member.age.notIn(10, 20) // age not in (10, 20)
member.age.between(10,30) //between 10, 30
member.age.goe(30) // age >= 30 greaterOrEqual
member.age.gt(30) // age > 30 GreaterThan
member.age.loe(30) // age <= 30 lowerOrEqual
member.age.lt(30) // age < 30 lowerThan
member.username.like("member%") //like 검색
member.username.contains("member") // like ‘%member%’ 검색 
member.username.startsWith("member") //like ‘member%’ 검색 
```

* fetch()
    * 리스트 조회
    * 없으면 빈 리스트 반환
* fetchOne()
    * 단건조회
    * 결과가 없으면 null, 둘 이상이면 exception
* fetchFirst()
    * = limit(1).fetchOne()
* fetchResults()
    * 페이징 정보 포함
    * total count 쿼리 추가 실행
* fetchCount()
    * count 쿼리로 변경해서, count수 조회

## 4. 기본 기능들

### 4-1) Sort

* [SortTest](../src/test/java/study/querydsl/basic/SortTest.java)
* `nullsLast`
    * null일 경우 마지막에 가져와라

### 4-2) Paging

* [PagingTest](../src/test/java/study/querydsl/basic/PagingTest.java)
* offset, limit으로 paging을 편하게 처리할 수 있다.
* `fetchResults()`는 deprectated이다.
    * 대신 `fetch()`를 사용해라.

<img width="700" alt="image" src="https://user-images.githubusercontent.com/51740388/194558020-28da61f5-be5d-43cd-9562-7bc7423eadcf.png">

### 4-3) Aggregation(집합)

* [AggregationTest](../src/test/java/study/querydsl/basic/AggregationTest.java)
