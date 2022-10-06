# chap1 - 도입

## 1. 설정

* [해당 페이지](https://www.inflearn.com/questions/355723)의 `build.gradle`설정을 추가해준다.

<img width="511" alt="image" src="https://user-images.githubusercontent.com/51740388/194339782-05d01506-bca2-4afd-9914-e9eda98f7972.png">

* gradle > compileQuerydsl을 눌러주면 아래와 같이 class 파일이 하나 생긴다.
    * 해당 파일은 Q파일이라고 한다.
    * git 같은 곳에 올리면 안된다.
        * 시스템이 만들어주는 파일이기 떄문이다.(시스템마다 값이 다를 수 있다.)
        * 보통 build폴더는 ignore에 들어가기 때문에, 해당 방법처럼 build 폴더안에 들어가게 하면 신경 안 쓸 수 있다.

<img width="402" alt="image" src="https://user-images.githubusercontent.com/51740388/194340035-7d463d0c-957c-4f46-9852-3fb001ef49af.png">

* 소스 파일의 root에서 
    * `./gradlew clean`
        * build 파일 삭제됨
    * `./gradlew compileQuerydsl`
        * 위 과정이 똑같이 생긴다.
    * `./gradlew compileJava`
        * java 컴파일 하는 과정에 queryDSL이 포함되기 때문에 역시 똑같이 생긴다.



