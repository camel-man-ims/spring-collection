# @ComponentScan

## 1. ComponentScan이란

### 1-1) ComponentScan 설명

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

### 1-2) ComponentScan의 동작원리

* @ComponentScan은 @Component가 붙은 모든 클래스를 Spring Bean으로 등록한다.
  * 이 때, 이름은 클래스명을 사용하되, 앞글자만 소문자를 사용한다.
    * MemberServiceImpl -> memberServiceImpl
  * 이름을 따로 부여하고 싶다면, `@Component("다른이름ㅋ")` 과 같이 부여하면 된다.

* @Autowired
  * 스프링 컨테이너가 자동으로 해당 Spring Bean을 탐색해서 주입한다.
  * 이 때, 기본 조회 전략은 type이 같은 bean이다.
  * `ac.getBean(MemberService.class)` 와 같은 동작원리라고 이해하면 된다.