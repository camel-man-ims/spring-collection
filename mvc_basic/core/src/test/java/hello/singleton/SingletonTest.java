package hello.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class SingletonTest {

    @Test
    @DisplayName("싱글톤 패턴 사용")
    void singletonTest(){
        SingletonService instance = SingletonService.getInstance();
        SingletonService instance2 = SingletonService.getInstance();

        // isSameAs : ==
        // isEqualTo : equals()
        Assertions.assertThat(instance).isSameAs(instance2);
    }

    @Test
    @DisplayName("스프링 컨테이너")
    void springContainer(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService ms1 = ac.getBean(MemberService.class);
        MemberService ms2 = ac.getBean(MemberService.class);

        System.out.println("ms1 = " + ms1);
        System.out.println("ms2 = " + ms2);

        Assertions.assertThat(ms1).isSameAs(ms2);
    }

    @Test
    @DisplayName("싱글톤 발생시 주의점")
    void statefulSingleton(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService ss1 = ac.getBean(StatefulService.class);
        StatefulService ss2 = ac.getBean(StatefulService.class);

        ss1.order("userA",10000);
        ss2.order("userB",20000);

        int ss1Price = ss1.getPrice();
        System.out.println("ss1Price = " + ss1Price);
    }
    static class TestConfig{
        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }
}
