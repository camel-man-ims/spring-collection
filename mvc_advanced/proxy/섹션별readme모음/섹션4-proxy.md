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

### 1-2) 무엇을 할 것인가?

* v1
    * 인터페이스와 구현 클래스
        * Spring Bean으로 수동등록
* v2
    * 인터페이스가 없는 구체 클래스
        * Spring Bean으로 수동등록
* v3
    * Component Scan으로 Spring Bean 자동등록