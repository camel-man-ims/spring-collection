package hello.beanfind;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextSameBeanFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanConfig.class);

    @Test
    @DisplayName("type으로 조회 시 같은 타입이 둘 이상 있으면 오류 발생")
    void findBeanByDuplicateType(){
        MemberRepository bean = ac.getBean(MemberRepository.class);
        System.out.println("bean = " + bean);
    }

    @Test
    @DisplayName("빈 이름 지정으로 해결")
    void findBeanByDuplicateTypeSolve() {
        MemberRepository bean = ac.getBean("memberRepository1", MemberRepository.class);
        System.out.println("bean = " + bean);
    }

    @Test
    @DisplayName("특정 타입을 모두 조회하기")
    void findAllBeanType(){
        Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);
        for (String s : beansOfType.keySet()) {
            System.out.println("s = " + s);
            System.out.println(beansOfType.get(s));
        }
    }

    @Configuration
    static class SameBeanConfig{
        @Bean
        public MemberRepository memberRepository1(){
            return new MemoryMemberRepository();
        }

        @Bean
        public MemberRepository memberRepository2(){
            return new MemoryMemberRepository();
        }
    }
}
